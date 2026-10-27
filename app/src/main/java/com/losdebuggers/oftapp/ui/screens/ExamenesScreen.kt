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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.ui.theme.*
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamenesScreen(
    onVolver: () -> Unit,
    onExamenClick: (Int) -> Unit = {}
) {
    // Obtenemos el contexto de Android para interactuar con el almacenamiento y mostrar Toasts
    val context = LocalContext.current

    Scaffold(
        containerColor = ColorFondo,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Historial de Exámenes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = ColorTextoPrincipal
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = ColorTextoPrincipal
                        )
                    }
                },
                actions = {
                    Text(
                        text = "1 anterior",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = ColorTextoSecundario,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorBlancoTarjetas)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TARJETA PRINCIPAL: Control Preventivo de Presión Ocular
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* No hace nada por ahora */ },
                colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    // Cabecera de la tarjeta (Chip de Estado e Icono lateral)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = ColorSecundario,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = ColorPrincipal
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Completado y Archivado",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorPrincipal
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier.size(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = ColorSecundario
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = ColorPrincipal,
                                    modifier = Modifier.size(22.sp.value.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Título del Examen
                    Text(
                        text = "Control Preventivo de Presión Ocular",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTextoPrincipal,
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Fecha de realización
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = ColorPrincipal
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Realizado el 15 de Mayo, 2024",
                            fontSize = 13.sp,
                            color = ColorTextoSecundario
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Subtarjeta interna de Conclusión
                    Card(
                        colors = CardDefaults.cardColors(containerColor = ColorFondo),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Conclusión: Presión en ambos ojos dentro de los rangos habituales y saludables.",
                                fontSize = 13.sp,
                                color = ColorTextoPrincipal,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón inferior para Descargar PDF Real (Paso 2 integrado)
                    Button(
                        onClick = {
                            descargarInformePDF(context)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorSecundario,
                            contentColor = ColorPrincipal
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = ColorPrincipal
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Descargar PDF (Mayo 2024)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = ColorPrincipal
                        )
                    }
                }
            }
        }
    }
}

/**
 * Función auxiliar (Paso 1) encargada de generar y guardar el archivo PDF real
 * en el almacenamiento del dispositivo utilizando PdfDocument.
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