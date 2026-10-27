package com.losdebuggers.oftapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.model.Usuario
import com.losdebuggers.oftapp.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun InicioScreen(
    usuario: Usuario,
    onNavigateToExamenes: () -> Unit,
    onNavigateToRecetas: () -> Unit,
    onLogout: () -> Unit
) {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var confirmarSalida by remember { mutableStateOf(false) }

    val proximamente: (String) -> Unit = { modulo ->
        scope.launch { snackbar.showSnackbar("$modulo estará disponible pronto") }
    }

    Scaffold(
        containerColor = ColorFondo,
        snackbarHost = { SnackbarHost(snackbar) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Cabecera con saludo
            item { Cabecera(usuario, onLogoutClick = { confirmarSalida = true }) }

            // 2. Próxima cita
            item {
                Column(Modifier.padding(horizontal = 16.dp).offset(y = (-36).dp)) {
                    ProximaCitaCard(onClick = { proximamente("Historial de citas") })
                }
            }

            // 3. Accesos rápidos rediseñados (Tarjetas grandes para pacientes con tus Iconos)
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp).offset(y = (-36).dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Titulo("Accesos rápidos")

                    // Tarjeta Grande: Recetas (Usando Icono de List / Documento)
                    AccesoRapidoPacienteCard(
                        icono = Icons.Default.List,
                        titulo = "Mis Recetas",
                        descripcion = "Revisa tus recetas de lentes o gotas activas",
                        badge = "2 vigentes",
                        onClick = onNavigateToRecetas // <-- Crearemos este callback
                    )

                    // Tarjeta Grande: Exámenes (Conectado a la navegación y usando Icono de Visibility)
                    AccesoRapidoPacienteCard(
                        icono = Icons.Default.Visibility,
                        titulo = "Mis Exámenes",
                        descripcion = "Revisa o descarga los resultados de tus exámenes",
                        badge = "Listos",
                        onClick = onNavigateToExamenes
                    )
                }
            }

            // 4. Actividad reciente
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp).offset(y = (-36).dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Titulo("Actividad reciente")
                    Actividad("Informe OCT Macular disponible", "Ayer • Dr. Rojas", ColorExito)
                    Actividad("Receta de lubricante ocular renovada", "Hace 3 días", ColorPrincipal)
                    Actividad("Control de presión intraocular", "12 Oct • Sucursal Central", ColorTextoSecundario)
                }
            }

            // 5. Privacidad
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).offset(y = (-36).dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lock, null, tint = ColorTextoSecundario, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Tus datos clínicos están protegidos", fontSize = 11.sp, color = ColorTextoSecundario)
                }
            }
        }
    }

    // Confirmación de cierre de sesión
    if (confirmarSalida) {
        AlertDialog(
            onDismissRequest = { confirmarSalida = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Deseas salir de tu cuenta? Tendrás que ingresar tus credenciales nuevamente.") },
            confirmButton = {
                TextButton(onClick = { confirmarSalida = false; onLogout() }) {
                    Text("Cerrar sesión", color = ColorAlerta, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = { TextButton(onClick = { confirmarSalida = false }) { Text("Cancelar") } }
        )
    }
}

@Composable
private fun Cabecera(usuario: Usuario, onLogoutClick: () -> Unit) {
    val primerNombre = usuario.nombre.trim().substringBefore(" ")
    val iniciales = usuario.nombre.trim().split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercase() }.joinToString("")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(listOf(ColorPrincipalOscuro, ColorPrincipal)),
                RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
            .padding(start = 20.dp, end = 8.dp, top = 12.dp, bottom = 64.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.2f), modifier = Modifier.size(52.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Text(iniciales, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Bienvenido(a)", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                Text(primerNombre, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1)
                Surface(shape = RoundedCornerShape(10.dp), color = Color.White.copy(alpha = 0.18f)) {
                    Text(
                        "Paciente • Sucursal Central",
                        fontSize = 11.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
            IconButton(onClick = onLogoutClick) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión", tint = Color.White)
            }
        }
    }
}

@Composable
private fun ProximaCitaCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            IconoCirculo(Icons.Default.DateRange)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("Próxima cita", fontSize = 11.sp, color = ColorTextoSecundario)
                Text("Control oftalmológico", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ColorTextoPrincipal)
                Text("Mar 21 Oct • 10:30 • Box 2", fontSize = 12.sp, color = ColorTextoSecundario)
            }
            Surface(shape = RoundedCornerShape(10.dp), color = ColorExito.copy(alpha = 0.12f)) {
                Text(
                    "Confirmada",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorExito,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun AccesoRapidoPacienteCard(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    badge: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconoCirculo(icono)
                Surface(
                    color = ColorSecundario,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = badge,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorPrincipal
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ColorTextoPrincipal
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = descripcion,
                fontSize = 13.sp,
                color = ColorTextoSecundario
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ver detalles",
                    fontWeight = FontWeight.Bold,
                    color = ColorPrincipal,
                    fontSize = 14.sp
                )
                IconButton(
                    onClick = onClick,
                    modifier = Modifier.size(38.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = ColorPrincipal,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Ir", modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun IconoCirculo(icono: ImageVector) {
    Surface(shape = CircleShape, color = ColorSecundario, modifier = Modifier.size(42.dp)) {
        Icon(icono, contentDescription = null, tint = ColorPrincipal, modifier = Modifier.padding(10.dp))
    }
}

@Composable
private fun Actividad(titulo: String, detalle: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(10.dp).background(color, CircleShape))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(titulo, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTextoPrincipal)
                Text(detalle, fontSize = 11.sp, color = ColorTextoSecundario)
            }
        }
    }
}

@Composable
private fun Titulo(texto: String) =
    Text(texto, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ColorTextoPrincipal)