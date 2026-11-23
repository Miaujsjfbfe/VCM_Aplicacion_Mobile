package com.losdebuggers.oftapp.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.losdebuggers.oftapp.model.Usuario
import com.losdebuggers.oftapp.viewmodel.ExamenViewModel
import com.losdebuggers.oftapp.viewmodel.RecetaViewModel

private object RutasMain {
    const val INICIO = "inicio"
    const val EXAMENES = "examenes"
    const val DETALLE_EXAMEN = "detalle_examen"
    const val RECETAS = "recetas"
    const val DETALLE_RECETA = "detalle_receta"
    const val PERFIL = "perfil"
}

@Composable
fun MainScreen(
    usuario: Usuario,
    examenViewModel: ExamenViewModel,
    recetaViewModel: RecetaViewModel = viewModel(),
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val examenes by examenViewModel.examenes.collectAsState()
    val examenSeleccionado by examenViewModel.examenSeleccionado.collectAsState()

    val recetas by recetaViewModel.recetas.collectAsState()
    val recetaSeleccionada by recetaViewModel.recetaSeleccionada.collectAsState()

    // Los detalles cuentan como su pestaña
    val seccionActual = when (currentRoute) {
        RutasMain.INICIO -> RutasMain.INICIO
        RutasMain.EXAMENES, RutasMain.DETALLE_EXAMEN -> RutasMain.EXAMENES
        RutasMain.RECETAS, RutasMain.DETALLE_RECETA -> RutasMain.RECETAS
        RutasMain.PERFIL -> RutasMain.PERFIL
        else -> null
    }

    // Si ya estás en esa pestaña, vuelve a su pantalla principal; si no, cambia de pestaña
    val irA: (String) -> Unit = { ruta ->
        if (seccionActual == ruta) {
            navController.popBackStack(ruta, inclusive = false)
        } else {
            navController.navigate(ruta) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            item(
                icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                label = { Text("Inicio") },
                selected = seccionActual == RutasMain.INICIO,
                onClick = { irA(RutasMain.INICIO) }
            )
            item(
                icon = { Icon(Icons.Default.Visibility, contentDescription = "Exámenes") },
                label = { Text("Exámenes") },
                selected = seccionActual == RutasMain.EXAMENES,
                onClick = { irA(RutasMain.EXAMENES) }
            )
            item(
                icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Recetas") },
                label = { Text("Recetas") },
                selected = seccionActual == RutasMain.RECETAS,
                onClick = { irA(RutasMain.RECETAS) }
            )
            item(
                icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                label = { Text("Perfil") },
                selected = seccionActual == RutasMain.PERFIL,
                onClick = { irA(RutasMain.PERFIL) }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = RutasMain.INICIO
        ) {
            composable(RutasMain.INICIO) {
                InicioScreen(
                    usuario = usuario,
                    examenes = examenes,
                    onNavigateToExamenes = { irA(RutasMain.EXAMENES) },
                    onNavigateToRecetas = { irA(RutasMain.RECETAS) },
                    onNavigateToPerfil = { irA(RutasMain.PERFIL) },
                    onLogout = onLogout
                )
            }

            composable(RutasMain.EXAMENES) {
                ExamenesScreen(
                    examenes = examenes,
                    onVolver = { navController.popBackStack() },
                    onExamenClick = { id ->
                        examenViewModel.seleccionarExamen(id)
                        navController.navigate(RutasMain.DETALLE_EXAMEN)
                    },
                    onAddExamenes = { nuevos ->
                        examenViewModel.agregarExamenes(nuevos)
                    }
                )
            }

            composable(RutasMain.DETALLE_EXAMEN) {
                val examen = examenSeleccionado
                if (examen != null) {
                    DetalleExamenScreen(
                        examen = examen,
                        onVolver = { navController.popBackStack() }
                    )
                }
            }

            composable(RutasMain.RECETAS) {
                RecetasScreen(
                    recetas = recetas,
                    onVolver = { navController.popBackStack() },
                    onRecetaClick = { id ->
                        recetaViewModel.seleccionarReceta(id)
                        navController.navigate(RutasMain.DETALLE_RECETA)
                    }
                )
            }

            composable(RutasMain.DETALLE_RECETA) {
                val receta = recetaSeleccionada
                if (receta != null) {
                    DetalleRecetaScreen(
                        receta = receta,
                        onVolver = { navController.popBackStack() }
                    )
                }
            }

            composable(RutasMain.PERFIL) {
                PerfilScreen(
                    usuario = usuario,
                    onLogout = onLogout
                )
            }
        }
    }
}
