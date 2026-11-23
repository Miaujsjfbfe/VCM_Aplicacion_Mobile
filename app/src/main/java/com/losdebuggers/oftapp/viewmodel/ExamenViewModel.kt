package com.losdebuggers.oftapp.viewmodel

import androidx.lifecycle.ViewModel
import com.losdebuggers.oftapp.model.Examen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import com.losdebuggers.oftapp.model.ImagenClinica
import com.losdebuggers.oftapp.model.TipoEstudioMedica

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
                rutPaciente = "18.765.432-1",
                imagenesClinicas = listOf(
                    ImagenClinica(
                        id = "img_1_1",
                        tipo = TipoEstudioMedica.TOPOGRAFIA_CORNEAL,
                        titulo = "Topografía Corneal Axial (OD)",
                        descripcion = "Mapa de curvatura anterior corneal en dioptrías. Muestra astigmatismo regular con la regla.",
                        urlOAsset = "https://images.unsplash.com/photo-1579684385127-1ef15d508118?w=1600&q=80",
                        ojo = "OD"
                    ),
                    ImagenClinica(
                        id = "img_1_2",
                        tipo = TipoEstudioMedica.TOPOGRAFIA_CORNEAL,
                        titulo = "Topografía Corneal Axial (OI)",
                        descripcion = "Mapa ceratómetro posterior y elevación del ojo izquierdo.",
                        urlOAsset = "https://images.unsplash.com/photo-1584515979956-d9f6e5d09982?w=1600&q=80",
                        ojo = "OI"
                    )
                )
            ),
            Examen(
                id = "2",
                categoria = Examen.CategoriaExamen.TONOMETRIA,
                medico = "Dr. Roberto Díaz",
                fecha = "04 Nov 2026",
                indicacionesMedicas = "Presión intraocular dentro de rango normal. Continuar control preventivo.",
                archivoPdf = "examen_2_tonometria.pdf",
                rutPaciente = "18.765.432-1",
                imagenesClinicas = listOf(
                    ImagenClinica(
                        id = "img_2_1",
                        tipo = TipoEstudioMedica.CAMPIMETRIA,
                        titulo = "Campimetría Computarizada 24-2 (OD)",
                        descripcion = "Ubicación de umbrales en escala de grises y mapa de desviación del patrón para descarte de glaucoma.",
                        urlOAsset = "https://images.unsplash.com/photo-1576091160550-2173dba999ef?w=1600&q=80",
                        ojo = "OD"
                    )
                )
            ),
            Examen(
                id = "3",
                categoria = Examen.CategoriaExamen.FONDO_OJO,
                medico = "Dra. Marcela Vidal",
                fecha = "28 Oct 2026",
                indicacionesMedicas = "Control anual de fondo de ojo; evitar frotar los ojos.",
                archivoPdf = "examen_3_fondo_ojo.pdf",
                rutPaciente = "18.765.432-1",
                imagenesClinicas = listOf(
                    ImagenClinica(
                        id = "img_3_1",
                        tipo = TipoEstudioMedica.RETINOGRAFIA,
                        titulo = "Retinografía Digital Polo Posterior (OD)",
                        descripcion = "Visualización de papila óptica, excavación fisiológica normal 0.3 y arterias retinianas sin signos de retinopatía.",
                        urlOAsset = "https://images.unsplash.com/photo-1516549655169-df83a0774514?w=1600&q=80",
                        ojo = "OD"
                    ),
                    ImagenClinica(
                        id = "img_3_2",
                        tipo = TipoEstudioMedica.RETINOGRAFIA,
                        titulo = "Retinografía Digital Periferia (OI)",
                        descripcion = "Mácula libre de exudados y hemorragias retinianas.",
                        urlOAsset = "https://images.unsplash.com/photo-1579684385127-1ef15d508118?w=1600&q=80",
                        ojo = "OI"
                    )
                )
            ),
            Examen(
                id = "4",
                categoria = Examen.CategoriaExamen.OCT_MACULAR,
                medico = "Dra. Marcela Vidal",
                fecha = "12 Nov 2026",
                indicacionesMedicas = "Sin alteraciones maculares relevantes en este control.",
                archivoPdf = "examen_4_oct_macular.pdf",
                rutPaciente = "18.765.432-1",
                imagenesClinicas = listOf(
                    ImagenClinica(
                        id = "img_4_1",
                        tipo = TipoEstudioMedica.OCT_MACULAR,
                        titulo = "Corte Tomográfico Macular B-Scan (OD)",
                        descripcion = "Corte tomográfico de alta resolución. Depresión foveal conservada con capas neurosensoriales intactas.",
                        urlOAsset = "https://images.unsplash.com/photo-1584515979956-d9f6e5d09982?w=1600&q=80",
                        ojo = "OD"
                    )
                )
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
