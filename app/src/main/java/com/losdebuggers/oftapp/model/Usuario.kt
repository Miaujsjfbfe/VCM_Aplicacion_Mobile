package com.losdebuggers.oftapp.model

data class Usuario(
    val id: String,
    val nombre: String,
    val email: String,
    val rol: String // p. ej. "Oftalmólogo", "Administrativo"
)