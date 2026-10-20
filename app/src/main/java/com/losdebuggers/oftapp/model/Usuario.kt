package com.losdebuggers.oftapp.model

data class Usuario(
    val id: Long,
    val nombre: String,
    val rut: String,
    val email: String,
    val rol: String // "Tecnólogo", "Oftalmólogo", "Paciente", "Admin"
)
