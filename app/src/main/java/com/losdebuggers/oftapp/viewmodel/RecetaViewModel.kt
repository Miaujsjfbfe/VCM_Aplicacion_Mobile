package com.losdebuggers.oftapp.viewmodel

import androidx.lifecycle.ViewModel
import com.losdebuggers.oftapp.model.Receta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecetaViewModel : ViewModel() {

    private val _recetas = MutableStateFlow<List<Receta>>(emptyList())
    val recetas: StateFlow<List<Receta>> = _recetas.asStateFlow()

    private val _recetaSeleccionada = MutableStateFlow<Receta?>(null)
    val recetaSeleccionada: StateFlow<Receta?> = _recetaSeleccionada.asStateFlow()

    init {
        cargarRecetasFicticias()
    }

    private fun cargarRecetasFicticias() {
        _recetas.value = listOf(
            Receta(
                id = "rec-1",
                caregoria = Receta.CategoriaReceta.FARMACOLOGICA,
                titulo = "Tratamiento Lubricante Ocular",
                indicacionesMedicas = "Hialuronato de Sodio 0.4% · 1 gota c/6 hrs por 30 días",
                medico = "Dra. Marcela Vidal",
                fecha = "15 Nov 2026",
                archivoPdf = "receta_1_gotas_oftalmicas.pdf",
                rutPaciente = "18.765.432-1"
            ),
            Receta(
                id = "rec-2",
                caregoria = Receta.CategoriaReceta.OPTICA,
                titulo = "Lentes Ópticos de Uso Permanente",
                indicacionesMedicas = "OD: -1.50 (-0.75 x 180°) | OI: -1.25 (-0.50 x 175°)",
                medico = "Dr. Álvaro Soto",
                fecha = "28 Oct 2026",
                archivoPdf = "receta_2_lentes_opticos.pdf",
                rutPaciente = "18.765.432-1"
            )
        )
    }

    fun seleccionarReceta(recetaId: String) {
        _recetaSeleccionada.value = _recetas.value.firstOrNull { it.id == recetaId }
    }
}