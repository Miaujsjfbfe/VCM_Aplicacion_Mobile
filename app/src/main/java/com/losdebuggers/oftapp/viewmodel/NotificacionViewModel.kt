package com.losdebuggers.oftapp.viewmodel

import androidx.lifecycle.ViewModel
import com.losdebuggers.oftapp.model.Notificacion
import com.losdebuggers.oftapp.model.TipoNotificacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class NotificacionViewModel : ViewModel() {

    private val _notificaciones = MutableStateFlow<List<Notificacion>>(emptyList())
    val notificaciones: StateFlow<List<Notificacion>> = _notificaciones.asStateFlow()

    init {
        cargarNotificacionesIniciales()
    }

    private fun cargarNotificacionesIniciales() {
        _notificaciones.value = listOf(
            Notificacion(
                id = "notif_1",
                tipo = TipoNotificacion.EXAMEN,
                titulo = "Nuevo Examen Disponible",
                mensaje = "Dra. Marcela Vidal ha cargado tu examen de Fondo de Ojo Bilateral.",
                fechaRelativa = "Hace 5 min",
                leida = false
            ),
            Notificacion(
                id = "notif_2",
                tipo = TipoNotificacion.RECETA,
                titulo = "Receta Médica Emitida",
                mensaje = "Se ha emitido tu receta para Gotas Oftálmicas Lubricantes (1 gota c/6 hrs).",
                fechaRelativa = "Hace 25 min",
                leida = false
            ),
            Notificacion(
                id = "notif_3",
                tipo = TipoNotificacion.CONTROL,
                titulo = "Recordatorio de Control Clínico",
                mensaje = "Tu control preventivo de Presión Intraocular está agendado para el 04 de Diciembre.",
                fechaRelativa = "Hace 2 horas",
                leida = false
            ),
            Notificacion(
                id = "notif_4",
                tipo = TipoNotificacion.SISTEMA,
                titulo = "Sincronización FHIR Exitosa",
                mensaje = "Los historiales clínicos se han sincronizado con el servidor de pruebas HAPI FHIR.",
                fechaRelativa = "Ayer, 18:30",
                leida = true
            )
        )
    }

    fun marcarComoLeida(id: String) {
        _notificaciones.value = _notificaciones.value.map { notif ->
            if (notif.id == id) notif.copy(leida = true) else notif
        }
    }

    fun marcarTodasComoLeidas() {
        _notificaciones.value = _notificaciones.value.map { notif ->
            notif.copy(leida = true)
        }
    }

    fun eliminarNotificacion(id: String) {
        _notificaciones.value = _notificaciones.value.filter { it.id != id }
    }

    fun agregarNotificacion(notificacion: Notificacion) {
        _notificaciones.value = listOf(notificacion) + _notificaciones.value
    }
}
