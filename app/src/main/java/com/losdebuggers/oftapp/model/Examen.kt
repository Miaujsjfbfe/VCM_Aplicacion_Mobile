package com.losdebuggers.oftapp.model

enum class TipoEstudioMedica(val etiqueta: String) {
    RETINOGRAFIA("Retinografía / Fondo de Ojo"),
    TOPOGRAFIA_CORNEAL("Topografía Corneal"),
    CAMPIMETRIA("Campimetría Visual (Humphrey)"),
    OCT_MACULAR("Tomografía OCT Macular")
}

data class ImagenClinica(
    val id: String,
    val tipo: TipoEstudioMedica,
    val titulo: String,
    val descripcion: String,
    val urlOAsset: String,
    val ojo: String = "Bilateral"
)

data class Examen(
    val id: String,
    val categoria: CategoriaExamen,
    val titulo: String = categoria.tituloBase,
    val indicacionesMedicas: String = "",
    val medico: String,
    val fecha: String,
    val archivoPdf: String, // ej: "examen_fondo_ojo.pdf"
    val rutPaciente: String = "",
    val imagenesClinicas: List<ImagenClinica> = emptyList()
){
    enum class CategoriaExamen(
        val tituloBase: String,
        val descripcionSobreExamen: String
        ) {
        REFRACCION(
            tituloBase = "Refracción Computarizada y Subjetiva",
            descripcionSobreExamen = "Evalúa la agudeza visual y detecta vicios de refracción como miopía, hipermetropía, astigmatismo o presbicia para determinar la graduación óptica."
        ),
        TONOMETRIA(
            tituloBase = "Tonometría Ocular",
            descripcionSobreExamen = "Mide la presión intraocular (en mmHg) para evaluar el riesgo o control del glaucoma y resguardar la salud del nervio óptico."
        ),
        FONDO_OJO(
            tituloBase = "Fondo de Ojo Bilateral",
            descripcionSobreExamen = "Permite observar la retina, el nervio óptico, la mácula y los vasos sanguíneos en controles preventivos o seguimiento clínico."
        ),
        OCT_MACULAR(
            tituloBase = "Tomografía de Coherencia Óptica (OCT)",
            descripcionSobreExamen = "Entrega un escáner por capas de alta resolución de la mácula y fibras nerviosas retinianas para un análisis detallado."
        )
    }
}
