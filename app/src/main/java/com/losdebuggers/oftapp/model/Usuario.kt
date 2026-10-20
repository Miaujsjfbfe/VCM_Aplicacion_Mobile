package com.losdebuggers.oftapp.model

data class Usuario(
    val id: Long = 0,
    val rut: String,
    val nombre: String,
    val email: String,
    val clave: String
)
