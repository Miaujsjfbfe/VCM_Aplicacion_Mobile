package com.losdebuggers.oftapp.ui.screens

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamenesScreen(
    onVolver: () -> Unit,
    onExamenClick: (Int) -> Unit = {}
) {
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
                    .clickable { onExamenClick(1) },
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

                    // Botón inferior para Descargar PDF
                    Button(
                        onClick = { /* Acción de descarga PDF */ },
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
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}