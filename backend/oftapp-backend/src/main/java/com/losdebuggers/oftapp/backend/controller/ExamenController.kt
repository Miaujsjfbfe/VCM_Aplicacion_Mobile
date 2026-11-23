package com.losdebuggers.oftapp.backend.controller

import com.losdebuggers.oftapp.backend.model.ExamenEntity
import com.losdebuggers.oftapp.backend.repository.ExamenRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/examenes")
@Tag(name = "Exámenes Clínicos", description = "Endpoints para la gestión de exámenes oftalmológicos en el microservicio Spring Boot")
class ExamenController(private val examenRepository: ExamenRepository) {

    @Operation(summary = "Listar todos los exámenes", description = "Devuelve la lista completa de exámenes oftalmológicos almacenados en la base de datos central.")
    @GetMapping
    fun listarTodos(): List<ExamenEntity> {
        return examenRepository.findAll()
    }

    @Operation(summary = "Registrar nuevo examen", description = "Guarda un nuevo examen clínico en la base de datos central de Spring Boot.")
    @PostMapping
    fun crearExamen(@RequestBody examen: ExamenEntity): ResponseEntity<ExamenEntity> {
        val guardado = examenRepository.save(examen)
        return ResponseEntity.ok(guardado)
    }

    @Operation(summary = "Buscar exámenes por RUT del paciente", description = "Filtra y devuelve los exámenes clínicos asociados a un RUT específico.")
    @GetMapping("/paciente/{rut}")
    fun listarPorRut(@PathVariable rut: String): List<ExamenEntity> {
        return examenRepository.findByRutPaciente(rut)
    }
}
