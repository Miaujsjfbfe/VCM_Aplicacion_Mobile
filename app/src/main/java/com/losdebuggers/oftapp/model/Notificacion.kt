package com.losdebuggers.oftapp.model

enum class TipoNotificacion(val etiqueta: String) {
    EXAMEN("Informe de Examen"),
    RECETA("Receta Médica"),
    CONTROL("Recordatorio Clínico"),
    SISTEMA("Sincronización Sistema")
}

data class Notificacion(
    val id: String,
    val tipo: TipoNotificacion,
    val titulo: String,
    val mensaje: String,
    val fechaRelativa: String,
    val leida: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
