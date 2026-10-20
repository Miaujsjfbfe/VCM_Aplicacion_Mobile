package com.losdebuggers.oftapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.ui.theme.*
import com.losdebuggers.oftapp.viewmodel.LoginViewModel

private data class Perfil(val titulo: String, val sub: String)

private val perfiles = listOf(
    Perfil("Tecnólogo", "Exámenes & OCT"),
    Perfil("Oftalmólogo", "Diagnóstico & Rx"),
    Perfil("Paciente", "Resultados & Citas"),
    Perfil("Admin", "Sedes & Auditoría")
)

/**
 * Login + Registro en una sola pantalla. Al autenticar, el ViewModel guarda la sesión
 * en SQLite y MainActivity reemplaza esta vista por Inicio (no hay callback de navegación).
 */
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val ui by viewModel.ui.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFondo)
            .verticalScroll(rememberScrollState())
    ) {
        // ---- Cabecera clínica con degradado ----
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(ColorPrincipalOscuro, ColorPrincipal)),
                    RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .statusBarsPadding()
                .padding(top = 28.dp, bottom = 56.dp, start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(shape = CircleShape, color = Color.White, modifier = Modifier.size(72.dp)) {
                    Icon(
                        Icons.Default.Visibility,
                        contentDescription = "OftApp",
                        tint = ColorPrincipal,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text("OftApp", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    "Portal clínico oftalmológico",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        // ---- Tarjeta del formulario (solapada sobre la cabecera) ----
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = (-32).dp),
            colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                // Selector Ingresar / Registrarme
                ModoSelector(registro = ui.modoRegistro, onCambio = viewModel::cambiarModo)

                Spacer(Modifier.height(16.dp))

                Text("Perfil de ingreso", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTextoSecundario)
                Spacer(Modifier.height(8.dp))
                // Admin no se puede auto-registrar
                val disponibles = if (ui.modoRegistro) perfiles.filter { it.titulo != "Admin" } else perfiles
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    disponibles.chunked(2).forEach { fila ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            fila.forEach { p ->
                                RoleCard(
                                    title = p.titulo,
                                    sub = p.sub,
                                    isSelected = ui.rol == p.titulo,
                                    onClick = { viewModel.seleccionarRol(p.titulo) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (fila.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                if (ui.modoRegistro) {
                    Campo("Nombre completo", ui.nombre, viewModel::onNombre, Icons.Default.Person)
                    Spacer(Modifier.height(12.dp))
                    Campo("RUT", ui.rut, viewModel::onRut, Icons.Default.Info, placeholder = "12.345.678-9")
                    Spacer(Modifier.height(12.dp))
                    Campo("Correo electrónico", ui.email, viewModel::onEmail, Icons.Default.Email, teclado = KeyboardType.Email)
                } else {
                    Campo(
                        "Correo o RUT", ui.identificador, viewModel::onIdentificador, Icons.Default.Person,
                        teclado = KeyboardType.Email
                    )
                }

                Spacer(Modifier.height(12.dp))

                Campo(
                    label = "Clave institucional",
                    valor = ui.clave,
                    onCambio = viewModel::onClave,
                    icono = Icons.Default.Lock,
                    teclado = KeyboardType.Password,
                    esClave = true,
                    claveVisible = ui.claveVisible,
                    onToggleClave = viewModel::toggleClaveVisible
                )

                // Mensaje de error
                if (ui.error != null) {
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ColorAlerta.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, null, tint = ColorAlerta, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(ui.error!!, color = ColorAlerta, fontSize = 12.sp)
                    }
                }

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = viewModel::enviar,
                    enabled = !ui.cargando,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ColorPrincipal, contentColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (ui.cargando) {
                        CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                    } else {
                        Text(
                            if (ui.modoRegistro) "Crear cuenta" else "Iniciar sesión",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Accesos demo (solo en login)
                if (!ui.modoRegistro) {
                    Spacer(Modifier.height(16.dp))
                    Text("Acceso de prueba (demo)", fontSize = 11.sp, color = ColorTextoSecundario)
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        DemoChip("Tecnólogo", Modifier.weight(1f)) { viewModel.cargarDemo("Tecnólogo", "tecnologo@oftapp.cl") }
                        DemoChip("Médico", Modifier.weight(1f)) { viewModel.cargarDemo("Oftalmólogo", "medico@oftapp.cl") }
                        DemoChip("Paciente", Modifier.weight(1f)) { viewModel.cargarDemo("Paciente", "paciente@oftapp.cl") }
                    }
                }
            }
        }

        // ---- Aviso de privacidad ----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-16).dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Lock, null, tint = ColorTextoSecundario, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(6.dp))
            Text(
                "Datos clínicos protegidos • Ley 20.584 Derechos del Paciente",
                fontSize = 10.sp,
                color = ColorTextoSecundario,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.navigationBarsPadding().height(8.dp))
    }
}

@Composable
private fun ModoSelector(registro: Boolean, onCambio: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorSecundario, RoundedCornerShape(14.dp))
            .padding(4.dp)
    ) {
        listOf(false to "Ingresar", true to "Registrarme").forEach { (esRegistro, texto) ->
            val activo = registro == esRegistro
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if (activo) ColorPrincipal else Color.Transparent, RoundedCornerShape(10.dp))
                    .clickable { onCambio(esRegistro) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    texto,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (activo) Color.White else ColorPrincipal
                )
            }
        }
    }
}

/** Campo de texto reutilizable con etiqueta, ícono y (opcional) botón mostrar/ocultar clave. */
@Composable
private fun Campo(
    label: String,
    valor: String,
    onCambio: (String) -> Unit,
    icono: ImageVector,
    teclado: KeyboardType = KeyboardType.Text,
    placeholder: String? = null,
    esClave: Boolean = false,
    claveVisible: Boolean = false,
    onToggleClave: () -> Unit = {}
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(label) },
        placeholder = if (placeholder != null) {
            { Text(placeholder) }
        } else null,
        singleLine = true,
        leadingIcon = { Icon(icono, contentDescription = null, tint = ColorPrincipal) },
        trailingIcon = if (esClave) {
            {
                IconButton(onClick = onToggleClave) {
                    Icon(
                        if (claveVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (claveVisible) "Ocultar clave" else "Mostrar clave",
                        tint = ColorTextoSecundario
                    )
                }
            }
        } else null,
        visualTransformation = if (esClave && !claveVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = ColorSecundario,
            unfocusedContainerColor = ColorSecundario,
            focusedBorderColor = ColorPrincipal,
            unfocusedBorderColor = Color.Transparent,
            focusedLabelColor = ColorPrincipal,
            focusedTextColor = ColorTextoPrincipal,
            unfocusedTextColor = ColorTextoPrincipal
        )
    )
}

@Composable
private fun DemoChip(texto: String, modifier: Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = ColorPrincipal)
    ) {
        Text(texto, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
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
        Row(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = ColorTextoPrincipal)
                Text(sub, fontSize = 10.sp, color = ColorTextoSecundario)
            }
            if (isSelected) {
                Icon(Icons.Default.CheckCircle, null, tint = ColorPrincipal, modifier = Modifier.size(18.dp))
            }
        }
    }
}
