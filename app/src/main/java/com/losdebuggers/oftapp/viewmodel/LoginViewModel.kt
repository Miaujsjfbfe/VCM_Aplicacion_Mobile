package com.losdebuggers.oftapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.losdebuggers.oftapp.data.UserDatabaseHelper
import com.losdebuggers.oftapp.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = UserDatabaseHelper(application)

    // Modo actual: true = Iniciar Sesión, false = Registrarse
    private val _isLoginMode = MutableStateFlow(true)
    val isLoginMode: StateFlow<Boolean> = _isLoginMode.asStateFlow()

    // Campos de entrada
    private val _rut = MutableStateFlow("")
    val rut: StateFlow<String> = _rut.asStateFlow()

    private val _clave = MutableStateFlow("")
    val clave: StateFlow<String> = _clave.asStateFlow()

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _recordarSesion = MutableStateFlow(true)
    val recordarSesion: StateFlow<Boolean> = _recordarSesion.asStateFlow()

    // Mensaje de estado (Error / Éxito)
    private val _mensajeEstado = MutableStateFlow<String?>(null)
    val mensajeEstado: StateFlow<String?> = _mensajeEstado.asStateFlow()

    private val _esError = MutableStateFlow(false)
    val esError: StateFlow<Boolean> = _esError.asStateFlow()

    // Usuario autenticado actual
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    fun setLoginMode(login: Boolean) {
        _isLoginMode.value = login
        _mensajeEstado.value = null
    }

    fun onRutChange(nuevoRut: String) {
        _rut.value = nuevoRut
    }

    fun onClaveChange(nuevaClave: String) {
        _clave.value = nuevaClave
    }

    fun onNombreChange(nuevoNombre: String) {
        _nombre.value = nuevoNombre
    }

    fun onEmailChange(nuevoEmail: String) {
        _email.value = nuevoEmail
    }

    fun toggleRecordarSesion(check: Boolean) {
        _recordarSesion.value = check
    }

    fun iniciarSesion(onSuccess: () -> Unit) {
        val currentRut = _rut.value.trim()
        val currentClave = _clave.value

        if (currentRut.isEmpty() || currentClave.isEmpty()) {
            _mensajeEstado.value = "Por favor, ingrese su RUT y contraseña."
            _esError.value = true
            return
        }

        val user = dbHelper.validarLogin(currentRut, currentClave)
        if (user != null) {
            _usuarioActual.value = user
            _mensajeEstado.value = "¡Bienvenido, ${user.nombre}!"
            _esError.value = false
            onSuccess()
        } else {
            _mensajeEstado.value = "RUT o contraseña incorrectos. Si no tiene cuenta, por favor regístrese."
            _esError.value = true
        }
    }

    fun registrarUsuario() {
        val currentNombre = _nombre.value.trim()
        val currentRut = _rut.value.trim()
        val currentEmail = _email.value.trim()
        val currentClave = _clave.value

        if (currentNombre.isEmpty() || currentRut.isEmpty() || currentEmail.isEmpty() || currentClave.isEmpty()) {
            _mensajeEstado.value = "Todos los campos son obligatorios para el registro."
            _esError.value = true
            return
        }

        val usuarioExistente = dbHelper.obtenerUsuarioPorRut(currentRut)
        if (usuarioExistente != null) {
            _mensajeEstado.value = "El RUT ya se encuentra registrado. Inicie sesión."
            _esError.value = true
            return
        }

        val nuevoUsuario = Usuario(
            rut = currentRut,
            nombre = currentNombre,
            email = currentEmail,
            clave = currentClave
        )

        val id = dbHelper.registrarUsuario(nuevoUsuario)
        if (id > 0) {
            _mensajeEstado.value = "¡Registro exitoso! Ya puede iniciar sesión con sus datos."
            _esError.value = false
            _isLoginMode.value = true
        } else {
            _mensajeEstado.value = "Error al guardar el usuario en la base de datos."
            _esError.value = true
        }
    }
}
