package com.losdebuggers.oftapp.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.losdebuggers.oftapp.model.Usuario
import java.security.MessageDigest
import java.security.SecureRandom

/**
 * Base de datos local SQLite.
 *  - usuarios(id, nombre, rut, email, password, rol, creado_en)
 *  - sesion(id, usuario_id, esta_logueado)  -> una única fila (id = 1)
 * Las llamadas son bloqueantes: invocar desde Dispatchers.IO.
 */
class OftDatabase private constructor(context: Context) :
    SQLiteOpenHelper(context.applicationContext, "oftapp.db", null, 1) {

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true) // respeta la FK sesion -> usuarios
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                rut TEXT NOT NULL UNIQUE,
                email TEXT NOT NULL UNIQUE COLLATE NOCASE,
                password TEXT NOT NULL,
                rol TEXT NOT NULL,
                creado_en INTEGER NOT NULL
            )"""
        )
        db.execSQL(
            """CREATE TABLE sesion (
                id INTEGER PRIMARY KEY,
                usuario_id INTEGER NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
                esta_logueado INTEGER NOT NULL DEFAULT 0
            )"""
        )
        // Usuarios demo (clave: demo123) para los botones de acceso rápido
        insertar(db, "Camilo Arancibia", "15.420.198-4", "tecnologo@oftapp.cl", "demo123", "Tecnólogo")
        insertar(db, "Valentina Rojas", "12.345.678-9", "medico@oftapp.cl", "demo123", "Oftalmólogo")
        insertar(db, "María Elena Soto", "8.765.432-1", "paciente@oftapp.cl", "demo123", "Paciente")
        insertar(db, "Administrador OftApp", "10.111.222-3", "admin@oftapp.cl", "demo123", "Admin")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS sesion")
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    // ---------- Registro ----------
    fun registrar(nombre: String, rut: String, email: String, password: String, rol: String): Result<Usuario> =
        try {
            val id = insertar(writableDatabase, nombre.trim(), rut, email.trim(), password, rol)
            Result.success(Usuario(id, nombre.trim(), normalizarRut(rut), email.trim(), rol))
        } catch (e: SQLiteConstraintException) {
            Result.failure(IllegalStateException("El correo o RUT ya está registrado"))
        }

    // ---------- Login ----------
    /** [identificador] puede ser correo o RUT. Valida también el perfil (rol) elegido. */
    fun autenticar(identificador: String, password: String, rol: String): Result<Usuario> {
        val id = identificador.trim()
        val esEmail = id.contains("@")
        val cursor = readableDatabase.rawQuery(
            "SELECT id, nombre, rut, email, password, rol FROM usuarios WHERE " +
                (if (esEmail) "email = ?" else "rut = ?"),
            arrayOf(if (esEmail) id else normalizarRut(id))
        )
        cursor.use {
            if (!it.moveToFirst() || !verificar(password, it.getString(4))) {
                return Result.failure(IllegalArgumentException("Credenciales incorrectas"))
            }
            if (it.getString(5) != rol) {
                return Result.failure(IllegalArgumentException("Este usuario no tiene el perfil $rol"))
            }
            return Result.success(Usuario(it.getLong(0), it.getString(1), it.getString(2), it.getString(3), it.getString(5)))
        }
    }

    // ---------- Sesión ----------
    /** Guarda esta_logueado = 1 para el usuario (reemplaza cualquier sesión anterior). */
    fun iniciarSesion(usuarioId: Long) {
        writableDatabase.execSQL(
            "INSERT OR REPLACE INTO sesion (id, usuario_id, esta_logueado) VALUES (1, ?, 1)",
            arrayOf<Any>(usuarioId)
        )
    }

    /** Cierra sesión: esta_logueado = 0. */
    fun cerrarSesion() {
        writableDatabase.execSQL("UPDATE sesion SET esta_logueado = 0 WHERE id = 1")
    }

    /** Devuelve el usuario con sesión activa, o null si no hay. */
    fun sesionActiva(): Usuario? {
        readableDatabase.rawQuery(
            """SELECT u.id, u.nombre, u.rut, u.email, u.rol
               FROM sesion s JOIN usuarios u ON u.id = s.usuario_id
               WHERE s.id = 1 AND s.esta_logueado = 1""", null
        ).use {
            return if (it.moveToFirst())
                Usuario(it.getLong(0), it.getString(1), it.getString(2), it.getString(3), it.getString(4))
            else null
        }
    }

    // ---------- Utilidades ----------
    private fun insertar(db: SQLiteDatabase, nombre: String, rut: String, email: String, password: String, rol: String): Long {
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("rut", normalizarRut(rut))
            put("email", email)
            put("password", hashear(password))
            put("rol", rol)
            put("creado_en", System.currentTimeMillis())
        }
        return db.insertOrThrow("usuarios", null, values)
    }

    companion object {
        @Volatile private var instancia: OftDatabase? = null
        fun get(context: Context): OftDatabase =
            instancia ?: synchronized(this) { instancia ?: OftDatabase(context).also { instancia = it } }

        /** "15.420.198-4" -> "154201984" */
        fun normalizarRut(rut: String) = rut.replace(".", "").replace("-", "").trim().uppercase()

        // Clave guardada como "salt:sha256(salt+clave)"; nunca en texto plano.
        // (Para producción conviene PBKDF2/bcrypt, pero esto evita dependencias nuevas.)
        private fun sha256(texto: String) =
            MessageDigest.getInstance("SHA-256").digest(texto.toByteArray()).joinToString("") { "%02x".format(it) }

        private fun hashear(password: String): String {
            val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }.joinToString("") { "%02x".format(it) }
            return "$salt:${sha256(salt + password)}"
        }

        private fun verificar(password: String, almacenado: String): Boolean {
            val partes = almacenado.split(":")
            return partes.size == 2 && sha256(partes[0] + password) == partes[1]
        }
    }
}
