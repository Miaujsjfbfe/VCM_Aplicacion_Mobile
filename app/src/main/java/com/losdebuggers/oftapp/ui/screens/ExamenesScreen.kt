package com.losdebuggers.oftapp.ui.screens

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.data.FhirNetwork
import com.losdebuggers.oftapp.model.Examen
import com.losdebuggers.oftapp.ui.theme.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

// --- PALETA ---
private val VerdeHeader = Color(0xFF004D40)
private val TealOscuro = Color(0xFF0B5A4E)
private val ChevronFondo = Color(0xFFD3F4EA)

private val VerdeIconoFondo = Color(0xFFCDF5E5)
private val VerdeIcono = Color(0xFF0F766E)
private val MoradoIconoFondo = Color(0xFFE8E6FD)
private val MoradoIcono = Color(0xFF5B4FCF)
private val NaranjaIconoFondo = Color(0xFFFFE9C7)
private val NaranjaIcono = Color(0xFFD9822B)
private val AzulIconoFondo = Color(0xFFE0F2FE)
private val AzulIcono = Color(0xFF0284C7)

private val TealAcento = Color(0xFF0F766E)
private val TealClaro = Color(0xFFCCFBF1)
private val TealMuyClaro = Color(0xFFE6FAF7)

const val TELEFONO_AYUDA = "800400656" // 800-400-OJO

@Composable
fun ExamenesScreen(
    examenes: List<Examen>,
    onVolver: () -> Unit,
    onExamenClick: (String) -> Unit,
    onAddExamenes: (List<Examen>) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Sincronización automática "bajo el capó" al abrir la pantalla de exámenes
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val datos = FhirNetwork.api.obtener("Observation?_count=3")
                val recursos = FhirNetwork.recursos(datos)
                val nuevosExamenes = recursos.mapIndexed { index, res ->
                    val code = res.getAsJsonObject("code")
                    val titulo = code?.get("text")?.asString ?: "Examen Clínico Nube #${index + 1}"
                    val fecha = res.get("effectiveDateTime")?.asString?.take(10) ?: "Reciente"
                    Examen(
                        id = "fhir_auto_${System.currentTimeMillis()}_$index",
                        categoria = Examen.CategoriaExamen.FONDO_OJO,
                        titulo = titulo,
                        indicacionesMedicas = "Sincronizado automáticamente desde el servidor clínico HAPI FHIR",
                        medico = "Dr. Sistema Clínico Cloud",
                        fecha = fecha,
                        archivoPdf = "examen_3_fondo_ojo.pdf",
                        rutPaciente = ""
                    )
                }
                if (nuevosExamenes.isNotEmpty()) {
                    onAddExamenes(nuevosExamenes)
                }
            } catch (_: Exception) {
                // Silencioso si no hay internet: se muestran los exámenes locales sin interrumpir al usuario
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFondo)
    ) {
        CabeceraExamenes(onVolver = onVolver)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            examenes.forEach { examen ->
                ExamenCard(
                    examen = examen,
                    onClick = { onExamenClick(examen.id) }
                )
            }

            Spacer(Modifier.height(10.dp))

            BannerAyudaRecetas(
                onLlamar = {
                    try {
                        context.startActivity(
                            Intent(Intent.ACTION_DIAL, Uri.parse("tel:$TELEFONO_AYUDA_RECETAS"))
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                    } catch (_: Exception) {
                        Toast.makeText(context, "No se pudo abrir el marcador", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}

@Composable
private fun CabeceraExamenes(onVolver: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(VerdeHeader)
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
private fun ExamenCard(
    examen: Examen,
    onClick: () -> Unit
) {
    val estiloCategoria = estiloParaCategoria(examen.categoria)

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = estiloCategoria.colorFondo,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = estiloCategoria.icono,
                        contentDescription = null,
                        tint = estiloCategoria.colorIcono,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(Modifier.width(14.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = examen.titulo,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTextoPrincipal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = examen.categoria.descripcionSobreExamen,
                    fontSize = 12.sp,
                    color = ColorTextoSecundario,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = null,
                        tint = estiloCategoria.colorIcono,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "${examen.medico} · ${examen.fecha}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = ColorTextoPrincipal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Surface(
                shape = CircleShape,
                color = ChevronFondo,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Ver examen",
                        tint = TealOscuro,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

private data class EstiloCategoria(
    val icono: ImageVector,
    val colorFondo: Color,
    val colorIcono: Color
)

private fun estiloParaCategoria(categoria: Examen.CategoriaExamen): EstiloCategoria {
    return when (categoria) {
        Examen.CategoriaExamen.FONDO_OJO -> EstiloCategoria(
            icono = Icons.Default.Visibility,
            colorFondo = VerdeIconoFondo,
            colorIcono = VerdeIcono
        )
        Examen.CategoriaExamen.OCT_MACULAR -> EstiloCategoria(
            icono = Icons.Default.Layers,
            colorFondo = MoradoIconoFondo,
            colorIcono = MoradoIcono
        )
        Examen.CategoriaExamen.TONOMETRIA -> EstiloCategoria(
            icono = Icons.Default.TrackChanges,
            colorFondo = NaranjaIconoFondo,
            colorIcono = NaranjaIcono
        )
        Examen.CategoriaExamen.REFRACCION -> EstiloCategoria(
            icono = Icons.Default.RemoveRedEye,
            colorFondo = AzulIconoFondo,
            colorIcono = AzulIcono
        )
    }
}


@Composable
private fun BannerAyudaRecetas(onLlamar: () -> Unit) {
    val colorBorde = Color(0xFFEAB308)               // Borde amarillo pastel suave
    val colorFondo = Color(0xFFFFFBEB).copy(alpha = 0.55f) // Más pálido y transparentoso
    val colorCirculo = Color(0xFFFEF9C3).copy(alpha = 0.70f)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = colorFondo,
        border = BorderStroke(1.dp, colorBorde)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = colorCirculo,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.SupportAgent,
                        contentDescription = null,
                        tint = colorBorde,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    "¿Dudas con tu receta?",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorBorde,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "Atención oftalmológica directa",
                    fontSize = 11.sp,
                    color = colorBorde,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.width(10.dp))

            Surface(
                onClick = onLlamar,
                shape = RoundedCornerShape(50),
                color = Color.White.copy(alpha = 0.75f),
                border = BorderStroke(1.dp, colorBorde)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = colorBorde,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "Llamar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorBorde,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
