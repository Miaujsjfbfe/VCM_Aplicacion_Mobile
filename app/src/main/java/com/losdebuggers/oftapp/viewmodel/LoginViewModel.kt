package com.losdebuggers.oftapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    // Estado del rol seleccionado (por defecto Tecnólogo)
    private val _rolSeleccionado = MutableStateFlow("Tecnólogo")
    val rolSeleccionado: StateFlow<String> = _rolSeleccionado.asStateFlow()

    // Campos del formulario
    private val _rut = MutableStateFlow("15.420.198-4")
    val rut: StateFlow<String> = _rut.asStateFlow()

    private val _clave = MutableStateFlow("12345678")
    val clave: StateFlow<String> = _clave.asStateFlow()

    private val _recordarSesion = MutableStateFlow(true)
    val recordarSesion: StateFlow<Boolean> = _recordarSesion.asStateFlow()

    // Funciones de actualización de estado
    fun seleccionarRol(rol: String) {
        _rolSeleccionado.value = rol
    }

    fun onRutChange(nuevoRut: String) {
        _rut.value = nuevoRut
    }

    fun onClaveChange(nuevaClave: String) {
        _clave.value = nuevaClave
    }

    fun toggleRecordarSesion(check: Boolean) {
        _recordarSesion.value = check
    }

    // Funciones para los accesos rápidos Demo
    fun cargarDemoTecnologo() {
        _rolSeleccionado.value = "Tecnólogo"
        _rut.value = "15.420.198-4"
        _clave.value = "demo123"
    }

    fun cargarDemoMedico() {
        _rolSeleccionado.value = "Oftalmólogo"
        _rut.value = "12.345.678-9"
        _clave.value = "demo123"
    }
}