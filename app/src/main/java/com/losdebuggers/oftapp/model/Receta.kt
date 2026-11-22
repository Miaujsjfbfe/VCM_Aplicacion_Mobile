package com.losdebuggers.oftapp.model

data class Receta(
    val id: String,
    val caregoria: CategoriaReceta,
    val titulo: String,
    val indicacionesMedicas: String, // ej: "1 gota en cada ojo cada 6 hrs por 30 días" o "OD: -1.50 (-0.75 x 180°)"
    val medico: String,
    val fecha: String,
    val archivoPdf: String, // ej: "receta_gotas_oftalmicas.pdf"
    val rutPaciente: String = ""
){
    enum class CategoriaReceta(
        val etiqueta: String,
        val descripcionGeneral: String
    ) {
        OPTICA(
            etiqueta = "Receta Óptica",
            descripcionGeneral = "Indicación óptica orientada a la corrección visual mediante lentes formulados según evaluación."
        ),
        FARMACOLOGICA(
            etiqueta = "Receta Farmacológica",
            descripcionGeneral = "Prescripción de medicamentos oftálmicos indicados para el tratamiento, lubricación o cuidado ocular."
        )
    }
}