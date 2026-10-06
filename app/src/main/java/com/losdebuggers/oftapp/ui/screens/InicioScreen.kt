package com.losdebuggers.oftapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores personalizados según el diseño
val PurplePrimary = Color(0xFF4A148C)
val PurpleLightContainer = Color(0xFFF3E5F5)
val PurpleSurface = Color(0xFFF8F0FC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    onNavigateToExamenes: () -> Unit
) {
    Scaffold(
        containerColor = PurpleSurface,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onNavigateToExamenes() },
                containerColor = PurplePrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Examen")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nuevo Examen", fontWeight = FontWeight.Bold)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // 1. Encabezado Doctor y Sucursal
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(PurpleLightContainer)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White
                        ) {
                            Text(
                                text = "🪪 Tecnólogo Médico • Oftalmología",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                color = PurplePrimary
                            )
                        }
                        Text(
                            text = "📍 Sucursal Central",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            color = PurplePrimary
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Avatar",
                                tint = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Hoy, 24 de Mayo • Turno Mañana",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                text = "¡Hola, Dr. Camilo Aranci...",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // 2. Rendimiento del Día
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Rendimiento del Día",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "🔄 En tiempo real",
                            fontSize = 12.sp,
                            color = PurplePrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MetricCard(
                            title = "18",
                            subtitle = "Exámenes hoy",
                            badgeText = "+3",
                            badgeColor = PurpleLightContainer,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            title = "4",
                            subtitle = "Pendientes",
                            titleColor = Color(0xFFD32F2F),
                            badgeColor = Color(0xFFFFEBEE),
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            title = "14",
                            subtitle = "Validados",
                            badgeText = "78%",
                            badgeColor = Color(0xFFE8F5E9),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // 3. Acciones Clínicas Rápidas
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Acciones Clínicas Rápidas",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Button(
                        onClick = { onNavigateToExamenes() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Registrar Nuevo Examen",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Icon(Icons.Default.ArrowForward, contentDescription = null)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("📁 Cargar Archivo", fontSize = 12.sp)
                        }
                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("⏱️ Trazabilidad", fontSize = 12.sp)
                        }
                    }
                }
            }

            // 4. Pacientes en Espera
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pacientes en Espera (3)",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    TextButton(onClick = {}) {
                        Text("Ver Sala Completa", color = PurplePrimary, fontSize = 12.sp)
                    }
                }
            }

            // Paciente 1
            item {
                PacienteCard(
                    nombre = "María Elena Soto",
                    info = "64 años • Rut: 12.345.678-9",
                    estado = "En espera",
                    examen = "OCT Macular",
                    box = "Box 2 • Protocolo Retina",
                    hora = "10:30 AM",
                    accionBoton = "Llamar Box"
                )
            }

            // Paciente 2
            item {
                PacienteCard(
                    nombre = "Jorge Valenzuela",
                    info = "48 años • Glaucoma Control",
                    estado = "En box",
                    examen = "Campimetría Computarizada",
                    box = "Box 1 • Humphrey 24-2",
                    hora = "11:15 AM",
                    accionBoton = "Capturar Resultados"
                )
            }

            item { Spacer(modifier = Modifier.height(60.dp)) } // Espacio para el FAB
        }
    }
}

// Componentes auxiliares pequeños para modularizar la vista
@Composable
fun MetricCard(
    title: String,
    subtitle: String,
    badgeText: String? = null,
    badgeColor: Color,
    titleColor: Color = Color.Black,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            if (badgeText != null) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = badgeColor,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(14.dp))
            }
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun PacienteCard(
    nombre: String,
    info: String,
    estado: String,
    examen: String,
    box: String,
    hora: String,
    accionBoton: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = info, fontSize = 12.sp, color = Color.Gray)
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PurpleLightContainer
                ) {
                    Text(
                        text = estado,
                        color = PurplePrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = PurpleSurface
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = examen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(text = box, fontSize = 11.sp, color = Color.Gray)
                    }
                    Text(text = "🕒 $hora", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(accionBoton, fontSize = 12.sp)
                }
            }
        }
    }
}