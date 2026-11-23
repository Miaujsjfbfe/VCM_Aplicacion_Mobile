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
                categoria = Examen.CategoriaExamen.REFRACCION,
                medico = "Dr. Álvaro Soto",
                fecha = "18 Nov 2026",
                indicacionesMedicas = "Usar corrección óptica indicada para lectura y trabajo en pantalla.",
                archivoPdf = "examen_1_refraccion.pdf",
                rutPaciente = "18.765.432-1"
            ),
            Examen(
                id = "2",
                categoria = Examen.CategoriaExamen.TONOMETRIA,
                medico = "Dr. Roberto Díaz",
                fecha = "04 Nov 2026",
                indicacionesMedicas = "Presión intraocular dentro de rango normal. Continuar control preventivo.",
                archivoPdf = "examen_2_tonometria.pdf",
                rutPaciente = "18.765.432-1"
            ),
            Examen(
                id = "3",
                categoria = Examen.CategoriaExamen.FONDO_OJO,
                medico = "Dra. Marcela Vidal",
                fecha = "28 Oct 2026",
                indicacionesMedicas = "Control anual de fondo de ojo; evitar frotar los ojos.",
                archivoPdf = "examen_3_fondo_ojo.pdf",
                rutPaciente = "18.765.432-1"
            ),
            Examen(
                id = "4",
                categoria = Examen.CategoriaExamen.OCT_MACULAR,
                medico = "Dra. Marcela Vidal",
                fecha = "12 Nov 2026",
                indicacionesMedicas = "Sin alteraciones maculares relevantes en este control.",
                archivoPdf = "examen_4_oct_macular.pdf",
                rutPaciente = "18.765.432-1"
            )
        )
    }

    fun seleccionarExamen(examenId: String) {
        _examenSeleccionado.value = _examenes.value.firstOrNull { it.id == examenId }
    }

    fun agregarExamenes(nuevos: List<Examen>) {
        val actual = _examenes.value
        val combinados = (nuevos + actual).distinctBy { it.id }
        _examenes.value = combinados
    }
}
