package com.losdebuggers.oftapp.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.losdebuggers.oftapp.data.OftDatabase
import com.losdebuggers.oftapp.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Estado del formulario (login y registro comparten pantalla). */
data class LoginUiState(
    val modoRegistro: Boolean = false,
    val rol: String = "Tecnólogo",
    val identificador: String = "",   // login: correo o RUT
    val nombre: String = "",          // registro   
    val rut: String = "",             // registro
    val email: String = "",           // registro
    val clave: String = "",
    val claveVisible: Boolean = false,
    val error: String? = null,
    val cargando: Boolean = false
)

class LoginViewModel(app: Application) : AndroidViewModel(app) {

    private val db = OftDatabase.get(app)

    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui.asStateFlow()

    /** Usuario con sesión activa (null = mostrar login). */
    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()

    /** false mientras se consulta SQLite al abrir la app (evita parpadeo del login). */
    private val _sesionVerificada = MutableStateFlow(false)
    val sesionVerificada: StateFlow<Boolean> = _sesionVerificada.asStateFlow()

    init {
        // Al abrir la app: ¿hay esta_logueado = 1 en SQLite?
        viewModelScope.launch {
            _usuario.value = withContext(Dispatchers.IO) { db.sesionActiva() }
            _sesionVerificada.value = true
        }
    }

    // ---- Actualización del formulario ----
    fun seleccionarRol(rol: String) = _ui.update { it.copy(rol = rol, error = null) }
    fun onIdentificador(v: String) = _ui.update { it.copy(identificador = v, error = null) }
    fun onNombre(v: String) = _ui.update { it.copy(nombre = v, error = null) }
    fun onRut(v: String) = _ui.update { it.copy(rut = v, error = null) }
    fun onEmail(v: String) = _ui.update { it.copy(email = v, error = null) }
    fun onClave(v: String) = _ui.update { it.copy(clave = v, error = null) }
    fun toggleClaveVisible() = _ui.update { it.copy(claveVisible = !it.claveVisible) }
    fun cambiarModo(registro: Boolean) = _ui.update {
        LoginUiState(modoRegistro = registro, rol = if (registro && it.rol == "Admin") "Paciente" else it.rol)
    }

    // ---- Accesos rápidos demo (usuarios sembrados en SQLite) ----
    fun cargarDemo(rol: String, email: String) = _ui.update {
        LoginUiState(rol = rol, identificador = email, clave = "demo123")
    }

    // ---- Login / Registro ----
    fun enviar() {
        val s = _ui.value
        if (s.cargando) return
        viewModelScope.launch {
            _ui.update { it.copy(cargando = true, error = null) }
            val resultado = withContext(Dispatchers.IO) {
                val r: Result<Usuario> = if (s.modoRegistro) validarRegistro(s) ?: db.registrar(s.nombre, s.rut, s.email, s.clave, s.rol)
                else if (s.identificador.isBlank() || s.clave.isBlank())
                    Result.failure(IllegalArgumentException("Ingrese su correo o RUT y su clave"))
                else db.autenticar(s.identificador, s.clave, s.rol)
                // Éxito: guardar esta_logueado = 1 en SQLite
                r.getOrNull()?.let { db.iniciarSesion(it.id) }
                r
            }
            resultado.fold(
                onSuccess = { u ->
                    _usuario.value = u          // MainActivity reemplaza Login por Inicio
                    _ui.value = LoginUiState()  // limpia credenciales de memoria
                },
                onFailure = { e -> _ui.update { it.copy(cargando = false, error = e.message) } }
            )
        }
    }

    private fun validarRegistro(s: LoginUiState): Result<Usuario>? = when {
        s.nombre.isBlank() -> "Ingrese su nombre completo"
        s.rut.filter { it.isLetterOrDigit() }.length < 8 -> "RUT inválido"
        !Patterns.EMAIL_ADDRESS.matcher(s.email.trim()).matches() -> "Correo electrónico inválido"
        s.clave.length < 6 -> "La clave debe tener al menos 6 caracteres"
        else -> null
    }?.let { Result.failure(IllegalArgumentException(it)) }

    // ---- Logout: esta_logueado = 0 y vuelta al login ----
    fun cerrarSesion() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { db.cerrarSesion() }
            _ui.value = LoginUiState()
            _usuario.value = null
        }
    }
}
