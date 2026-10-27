package com.losdebuggers.oftapp.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetasScreen(
    onVolver: () -> Unit
) {
    Scaffold(
        containerColor = ColorFondo,
        topBar = {
            TopAppBar(
                title = {
                    Text("Receta De Lentes", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ColorTextoPrincipal)
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = ColorTextoPrincipal)
                    }
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

            // Título de Sección
            Column {
                Text(
                    text = "CLÍNICA OFTALMOLÓGICA",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorPrincipal
                )
                Text(
                    text = "Mis Recetas Médicas",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTextoPrincipal
                )
                Text(
                    text = "Instrucciones claras y actuales prescritas para tu salud ocular.",
                    fontSize = 13.sp,
                    color = ColorTextoSecundario
                )
            }

            // 2. TARJETA: Receta de Lentes
            Card(
                colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(shape = CircleShape, color = ColorSecundario, modifier = Modifier.size(48.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Edit, contentDescription = null, tint = ColorPrincipal, modifier = Modifier.size(24.dp))
                            }
                        }
                        Surface(
                            color = ColorSecundario,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                "Vigente hasta Dic 2025",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorPrincipal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Receta de Lentes para Cerca y Lejos", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ColorTextoPrincipal)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Subtarjeta interna
                    Card(
                        colors = CardDefaults.cardColors(containerColor = ColorFondo),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ColorPrincipal, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("INDICACIÓN PRINCIPAL", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTextoSecundario)
                                    Text("Uso diario para lectura y visión de lejos", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTextoPrincipal)
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = ColorPrincipal, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Oftalmólogo Tratante", fontSize = 10.sp, color = ColorTextoSecundario)
                                    Text("Dr. Álvaro Soto (Vigente)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTextoPrincipal)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { /* Descargar PDF */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorPrincipal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("DESCARGAR RECETA DE LENTES (PDF)", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // 3. TARJETA: Gotas Oftálmicas
            Card(
                colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(shape = CircleShape, color = ColorSecundario, modifier = Modifier.size(48.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = ColorPrincipal, modifier = Modifier.size(24.dp))
                            }
                        }
                        Surface(
                            color = ColorSecundario,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                "Tratamiento Continuo",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorPrincipal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Gotas Oftálmicas (Lágrimas Artificiales)", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ColorTextoPrincipal)

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = ColorFondo),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("DOSIS PRESCRITA", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTextoSecundario)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Aplicar 1 gota en cada ojo cada 8 horas", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ColorTextoPrincipal)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("• Para descanso y sequedad ocular", fontSize = 12.sp, color = ColorTextoSecundario)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { /* Recordatorio */ },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ColorPrincipal)
                    ) {
                        Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("RECORDAR HORARIO DE GOTAS", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}