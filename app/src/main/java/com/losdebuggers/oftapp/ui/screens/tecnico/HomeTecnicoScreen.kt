package com.losdebuggers.oftapp.ui.screens.tecnico

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.UploadFile
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
import com.losdebuggers.oftapp.ui.screens.rememberFotoPerfil
import com.losdebuggers.oftapp.ui.theme.*
import kotlinx.coroutines.launch

// --- PALETA TEAL (misma base que InicioScreen de paciente) ---
private val TealOscuro = Color(0xFF0B5A4E)
private val TealMedio = Color(0xFF1C8C7A)
private val TealAcento = Color(0xFF0F766E)
private val TealClaro = Color(0xFFCCFBF1)
private val TealMuyClaro = Color(0xFFE6FAF7)
private val AmbarFondo = Color(0xFFFEF3C7)
private val AmbarTexto = Color(0xFF92400E)
private val NaranjaNotificacion = Color(0xFFF59E0B)
private val VerdeOnline = Color(0xFF22C55E)
// Colores sutiles y transparentosos para los dos avisos
private val AzulAvisoFondo = Color(0xFFE0F2FE).copy(alpha = 0.55f)
private val AzulAvisoBorde = Color(0xFF7DD3FC).copy(alpha = 0.50f)
private val AzulAvisoTexto = Color(0xFF0369A1)

private val MoradoAvisoFondo = Color(0xFFF3E8FF).copy(alpha = 0.55f)
private val MoradoAvisoBorde = Color(0xFFD8B4FE).copy(alpha = 0.50f)
private val MoradoAvisoTexto = Color(0xFF6B21A8)

private fun Modifier.overlapUp(altura: Dp) = this.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val px = altura.roundToPx()
    layout(placeable.width, (placeable.height - px).coerceAtLeast(0)) {
        placeable.place(0, -px)
    }
}

@Composable
fun InicioTecnicoScreen(
    usuario: Usuario,
    onNavigateToSubirExamen: () -> Unit,
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFondo),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 800.dp),
            contentPadding = PaddingValues(bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Cabecera Teal + tarjeta compacta de carga clínica
            item {
                Column(Modifier.fillMaxWidth()) {
                    CabeceraTecnico(
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
                        CentroCargaClinicaCard(
                            subidosHoy = 3,
                            onClick = onNavigateToSubirExamen
                        )
                    }
                }
            }

// 2. Pequeños avisos / recordatorios (no botones grandes de navegación, ya que está el navbar)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        color = AzulAvisoFondo,
                        border = BorderStroke(1.dp, AzulAvisoBorde)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(AzulAvisoTexto, CircleShape)
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Outlined.UploadFile,
                                contentDescription = null,
                                tint = AzulAvisoTexto,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    text = "Exámenes",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AzulAvisoTexto,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Carga en 3 pasos",
                                    fontSize = 10.sp,
                                    color = AzulAvisoTexto.copy(alpha = 0.85f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        color = MoradoAvisoFondo,
                        border = BorderStroke(1.dp, MoradoAvisoBorde)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(MoradoAvisoTexto, CircleShape)
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.Assignment,
                                contentDescription = null,
                                tint = MoradoAvisoTexto,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    text = "Recetas y guías",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MoradoAvisoTexto,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Revisión rápida",
                                    fontSize = 10.sp,
                                    color = MoradoAvisoTexto.copy(alpha = 0.85f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
            // 3. Título últimos exámenes subidos
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TituloSeccion("Últimos exámenes subidos")
                    Text(
                        "Subir nuevo",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TealAcento,
                        modifier = Modifier.clickable { onNavigateToSubirExamen() }
                    )
                }
            }

            // 4. Lista de últimos exámenes subidos
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ActividadTecnicoCard(
                        icono = Icons.Outlined.Description,
                        titulo = "Examen de Fondo de Ojo",
                        detalle = "RUT 12.345.678-9 • Dra. Marcela Silva",
                        tiempo = "Ayer, 16:30",
                        colorFondoIcono = TealMuyClaro,
                        colorIcono = TealAcento,
                        colorBorde = TealClaro,
                        onClick = onNavigateToSubirExamen
                    )
                    ActividadTecnicoCard(
                        icono = Icons.Default.MedicalServices,
                        titulo = "Receta Oftalmológica",
                        detalle = "RUT 16.789.432-1 • Dr. Diego Morales",
                        tiempo = "Hace 3 días",
                        colorFondoIcono = TealMuyClaro,
                        colorIcono = TealAcento,
                        colorBorde = TealClaro,
                        onClick = onNavigateToRecetas
                    )
                    ActividadTecnicoCard(
                        icono = Icons.Default.EventAvailable,
                        titulo = "Campo Visual Computarizado",
                        detalle = "RUT 14.222.890-K • Dra. Valentina Herrera",
                        tiempo = "12 Oct",
                        colorFondoIcono = AmbarFondo,
                        colorIcono = AmbarTexto,
                        colorBorde = Color(0xFFFDE68A),
                        onClick = onNavigateToSubirExamen
                    )
                }
            }

            // 5. Nota de respaldo
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
                    Text(
                        "Registro clínico operativo • OftApp",
                        fontSize = 11.sp,
                        color = ColorTextoSecundario
                    )
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
private fun CabeceraTecnico(
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
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 48.dp)
    ) {
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
                    Text(
                        "CLÍNICA VISIÓN CLARA",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
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
                        Icon(
                            Icons.Default.NotificationsNone,
                            contentDescription = "Notificaciones",
                            tint = TealOscuro,
                            modifier = Modifier.size(20.dp)
                        )
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    "PORTAL DEL TÉCNICO",
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
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = TealClaro.copy(alpha = 0.92f)
                    ) {
                        Text(
                            usuario.rol.ifBlank { "Tecnólogo" },
                            fontSize = 10.sp,
                            color = TealOscuro,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
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
                            Text(
                                iniciales,
                                color = TealOscuro,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
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
private fun CentroCargaClinicaCard(
    subidosHoy: Int,
    onClick: () -> Unit
) {
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = TealMuyClaro,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Visibility,
                            contentDescription = null,
                            tint = TealAcento,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }

                Spacer(Modifier.width(10.dp))

                Text(
                    text = "Centro de Carga Clínica",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = ColorTextoPrincipal,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(50),
                    color = TealClaro
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .size(6.dp)
                                .background(TealAcento, CircleShape)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = "$subidosHoy hoy",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealAcento,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Registra exámenes y recetas por paciente, asigna médico encargado y adjunta el PDF en 3 pasos.",
                fontSize = 12.sp,
                color = ColorTextoSecundario,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun TarjetaAccionPrincipal(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    badge: String,
    fondoTarjeta: Brush,
    bordeTarjeta: Color,
    fondoIcono: Color,
    colorIcono: Color,
    colorTextoPrincipal: Color,
    colorTextoSecundario: Color,
    fondoBadge: Color,
    colorTextoBadge: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, bordeTarjeta),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(fondoTarjeta)
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = fondoIcono,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icono, null, tint = colorIcono, modifier = Modifier.size(19.dp))
                    }
                }

                Surface(color = fondoBadge, shape = RoundedCornerShape(50)) {
                    Text(
                        text = badge,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorTextoBadge,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = titulo,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = colorTextoPrincipal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(3.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = descripcion,
                    fontSize = 11.sp,
                    color = colorTextoSecundario,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = colorTextoPrincipal,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

@Composable
private fun ActividadTecnicoCard(
    icono: ImageVector,
    titulo: String,
    detalle: String,
    tiempo: String,
    colorFondoIcono: Color,
    colorIcono: Color,
    colorBorde: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
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
                Text(
                    titulo,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTextoPrincipal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    detalle,
                    fontSize = 12.sp,
                    color = ColorTextoSecundario,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.width(8.dp))

            Text(
                tiempo,
                fontSize = 11.sp,
                color = ColorTextoSecundario,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

@Composable
private fun TituloSeccion(texto: String) =
    Text(texto, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ColorTextoPrincipal)