package com.losdebuggers.oftapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.ui.theme.*

// --- PALETA TEAL ---
private val TealOscuro = Color(0xFF0B5A4E)
private val TealMedio = Color(0xFF1C8C7A)
private val TealAcento = Color(0xFF0F766E)
private val TealClaro = Color(0xFFCCFBF1)
private val TealMuyClaro = Color(0xFFE6FAF7)
private val AzulFondo = Color(0xFFDBEAFE)
private val AzulIcono = Color(0xFF1D4ED8)
private val BordeGris = Color(0xFFE2E8F0)
private val Solecito = Color(0xFFF59E0B)
private val Lunita = Color(0xFF6D5BD0)

@Composable
fun RecetasScreen(
    onVolver: () -> Unit
) {
    val context = LocalContext.current
    val descargar: (String) -> Unit = { nombre ->
        // TODO: reemplazar por la generación real del PDF
        Toast.makeText(context, "$nombre: descarga disponible pronto", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFondo)
    ) {
        CabeceraRecetas(onVolver = onVolver)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RecetaLentesCard(onDescargar = { descargar("Receta de lentes") })
            RecetaGotasCard(onDescargar = { descargar("Receta de gotas") })
        }
    }
}

@Composable
private fun CabeceraRecetas(onVolver: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(TealOscuro, TealMedio)))
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            onClick = onVolver,
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.18f),
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Text("Recetas Médicas", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

/** Fila superior común: ícono + título/subtítulo + chip de estado. */
@Composable
private fun EncabezadoReceta(
    icono: ImageVector,
    colorFondoIcono: Color,
    colorIcono: Color,
    titulo: String,
    subtitulo: String,
    colorSubtitulo: Color,
    chip: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(shape = CircleShape, color = colorFondoIcono, modifier = Modifier.size(48.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icono, contentDescription = null, tint = colorIcono, modifier = Modifier.size(24.dp))
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(
                titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ColorTextoPrincipal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                subtitulo,
                fontSize = 12.sp,
                color = colorSubtitulo,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.width(8.dp))

        Surface(
            shape = RoundedCornerShape(50),
            color = TealMuyClaro,
            border = BorderStroke(1.dp, TealClaro)
        ) {
            Text(
                chip,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TealAcento,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun BotonDescargarReceta(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = TealOscuro, contentColor = Color.White),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text("Descargar receta (PDF)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

// ---------------------------------------------------------------------------
// TARJETA 1: LENTES
// ---------------------------------------------------------------------------
@Composable
private fun RecetaLentesCard(onDescargar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            EncabezadoReceta(
                icono = Icons.Default.Visibility,
                colorFondoIcono = AzulFondo,
                colorIcono = AzulIcono,
                titulo = "Lentes Cerca y Lejos",
                subtitulo = "Uso diario para lectura y distancia",
                colorSubtitulo = ColorTextoSecundario,
                chip = "Vigente"
            )

            Spacer(Modifier.height(16.dp))

            // Graduación por ojo
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = TealMuyClaro,
                border = BorderStroke(1.dp, TealClaro)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GraduacionOjo(
                        modifier = Modifier.weight(1f),
                        etiqueta = "Ojo Derecho (OD)",
                        esfera = "+1.25",
                        cilindro = "Cil: -0.50 (90°)"
                    )
                    GraduacionOjo(
                        modifier = Modifier.weight(1f),
                        etiqueta = "Ojo Izquierdo (OI)",
                        esfera = "+1.50",
                        cilindro = "Cil: -0.25 (85°)"
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // Fechas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FechaItem(Icons.Default.CalendarToday, "Emisión: 12 May 2024")
                FechaItem(Icons.Default.Schedule, "Vence: 12 May 2025")
            }

            Spacer(Modifier.height(16.dp))

            BotonDescargarReceta(onClick = onDescargar)
        }
    }
}

@Composable
private fun GraduacionOjo(
    modifier: Modifier = Modifier,
    etiqueta: String,
    esfera: String,
    cilindro: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BordeGris)
    ) {
        Column(Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    etiqueta,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TealAcento,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Default.Visibility, null, tint = TealAcento, modifier = Modifier.size(14.dp))
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ColorTextoPrincipal)) {
                        append(esfera)
                    }
                    withStyle(SpanStyle(fontSize = 12.sp, color = ColorTextoSecundario)) {
                        append(" Esf")
                    }
                }
            )

            Spacer(Modifier.height(4.dp))

            Text(cilindro, fontSize = 11.sp, color = ColorTextoSecundario, maxLines = 1)
        }
    }
}

@Composable
private fun FechaItem(icono: ImageVector, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, null, tint = TealAcento, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(6.dp))
        Text(texto, fontSize = 12.sp, color = ColorTextoSecundario)
    }
}

// ---------------------------------------------------------------------------
// TARJETA 2: GOTAS
// ---------------------------------------------------------------------------
@Composable
private fun RecetaGotasCard(onDescargar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            EncabezadoReceta(
                icono = Icons.Default.WaterDrop,
                colorFondoIcono = TealClaro,
                colorIcono = TealAcento,
                titulo = "Gotas Oftálmicas",
                subtitulo = "Para sequedad y descanso ocular",
                colorSubtitulo = TealAcento,
                chip = "En curso"
            )

            Spacer(Modifier.height(16.dp))

            // Dosis
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = TealMuyClaro,
                border = BorderStroke(1.dp, TealClaro)
            ) {
                Column(Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "INDICACIÓN Y DOSIS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTextoSecundario
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, null, tint = TealAcento, modifier = Modifier.size(13.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Cada 8 hrs", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TealAcento)
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("1 gota") }
                            append(" en ambos ojos")
                        },
                        fontSize = 18.sp,
                        color = ColorTextoPrincipal
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        "Aplicar cada 8 horas según pauta oftalmológica",
                        fontSize = 12.sp,
                        color = ColorTextoSecundario,
                        lineHeight = 17.sp
                    )

                    Spacer(Modifier.height(12.dp))

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(TealClaro)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text("Horarios recomendados:", fontSize = 11.sp, color = ColorTextoSecundario)

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        HorarioChip(Modifier.weight(1f), Icons.Default.WbSunny, Solecito, "08:00")
                        HorarioChip(Modifier.weight(1f), Icons.Default.WbSunny, Solecito, "16:00")
                        HorarioChip(Modifier.weight(1f), Icons.Default.NightsStay, Lunita, "22:00")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            BotonDescargarReceta(onClick = onDescargar)
        }
    }
}

@Composable
private fun HorarioChip(
    modifier: Modifier = Modifier,
    icono: ImageVector,
    colorIcono: Color,
    hora: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        border = BorderStroke(1.dp, TealClaro)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icono, null, tint = colorIcono, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(5.dp))
            Text(hora, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTextoPrincipal)
        }
    }
}