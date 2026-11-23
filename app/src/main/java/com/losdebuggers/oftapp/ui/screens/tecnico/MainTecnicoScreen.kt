package com.losdebuggers.oftapp.ui.screens.tecnico

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.losdebuggers.oftapp.model.Usuario
import com.losdebuggers.oftapp.ui.screens.PerfilScreen
import com.losdebuggers.oftapp.viewmodel.ExamenViewModel

private object RutasTecnico {
    const val INICIO = "inicio_tecnico"
    const val SUBIR = "subir_examen"
    const val REGISTROS = "registros"
    const val PERFIL = "perfil_tecnico"
}

@Composable
fun MainTecnicoScreen(
    usuario: Usuario,
    examenViewModel: ExamenViewModel,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val irA: (String) -> Unit = { ruta ->
        if (currentRoute == ruta) {
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
                selected = currentRoute == RutasTecnico.INICIO,
                onClick = { irA(RutasTecnico.INICIO) }
            )
            item(
                icon = { Icon(Icons.Default.UploadFile, contentDescription = "Subir") },
                label = { Text("Subir") },
                selected = currentRoute == RutasTecnico.SUBIR,
                onClick = { irA(RutasTecnico.SUBIR) }
            )
            item(
                icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Registros") },
                label = { Text("Registros") },
                selected = currentRoute == RutasTecnico.REGISTROS,
                onClick = { irA(RutasTecnico.REGISTROS) }
            )
            item(
                icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                label = { Text("Perfil") },
                selected = currentRoute == RutasTecnico.PERFIL,
                onClick = { irA(RutasTecnico.PERFIL) }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = RutasTecnico.INICIO
        ) {
            composable(RutasTecnico.INICIO) {
                InicioTecnicoScreen(
                    usuario = usuario,
                    onNavigateToSubirExamen = { irA(RutasTecnico.SUBIR) },
                    onNavigateToExamenes = { irA(RutasTecnico.REGISTROS) },
                    onNavigateToRecetas = { irA(RutasTecnico.REGISTROS) },
                    onLogout = onLogout,
                    onNavigateToPerfil = { irA(RutasTecnico.PERFIL) }
                )
            }

            composable(RutasTecnico.SUBIR) {
                SubirExamenScreen(
                    onVolver = { irA(RutasTecnico.INICIO) },
                    onFinalizado = { irA(RutasTecnico.REGISTROS) }
                )
            }

            composable(RutasTecnico.REGISTROS) {
                HistorialExamenesTecnicoScreen(
                    onVolver = { irA(RutasTecnico.INICIO) },
                    onSubirNuevo = { irA(RutasTecnico.SUBIR) }
                )
            }

            composable(RutasTecnico.PERFIL) {
                PerfilScreen(
                    usuario = usuario,
                    onLogout = onLogout
                )
            }
        }
    }
}
