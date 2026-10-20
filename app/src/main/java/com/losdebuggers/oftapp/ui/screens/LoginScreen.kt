package com.losdebuggers.oftapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val isLoginMode by viewModel.isLoginMode.collectAsState()
    val rut by viewModel.rut.collectAsState()
    val clave by viewModel.clave.collectAsState()
    val nombre by viewModel.nombre.collectAsState()
    val email by viewModel.email.collectAsState()
    val recordarSesion by viewModel.recordarSesion.collectAsState()
    val mensajeEstado by viewModel.mensajeEstado.collectAsState()
    val esError by viewModel.esError.collectAsState()

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

            Text(
                text = if (isLoginMode) "Acceso OftApp" else "Registro de Usuario",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ColorTextoPrincipal
            )
            Text(
                text = "Gestión oftalmológica integral, telemetría ocular y expediente clínico seguro.",
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

                    // Selector de Modo (Iniciar Sesión / Registrarse)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ColorSecundario, RoundedCornerShape(12.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { viewModel.setLoginMode(true) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isLoginMode) ColorPrincipal else Color.Transparent,
                                contentColor = if (isLoginMode) ColorBlancoTarjetas else ColorTextoPrincipal
                            ),
                            shape = RoundedCornerShape(10.dp),
                            elevation = null,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Iniciar Sesión", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Button(
                            onClick = { viewModel.setLoginMode(false) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!isLoginMode) ColorPrincipal else Color.Transparent,
                                contentColor = if (!isLoginMode) ColorBlancoTarjetas else ColorTextoPrincipal
                            ),
                            shape = RoundedCornerShape(10.dp),
                            elevation = null,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Registrarse", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isLoginMode) {
                        // Formulario de Iniciar Sesión
                        Text(
                            "RUT / Identificación",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = ColorTextoSecundario
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = rut,
                            onValueChange = { viewModel.onRutChange(it) },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ColorPrincipal) },
                            placeholder = { Text("Ej: 12.345.678-9") },
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

                        Text(
                            "Clave / Contraseña",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = ColorTextoSecundario
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = clave,
                            onValueChange = { viewModel.onClaveChange(it) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ColorPrincipal) },
                            visualTransformation = PasswordVisualTransformation(),
                            placeholder = { Text("••••••••") },
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

                    } else {
                        // Formulario de Registro
                        Text(
                            "Nombre Completo",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = ColorTextoSecundario
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { viewModel.onNombreChange(it) },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ColorPrincipal) },
                            placeholder = { Text("Ej: Dr. Juan Pérez") },
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

                        Text(
                            "RUT / Identificación",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = ColorTextoSecundario
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = rut,
                            onValueChange = { viewModel.onRutChange(it) },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ColorPrincipal) },
                            placeholder = { Text("Ej: 12.345.678-9") },
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

                        Text(
                            "Correo Electrónico",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = ColorTextoSecundario
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { viewModel.onEmailChange(it) },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = ColorPrincipal) },
                            placeholder = { Text("correo@ejemplo.com") },
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

                        Text(
                            "Clave / Contraseña",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = ColorTextoSecundario
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = clave,
                            onValueChange = { viewModel.onClaveChange(it) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ColorPrincipal) },
                            visualTransformation = PasswordVisualTransformation(),
                            placeholder = { Text("••••••••") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ColorSecundario,
                                unfocusedContainerColor = ColorSecundario,
                                focusedTextColor = ColorTextoPrincipal,
                                unfocusedTextColor = ColorTextoPrincipal
                            )
                        )
                    }

                    // Mensaje de Alerta (Error o Éxito)
                    if (mensajeEstado != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (esError) Color(0xFFFFEBEE) else Color(0xFFE8F5E9),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = mensajeEstado!!,
                                color = if (esError) Color(0xFFC62828) else Color(0xFF2E7D32),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(10.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón de Acción Principal
                    Button(
                        onClick = {
                            if (isLoginMode) {
                                viewModel.iniciarSesion(onSuccess = onLoginSuccess)
                            } else {
                                viewModel.registrarUsuario()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorPrincipal,
                            contentColor = ColorBlancoTarjetas
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = if (isLoginMode) "➡️  Iniciar Sesión" else "📝  Registrarse",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
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
