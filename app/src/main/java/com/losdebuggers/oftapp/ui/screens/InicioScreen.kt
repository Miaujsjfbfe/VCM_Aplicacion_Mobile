package com.losdebuggers.oftapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.model.Usuario
import com.losdebuggers.oftapp.ui.theme.*
import kotlinx.coroutines.launch

// --- PALETA TEAL ---
private val TealOscuro = Color(0xFF0B5A4E)
private val TealMedio = Color(0xFF1C8C7A)
private val TealAcento = Color(0xFF0F766E)
private val TealClaro = Color(0xFFCCFBF1)
private val TealMuyClaro = Color(0xFFE6FAF7)
private val AmbarFondo = Color(0xFFFEF3C7)
private val AmbarTexto = Color(0xFF92400E)
private val NaranjaNotificacion = Color(0xFFF59E0B)
private val VerdeOnline = Color(0xFF22C55E)

/** Sube el elemento [altura] sobre el anterior y reduce el espacio ocupado (sin huecos). */
private fun Modifier.overlapUp(altura: Dp) = this.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val px = altura.roundToPx()
    layout(placeable.width, (placeable.height - px).coerceAtLeast(0)) {
        placeable.place(0, -px)
    }
}

@Composable
fun InicioScreen(
    usuario: Usuario,
    onNavigateToExamenes: () -> Unit,
    onNavigateToRecetas: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToPerfil: () -> Unit = {}
) {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val fotoPerfil = rememberFotoPerfil(usuario.rut)

    val proximamente: (String) -> Unit = { modulo ->
        scope.launch { snackbar.showSnackbar("$modulo estará disponible pronto") }
    }

    Box(Modifier.fillMaxSize().background(ColorFondo)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Cabecera + tarjeta de estado (la tarjeta solo invade el margen inferior de la cabecera)
            item {
                Column(Modifier.fillMaxWidth()) {
                    Cabecera(
                        usuario = usuario,
                        fotoPerfil = fotoPerfil,
                        onNotificaciones = { proximamente("Notificaciones") },
                        onAvatar = onNavigateToPerfil
                    )
                    Box(
                        Modifier
                            .padding(horizontal = 16.dp)
                            .overlapUp(32.dp)
                    ) {
                        EstadoClinicoVisualCard(onClick = onNavigateToExamenes)
                    }
                }
            }

            // 2. Accesos rápidos
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(Modifier.weight(1f)) {
                        AccesoRapidoCard(
                            icono = Icons.Outlined.FolderOpen,
                            titulo = "Mis Exámenes",
                            descripcion = "Descarga de informes",
                            badge = "1 nuevo",
                            colorBadge = AmbarFondo,
                            colorTextoBadge = AmbarTexto,
                            onClick = onNavigateToExamenes
                        )
                    }
                    Box(Modifier.weight(1f)) {
                        AccesoRapidoCard(
                            icono = Icons.AutoMirrored.Outlined.Assignment,
                            titulo = "Mis Recetas",
                            descripcion = "Lentes y fármacos",
                            badge = "Vigentes",
                            colorBadge = TealClaro,
                            colorTextoBadge = TealAcento,
                            onClick = onNavigateToRecetas
                        )
                    }
                }
            }

            // 3. Título de actividad reciente
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Titulo("Actividad reciente")
                    Text(
                        "Ver todo",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TealAcento,
                        modifier = Modifier.clickable { proximamente("Historial completo") }
                    )
                }
            }

            // 4. Lista de actividades
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ActividadCard(
                        icono = Icons.Outlined.Description,
                        titulo = "Examen de Fondo de Ojo",
                        detalle = "Informe PDF disponible",
                        tiempo = "Ayer, 16:30",
                        colorFondoIcono = TealMuyClaro,
                        colorIcono = TealAcento,
                        colorBorde = TealClaro
                    )
                    ActividadCard(
                        icono = Icons.Default.MedicalServices,
                        titulo = "Receta Médica Renovada",
                        detalle = "Gotas Lágrimas Artificiales",
                        tiempo = "Hace 3 días",
                        colorFondoIcono = TealMuyClaro,
                        colorIcono = TealAcento,
                        colorBorde = TealClaro
                    )
                    ActividadCard(
                        icono = Icons.Default.EventAvailable,
                        titulo = "Control Oftalmológico",
                        detalle = "Dra. Marcela Silva • Providencia",
                        tiempo = "12 Oct",
                        colorFondoIcono = AmbarFondo,
                        colorIcono = AmbarTexto,
                        colorBorde = Color(0xFFFDE68A)
                    )
                }
            }

            // 5. Nota de privacidad
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lock, null, tint = ColorTextoSecundario, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Tus datos clínicos están protegidos", fontSize = 11.sp, color = ColorTextoSecundario)
                }
            }
        }

        SnackbarHost(
            hostState = snackbar,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun Cabecera(
    usuario: Usuario,
    fotoPerfil: ImageBitmap?,
    onNotificaciones: () -> Unit,
    onAvatar: () -> Unit
) {
    val primerNombre = usuario.nombre.trim().substringBefore(" ")
    val iniciales = usuario.nombre.trim().split(" ").take(2)
        .mapNotNull { it.firstOrNull()?.uppercase() }.joinToString("")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(TealOscuro, TealMedio, ColorFondo)))
            .statusBarsPadding()
            // bottom grande: es el espacio sobre el que se superpone la tarjeta
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 48.dp)
    ) {
        // Fila superior: clínica + campana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White.copy(alpha = 0.18f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.LocalHospital, null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("CLÍNICA VISIÓN CLARA", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Box {
                Surface(
                    onClick = onNotificaciones,
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.NotificationsNone, "Notificaciones", tint = TealOscuro, modifier = Modifier.size(20.dp))
                    }
                }
                Box(
                    Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 2.dp, y = (-2).dp)
                        .size(10.dp)
                        .background(NaranjaNotificacion, CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Saludo + avatar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    "PORTAL DEL PACIENTE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.8f),
                    letterSpacing = 0.5.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "¡Hola, $primerNombre!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFE5E7EB).copy(alpha = 0.9f)) {
                        Text(
                            "Fonasa B",
                            fontSize = 10.sp,
                            color = Color(0xFF374151),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        "RUT ${usuario.rut}",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.95f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Box(Modifier.clickable(onClick = onAvatar)) {
                Surface(
                    shape = CircleShape,
                    color = TealClaro,
                    border = BorderStroke(2.dp, Color.White),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (fotoPerfil != null) {
                            Image(
                                bitmap = fotoPerfil,
                                contentDescription = "Foto de perfil",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(iniciales, color = TealOscuro, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
                Box(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .size(13.dp)
                        .background(VerdeOnline, CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
            }
        }
    }
}

@Composable
private fun EstadoClinicoVisualCard(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(12.dp), color = TealMuyClaro, modifier = Modifier.size(40.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Visibility, null, tint = TealAcento, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("Estado Clínico Visual", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = ColorTextoPrincipal)
                }
                Surface(shape = RoundedCornerShape(50), color = TealClaro) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.size(6.dp).background(TealAcento, CircleShape))
                        Spacer(Modifier.width(5.dp))
                        Text("Al día", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TealAcento)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                "Tu salud visual está al día. Tienes 1 examen reciente disponible y tus recetas oftalmológicas vigentes para descarga.",
                fontSize = 12.sp,
                color = ColorTextoSecundario,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun AccesoRapidoCard(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    badge: String,
    colorBadge: Color,
    colorTextoBadge: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = TealMuyClaro,
                    border = BorderStroke(1.dp, TealClaro),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icono, null, tint = TealAcento, modifier = Modifier.size(18.dp))
                    }
                }
                Surface(color = colorBadge, shape = RoundedCornerShape(50)) {
                    Text(
                        badge,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorTextoBadge
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Text(titulo, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ColorTextoPrincipal)
            Spacer(Modifier.height(4.dp))
            Text(descripcion, fontSize = 11.sp, color = ColorTextoSecundario, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun ActividadCard(
    icono: ImageVector,
    titulo: String,
    detalle: String,
    tiempo: String,
    colorFondoIcono: Color,
    colorIcono: Color,
    colorBorde: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = colorFondoIcono,
                border = BorderStroke(1.dp, colorBorde),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icono, null, tint = colorIcono, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(titulo, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ColorTextoPrincipal, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(2.dp))
                Text(detalle, fontSize = 12.sp, color = ColorTextoSecundario, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }

            Spacer(Modifier.width(8.dp))

            Text(tiempo, fontSize = 11.sp, color = ColorTextoSecundario, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun Titulo(texto: String) =
    Text(texto, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ColorTextoPrincipal)