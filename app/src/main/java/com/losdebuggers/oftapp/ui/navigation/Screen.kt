package com.losdebuggers.oftapp.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Inicio : Screen("inicio")
    object Examenes : Screen("examenes")
    object NuevoExamen : Screen("nuevo_examen")

    object Recetas : Screen("recetas")
    object Perfil : Screen("perfil")
    object DetalleExamen : Screen("detalle_examen/{examenId}") {
        fun createRoute(examenId: Int) = "detalle_examen/$examenId"
    }
}