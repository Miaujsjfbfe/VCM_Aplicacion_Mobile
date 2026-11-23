package com.losdebuggers.oftapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.DoneAll
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.losdebuggers.oftapp.model.Notificacion
import com.losdebuggers.oftapp.model.TipoNotificacion

private val TealOscuro = Color(0xFF0B5A4E)
private val TealMedio = Color(0xFF1C8C7A)
private val TealClaro = Color(0xFFCCFBF1)
private val TealMuyClaro = Color(0xFFE6FAF7)
private val ColorFondoDialog = Color(0xFFF8FAFC)

private val CategoriasFiltro = listOf("Todas", "Exámenes", "Recetas", "Recordatorios", "Sistema")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionesDialog(
    notificaciones: List<Notificacion>,
    onMarcarLeida: (String) -> Unit,
    onMarcarTodasLeidas: () -> Unit,
    onEliminar: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var filtroSeleccionado by remember { mutableStateOf("Todas") }

    val noLeidasCount = remember(notificaciones) {
        notificaciones.count { !it.leida }
    }

    val notificacionesFiltradas = remember(notificaciones, filtroSeleccionado) {
        when (filtroSeleccionado) {
            "Exámenes" -> notificaciones.filter { it.tipo == TipoNotificacion.EXAMEN }
            "Recetas" -> notificaciones.filter { it.tipo == TipoNotificacion.RECETA }
            "Recordatorios" -> notificaciones.filter { it.tipo == TipoNotificacion.CONTROL }
            "Sistema" -> notificaciones.filter { it.tipo == TipoNotificacion.SISTEMA }
            else -> notificaciones
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = ColorFondoDialog
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                // BARRA SUPERIOR
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Notificaciones Clínicas",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            if (noLeidasCount > 0) {
                                Spacer(Modifier.width(8.dp))
                                Surface(
                                    color = Color(0xFFEF4444),
                                    shape = CircleShape
                                ) {
                                    Text(
                                        text = "$noLeidasCount no leídas",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Cerrar",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        if (noLeidasCount > 0) {
                            IconButton(onClick = onMarcarTodasLeidas) {
                                Icon(
                                    Icons.Outlined.DoneAll,
                                    contentDescription = "Marcar todas como leídas",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = TealOscuro
                    )
                )

                // FILTROS DE CATEGORÍA
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(CategoriasFiltro) { cat ->
                        val seleccionado = cat == filtroSeleccionado
                        FilterChip(
                            selected = seleccionado,
                            onClick = { filtroSeleccionado = cat },
                            label = { Text(cat, fontSize = 12.sp, fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TealOscuro,
                                selectedLabelColor = Color.White,
                                containerColor = TealMuyClaro,
                                labelColor = TealOscuro
                            )
                        )
                    }
                }

                HorizontalDivider(color = Color(0xFFE2E8F0))

                // LISTA DE NOTIFICACIONES
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    if (notificacionesFiltradas.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Outlined.Notifications,
                                contentDescription = null,
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "No hay notificaciones en esta categoría",
                                fontSize = 14.sp,
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(notificacionesFiltradas, key = { it.id }) { notif ->
                                ItemNotificacionCard(
                                    notificacion = notif,
                                    onClick = { onMarcarLeida(notif.id) },
                                    onEliminar = { onEliminar(notif.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemNotificacionCard(
    notificacion: Notificacion,
    onClick: () -> Unit,
    onEliminar: () -> Unit
) {
    val fondoColor = if (notificacion.leida) Color.White else Color(0xFFF0FDF4)
    val bordeColor = if (notificacion.leida) Color(0xFFE2E8F0) else Color(0xFFBBF7D0)

    val (icono, colorIcono) = when (notificacion.tipo) {
        TipoNotificacion.EXAMEN -> Icons.Outlined.Description to Color(0xFF0284C7)
        TipoNotificacion.RECETA -> Icons.Outlined.Medication to Color(0xFF0F766E)
        TipoNotificacion.CONTROL -> Icons.Outlined.CheckCircle to Color(0xFFD97706)
        TipoNotificacion.SISTEMA -> Icons.Outlined.Sync to Color(0xFF6B21A8)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = fondoColor),
        border = BorderStroke(1.dp, bordeColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (notificacion.leida) 1.dp else 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = CircleShape,
                color = colorIcono.copy(alpha = 0.12f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = colorIcono,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notificacion.titulo,
                        fontSize = 14.sp,
                        fontWeight = if (notificacion.leida) FontWeight.SemiBold else FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    if (!notificacion.leida) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF22C55E), CircleShape)
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    text = notificacion.mensaje,
                    fontSize = 12.sp,
                    color = Color(0xFF334155),
                    lineHeight = 16.sp
                )

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notificacion.fechaRelativa,
                        fontSize = 10.sp,
                        color = Color(0xFF94A3B8),
                        fontWeight = FontWeight.Medium
                    )

                    IconButton(
                        onClick = onEliminar,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Eliminar notificación",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
