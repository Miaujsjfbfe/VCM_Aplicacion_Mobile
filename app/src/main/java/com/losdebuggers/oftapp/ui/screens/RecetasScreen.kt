package com.losdebuggers.oftapp.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.model.Receta
import com.losdebuggers.oftapp.ui.theme.*

private val VerdeHeader = Color(0xFF004D40)
private val TealOscuro = Color(0xFF0B5A4E)
private val TealAcento = Color(0xFF0F766E)
private val TealClaro = Color(0xFFCCFBF1)
private val TealMuyClaro = Color(0xFFE6FAF7)
private val ChevronFondo = Color(0xFFD3F4EA)

private val MoradoIconoFondo = Color(0xFFE8E6FD)

private val MoradoIcono = Color(0xFF5B4FCF)

const val TELEFONO_AYUDA_RECETAS = "800400656"

@Composable
fun RecetasScreen(
    recetas: List<Receta>,
    onVolver: () -> Unit,
    onRecetaClick: (String) -> Unit
) {
    val context = LocalContext.current

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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (recetas.isEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = ColorBlancoTarjetas,
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Text(
                        text = "No tienes recetas registradas por el momento.",
                        fontSize = 14.sp,
                        color = ColorTextoSecundario,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                recetas.forEach { receta ->
                    RecetaListaCard(
                        receta = receta,
                        onClick = { onRecetaClick(receta.id) }
                    )
                }
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
private fun CabeceraRecetas(onVolver: () -> Unit) {
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
            text = "Recetas Médicas",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
@Composable
private fun RecetaListaCard(
    receta: Receta,
    onClick: () -> Unit
) {
    val esOptica = receta.caregoria == Receta.CategoriaReceta.OPTICA
    val icono: ImageVector = if (esOptica) Icons.Default.Visibility else Icons.Default.WaterDrop
    val colorFondoIcono = if (esOptica) MoradoIconoFondo else TealClaro
    val colorIcono = if (esOptica) MoradoIcono else TealAcento

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
                color = colorFondoIcono,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = colorIcono,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(Modifier.width(14.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = receta.titulo,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTextoPrincipal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = receta.caregoria.descripcionGeneral,
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
                        tint = colorIcono,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "${receta.medico} · ${receta.fecha}",
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
                        contentDescription = "Ver detalle de receta",
                        tint = TealOscuro,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
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