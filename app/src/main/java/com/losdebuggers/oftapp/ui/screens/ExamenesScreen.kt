package com.losdebuggers.oftapp.ui.screens

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.ui.theme.*
import java.io.File
import java.io.FileOutputStream

// --- PALETA TEAL ---
private val TealOscuro = Color(0xFF0B5A4E)
private val TealMedio = Color(0xFF1C8C7A)
private val TealAcento = Color(0xFF0F766E)
private val TealClaro = Color(0xFFCCFBF1)
private val TealMuyClaro = Color(0xFFE6FAF7)
private val GrisChip = Color(0xFFF1F5F9)

@Composable
fun ExamenesScreen(
    onVolver: () -> Unit,
    onExamenClick: (Int) -> Unit = {}
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFondo)
    ) {
        // Cabecera fija
        CabeceraExamenes(onVolver = onVolver)

        // Contenido con scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExamenDestacadoCard(onDescargar = { descargarInformePDF(context) })

            // Título del historial
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Historial de Exámenes",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = ColorTextoPrincipal
                )
                Text(
                    "1 anterior",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TealAcento
                )
            }

            ExamenHistorialCard(
                titulo = "Control Preventivo de Presión Ocular",
                fecha = "15 de Mayo, 2024",
                onDescargar = { descargarInformePDF(context) }
            )

            BannerAyuda()
        }
    }
}

@Composable
private fun CabeceraExamenes(onVolver: () -> Unit) {
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

        Text(
            "Mis Exámenes",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun ExamenDestacadoCard(onDescargar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            // Fila 1: estado + fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(shape = RoundedCornerShape(50), color = TealClaro) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircleOutline,
                            contentDescription = null,
                            tint = TealAcento,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text("Disponible", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TealAcento)
                    }
                }
                Text("28 Oct, 2024", fontSize = 12.sp, color = ColorTextoSecundario, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(14.dp))

            // Título y médico
            Text(
                "Revisión de Retina (Fondo de Ojo)",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ColorTextoPrincipal,
                lineHeight = 26.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Atendido por Dr. Álvaro Soto · Oftalmología",
                fontSize = 12.sp,
                color = ColorTextoSecundario
            )

            Spacer(Modifier.height(16.dp))

            // Caja de resultado
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = TealMuyClaro,
                border = BorderStroke(1.dp, TealClaro)
            ) {
                Column(Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = TealAcento,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Retina saludable y estable",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealAcento
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Parámetros normales. No requiere tratamiento inmediato.",
                        fontSize = 12.sp,
                        color = ColorTextoPrincipal,
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Botón principal
            Button(
                onClick = onDescargar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TealOscuro,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Text("DESCARGAR PDF", fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 0.5.sp)
            }
        }
    }
}

@Composable
private fun ExamenHistorialCard(
    titulo: String,
    fecha: String,
    onDescargar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Surface(shape = RoundedCornerShape(8.dp), color = GrisChip) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = ColorTextoSecundario,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Completado", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTextoSecundario)
                    }
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    titulo,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTextoPrincipal,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = ColorTextoSecundario,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(fecha, fontSize = 12.sp, color = ColorTextoSecundario)
                }
            }

            Spacer(Modifier.width(12.dp))

            Surface(
                onClick = onDescargar,
                shape = CircleShape,
                color = TealMuyClaro,
                border = BorderStroke(1.dp, TealClaro),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Download,
                        contentDescription = "Descargar PDF",
                        tint = TealOscuro,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BannerAyuda() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = TealMuyClaro,
        border = BorderStroke(1.dp, TealClaro)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = TealClaro,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.SupportAgent,
                        contentDescription = null,
                        tint = TealAcento,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Text(
                text = buildAnnotatedString {
                    append("¿Dudas con tus resultados? Llámanos al ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("800-400-OJO") }
                    append(" y revisamos tu informe contigo.")
                },
                fontSize = 12.sp,
                color = ColorTextoPrincipal,
                lineHeight = 17.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Genera y guarda un PDF real en la carpeta Descargas del dispositivo.
 */
fun descargarInformePDF(context: Context) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas: Canvas = page.canvas
    val paint = Paint()

    var y = 50f
    val xMargin = 40f

    // --- ENCABEZADO Y TÍTULO ---
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    paint.textSize = 16f
    canvas.drawText("OFTAPP - CENTRO CLÍNICO OFTALMOLÓGICO", xMargin, y, paint)

    y += 20f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.textSize = 11f
    canvas.drawText("Unidad de Diagnóstico y Prevención Ocular", xMargin, y, paint)

    y += 15f
    canvas.drawText("Puerto Montt, Chile • Teléfono: +56 9 1234 5678", xMargin, y, paint)

    y += 20f
    paint.strokeWidth = 1f
    canvas.drawLine(xMargin, y, 555f, y, paint)

    y += 30f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    paint.textSize = 14f
    canvas.drawText("INFORME DE ATENCIÓN Y EXAMEN OFTALMOLÓGICO", xMargin, y, paint)

    y += 20f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.textSize = 10f
    canvas.drawText("Fecha de emisión: 15 de Mayo, 2024", xMargin, y, paint)
    y += 15f
    canvas.drawText("Establecimiento: Sede Principal OftApp", xMargin, y, paint)

    // --- SECCIÓN 1: DATOS DEL PACIENTE ---
    y += 30f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    paint.textSize = 12f
    canvas.drawText("1. Datos del Paciente", xMargin, y, paint)

    y += 18f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.textSize = 10f
    canvas.drawText("Nombre: Paciente Demostración", xMargin + 10f, y, paint)
    y += 15f
    canvas.drawText("RUT: 12.345.678-9", xMargin + 10f, y, paint)
    y += 15f
    canvas.drawText("Previsión: FONASA / Isapre", xMargin + 10f, y, paint)

    // --- SECCIÓN 2: MOTIVO DE CONSULTA ---
    y += 25f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    paint.textSize = 12f
    canvas.drawText("2. Motivo de Consulta y Examen", xMargin, y, paint)

    y += 18f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.textSize = 10f
    canvas.drawText("Examen realizado: Control Preventivo de Presión Ocular (Tonometría de Aplanación)", xMargin + 10f, y, paint)
    y += 15f
    canvas.drawText("Tecnólogo Méd. a cargo: Dr. Roberto Gómez", xMargin + 10f, y, paint)

    // --- SECCIÓN 3: RESULTADOS CLÍNICOS ---
    y += 25f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    paint.textSize = 12f
    canvas.drawText("3. Resultados Clínicos", xMargin, y, paint)

    y += 18f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.textSize = 10f
    canvas.drawText("Presión Intraocular Ojo Derecho (OD): 15 mmHg", xMargin + 10f, y, paint)
    y += 15f
    canvas.drawText("Presión Intraocular Ojo Izquierdo (OI): 14 mmHg", xMargin + 10f, y, paint)
    y += 15f
    canvas.drawText("Rango de Referencia Saludable: 10.0 mmHg a 21.0 mmHg", xMargin + 10f, y, paint)

    // --- SECCIÓN 4: CONCLUSIÓN MÉDICA ---
    y += 25f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    paint.textSize = 12f
    canvas.drawText("4. Conclusión Médica", xMargin, y, paint)

    y += 18f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.textSize = 10f
    canvas.drawText("Presión en ambos ojos dentro de los rangos habituales y saludables.", xMargin + 10f, y, paint)
    y += 15f
    canvas.drawText("No se aprecian signos de hipertensión ocular ni anomalías en la estructura examinada.", xMargin + 10f, y, paint)
    y += 15f
    canvas.drawText("Se sugiere control preventivo de rutina en un plazo de 12 meses.", xMargin + 10f, y, paint)

    pdfDocument.finishPage(page)

    val nombreArchivo = "OFTAPP_Informe_Examen.pdf"

    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, nombreArchivo)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            if (uri != null) {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
                Toast.makeText(context, "PDF guardado en la carpeta Descargas", Toast.LENGTH_LONG).show()
            }
        } else {
            val targetFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), nombreArchivo)
            FileOutputStream(targetFile).use { outputStream ->
                pdfDocument.writeTo(outputStream)
            }
            Toast.makeText(context, "PDF descargado en Descargas", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        Toast.makeText(context, "Error al descargar el PDF: ${e.message}", Toast.LENGTH_SHORT).show()
    } finally {
        pdfDocument.close()
    }
}