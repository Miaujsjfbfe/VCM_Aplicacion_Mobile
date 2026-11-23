package com.losdebuggers.oftapp.backend.repository

import com.losdebuggers.oftapp.backend.model.ExamenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExamenRepository : JpaRepository<ExamenEntity, Long> {
    fun findByRutPaciente(rutPaciente: String): List<ExamenEntity>
}
