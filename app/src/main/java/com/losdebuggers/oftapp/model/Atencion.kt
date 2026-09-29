package com.losdebuggers.oftapp.model
import java.util.Date

data class Atencion(
    val id: String,
    val pacienteId: String,
    val fecha: Date,
    val motivoConsulta: String
)