package com.losdebuggers.oftapp.model

data class Paciente(
    val id: String,
    val rut: String,
    val nombre: String,
    val edad: Int,
    val telefono: String
)