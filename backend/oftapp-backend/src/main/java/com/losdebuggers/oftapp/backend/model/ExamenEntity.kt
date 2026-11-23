package com.losdebuggers.oftapp.backend.model

import jakarta.persistence.*

@Entity
@Table(name = "examenes")
data class ExamenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val categoria: String,
    val titulo: String,
    val indicacionesMedicas: String,
    val medico: String,
    val fecha: String,
    val archivoPdf: String,
    val rutPaciente: String
)
