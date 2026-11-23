package com.losdebuggers.oftapp.ui.screens.tecnico

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.ui.theme.*

// Paleta Teal unificada con el flujo de OftApp
private val TealOscuro = Color(0xFF0B5A4E)
private val TealMedio = Color(0xFF1C8C7A)
private val TealAcento = Color(0xFF0F766E)
private val TealClaro = Color(0xFFCCFBF1)
private val TealMuyClaro = Color(0xFFE6FAF7)
private val AmbarFondo = Color(0xFFFEF3C7)
private val AmbarTexto = Color(0xFF92400E)

private data class RegistroHistorialTecnico(
    val id: String,
    val paciente: String,
    val rut: String,
    val tipoDocumento: String,
    val fecha: String,
    val medico: String,
    val archivoPdf: String,
    val indicaciones: String,
    val sincronizado: Boolean = true
)

private val historialDemoTecnico = listOf(
    RegistroHistorialTecnico(
        id = "EX-104",
        paciente = "Roberto Muñoz",
        rut = "12.345.678-9",
        tipoDocumento = "Fondo de Ojo",
        fecha = "22/11/2026",
        medico = "Dra. Marcela Silva",
        archivoPdf = "fondo_ojo_123456789.pdf",
        indicaciones = "Control en 6 meses con agudeza visual actualizada.",
        sincronizado = true
    ),
    RegistroHistorialTecnico(
        id = "EX-103",
        paciente = "Camila Soto",
        rut = "16.789.432-1",
        tipoDocumento = "Campo Visual",
        fecha = "22/11/2026",
        medico = "Dr. Diego Morales",
        archivoPdf = "campo_visual_167894321.pdf",
        indicaciones = "No hay indicaciones médicas para este examen",
        sincronizado = true
    ),
    RegistroHistorialTecnico(
        id = "EX-102",
        paciente = "Javier Rojas",
        rut = "14.222.890-K",
        tipoDocumento = "OCT Macular",
        fecha = "21/11/2026",
        medico = "Dra. Valentina Herrera",
        archivoPdf = "oct_macular_14222890k.pdf",
        indicaciones = "Revisar evolución macular en próxima cita.",
        sincronizado = false
    ),
    RegistroHistorialTecnico(
        id = "REC-089",
        paciente = "Renata Vera",
        rut = "18.456.123-5",
        tipoDocumento = "Receta Oftalmológica",
        fecha = "20/11/2026",
        medico = "Dra. Marcela Silva",
        archivoPdf = "receta_184561235.pdf",
        indicaciones = "Lubricante ocular cada 6 horas por 30 días.",
        sincronizado = true
    )
)

private val filtrosHistorial = listOf(
    "Todos",
    "Fondo de Ojo",
    "Campo Visual",
    "OCT Macular",
    "Receta Oftalmológica"
)

@Composable
fun HistorialExamenesTecnicoScreen(
    onVolver: () -> Unit = {},
    onSubirNuevo: () -> Unit = {}
) {
    var busqueda by remember { mutableStateOf("") }
    var filtroSeleccionado by remember { mutableStateOf("Todos") }

    val registrosFiltrados = remember(busqueda, filtroSeleccionado) {
        val query = busqueda.trim()
        historialDemoTecnico.filter { item ->
            val coincideFiltro = filtroSeleccionado == "Todos" ||
                    item.tipoDocumento.equals(filtroSeleccionado, ignoreCase = true)
            val coincideBusqueda = query.isEmpty() ||
                    item.paciente.contains(query, ignoreCase = true) ||
                    item.rut.contains(query, ignoreCase = true) ||
                    item.tipoDocumento.contains(query, ignoreCase = true) ||
                    item.medico.contains(query, ignoreCase = true)
            coincideFiltro && coincideBusqueda
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFondo)
    ) {
        // Barra superior en degradado verde / Teal
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(TealOscuro)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onVolver,
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.20f),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        text = "Historial de Exámenes",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${registrosFiltrados.size} registro(s) simulados disponibles",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.88f)
                    )
                }


            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorFondo),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 850.dp)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
            // Buscador por nombre, RUT o tipo
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Buscar por paciente, RUT...") },
                placeholder = { Text("Ej: Roberto, 12.345.678-9 o OCT") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, null, tint = TealAcento) },
                trailingIcon = {
                    if (busqueda.isNotEmpty()) {
                        IconButton(onClick = { busqueda = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar búsqueda", tint = TealAcento)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ColorBlancoTarjetas,
                    unfocusedContainerColor = ColorBlancoTarjetas,
                    focusedBorderColor = TealAcento,
                    unfocusedBorderColor = TealClaro,
                    focusedLabelColor = TealOscuro
                )
            )

            // Filtros horizontales compactos
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filtrosHistorial.forEach { filtro ->
                    val activo = filtroSeleccionado == filtro
                    FilterChip(
                        selected = activo,
                        onClick = { filtroSeleccionado = filtro },
                        label = {
                            Text(
                                text = filtro,
                                fontSize = 11.sp,
                                fontWeight = if (activo) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1,
                                softWrap = false
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = ColorBlancoTarjetas,
                            labelColor = ColorTextoPrincipal,
                            selectedContainerColor = TealClaro,
                            selectedLabelColor = TealOscuro
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = activo,
                            borderColor = TealClaro,
                            selectedBorderColor = TealAcento
                        )
                    )
                }
            }

            if (registrosFiltrados.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = TealAcento,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Sin resultados para \"$busqueda\"",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTextoPrincipal
                        )
                        Text(
                            text = "Prueba limpiando el filtro o buscando otro nombre o RUT.",
                            fontSize = 12.sp,
                            color = ColorTextoSecundario
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(registrosFiltrados, key = { it.id }) { item ->
                        TarjetaRegistroHistorial(item = item)
                    }
                }
            }
        }
    }
}
}

@Composable
private fun TarjetaRegistroHistorial(item: RegistroHistorialTecnico) {
    val badgeBg = if (item.sincronizado) TealClaro else AmbarFondo
    val badgeFg = if (item.sincronizado) TealOscuro else AmbarTexto
    val badgeTexto = if (item.sincronizado) "Sincronizado" else "Pendiente"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = TealMuyClaro,
                    border = BorderStroke(1.dp, TealClaro),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Description,
                            contentDescription = null,
                            tint = TealAcento,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.width(10.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        text = item.tipoDocumento,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTextoPrincipal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${item.paciente} • RUT ${item.rut}",
                        fontSize = 12.sp,
                        color = TealAcento,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(50),
                    color = badgeBg
                ) {
                    Text(
                        text = badgeTexto,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeFg,
                        maxLines = 1,
                        softWrap = false,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "Médico: ${item.medico} • Fecha: ${item.fecha}",
                fontSize = 11.sp,
                color = ColorTextoSecundario,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = ColorSecundario,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.PictureAsPdf,
                            contentDescription = null,
                            tint = TealAcento,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = item.archivoPdf,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TealOscuro,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = item.indicaciones,
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