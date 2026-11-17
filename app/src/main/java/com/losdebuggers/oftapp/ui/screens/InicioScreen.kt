package com.losdebuggers.oftapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

data class ActividadItem(
    val id: Int,
    val titulo: String,
    val detalle: String,
    val tiempo: String,
    val tipo: String,
    val icono: ImageVector,
    val colorFondoIcono: Color,
    val colorIcono: Color,
    val colorBorde: Color
)

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
    var showHistorialBottomSheet by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val listaActividades = remember {
        listOf(
            ActividadItem(1, "Examen de Fondo de Ojo", "Informe PDF disponible", "Ayer, 16:30", "Examen", Icons.Outlined.Description, TealMuyClaro, TealAcento, TealClaro),
            ActividadItem(2, "Receta Médica Renovada", "Gotas Lágrimas Artificiales", "Hace 3 días", "Receta", Icons.Default.MedicalServices, TealMuyClaro, TealAcento, TealClaro),
            ActividadItem(3, "Control Oftalmológico", "Dra. Marcela Silva • Providencia", "12 Oct", "Control", Icons.Default.EventAvailable, AmbarFondo, AmbarTexto, Color(0xFFFDE68A)),
            ActividadItem(4, "Topografía Corneal", "Resultados listos", "5 Oct", "Examen", Icons.Outlined.Description, TealMuyClaro, TealAcento, TealClaro),
            ActividadItem(5, "Receta Lentes de Contacto", "Medida -2.50 OD / -2.25 OI", "28 Sep", "Receta", Icons.Default.MedicalServices, TealMuyClaro, TealAcento, TealClaro),
            ActividadItem(6, "Examen de Agudeza Visual", "Certificado emitido", "15 Sep", "Examen", Icons.Outlined.Description, TealMuyClaro, TealAcento, TealClaro),
            ActividadItem(7, "Control Post-Operatorio", "Dr. Juan Pérez • Las Condes", "01 Sep", "Control", Icons.Default.EventAvailable, AmbarFondo, AmbarTexto, Color(0xFFFDE68A))
        )
    }

    val actividadesFiltradas = remember(searchQuery, listaActividades) {
        if (searchQuery.isBlank()) {
            listaActividades
        } else {
            listaActividades.filter {
                it.titulo.contains(searchQuery, ignoreCase = true) ||
                it.detalle.contains(searchQuery, ignoreCase = true) ||
                it.tipo.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    val proximamente: (String) -> Unit = { modulo ->
        scope.launch { snackbar.showSnackbar("$modulo estará disponible pronto") }
    }

    Box(Modifier.fillMaxSize().background(ColorFondo)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Cabecera + tarjeta de estado
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
                        modifier = Modifier.clickable { showHistorialBottomSheet = true }
                    )
                }
            }

            // 4. Lista de actividades (muestra las 3 principales en el Home)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listaActividades.take(3).forEach { act ->
                        ActividadCard(
                            icono = act.icono,
                            titulo = act.titulo,
                            detalle = act.detalle,
                            tiempo = act.tiempo,
                            colorFondoIcono = act.colorFondoIcono,
                            colorIcono = act.colorIcono,
                            colorBorde = act.colorBorde
                        )
                    }
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

        // Panel Modal Estático Personalizado con Animación Fluida de Slide & Fade
        AnimatedVisibility(
            visible = showHistorialBottomSheet,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        showHistorialBottomSheet = false
                        searchQuery = ""
                    },
                contentAlignment = Alignment.BottomCenter
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { /* Consumir clics para que no se cierre al presionar dentro del panel */ },
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                    color = ColorBlancoTarjetas,
                    shadowElevation = 16.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        // Cabecera con Título y Botón Cerrar (X)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    "Historial Clínico Reciente",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTextoPrincipal
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "Busca y revisa todos tus exámenes, recetas y controles.",
                                    fontSize = 13.sp,
                                    color = ColorTextoSecundario
                                )
                            }
                            IconButton(
                                onClick = {
                                    showHistorialBottomSheet = false
                                    searchQuery = ""
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cerrar",
                                    tint = ColorTextoPrincipal
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        // Barra de búsqueda
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Buscar por examen, receta o tipo...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TealAcento) },
                            trailingIcon = if (searchQuery.isNotEmpty()) {
                                {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Limpiar", tint = ColorTextoSecundario)
                                    }
                                }
                            } else null,
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TealAcento,
                                unfocusedBorderColor = Color(0xFFD1D5DB)
                            )
                        )

                        Spacer(Modifier.height(16.dp))

                        // Contenedor estático con scroll 100% fluido (LazyColumn)
                        Surface(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            color = ColorFondo,
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                            ) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    contentPadding = PaddingValues(8.dp)
                                ) {
                                    items(actividadesFiltradas) { act ->
                                        ActividadCard(
                                            icono = act.icono,
                                            titulo = act.titulo,
                                            detalle = act.detalle,
                                            tiempo = act.tiempo,
                                            colorFondoIcono = act.colorFondoIcono,
                                            colorIcono = act.colorIcono,
                                            colorBorde = act.colorBorde
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
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
