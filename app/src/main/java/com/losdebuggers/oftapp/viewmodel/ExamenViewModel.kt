package com.losdebuggers.oftapp.viewmodel

import androidx.lifecycle.ViewModel
import com.losdebuggers.oftapp.model.Examen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExamenViewModel : ViewModel() {

    // Estado interno modificable
    private val _examenes = MutableStateFlow<List<Examen>>(emptyList())
    // Estado público de solo lectura para la pantalla
    val examenes: StateFlow<List<Examen>> = _examenes.asStateFlow()

    // Estado para guardar el examen seleccionado en el detalle
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

    // Acción para seleccionar un examen por su ID
    fun seleccionarExamen(examenId: String) {
        _examenSeleccionado.value = _examenes.value.find { it.id == examenId }
    }
}