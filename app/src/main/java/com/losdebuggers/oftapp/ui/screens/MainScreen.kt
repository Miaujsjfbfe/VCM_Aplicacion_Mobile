package com.losdebuggers.oftapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.losdebuggers.oftapp.model.Usuario
import com.losdebuggers.oftapp.ui.navigation.Screen
import com.losdebuggers.oftapp.viewmodel.ExamenViewModel
import com.losdebuggers.oftapp.ui.theme.*
import androidx.compose.ui.unit.dp
@Composable
fun MainScreen(
    usuario: Usuario,
    examenViewModel: ExamenViewModel,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = ColorBlancoTarjetas,
                tonalElevation = 8.dp
            ) {
                // 1. Inicio
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio", fontWeight = if (currentRoute == Screen.Inicio.route) FontWeight.Bold else FontWeight.Normal) },
                    selected = currentRoute == Screen.Inicio.route,
                    onClick = {
                        navController.navigate(Screen.Inicio.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ColorPrincipal,
                        selectedTextColor = ColorPrincipal,
                        indicatorColor = ColorSecundario,
                        unselectedIconColor = ColorTextoSecundario,
                        unselectedTextColor = ColorTextoSecundario
                    )
                )

                // 2. Exámenes
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Visibility, contentDescription = "Exámenes") },
                    label = { Text("Exámenes", fontWeight = if (currentRoute == Screen.Examenes.route) FontWeight.Bold else FontWeight.Normal) },
                    selected = currentRoute == Screen.Examenes.route,
                    onClick = {
                        navController.navigate(Screen.Examenes.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ColorPrincipal,
                        selectedTextColor = ColorPrincipal,
                        indicatorColor = ColorSecundario,
                        unselectedIconColor = ColorTextoSecundario,
                        unselectedTextColor = ColorTextoSecundario
                    )
                )

                // 3. Recetas
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Recetas") },
                    label = { Text("Recetas", fontWeight = if (currentRoute == Screen.Recetas.route) FontWeight.Bold else FontWeight.Normal) },
                    selected = currentRoute == Screen.Recetas.route,
                    onClick = {
                        navController.navigate(Screen.Recetas.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ColorPrincipal,
                        selectedTextColor = ColorPrincipal,
                        indicatorColor = ColorSecundario,
                        unselectedIconColor = ColorTextoSecundario,
                        unselectedTextColor = ColorTextoSecundario
                    )
                )

                // 4. Perfil
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil", fontWeight = if (currentRoute == Screen.Perfil.route) FontWeight.Bold else FontWeight.Normal) },
                    selected = currentRoute == Screen.Perfil.route,
                    onClick = {
                        navController.navigate(Screen.Perfil.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ColorPrincipal,
                        selectedTextColor = ColorPrincipal,
                        indicatorColor = ColorSecundario,
                        unselectedIconColor = ColorTextoSecundario,
                        unselectedTextColor = ColorTextoSecundario
                    )
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Inicio.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Inicio.route) {
                InicioScreen(
                    usuario = usuario,
                    onNavigateToExamenes = { navController.navigate(Screen.Examenes.route) },
                    onNavigateToRecetas = { navController.navigate(Screen.Recetas.route) }, // <--- Aquí
                    onLogout = onLogout
                )
            }
            composable(Screen.Examenes.route) {
                ExamenesScreen(
                    onVolver = { navController.popBackStack() },
                    onExamenClick = { id -> navController.navigate(Screen.DetalleExamen.createRoute(id)) }
                )
            }
            composable(Screen.Recetas.route) {
                RecetasScreen(
                    onVolver = { navController.popBackStack() }
                )
            }
            composable(Screen.Perfil.route) {
                PerfilScreen(
                    usuario = usuario,
                    onLogout = onLogout
                )
            }
        }
    }
}