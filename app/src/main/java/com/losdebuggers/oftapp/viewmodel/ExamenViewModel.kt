package com.losdebuggers.oftapp.viewmodel

import androidx.lifecycle.ViewModel
import com.losdebuggers.oftapp.model.Examen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExamenViewModel : ViewModel() {

    private val _examenes = MutableStateFlow<List<Examen>>(emptyList())
    val examenes: StateFlow<List<Examen>> = _examenes.asStateFlow()

    private val _examenSeleccionado = MutableStateFlow<Examen?>(null)
    val examenSeleccionado: StateFlow<Examen?> = _examenSeleccionado.asStateFlow()

    init {
        cargarExamenesFicticios()
    }

    private fun cargarExamenesFicticios() {
        _examenes.value = listOf(
            Examen(
                id = "1",
                nombre = "Agudeza Visual",
                descripcion = "Evaluación de la nitidez y claridad de la visión a corta y larga distancia."
            ),
            Examen(
                id = "2",
                nombre = "Tonometría",
                descripcion = "Medición de la presión intraocular para la detección precoz de glaucoma."
            ),
            Examen(
                id = "3",
                nombre = "Campimetría",
                descripcion = "Examen del campo visual para detectar pérdidas en la visión periférica."
            )
        )
    }

    fun seleccionarExamen(examenId: String) {
        _examenSeleccionado.value = _examenes.value.firstOrNull { it.id == examenId }
    }
}
