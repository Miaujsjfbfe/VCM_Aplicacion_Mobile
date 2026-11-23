package com.losdebuggers.oftapp.ui.screens.tecnico

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.losdebuggers.oftapp.model.Usuario
import com.losdebuggers.oftapp.ui.screens.PerfilScreen
import com.losdebuggers.oftapp.viewmodel.ExamenViewModel

private val TurquesaOscuroNav = Color(0xFF064E5C)
private val TurquesaClaroNav = Color(0xFFCFFAFE).copy(alpha = 0.65f)
private val GrisInactivoNav = Color(0xFF6B7280)

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

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            BarraNavegacionTecnico(
                rutaActual = currentRoute,
                onInicio = { irA(RutasTecnico.INICIO) },
                onSubir = { irA(RutasTecnico.SUBIR) },
                onRegistros = { irA(RutasTecnico.REGISTROS) },
                onPerfil = { irA(RutasTecnico.PERFIL) }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RutasTecnico.INICIO,
            modifier = Modifier.padding(innerPadding)
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

@Composable
private fun BarraNavegacionTecnico(
    rutaActual: String?,
    onInicio: () -> Unit,
    onSubir: () -> Unit,
    onRegistros: () -> Unit,
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
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemBarraTecnico(
                modifier = Modifier.weight(1f),
                icono = Icons.Default.Home,
                etiqueta = "Inicio",
                seleccionado = rutaActual == RutasTecnico.INICIO,
                onClick = onInicio
            )
            ItemBarraTecnico(
                modifier = Modifier.weight(1f),
                icono = Icons.Default.UploadFile,
                etiqueta = "Subir",
                seleccionado = rutaActual == RutasTecnico.SUBIR,
                onClick = onSubir
            )
            ItemBarraTecnico(
                modifier = Modifier.weight(1f),
                icono = Icons.AutoMirrored.Filled.List,
                etiqueta = "Registros",
                seleccionado = rutaActual == RutasTecnico.REGISTROS,
                onClick = onRegistros
            )
            ItemBarraTecnico(
                modifier = Modifier.weight(1f),
                icono = Icons.Default.Person,
                etiqueta = "Perfil",
                seleccionado = rutaActual == RutasTecnico.PERFIL,
                onClick = onPerfil
            )
        }
    }
}

@Composable
private fun ItemBarraTecnico(
    modifier: Modifier = Modifier,
    icono: ImageVector,
    etiqueta: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    val color = if (seleccionado) TurquesaOscuroNav else GrisInactivoNav
    val fondo = if (seleccionado) TurquesaClaroNav else Color.Transparent
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .heightIn(min = 48.dp)
            .semantics {
                contentDescription = if (seleccionado) "$etiqueta, pestaña seleccionada" else etiqueta
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Tab,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(fondo),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
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