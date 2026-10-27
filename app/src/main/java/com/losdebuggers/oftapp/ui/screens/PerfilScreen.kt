package com.losdebuggers.oftapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.model.Usuario
import com.losdebuggers.oftapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    usuario: Usuario,
    onVolver: () -> Unit = {},
    onLogout: () -> Unit
) {
    // Datos de respaldo para la interfaz del paciente mientras no estén en el modelo Usuario
    val previsionTexto = "Fonasa B"
    val telefonoTexto = "+56 9 8765 4321"
    val emailTexto = "roberto.gomez@email.com"
    val ciudadTexto = "Santiago, Chile"

    Scaffold(
        containerColor = ColorFondo,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mi Perfil",
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. TARJETA DE INFORMACIÓN PERSONAL
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = usuario.nombre,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTextoPrincipal
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "RUT: ${usuario.rut}",
                        fontSize = 14.sp,
                        color = ColorTextoSecundario
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        color = ColorSecundario,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = ColorPrincipal
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Previsión: $previsionTexto",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorPrincipal
                            )
                        }
                    }
                }
            }

            // 2. TARJETA: DATOS DE CONTACTO
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = ColorPrincipal,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Datos de Contacto",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTextoPrincipal
                        )
                    }

                    // Item 1: Teléfono
                    DatoContactoItem(
                        icono = Icons.Default.Phone,
                        etiqueta = "Teléfono",
                        valor = telefonoTexto
                    )

                    // Item 2: Correo Electrónico
                    DatoContactoItem(
                        icono = Icons.Default.Email,
                        etiqueta = "Correo Electrónico",
                        valor = emailTexto
                    )

                    // Item 3: Ciudad
                    DatoContactoItem(
                        icono = Icons.Default.LocationOn,
                        etiqueta = "Ciudad",
                        valor = ciudadTexto
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 3. BOTÓN DE CERRAR SESIÓN
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorSecundario,
                    contentColor = ColorAlerta
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar Sesión",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Clínica Visión Clara",
                fontSize = 12.sp,
                color = ColorTextoSecundario
            )
        }
    }
}

@Composable
private fun DatoContactoItem(
    icono: ImageVector,
    etiqueta: String,
    valor: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ColorFondo),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = ColorSecundario
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = ColorPrincipal,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = etiqueta,
                    fontSize = 11.sp,
                    color = ColorTextoSecundario
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = valor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTextoPrincipal
                )
            }
        }
    }
}