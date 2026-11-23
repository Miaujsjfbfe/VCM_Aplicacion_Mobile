package com.losdebuggers.oftapp.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.ui.theme.*
import com.losdebuggers.oftapp.utils.compartirPdf
import com.losdebuggers.oftapp.utils.descargarPdfDesdeAssets
import com.losdebuggers.oftapp.utils.renderizarPdf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val TealOscuroVisor = Color(0xFF0B5A4E)
private val TealMedioVisor = Color(0xFF1C8C7A)
private val TealAcentoVisor = Color(0xFF0F766E)
private val TealClaroVisor = Color(0xFFCCFBF1)

@Composable
fun VisorPdfExamenScreen(
    tituloDocumento: String,
    nombreArchivo: String,
    resumenAccesible: String = "",
    archivoRespaldoAssets: String? = null,
    onVolver: () -> Unit
) {
    val context = LocalContext.current
    var paginas by remember(nombreArchivo) { mutableStateOf<List<Bitmap>>(emptyList()) }
    var archivoUsado by remember(nombreArchivo) { mutableStateOf(nombreArchivo) }
    var cargando by remember(nombreArchivo) { mutableStateOf(true) }
    var errorCarga by remember(nombreArchivo) { mutableStateOf<String?>(null) }

    LaunchedEffect(nombreArchivo, archivoRespaldoAssets) {
        cargando = true
        errorCarga = null
        val resultado = withContext(Dispatchers.IO) {
            val principales = renderizarPdf(context, nombreArchivo, anchoMaximo = 1200)
            if (principales.isNotEmpty()) {
                nombreArchivo to principales
            } else if (!archivoRespaldoAssets.isNullOrBlank()) {
                archivoRespaldoAssets to renderizarPdf(context, archivoRespaldoAssets, anchoMaximo = 1200)
            } else {
                nombreArchivo to emptyList()
            }
        }
        archivoUsado = resultado.first
        paginas = resultado.second
        if (resultado.second.isEmpty()) {
            errorCarga = "No se pudo renderizar '${resultado.first}'. Verifica que el PDF ficticio exista en app/src/main/assets."
        }
        cargando = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFondo)
    ) {
        // Encabezado superior con controles >= 48.dp
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(TealOscuroVisor, TealMedioVisor)),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onVolver,
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.20f),
                    modifier = Modifier
                        .size(48.dp)
                        .semantics { contentDescription = "Volver atrás del visor PDF" }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        text = tituloDocumento,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = archivoUsado,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.88f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = { descargarPdfDesdeAssets(context, archivoUsado) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Descargar informe PDF",
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = { compartirPdf(context, archivoUsado, tituloDocumento) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Compartir informe PDF",
                        tint = Color.White
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Descripción textual accesible para TalkBack / baja visión
            if (resumenAccesible.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = ColorBlancoTarjetas,
                    border = BorderStroke(1.dp, TealClaroVisor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            text = "Resumen clínico accesible (simulado)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealOscuroVisor
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = resumenAccesible,
                            fontSize = 13.sp,
                            color = ColorTextoPrincipal,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            when {
                cargando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = TealAcentoVisor)
                            Spacer(Modifier.height(10.dp))
                            Text("Renderizando páginas del PDF...", fontSize = 13.sp, color = ColorTextoSecundario)
                        }
                    }
                }

                errorCarga != null -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Visor PDF no disponible",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TealOscuroVisor
                            )
                            Text(
                                text = errorCarga ?: "",
                                fontSize = 13.sp,
                                color = ColorTextoSecundario
                            )
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        itemsIndexed(paginas) { index, bitmap ->
                            PaginaPdfZoomableCard(
                                paginaNumero = index + 1,
                                totalPaginas = paginas.size,
                                bitmap = bitmap
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaginaPdfZoomableCard(
    paginaNumero: Int,
    totalPaginas: Int,
    bitmap: Bitmap
) {
    var escala by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val ajustarEscala: (Float) -> Unit = { nueva ->
        val clamped = nueva.coerceIn(1f, 4f)
        escala = clamped
        if (clamped == 1f) offset = Offset.Zero
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Página $paginaNumero de $totalPaginas (${(escala * 100).toInt()}%)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealOscuroVisor
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = { ajustarEscala(escala - 0.5f) },
                        enabled = escala > 1f,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ZoomOut,
                            contentDescription = "Alejar página $paginaNumero",
                            tint = TealAcentoVisor
                        )
                    }
                    IconButton(
                        onClick = { ajustarEscala(escala + 0.5f) },
                        enabled = escala < 4f,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ZoomIn,
                            contentDescription = "Acercar página $paginaNumero",
                            tint = TealAcentoVisor
                        )
                    }
                    if (escala > 1f || offset != Offset.Zero) {
                        IconButton(
                            onClick = {
                                escala = 1f
                                offset = Offset.Zero
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = "Restablecer zoom de página $paginaNumero",
                                tint = TealOscuroVisor
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 280.dp)
                    .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
                    .pointerInput(escala) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            val nuevaEscala = (escala * zoom).coerceIn(1f, 4f)
                            escala = nuevaEscala
                            if (nuevaEscala > 1f) {
                                val maxX = (size.width * (nuevaEscala - 1f)) / 2f
                                val maxY = (size.height * (nuevaEscala - 1f)) / 2f
                                offset = Offset(
                                    x = (offset.x + pan.x * nuevaEscala).coerceIn(-maxX, maxX),
                                    y = (offset.y + pan.y * nuevaEscala).coerceIn(-maxY, maxY)
                                )
                            } else {
                                offset = Offset.Zero
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Página $paginaNumero de $totalPaginas del informe PDF",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            scaleX = escala
                            scaleY = escala
                            translationX = offset.x
                            translationY = offset.y
                        }
                )
            }
        }
    }
}