package com.losdebuggers.oftapp.model

data class Usuario(
    val id: Long = 0,
    val nombre: String,
    val rut: String,
    val email: String,
    val rol: String = "Paciente", // "Tecnólogo", "Oftalmólogo", "Paciente", "Admin"
    val clave: String = ""
)
