package com.losdebuggers.oftapp.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.losdebuggers.oftapp.model.Usuario

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "oftapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USUARIOS = "usuarios"

        private const val COLUMN_ID = "id"
        private const val COLUMN_RUT = "rut"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_CLAVE = "clave"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_USUARIOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_RUT TEXT UNIQUE NOT NULL,
                $COLUMN_NOMBRE TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL,
                $COLUMN_CLAVE TEXT NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        onCreate(db)
    }

    fun registrarUsuario(usuario: Usuario): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_RUT, usuario.rut.trim())
            put(COLUMN_NOMBRE, usuario.nombre.trim())
            put(COLUMN_EMAIL, usuario.email.trim())
            put(COLUMN_CLAVE, usuario.clave)
        }
        val id = db.insert(TABLE_USUARIOS, null, values)
        db.close()
        return id
    }

    fun obtenerUsuarioPorRut(rut: String): Usuario? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            arrayOf(COLUMN_ID, COLUMN_RUT, COLUMN_NOMBRE, COLUMN_EMAIL, COLUMN_CLAVE),
            "$COLUMN_RUT = ?",
            arrayOf(rut.trim()),
            null, null, null
        )

        var usuario: Usuario? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val userRut = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RUT))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val clave = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLAVE))
            usuario = Usuario(id, userRut, nombre, email, clave)
        }
        cursor.close()
        db.close()
        return usuario
    }

    fun validarLogin(rut: String, clave: String): Usuario? {
        val usuario = obtenerUsuarioPorRut(rut)
        return if (usuario != null && usuario.clave == clave) {
            usuario
        } else {
            null
        }
    }
}
