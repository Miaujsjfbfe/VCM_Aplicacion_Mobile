package com.losdebuggers.oftapp.model

data class Resultado(
    val id: String,
    val examenId: String,
    val pacienteId: String,
    val diagnostico: String,
    val observaciones: String
)