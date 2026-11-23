package com.losdebuggers.oftapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.losdebuggers.oftapp.model.Usuario
import com.losdebuggers.oftapp.viewmodel.ExamenViewModel
import com.losdebuggers.oftapp.viewmodel.RecetaViewModel

private val TealOscuro = Color(0xFF0B5A4E)
private val TealClaro = Color(0xFFCCFBF1)
private val GrisInactivo = Color(0xFF6B7280)

// Círculo del ítem seleccionado: teal claro con transparencia para que sea más suave
private val IndicadorSuave = TealClaro.copy(alpha = 0.55f)

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

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            BarraNavegacion(
                seccionActual = seccionActual,
                onInicio = { irA(RutasMain.INICIO) },
                onExamenes = { irA(RutasMain.EXAMENES) },
                onRecetas = { irA(RutasMain.RECETAS) },
                onPerfil = { irA(RutasMain.PERFIL) }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RutasMain.INICIO,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(RutasMain.INICIO) {
                InicioScreen(
                    usuario = usuario,
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

// ---------------------------------------------------------------------------
// BARRA INFERIOR PERSONALIZADA
// ---------------------------------------------------------------------------
@Composable
private fun BarraNavegacion(
    seccionActual: String?,
    onInicio: () -> Unit,
    onExamenes: () -> Unit,
    onRecetas: () -> Unit,
    onPerfil: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemBarra(
                modifier = Modifier.weight(1f),
                icono = Icons.Default.Home,
                etiqueta = "Inicio",
                seleccionado = seccionActual == RutasMain.INICIO,
                onClick = onInicio
            )
            ItemBarra(
                modifier = Modifier.weight(1f),
                icono = Icons.Default.Visibility,
                etiqueta = "Exámenes",
                seleccionado = seccionActual == RutasMain.EXAMENES,
                onClick = onExamenes
            )
            ItemBarra(
                modifier = Modifier.weight(1f),
                icono = Icons.AutoMirrored.Filled.List,
                etiqueta = "Recetas",
                seleccionado = seccionActual == RutasMain.RECETAS,
                onClick = onRecetas
            )
            ItemBarra(
                modifier = Modifier.weight(1f),
                icono = Icons.Default.Person,
                etiqueta = "Perfil",
                seleccionado = seccionActual == RutasMain.PERFIL,
                onClick = onPerfil
            )
        }
    }
}



@Composable
private fun ItemBarra(
    modifier: Modifier = Modifier,
    icono: ImageVector,
    etiqueta: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    val color = if (seleccionado) TealOscuro else GrisInactivo

    Column(modifier = modifier
        .clip(RoundedCornerShape(46.dp))
        .clickable(
            role = Role.Tab,
            onClick = onClick
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(34.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = etiqueta,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = etiqueta,
            fontSize = 11.sp,
            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium,
            color = color,
            maxLines = 1,
            modifier = Modifier.padding(top = 1.dp)
        )
    }
}
