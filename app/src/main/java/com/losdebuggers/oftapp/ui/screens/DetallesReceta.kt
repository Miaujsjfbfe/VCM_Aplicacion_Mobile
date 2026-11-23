package com.losdebuggers.oftapp.ui.screens

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.model.Receta
import com.losdebuggers.oftapp.ui.theme.*
import com.losdebuggers.oftapp.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// --- PALETA ---
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

private const val TELEFONO_AYUDA_DETALLE = "800400656" // 800-400-OJO

@Composable
fun DetalleRecetaScreen(
    receta: Receta,
    onVolver: () -> Unit,
    onDescargarPdf: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    val manejarDescarga: (String) -> Unit = onDescargarPdf ?: { nombreArchivo ->
        descargarPdfDesdeAssets(
            context = context,
            nombreArchivo = nombreArchivo,
            mensajeExito = "Receta guardada en Descargas"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFondo)
    ) {
        CabeceraDetalleReceta(onVolver = onVolver)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InformacionRecetaCard(receta = receta)

            InformePdfRecetaCard(receta = receta)

            BotonDescargarReceta(
                onClick = { manejarDescarga(receta.archivoPdf) }
            )

            AyudaReceta(onClick = {
                try {
                    context.startActivity(
                        Intent(Intent.ACTION_DIAL, Uri.parse("tel:$TELEFONO_AYUDA_DETALLE"))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                } catch (_: Exception) {
                    Toast.makeText(context, "Llámanos al 800-400-OJO", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

@Composable
private fun CabeceraDetalleReceta(onVolver: () -> Unit) {
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
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(Modifier.width(8.dp))
        Text("Recetas Médicas", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
private fun PildoraCategoriaReceta(texto: String, icono: ImageVector) {
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
                imageVector = icono,
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
private fun InformacionRecetaCard(receta: Receta) {
    val esOptica = receta.caregoria == Receta.CategoriaReceta.OPTICA
    val iconoPrincipal = if (esOptica) Icons.Default.Visibility else Icons.Default.WaterDrop

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
                    PildoraCategoriaReceta(
                        texto = receta.caregoria.etiqueta.uppercase(),
                        icono = iconoPrincipal
                    )
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
                        text = receta.fecha,
                        fontSize = 12.sp,
                        color = ColorTextoSecundario,
                        maxLines = 1
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Text(
                receta.titulo,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = TealOscuro,
                lineHeight = 26.sp
            )

            Spacer(Modifier.height(16.dp))
            Box(Modifier.fillMaxWidth().height(1.dp).background(TealClaro))
            Spacer(Modifier.height(12.dp))

            // --- Tarjeta del médico ---
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
                            receta.medico,
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

            // --- Sobre esta receta ---
            BloqueInformativo(
                icono = Icons.Default.Info,
                titulo = "SOBRE ESTA RECETA",
                colorFondo = VioletaFondo,
                colorBorde = VioletaBorde,
                colorTitulo = VioletaTexto,
                paddingVertical = 12.dp,
                espacioTitulo = 7.dp
            ) {
                Text(
                    receta.caregoria.descripcionGeneral,
                    fontSize = 12.sp,
                    color = ColorTextoPrincipal,
                    lineHeight = 16.sp
                )
            }

            Spacer(Modifier.height(10.dp))

            // --- Especificaciones médicas ---
            BloqueInformativo(
                icono = iconoPrincipal,
                titulo = "ESPECIFICACIONES MÉDICAS",
                colorFondo = AmbarFondo,
                colorBorde = AmbarBorde,
                colorTitulo = AmbarTexto
            ) {
                val textoIndicaciones = receta.indicacionesMedicas.ifBlank {
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
private fun InformePdfRecetaCard(receta: Receta) {
    val context = LocalContext.current

    var expandido by remember(receta.archivoPdf) { mutableStateOf(false) }
    var paginas by remember(receta.archivoPdf) { mutableStateOf<List<Bitmap>>(emptyList()) }
    var cargando by remember(receta.archivoPdf) { mutableStateOf(true) }

    // Procesar el PDF fuera del hilo principal
    LaunchedEffect(receta.archivoPdf) {
        cargando = true
        paginas = withContext(Dispatchers.IO) {
            renderizarPdf(context, receta.archivoPdf)
        }
        cargando = false
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

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

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Receta PDF",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTextoPrincipal
                    )
                    Text(
                        receta.archivoPdf,
                        fontSize = 11.sp,
                        color = ColorTextoSecundario,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.width(8.dp))

                // Botón redondito para compartir el PDF
                Surface(
                    onClick = { compartirPdf(context, receta.archivoPdf, receta.titulo) },
                    shape = CircleShape,
                    color = TealMuyClaro,
                    border = BorderStroke(1.dp, TealClaro),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir PDF",
                            tint = TealAcento,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))
            Box(Modifier.fillMaxWidth().height(1.dp).background(TealClaro))
            Spacer(Modifier.height(14.dp))

            // VISOR DEL DOCUMENTO
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF2B2B2B),
                border = BorderStroke(1.dp, Color(0xFF333333))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {

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
                            MiniDocumento(firma = "Emitida por ${receta.medico}")
                        }

                        !expandido -> {
                            // Vista contraída: solo el inicio de la primera página
                            Image(
                                bitmap = paginas.first().asImageBitmap(),
                                contentDescription = "Inicio de la receta PDF",
                                contentScale = ContentScale.FillWidth,
                                alignment = Alignment.TopCenter,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(190.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }

                        else -> {
                            // Vista expandida: todas las páginas con scroll
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 480.dp)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                paginas.forEachIndexed { indice, bitmap ->
                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = "Página ${indice + 1} de la receta",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat())
                                    )
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

                        TextButton(
                            onClick = { expandido = !expandido },
                            enabled = !cargando && paginas.isNotEmpty()
                        ) {
                            Text(
                                if (expandido) "Contraer" else "Ver completo",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                imageVector = if (expandido) Icons.Default.KeyboardArrowUp
                                else Icons.Default.KeyboardArrowDown,
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

/** Vista de respaldo cuando el PDF no se pudo renderizar. */
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
private fun BotonDescargarReceta(onClick: () -> Unit) {
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
                "Descargar Receta en PDF",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun AyudaReceta(onClick: () -> Unit) {
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
            "¿Dudas con tu receta?",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = ColorTextoPrincipal
        )
    }
}

/**
 * Se mantiene por compatibilidad con código que ya la use.
 * Delega en la función compartida de utils.
 */
fun descargarRecetaDesdeAssets(context: android.content.Context, nombreArchivo: String) {
    descargarPdfDesdeAssets(
        context = context,
        nombreArchivo = nombreArchivo,
        mensajeExito = "Receta guardada en Descargas"
    )
}