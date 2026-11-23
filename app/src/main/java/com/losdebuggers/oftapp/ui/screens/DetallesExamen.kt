package com.losdebuggers.oftapp.ui.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.model.Examen
import com.losdebuggers.oftapp.model.ImagenClinica
import com.losdebuggers.oftapp.ui.theme.*
import com.losdebuggers.oftapp.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// --- PALETA ---
private val VerdeHeader = Color(0xFF004D40)
private val TealOscuro = Color(0xFF0B5A4E)
private val TealMedio = Color(0xFF1C8C7A)
private val TealAcento = Color(0xFF0F766E)
private val TealClaro = Color(0xFFCCFBF1)
private val TealMuyClaro = Color(0xFFE6FAF7)

private val AzulFondo = Color(0xFFEFF8FF)
private val AzulBorde = Color(0xFFBAE6FD)
private val AzulIconoFondo = Color(0xFF0284C7)
private val AzulTexto = Color(0xFF0369A1)

private val VioletaFondo = Color(0xFFF1F3FF)
private val VioletaBorde = Color(0xFFDDE1FB)
private val VioletaTexto = Color(0xFF3B3A8F)

private val AmbarFondo = Color(0xFFFFFBEB)
private val AmbarBorde = Color(0xFFFDE68A)
private val AmbarTexto = Color(0xFF78350F)
private val AmbarPunto = Color(0xFFD97706)

private val RojoPdfFondo = Color(0xFFFEE2E2)
private val RojoPdf = Color(0xFFB91C1C)
private val GrisBorde = Color(0xFFE2E8F0)
private val GrisSkeleton = Color(0xFFE2E8F0)

@Composable
fun DetalleExamenScreen(
    examen: Examen,
    onVolver: () -> Unit,
    onDescargarPdf: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    var imagenSeleccionadaParaVisor by remember { mutableStateOf<ImagenClinica?>(null) }
    var verPdfEnPantallaCompleta by remember { mutableStateOf(false) }

    var estaLeyendo by remember { mutableStateOf(false) }
    val ttsHelper = remember {
        TextToSpeechHelper(
            context = context,
            onSpeakingChanged = { leyendo ->
                estaLeyendo = leyendo
            }
        )
    }
    androidx.compose.runtime.DisposableEffect(Unit) {
        onDispose {
            ttsHelper.destruir()
        }
    }
    val textoLectura = "Examen ${examen.titulo}. Atendido por el doctor ${examen.medico} en fecha ${examen.fecha}. Indicaciones médicas: ${examen.indicacionesMedicas}"

    val manejarDescarga: (String) -> Unit = onDescargarPdf ?: { nombreArchivo ->
        descargarPdfDesdeAssets(
            context = context,
            nombreArchivo = nombreArchivo,
            mensajeExito = "Examen guardado en Descargas"
        )
    }

    if (verPdfEnPantallaCompleta) {
        VisorPdfDialog(
            archivoPdf = examen.archivoPdf,
            tituloExamen = examen.titulo,
            onDismiss = { verPdfEnPantallaCompleta = false }
        )
    }

    imagenSeleccionadaParaVisor?.let { imagen ->
        VisorImagenMedicaDialog(
            imagenClinica = imagen,
            onDismiss = { imagenSeleccionadaParaVisor = null }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFondo)
    ) {
        CabeceraDetalle(onVolver = onVolver)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // BOTÓN DE ACCESIBILIDAD Y LECTURA POR VOZ (TEXTTOSPEECH - WCAG 2.2 AA)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Lectura por voz accesible con TextToSpeech para TalkBack" },
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFE0F2FE),
                border = BorderStroke(1.dp, Color(0xFFBAE6FD))
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (estaLeyendo) Color(0xFFB91C1C) else Color(0xFF0284C7),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (estaLeyendo) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                if (estaLeyendo) "Reproduciendo lectura..." else "Lectura Accesible (TTS)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0369A1)
                            )
                            Text(
                                if (estaLeyendo) "Toca el botón rojo para detener" else "Escuchar resumen médico en voz alta",
                                fontSize = 11.sp,
                                color = Color(0xFF0369A1).copy(alpha = 0.8f)
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (estaLeyendo) {
                                ttsHelper.detener()
                            } else {
                                ttsHelper.hablar(textoLectura)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (estaLeyendo) Color(0xFFB91C1C) else Color(0xFF0284C7)
                        ),
                        modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                    ) {
                        Icon(
                            imageVector = if (estaLeyendo) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = if (estaLeyendo) "Detener lectura" else "Reproducir voz",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            InformacionExamenCard(examen = examen)

            if (examen.imagenesClinicas.isNotEmpty()) {
                SeccionImagenesClinicas(
                    imagenes = examen.imagenesClinicas,
                    onImagenClick = { imagenSeleccionadaParaVisor = it }
                )
            }

            InformePdfCard(
                examen = examen,
                onAbrirPdfCompleto = { verPdfEnPantallaCompleta = true }
            )

            BotonDescargarInforme(
                onClick = { manejarDescarga(examen.archivoPdf) }
            )

            AyudaExamen(onClick = {
                Toast.makeText(context, "Llámanos al 800-400-OJO", Toast.LENGTH_SHORT).show()
            })
        }
    }
}

@Composable
private fun SeccionImagenesClinicas(
    imagenes: List<ImagenClinica>,
    onImagenClick: (ImagenClinica) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AzulIconoFondo,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.ZoomIn,
                            contentDescription = null,
                            tint = AzulTexto,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Estudios e Imágenes Médicas",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTextoPrincipal
                    )
                    Text(
                        "Coil & Telephoto (Zoom y Desplazamiento)",
                        fontSize = 11.sp,
                        color = ColorTextoSecundario
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                imagenes.forEach { imagen ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onImagenClick(imagen) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Black),
                                contentAlignment = Alignment.Center
                            ) {
                                coil.compose.AsyncImage(
                                    model = imagen.urlOAsset,
                                    contentDescription = imagen.titulo,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                Surface(
                                    color = Color(0xCC0F172A),
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.ZoomIn,
                                            contentDescription = null,
                                            tint = Color(0xFF38BDF8),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            "Tocar para Zoom & Pan",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                                Surface(
                                    color = Color(0xFF0284C7),
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = imagen.ojo,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = imagen.titulo,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTextoPrincipal
                            )
                            Text(
                                text = imagen.descripcion,
                                fontSize = 11.sp,
                                color = ColorTextoSecundario,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CabeceraDetalle(onVolver: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(TealOscuro)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onVolver,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver a mis exámenes",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(Modifier.width(8.dp))
        Text("Mis Exámenes", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
private fun PildoraCategoriaExamen(texto: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = TealMuyClaro,
        border = BorderStroke(1.dp, TealClaro)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = null,
                tint = TealAcento,
                modifier = Modifier.size(12.dp)
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = texto,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                color = TealAcento,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun InformacionExamenCard(examen: Examen) {
    val etiquetaCategoria = when (examen.categoria) {
        Examen.CategoriaExamen.REFRACCION -> "REFRACCIÓN OCULAR"
        Examen.CategoriaExamen.TONOMETRIA -> "PRESIÓN INTRAOCULAR"
        Examen.CategoriaExamen.FONDO_OJO -> "RETINA Y NERVIO ÓPTICO"
        Examen.CategoriaExamen.OCT_MACULAR -> "RETINA Y MÁCULA"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f, fill = false)) {
                    PildoraCategoriaExamen(texto = etiquetaCategoria)
                }
                Spacer(Modifier.width(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = ColorTextoSecundario,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = examen.fecha,
                        fontSize = 12.sp,
                        color = ColorTextoSecundario,
                        maxLines = 1
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Text(
                examen.titulo,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = TealOscuro,
                lineHeight = 26.sp
            )

            Spacer(Modifier.height(16.dp))
            Box(Modifier.fillMaxWidth().height(1.dp).background(TealClaro))
            Spacer(Modifier.height(12.dp))

            // --- Tarjeta del doctor ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = AzulFondo,
                border = BorderStroke(1.dp, AzulBorde)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(shape = CircleShape, color = AzulIconoFondo, modifier = Modifier.size(28.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.MedicalServices, null, tint = Color.White, modifier = Modifier.size(15.dp))
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            examen.medico,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTextoPrincipal,
                            lineHeight = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            "Especialista Oftalmología",
                            fontSize = 10.sp,
                            color = AzulTexto,
                            lineHeight = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // --- Sobre este examen ---
            BloqueInformativo(
                icono = Icons.Default.Info,
                titulo = "SOBRE ESTE EXAMEN",
                colorFondo = VioletaFondo,
                colorBorde = VioletaBorde,
                colorTitulo = VioletaTexto,
                paddingVertical = 12.dp,
                espacioTitulo = 7.dp
            ) {
                Text(
                    examen.categoria.descripcionSobreExamen,
                    fontSize = 12.sp,
                    color = ColorTextoPrincipal,
                    lineHeight = 16.sp
                )
            }

            Spacer(Modifier.height(10.dp))

            // --- Indicaciones médicas ---
            BloqueInformativo(
                icono = Icons.Default.MedicalServices,
                titulo = "INDICACIONES MÉDICAS",
                colorFondo = AmbarFondo,
                colorBorde = AmbarBorde,
                colorTitulo = AmbarTexto
            ) {
                val textoIndicaciones = examen.indicacionesMedicas.ifBlank {
                    "Sin indicaciones adicionales registradas."
                }
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        Modifier
                            .padding(top = 6.dp)
                            .size(5.dp)
                            .background(AmbarPunto, CircleShape)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = textoIndicaciones,
                        fontSize = 11.sp,
                        color = ColorTextoPrincipal,
                        lineHeight = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun BloqueInformativo(
    icono: ImageVector,
    titulo: String,
    colorFondo: Color,
    colorBorde: Color,
    colorTitulo: Color,
    paddingVertical: Dp = 9.dp,
    espacioTitulo: Dp = 5.dp,
    contenido: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = colorFondo,
        border = BorderStroke(1.dp, colorBorde)
    ) {
        Column(Modifier.padding(horizontal = 14.dp, vertical = paddingVertical)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icono,
                    null,
                    tint = colorTitulo,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    titulo,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorTitulo,
                    letterSpacing = 0.4.sp
                )
            }

            Spacer(Modifier.height(espacioTitulo))
            contenido()
        }
    }
}

@Composable
private fun InformePdfCard(
    examen: Examen,
    onAbrirPdfCompleto: (() -> Unit)? = null
) {
    val context = LocalContext.current

    var expandido by remember(examen.archivoPdf) {
        mutableStateOf(false)
    }

    var paginas by remember(examen.archivoPdf) {
        mutableStateOf<List<Bitmap>>(emptyList())
    }

    var cargando by remember(examen.archivoPdf) {
        mutableStateOf(true)
    }

    var zoomFactor by remember(examen.archivoPdf, expandido) {
        mutableFloatStateOf(1f)
    }

    var panOffset by remember(examen.archivoPdf, expandido) {
        mutableStateOf(Offset.Zero)
    }

    // Procesar el PDF fuera del hilo principal
    LaunchedEffect(examen.archivoPdf) {
        cargando = true
        zoomFactor = 1f
        panOffset = Offset.Zero

        paginas = withContext(Dispatchers.IO) {
            renderizarPdf(
                context,
                examen.archivoPdf
            )
        }

        cargando = false
    }

    val ajustarZoom: (Float) -> Unit = { nuevoZoom ->
        val clamped = nuevoZoom.coerceIn(1f, 3.5f)
        zoomFactor = clamped
        if (clamped == 1f) panOffset = Offset.Zero
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = ColorBlancoTarjetas
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // ENCABEZADO DEL PDF
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = RojoPdfFondo,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Description,
                            contentDescription = null,
                            tint = RojoPdf,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(Modifier.width(10.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Informe PDF",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTextoPrincipal
                    )

                    Text(
                        examen.archivoPdf,
                        fontSize = 11.sp,
                        color = ColorTextoSecundario,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Botón para compartir el PDF (>= 48.dp)
                Surface(
                    onClick = { compartirPdf(context, examen.archivoPdf, examen.titulo) },
                    shape = CircleShape,
                    color = TealMuyClaro,
                    border = BorderStroke(1.dp, TealClaro),
                    modifier = Modifier
                        .size(48.dp)
                        .semantics { contentDescription = "Compartir informe PDF" }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = TealAcento,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(TealClaro)
            )

            Spacer(Modifier.height(14.dp))

            // Controles de zoom cuando hay PDF renderizado
            if (!cargando && paginas.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Zoom: ${(zoomFactor * 100).toInt()}% (${paginas.size} pág.)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TealOscuro
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        IconButton(
                            onClick = { ajustarZoom(zoomFactor - 0.5f) },
                            enabled = zoomFactor > 1f,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ZoomOut,
                                contentDescription = "Alejar informe PDF",
                                tint = TealAcento
                            )
                        }
                        IconButton(
                            onClick = { ajustarZoom(zoomFactor + 0.5f) },
                            enabled = zoomFactor < 3.5f,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ZoomIn,
                                contentDescription = "Acercar informe PDF",
                                tint = TealAcento
                            )
                        }
                        if (zoomFactor > 1f || panOffset != Offset.Zero) {
                            IconButton(
                                onClick = {
                                    zoomFactor = 1f
                                    panOffset = Offset.Zero
                                },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RestartAlt,
                                    contentDescription = "Restablecer zoom del PDF",
                                    tint = TealOscuro
                                )
                            }
                        }
                    }
                }
            }

            // VISOR DEL DOCUMENTO CON SOPORTE DE PELLIZCO Y DESPLAZAMIENTO
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF2B2B2B),
                border = BorderStroke(1.dp, Color(0xFF333333))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(expandido, paginas.size) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                    if (paginas.isNotEmpty()) {
                                        val nextZoom = (zoomFactor * zoom).coerceIn(1f, 3.5f)
                                        zoomFactor = nextZoom
                                        if (nextZoom > 1f) {
                                            val maxX = (size.width * (nextZoom - 1f)) / 2f
                                            val maxY = (size.height * (nextZoom - 1f)) / 2f
                                            panOffset = Offset(
                                                x = (panOffset.x + pan.x * nextZoom).coerceIn(-maxX, maxX),
                                                y = (panOffset.y + pan.y * nextZoom).coerceIn(-maxY, maxY)
                                            )
                                        } else {
                                            panOffset = Offset.Zero
                                        }
                                    }
                                }
                            }
                    ) {
                        when {
                            cargando -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(170.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = TealAcento,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            paginas.isEmpty() -> {
                                MiniDocumento(
                                    firma = "Validado por ${examen.medico}"
                                )
                            }

                            else -> {
                                val modifierZoom = Modifier
                                    .fillMaxWidth()
                                    .graphicsLayer {
                                        scaleX = zoomFactor
                                        scaleY = zoomFactor
                                        translationX = panOffset.x
                                        translationY = panOffset.y
                                    }

                                if (!expandido) {
                                    Image(
                                        bitmap = paginas.first().asImageBitmap(),
                                        contentDescription = "Vista previa del informe PDF, página 1 de ${paginas.size}",
                                        contentScale = ContentScale.FillWidth,
                                        alignment = Alignment.TopCenter,
                                        modifier = modifierZoom
                                            .height(190.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                } else {
                                    Column(
                                        modifier = modifierZoom
                                            .heightIn(max = 480.dp)
                                            .verticalScroll(rememberScrollState()),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        paginas.forEachIndexed { indice, bitmap ->
                                            Image(
                                                bitmap = bitmap.asImageBitmap(),
                                                contentDescription = "Página ${indice + 1} de ${paginas.size} del informe PDF",
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(
                                                        bitmap.width.toFloat() /
                                                                bitmap.height.toFloat()
                                                    )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    // AVANCE + BOTÓN VER COMPLETO
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Avance del documento",
                            modifier = Modifier.padding(start = 8.dp),
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.70f)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            onAbrirPdfCompleto?.let { abrir ->
                                IconButton(
                                    onClick = abrir,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Fullscreen,
                                        contentDescription = "Pantalla completa PDF",
                                        tint = Color.White
                                    )
                                }
                            }

                            TextButton(
                                onClick = {
                                    expandido = !expandido
                                },
                                enabled = !cargando && paginas.isNotEmpty(),
                                modifier = Modifier.heightIn(min = 48.dp)
                            ) {
                                Text(
                                    if (expandido) "Contraer" else "Ver completo",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Spacer(Modifier.width(4.dp))

                                Icon(
                                    imageVector = if (expandido)
                                        Icons.Default.KeyboardArrowUp
                                    else
                                        Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
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
private fun MiniDocumento(firma: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        border = BorderStroke(1.dp, GrisBorde)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.LocalHospital, null, tint = TealOscuro, modifier = Modifier.size(13.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    "Centro Oftalmológico Visión Clara",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTextoPrincipal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(10.dp))
            BarraSkeleton(1f)
            Spacer(Modifier.height(5.dp))
            BarraSkeleton(0.7f)

            Spacer(Modifier.height(10.dp))
            Box(Modifier.fillMaxWidth().height(1.dp).background(GrisBorde))
            Spacer(Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = TealMuyClaro,
                    border = BorderStroke(1.dp, TealClaro),
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Visibility, null, tint = TealAcento, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    BarraSkeleton(0.8f)
                    Spacer(Modifier.height(5.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, null, tint = TealAcento, modifier = Modifier.size(9.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(
                            firma,
                            fontSize = 8.sp,
                            color = TealAcento,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BarraSkeleton(fraccion: Float) {
    Box(
        Modifier
            .fillMaxWidth(fraccion)
            .height(6.dp)
            .background(GrisSkeleton, RoundedCornerShape(50))
    )
}

@Composable
private fun BotonDescargarInforme(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(50))
            .background(Brush.horizontalGradient(listOf(TealMedio, TealOscuro)))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Description, null, tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
            Text(
                "Descargar Informe PDF",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun AyudaExamen(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.SupportAgent, null, tint = TealAcento, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(
            "¿Dudas con tu examen?",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = ColorTextoPrincipal
        )
    }
}