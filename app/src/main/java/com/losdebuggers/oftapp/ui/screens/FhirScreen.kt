package com.losdebuggers.oftapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.JsonObject
import com.losdebuggers.oftapp.data.FhirNetwork
import com.losdebuggers.oftapp.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FhirScreen(onVolver: () -> Unit) {
    var pacientes by remember { mutableStateOf(emptyList<String>()) }
    var observaciones by remember { mutableStateOf(emptyList<String>()) }
    var mensaje by remember { mutableStateOf("Presione 'Consultar pacientes' para conectar con HAPI FHIR") }
    var cargando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Servidor Clínico HAPI FHIR", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorPrincipal)
            )
        },
        containerColor = ColorFondo
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CloudSync, null, tint = ColorPrincipal, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Integración FHIR en Tiempo Real", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ColorTextoPrincipal)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Conexión oficial con el servidor público de pruebas HAPI FHIR para consulta de pacientes e historial clínico.", fontSize = 13.sp, color = ColorTextoSecundario)
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                cargando = true
                                mensaje = "Consultando pacientes en HAPI FHIR..."
                                pacientes = emptyList()
                                observaciones = emptyList()
                                try {
                                    val datos = FhirNetwork.api.obtener("Patient?_count=5")
                                    pacientes = FhirNetwork.recursos(datos).map { res ->
                                        res.get("id")?.asString ?: "ID desconocido"
                                    }
                                    mensaje = "${pacientes.size} pacientes encontrados en la nube"
                                } catch (e: Exception) {
                                    mensaje = "Error de conexión: ${e.localizedMessage ?: "No se pudo conectar"}"
                                } finally {
                                    cargando = false
                                }
                            }
                        },
                        enabled = !cargando,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorPrincipal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Consultar 5 pacientes", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Text(mensaje, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = ColorTextoSecundario)

            // Lista de pacientes y observaciones
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (pacientes.isNotEmpty()) {
                    item {
                        Text("Pacientes Disponibles:", fontWeight = FontWeight.Bold, color = ColorTextoPrincipal, fontSize = 14.sp)
                    }
                }
                items(pacientes) { id ->
                    Card(
                        onClick = {
                            scope.launch {
                                cargando = true
                                mensaje = "Consultando historial para paciente $id..."
                                observaciones = emptyList()
                                try {
                                    val datos = FhirNetwork.api.obtener("Observation?patient=$id&_count=5")
                                    observaciones = FhirNetwork.recursos(datos).map { res ->
                                        val code = res.getAsJsonObject("code")
                                        code?.get("text")?.asString ?: "Observación clínica general"
                                    }
                                    mensaje = "${observaciones.size} observaciones encontradas para el paciente $id"
                                } catch (e: Exception) {
                                    mensaje = "Error al consultar historial clínico"
                                } finally {
                                    cargando = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("ID Paciente: $id", fontWeight = FontWeight.Bold, color = ColorPrincipal)
                            Text("Ver historial ➔", fontSize = 12.sp, color = ColorTextoSecundario)
                        }
                    }
                }

                if (observaciones.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(8.dp))
                        Text("Historial de Observaciones Clínicas:", fontWeight = FontWeight.Bold, color = ColorTextoPrincipal, fontSize = 14.sp)
                    }
                }
                items(observaciones) { obs ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = ColorSecundario
                    ) {
                        Text("• $obs", modifier = Modifier.padding(12.dp), fontSize = 13.sp, color = ColorTextoPrincipal)
                    }
                }
            }
        }
    }
}
