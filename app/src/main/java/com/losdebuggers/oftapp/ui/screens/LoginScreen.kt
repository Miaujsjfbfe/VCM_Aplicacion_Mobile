package com.losdebuggers.oftapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.ui.theme.*
import com.losdebuggers.oftapp.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = LoginViewModel(),
    onLoginSuccess: () -> Unit
) {
    val rolSeleccionado by viewModel.rolSeleccionado.collectAsState()
    val rut by viewModel.rut.collectAsState()
    val clave by viewModel.clave.collectAsState()
    val recordarSesion by viewModel.recordarSesion.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFondo)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Logo Header
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = ColorBlancoTarjetas,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(80.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("👁️ OFTALMOLOGÍA", fontWeight = FontWeight.Bold, color = ColorPrincipal, fontSize = 12.sp)
                    Text("Visión Clara", fontWeight = FontWeight.Bold, color = ColorTextoPrincipal, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = ColorSecundario
            ) {
                Text(
                    text = "🛡️ Portal Clínico & Quirúrgico",
                    fontSize = 11.sp,
                    color = ColorPrincipal,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Acceso OftApp", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = ColorTextoPrincipal)
            Text(
                "Gestión oftalmológica integral, telemetría ocular y expediente clínico seguro.",
                fontSize = 12.sp,
                color = ColorTextoSecundario,
                modifier = Modifier.padding(horizontal = 24.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta de Formulario Principal
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("🪪 Perfil de Ingreso", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = ColorTextoPrincipal)
                        Text("1 de 4 disponible", fontSize = 11.sp, color = ColorPrincipal)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Grid de Roles
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            RoleCard(
                                title = "Tecnólogo",
                                sub = "Exámenes & OCT",
                                isSelected = rolSeleccionado == "Tecnólogo",
                                onClick = { viewModel.seleccionarRol("Tecnólogo") },
                                modifier = Modifier.weight(1f)
                            )
                            RoleCard(
                                title = "Oftalmólogo",
                                sub = "Diagnóstico & Rx",
                                isSelected = rolSeleccionado == "Oftalmólogo",
                                onClick = { viewModel.seleccionarRol("Oftalmólogo") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            RoleCard(
                                title = "Paciente",
                                sub = "Resultados & Cita",
                                isSelected = rolSeleccionado == "Paciente",
                                onClick = { viewModel.seleccionarRol("Paciente") },
                                modifier = Modifier.weight(1f)
                            )
                            RoleCard(
                                title = "Admin",
                                sub = "Sedes & Auditoría",
                                isSelected = rolSeleccionado == "Admin",
                                onClick = { viewModel.seleccionarRol("Admin") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Acceso Rápido de Prueba (Demo)", fontSize = 11.sp, color = ColorTextoSecundario)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { viewModel.cargarDemoTecnologo() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = ColorSecundario,
                                contentColor = ColorPrincipal
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Demo Tecnólogo", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = { viewModel.cargarDemoMedico() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = ColorSecundario,
                                contentColor = ColorPrincipal
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Demo Médico", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Inputs RUT y Clave
                    Text("RUT / Identificación Hospitalaria", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = ColorTextoSecundario)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = rut,
                        onValueChange = { viewModel.onRutChange(it) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ColorPrincipal) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ColorSecundario,
                            unfocusedContainerColor = ColorSecundario,
                            focusedTextColor = ColorTextoPrincipal,
                            unfocusedTextColor = ColorTextoPrincipal
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Clave Institucional", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = ColorTextoSecundario)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = clave,
                        onValueChange = { viewModel.onClaveChange(it) },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ColorPrincipal) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ColorSecundario,
                            unfocusedContainerColor = ColorSecundario,
                            focusedTextColor = ColorTextoPrincipal,
                            unfocusedTextColor = ColorTextoPrincipal
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Checkbox y Olvidó Clave
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = recordarSesion,
                                onCheckedChange = { viewModel.toggleRecordarSesion(it) },
                                colors = CheckboxDefaults.colors(checkedColor = ColorPrincipal)
                            )
                            Text("Recordar sesión", fontSize = 12.sp, color = ColorTextoPrincipal)
                        }
                        Text(
                            text = "¿Olvidó su clave?",
                            fontSize = 12.sp,
                            color = ColorPrincipal,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onLoginSuccess() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorPrincipal,
                            contentColor = ColorBlancoTarjetas
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("➡️️  Iniciar Sesión Clínica", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = ColorSecundario,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "🔒 Conexión Encriptada TLS 1.3 • Ley 20.584 Derechos Paciente",
                    fontSize = 10.sp,
                    color = ColorTextoSecundario,
                    modifier = Modifier.padding(10.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun RoleCard(
    title: String,
    sub: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) ColorPrincipal else ColorSecundario,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) ColorSecundario else ColorBlancoTarjetas
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = ColorPrincipal,
                    modifier = Modifier.size(16.dp).align(Alignment.End)
                )
            }
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = ColorTextoPrincipal)
            Text(sub, fontSize = 10.sp, color = ColorTextoSecundario)
        }
    }
}