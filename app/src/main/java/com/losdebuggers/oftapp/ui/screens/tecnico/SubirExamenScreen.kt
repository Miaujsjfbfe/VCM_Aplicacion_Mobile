package com.losdebuggers.oftapp.ui.screens.tecnico

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Botón siguiente en su color azul base
private val AzulOscuro = Color(0xFF064E5C)
private val AzulPrincipal = Color(0xFF0E8A9D)
private val AzulClaro = Color(0xFFCFFAFE)

// Barra de pasos en verde
private val VerdeBarra = Color(0xFF10B981)
private val VerdeBarraOscuro = Color(0xFF059669)
private val VerdeSuave = Color(0xFFD1FAE5)

// Verde sutil para PDF adjunto o tic final
private val VerdeSutilFondo = Color(0xFFEAF7F0).copy(alpha = 0.72f)
private val VerdeSutilBorde = Color(0xFFB7E4CC).copy(alpha = 0.80f)
private val VerdeSutilTexto = Color(0xFF23644B)
private val VerdeSutilIcono = Color(0xFF2F8564)

// Paso 1: morado pastel bien transparentoso + gris bloqueado compacto
private val MoradoPastelFondo = Color(0xFFF3E8FF).copy(alpha = 0.36f)
private val MoradoPastelSeleccionado = Color(0xFFEDE9FE).copy(alpha = 0.78f)
private val MoradoPastelBorde = Color(0xFFD8B4FE).copy(alpha = 0.45f)
private val MoradoPastelTexto = Color(0xFF5B21B6)
private val MoradoPastelIcono = Color(0xFF7E22CE)

private val GrisBloqueadoFondo = Color(0xFFE5E7EB).copy(alpha = 0.65f)
private val GrisBloqueadoBorde = Color(0xFFD1D5DB).copy(alpha = 0.8f)
private val GrisBloqueadoTexto = Color(0xFF4B5563)

// Paso 2: rosado pastel suave y transparentoso
private val RosadoPastelFondo = Color(0xFFFCE7F3).copy(alpha = 0.34f)
private val RosadoPastelBorde = Color(0xFFFBCFE8).copy(alpha = 0.48f)
private val RosadoPastelTexto = Color(0xFF9D174D)
private val RosadoPastelIcono = Color(0xFFBE185D)

// Paso 3: amarillo sutil y transparentoso
private val AmarilloSutilFondo = Color(0xFFFEF9C3).copy(alpha = 0.40f)
private val AmarilloSutilBorde = Color(0xFFFDE047).copy(alpha = 0.45f)
private val AmarilloSutilTexto = Color(0xFF854D0E)
private val AmarilloSutilIcono = Color(0xFFA16207)
private val TealOscuro = Color(0xFF0B5A4E)

private const val MENSAJE_SIN_INDICACIONES = "No hay indicaciones médicas para este examen"

private data class PacienteDemo(
    val nombre: String,
    val rut: String,
    val prevision: String
)

private val pacientesSugeridos = listOf(
    PacienteDemo("Roberto Muñoz", "12.345.678-9", "FONASA B"),
    PacienteDemo("Camila Soto", "16.789.432-1", "Isapre Colmena"),
    PacienteDemo("Javier Rojas", "14.222.890-K", "FONASA A"),
    PacienteDemo("Renata Vera", "18.456.123-5", "FONASA C")
)

private val tiposDocumento = listOf(
    "Fondo de Ojo",
    "Campo Visual",
    "OCT Macular",
    "Topografía Corneal",
    "Receta Oftalmológica"
)

private val medicosEncargados = listOf(
    "Dra. Marcela Silva",
    "Dr. Diego Morales",
    "Dra. Valentina Herrera"
)

@Composable
fun SubirExamenScreen(
    onVolver: () -> Unit = {},
    onFinalizado: () -> Unit = {}
) {
    var pasoActual by remember { mutableIntStateOf(0) }
    var direccionAvance by remember { mutableIntStateOf(1) }

    var busquedaPaciente by remember { mutableStateOf("") }
    var rutPaciente by remember { mutableStateOf("") }
    var nombrePaciente by remember { mutableStateOf("") }
    var previsionPaciente by remember { mutableStateOf("") }

    var tipoSeleccionado by remember { mutableStateOf(tiposDocumento.first()) }
    var menuTipoAbierto by remember { mutableStateOf(false) }
    val fechaHoy = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale("es", "CL")).format(Date())
    }
    var fechaDocumento by remember { mutableStateOf(fechaHoy) }
    var nombreArchivo by remember { mutableStateOf("") }
    var medicoSeleccionado by remember { mutableStateOf(medicosEncargados.first()) }
    var menuMedicoAbierto by remember { mutableStateOf(false) }

    var indicacionesMedicas by remember { mutableStateOf("") }
    var sinIndicacionesMarcado by remember { mutableStateOf(false) }

    val selectorArchivo = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val ultimoSegmento = uri.lastPathSegment?.substringAfterLast("/") ?: "documento_adjunto.pdf"
            nombreArchivo = ultimoSegmento
        }
    }

    val avanzar: () -> Unit = {
        direccionAvance = 1
        pasoActual = (pasoActual + 1).coerceAtMost(3)
    }

    val retroceder: () -> Unit = {
        if (pasoActual > 0 && pasoActual < 3) {
            direccionAvance = -1
            pasoActual -= 1
        } else {
            onVolver()
        }
    }

    val reiniciarFlujo: () -> Unit = {
        direccionAvance = -1
        busquedaPaciente = ""
        rutPaciente = ""
        nombrePaciente = ""
        previsionPaciente = ""
        nombreArchivo = ""
        indicacionesMedicas = ""
        sinIndicacionesMarcado = false
        fechaDocumento = fechaHoy
        pasoActual = 0
    }

    val paso1Listo = nombrePaciente.isNotBlank() && rutPaciente.isNotBlank()
    val paso2Listo = tipoSeleccionado.isNotBlank() &&
            fechaDocumento.isNotBlank() &&
            medicoSeleccionado.isNotBlank() &&
            nombreArchivo.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFondo)
    ) {
        // Barra superior del título en verde (estilo coherente con el resto de pantallas)
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
                    onClick = retroceder,
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
                        text = "Subir Examen o Receta",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = if (pasoActual < 3) "Paso ${pasoActual + 1} de 3" else "Registro completado",
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
                    .widthIn(max = 700.dp)
                    .padding(16.dp)
            ) {
            // Barra de progreso verde mostrando el avance de los 3 pasos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (indice in 0..2) {
                    val completado = pasoActual == 3 || indice < pasoActual
                    val pasoActivo = pasoActual < 3 && indice == pasoActual
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .background(
                                color = when {
                                    completado -> VerdeBarraOscuro
                                    pasoActivo -> VerdeBarra
                                    else -> VerdeSuave
                                },
                                shape = RoundedCornerShape(50)
                            )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            AnimatedContent(
                targetState = pasoActual,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                transitionSpec = {
                    val enter = slideInHorizontally(
                        animationSpec = tween(280),
                        initialOffsetX = { fullWidth -> fullWidth * direccionAvance }
                    ) + fadeIn(animationSpec = tween(220))

                    val exit = slideOutHorizontally(
                        animationSpec = tween(280),
                        targetOffsetX = { fullWidth -> -fullWidth * direccionAvance }
                    ) + fadeOut(animationSpec = tween(180))

                    enter togetherWith exit
                },
                label = "transicionTarjetasSubir"
            ) { paso ->
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorBlancoTarjetas),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    ) {
                        when (paso) {
                            0 -> TarjetaPasoPaciente(
                                busqueda = busquedaPaciente,
                                onBusquedaChange = { texto ->
                                    busquedaPaciente = texto
                                    if (nombrePaciente.isNotBlank() && !texto.equals(nombrePaciente, ignoreCase = true)) {
                                        nombrePaciente = ""
                                        rutPaciente = ""
                                        previsionPaciente = ""
                                    }
                                },
                                rut = rutPaciente,
                                prevision = previsionPaciente,
                                nombreSeleccionado = nombrePaciente,
                                onSeleccionarPaciente = { paciente ->
                                    busquedaPaciente = paciente.nombre
                                    nombrePaciente = paciente.nombre
                                    rutPaciente = paciente.rut
                                    previsionPaciente = paciente.prevision
                                },
                                onLimpiarSeleccion = {
                                    busquedaPaciente = ""
                                    nombrePaciente = ""
                                    rutPaciente = ""
                                    previsionPaciente = ""
                                },
                                puedeSiguiente = paso1Listo,
                                onSiguiente = avanzar
                            )

                            1 -> TarjetaPasoDocumento(
                                rutPaciente = rutPaciente,
                                nombrePaciente = nombrePaciente,
                                previsionPaciente = previsionPaciente,
                                tipoSeleccionado = tipoSeleccionado,
                                menuTipoAbierto = menuTipoAbierto,
                                onToggleMenuTipo = { menuTipoAbierto = it },
                                onSeleccionarTipo = {
                                    tipoSeleccionado = it
                                    menuTipoAbierto = false
                                },
                                fecha = fechaDocumento,
                                onFechaChange = { fechaDocumento = it },
                                nombreArchivo = nombreArchivo,
                                onBuscarArchivo = { selectorArchivo.launch("*/*") },
                                onSimularArchivo = {
                                    val limpio = rutPaciente.filter { c -> c.isLetterOrDigit() }.ifBlank { "paciente" }
                                    nombreArchivo = "examen_${limpio}.pdf"
                                },
                                medicoSeleccionado = medicoSeleccionado,
                                menuMedicoAbierto = menuMedicoAbierto,
                                onToggleMenuMedico = { menuMedicoAbierto = it },
                                onSeleccionarMedico = {
                                    medicoSeleccionado = it
                                    menuMedicoAbierto = false
                                },
                                puedeSiguiente = paso2Listo,
                                onSiguiente = avanzar
                            )

                            2 -> TarjetaPasoIndicaciones(
                                indicaciones = indicacionesMedicas,
                                sinIndicacionesMarcado = sinIndicacionesMarcado,
                                onIndicacionesChange = { texto ->
                                    indicacionesMedicas = texto
                                    if (texto != MENSAJE_SIN_INDICACIONES) {
                                        sinIndicacionesMarcado = false
                                    }
                                },
                                onMarcarSinIndicaciones = {
                                    sinIndicacionesMarcado = true
                                    indicacionesMedicas = MENSAJE_SIN_INDICACIONES
                                },
                                onSubir = avanzar
                            )

                            else -> TarjetaExamenSubido(
                                rutPaciente = rutPaciente,
                                nombrePaciente = nombrePaciente,
                                previsionPaciente = previsionPaciente,
                                tipo = tipoSeleccionado,
                                fecha = fechaDocumento,
                                medico = medicoSeleccionado,
                                archivo = nombreArchivo,
                                indicaciones = indicacionesMedicas,
                                onSubirOtro = reiniciarFlujo,
                                onIrInicio = onFinalizado
                            )
                        }
                    }
                }
            }
        }
    }
}
}

@Composable
private fun CabeceraTarjetaSeccion(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    fondoIcono: Color,
    bordeIcono: Color,
    colorIcono: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = fondoIcono,
            border = BorderStroke(1.dp, bordeIcono),
            modifier = Modifier.size(42.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icono, contentDescription = null, tint = colorIcono, modifier = Modifier.size(20.dp))
            }
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = titulo,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = ColorTextoPrincipal
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = descripcion,
            fontSize = 12.sp,
            color = ColorTextoSecundario,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun CampoBloqueadoCompacto(
    valor: String,
    etiqueta: String,
    placeholder: String,
    icono: ImageVector,
    seleccionado: Boolean
) {
    val fondo = if (seleccionado) MoradoPastelSeleccionado else GrisBloqueadoFondo
    val borde = if (seleccionado) MoradoPastelBorde else GrisBloqueadoBorde
    val textoColor = if (seleccionado) MoradoPastelTexto else GrisBloqueadoTexto

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = fondo,
        border = BorderStroke(1.dp, borde),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = textoColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = etiqueta,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = textoColor.copy(alpha = 0.8f),
                    maxLines = 1,
                    softWrap = false
                )
                Text(
                    text = valor.ifBlank { placeholder },
                    fontSize = 13.sp,
                    fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (valor.isNotBlank()) textoColor else textoColor.copy(alpha = 0.65f),
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = if (seleccionado) Icons.Default.Check else Icons.Default.Lock,
                contentDescription = null,
                tint = textoColor.copy(alpha = 0.7f),
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Composable
private fun BoxScope.TarjetaPasoPaciente(
    busqueda: String,
    onBusquedaChange: (String) -> Unit,
    rut: String,
    prevision: String,
    nombreSeleccionado: String,
    onSeleccionarPaciente: (PacienteDemo) -> Unit,
    onLimpiarSeleccion: () -> Unit,
    puedeSiguiente: Boolean,
    onSiguiente: () -> Unit
) {
    val coincidenciasAlEscribir = remember(busqueda, nombreSeleccionado) {
        val query = busqueda.trim()
        if (query.isEmpty() || nombreSeleccionado.equals(query, ignoreCase = true)) {
            emptyList()
        } else {
            pacientesSugeridos.filter {
                it.nombre.contains(query, ignoreCase = true) ||
                        it.rut.contains(query, ignoreCase = true)
            }
        }
    }

    val pacienteElegido = nombreSeleccionado.isNotBlank() && rut.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 64.dp)
    ) {
        CabeceraTarjetaSeccion(
            icono = Icons.Default.PersonSearch,
            titulo = "Datos del paciente",
            descripcion = "Escribe el nombre (ej. \"R\") y selecciónalo para rellenar el RUT y FONASA.",
            fondoIcono = MoradoPastelFondo,
            bordeIcono = MoradoPastelBorde,
            colorIcono = MoradoPastelIcono
        )

        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = busqueda,
            onValueChange = onBusquedaChange,
            label = { Text("Nombre del paciente") },
            placeholder = { Text("Ej: R o Roberto") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, null, tint = MoradoPastelIcono) },
            trailingIcon = {
                if (busqueda.isNotEmpty()) {
                    IconButton(onClick = onLimpiarSeleccion) {
                        Icon(Icons.Default.Close, contentDescription = "Limpiar", tint = MoradoPastelIcono)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = if (pacienteElegido) MoradoPastelSeleccionado else MoradoPastelFondo,
                unfocusedContainerColor = if (pacienteElegido) MoradoPastelSeleccionado else MoradoPastelFondo,
                focusedBorderColor = MoradoPastelIcono,
                unfocusedBorderColor = MoradoPastelBorde,
                focusedLabelColor = MoradoPastelTexto,
                unfocusedLabelColor = MoradoPastelTexto.copy(alpha = 0.8f)
            )
        )

        if (coincidenciasAlEscribir.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MoradoPastelFondo,
                border = BorderStroke(1.dp, MoradoPastelBorde),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(6.dp)) {
                    coincidenciasAlEscribir.forEach { paciente ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSeleccionarPaciente(paciente) }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    text = paciente.nombre,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MoradoPastelTexto
                                )
                                Text(
                                    text = "RUT ${paciente.rut} • ${paciente.prevision}",
                                    fontSize = 11.sp,
                                    color = ColorTextoSecundario
                                )
                            }
                            Icon(
                                Icons.Default.NorthWest,
                                contentDescription = "Seleccionar",
                                tint = MoradoPastelIcono,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        CampoBloqueadoCompacto(
            valor = rut,
            etiqueta = "RUT del paciente",
            placeholder = "Sin seleccionar",
            icono = Icons.Default.Badge,
            seleccionado = pacienteElegido
        )

        Spacer(Modifier.height(8.dp))

        CampoBloqueadoCompacto(
            valor = prevision,
            etiqueta = "FONASA / Previsión",
            placeholder = "Sin seleccionar",
            icono = Icons.Default.HealthAndSafety,
            seleccionado = pacienteElegido
        )
    }

    Button(
        onClick = onSiguiente,
        enabled = puedeSiguiente,
        modifier = Modifier.align(Alignment.BottomEnd),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = AzulPrincipal,
            contentColor = Color.White,
            disabledContainerColor = AzulClaro,
            disabledContentColor = AzulOscuro.copy(alpha = 0.55f)
        )
    ) {
        Text("Siguiente", fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(6.dp))
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.TarjetaPasoDocumento(
    rutPaciente: String,
    nombrePaciente: String,
    previsionPaciente: String,
    tipoSeleccionado: String,
    menuTipoAbierto: Boolean,
    onToggleMenuTipo: (Boolean) -> Unit,
    onSeleccionarTipo: (String) -> Unit,
    fecha: String,
    onFechaChange: (String) -> Unit,
    nombreArchivo: String,
    onBuscarArchivo: () -> Unit,
    onSimularArchivo: () -> Unit,
    medicoSeleccionado: String,
    menuMedicoAbierto: Boolean,
    onToggleMenuMedico: (Boolean) -> Unit,
    onSeleccionarMedico: (String) -> Unit,
    puedeSiguiente: Boolean,
    onSiguiente: () -> Unit
) {
    val archivoSeleccionado = nombreArchivo.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 64.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CabeceraTarjetaSeccion(
            icono = Icons.Default.UploadFile,
            titulo = "Examen o receta",
            descripcion = "$nombrePaciente • $rutPaciente${if (previsionPaciente.isNotBlank()) " • $previsionPaciente" else ""}",
            fondoIcono = RosadoPastelFondo,
            bordeIcono = RosadoPastelBorde,
            colorIcono = RosadoPastelIcono
        )

        ExposedDropdownMenuBox(
            expanded = menuTipoAbierto,
            onExpandedChange = onToggleMenuTipo
        ) {
            OutlinedTextField(
                value = tipoSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de examen o receta") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuTipoAbierto) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = RosadoPastelFondo,
                    unfocusedContainerColor = RosadoPastelFondo,
                    focusedBorderColor = RosadoPastelIcono,
                    unfocusedBorderColor = RosadoPastelBorde,
                    focusedLabelColor = RosadoPastelTexto,
                    unfocusedLabelColor = RosadoPastelTexto.copy(alpha = 0.8f)
                )
            )
            ExposedDropdownMenu(
                expanded = menuTipoAbierto,
                onDismissRequest = { onToggleMenuTipo(false) },
                containerColor = ColorBlancoTarjetas
            ) {
                tiposDocumento.forEach { opcion ->
                    val esActual = opcion == tipoSeleccionado
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = opcion,
                                fontWeight = if (esActual) FontWeight.Bold else FontWeight.Normal,
                                color = if (esActual) RosadoPastelTexto else ColorTextoPrincipal
                            )
                        },
                        trailingIcon = if (esActual) {
                            { Icon(Icons.Default.Check, null, tint = RosadoPastelIcono, modifier = Modifier.size(18.dp)) }
                        } else null,
                        onClick = { onSeleccionarTipo(opcion) }
                    )
                }
            }
        }

        OutlinedTextField(
            value = fecha,
            onValueChange = onFechaChange,
            label = { Text("Fecha") },
            placeholder = { Text("dd/mm/aaaa") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = RosadoPastelIcono) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = RosadoPastelFondo,
                unfocusedContainerColor = RosadoPastelFondo,
                focusedBorderColor = RosadoPastelIcono,
                unfocusedBorderColor = RosadoPastelBorde,
                focusedLabelColor = RosadoPastelTexto,
                unfocusedLabelColor = RosadoPastelTexto.copy(alpha = 0.8f)
            )
        )

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (archivoSeleccionado) VerdeSutilFondo else RosadoPastelFondo,
            border = BorderStroke(1.dp, if (archivoSeleccionado) VerdeSutilBorde else RosadoPastelBorde),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (archivoSeleccionado) VerdeSutilBorde else ColorBlancoTarjetas,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (archivoSeleccionado) Icons.Default.Check else Icons.Default.PictureAsPdf,
                                contentDescription = null,
                                tint = if (archivoSeleccionado) VerdeSutilTexto else RosadoPastelIcono,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = if (archivoSeleccionado) "PDF adjunto" else "Archivo PDF",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (archivoSeleccionado) VerdeSutilTexto else RosadoPastelTexto
                        )
                        Text(
                            text = if (archivoSeleccionado) nombreArchivo else "Selecciona un PDF o usa el demo",
                            fontSize = 11.sp,
                            color = ColorTextoSecundario,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onBuscarArchivo,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(
                            1.dp,
                            if (archivoSeleccionado) VerdeSutilIcono else RosadoPastelIcono
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = ColorBlancoTarjetas.copy(alpha = 0.75f),
                            contentColor = if (archivoSeleccionado) VerdeSutilTexto else RosadoPastelTexto
                        )
                    ) {
                        Icon(Icons.Default.AttachFile, null, modifier = Modifier.size(15.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = if (archivoSeleccionado) "Cambiar PDF" else "Elegir archivo",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                    }

                    OutlinedButton(
                        onClick = onSimularArchivo,
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(
                            1.dp,
                            if (archivoSeleccionado) VerdeSutilBorde else RosadoPastelBorde
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = ColorBlancoTarjetas.copy(alpha = 0.75f),
                            contentColor = if (archivoSeleccionado) VerdeSutilTexto else RosadoPastelTexto
                        )
                    ) {
                        Text("PDF demo", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = menuMedicoAbierto,
            onExpandedChange = onToggleMenuMedico
        ) {
            OutlinedTextField(
                value = medicoSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Doctor encargado") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuMedicoAbierto) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = RosadoPastelFondo,
                    unfocusedContainerColor = RosadoPastelFondo,
                    focusedBorderColor = RosadoPastelIcono,
                    unfocusedBorderColor = RosadoPastelBorde,
                    focusedLabelColor = RosadoPastelTexto,
                    unfocusedLabelColor = RosadoPastelTexto.copy(alpha = 0.8f)
                )
            )
            ExposedDropdownMenu(
                expanded = menuMedicoAbierto,
                onDismissRequest = { onToggleMenuMedico(false) },
                containerColor = ColorBlancoTarjetas
            ) {
                medicosEncargados.forEach { medico ->
                    val esActual = medico == medicoSeleccionado
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = medico,
                                fontWeight = if (esActual) FontWeight.Bold else FontWeight.Normal,
                                color = if (esActual) RosadoPastelTexto else ColorTextoPrincipal
                            )
                        },
                        trailingIcon = if (esActual) {
                            { Icon(Icons.Default.Check, null, tint = RosadoPastelIcono, modifier = Modifier.size(18.dp)) }
                        } else null,
                        onClick = { onSeleccionarMedico(medico) }
                    )
                }
            }
        }
    }

    Button(
        onClick = onSiguiente,
        enabled = puedeSiguiente,
        modifier = Modifier.align(Alignment.BottomEnd),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = AzulPrincipal,
            contentColor = Color.White,
            disabledContainerColor = AzulClaro,
            disabledContentColor = AzulOscuro.copy(alpha = 0.55f)
        )
    ) {
        Text("Siguiente", fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(6.dp))
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun BoxScope.TarjetaPasoIndicaciones(
    indicaciones: String,
    sinIndicacionesMarcado: Boolean,
    onIndicacionesChange: (String) -> Unit,
    onMarcarSinIndicaciones: () -> Unit,
    onSubir: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 64.dp)
    ) {
        CabeceraTarjetaSeccion(
            icono = Icons.Default.MedicalInformation,
            titulo = "Indicaciones médicas",
            descripcion = "Opcional: escribe indicaciones o pulsa abajo si no aplican.",
            fondoIcono = AmarilloSutilFondo,
            bordeIcono = AmarilloSutilBorde,
            colorIcono = AmarilloSutilIcono
        )

        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = indicaciones,
            onValueChange = onIndicacionesChange,
            label = { Text("Indicaciones médicas (opcional)") },
            placeholder = { Text("Ej: Control en 6 meses, traer lentes actuales...") },
            minLines = 3,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = AmarilloSutilFondo,
                unfocusedContainerColor = AmarilloSutilFondo,
                focusedBorderColor = AmarilloSutilIcono,
                unfocusedBorderColor = AmarilloSutilBorde,
                focusedLabelColor = AmarilloSutilTexto,
                unfocusedLabelColor = AmarilloSutilTexto.copy(alpha = 0.8f)
            )
        )

        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            onClick = onMarcarSinIndicaciones,
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, AmarilloSutilBorde),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = AmarilloSutilFondo,
                contentColor = AmarilloSutilTexto
            )
        ) {
            Icon(
                imageVector = if (sinIndicacionesMarcado) Icons.Default.Check else Icons.Default.Info,
                contentDescription = null,
                tint = AmarilloSutilIcono,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "Sin indicaciones médicas",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    Button(
        onClick = onSubir,
        modifier = Modifier.align(Alignment.BottomEnd),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = AzulPrincipal,
            contentColor = Color.White
        )
    ) {
        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text("Subir examen", fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun TarjetaExamenSubido(
    rutPaciente: String,
    nombrePaciente: String,
    previsionPaciente: String,
    tipo: String,
    fecha: String,
    medico: String,
    archivo: String,
    indicaciones: String,
    onSubirOtro: () -> Unit,
    onIrInicio: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = CircleShape,
            color = VerdeSutilFondo,
            border = BorderStroke(2.dp, VerdeSutilBorde),
            modifier = Modifier.size(76.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Examen subido",
                    tint = VerdeSutilIcono,
                    modifier = Modifier.size(42.dp)
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        Text(
            text = "Examen subido",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = ColorPrincipalOscuro
        )
        Text(
            text = "El registro quedó guardado correctamente.",
            fontSize = 12.sp,
            color = ColorTextoSecundario
        )

        Spacer(Modifier.height(18.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = ColorSecundario,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Paciente: $nombrePaciente ($rutPaciente)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorPrincipalOscuro
                )
                if (previsionPaciente.isNotBlank()) {
                    Text("Previsión: $previsionPaciente", fontSize = 12.sp, color = ColorTextoPrincipal)
                }
                Text("Tipo: $tipo • Fecha: $fecha", fontSize = 12.sp, color = ColorTextoPrincipal)
                Text("Médico: $medico", fontSize = 12.sp, color = ColorTextoPrincipal)
                Text("Archivo: $archivo", fontSize = 12.sp, color = ColorPrincipal, fontWeight = FontWeight.SemiBold)
                Text(
                    text = "Indicaciones: ${if (indicaciones.isBlank()) MENSAJE_SIN_INDICACIONES else indicaciones.trim()}",
                    fontSize = 12.sp,
                    color = ColorTextoSecundario
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onSubirOtro,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, ColorPrincipal),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ColorPrincipalOscuro)
            ) {
                Text("Subir otro", fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onIrInicio,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = ColorPrincipal, contentColor = Color.White)
            ) {
                Text("Ir al inicio", fontWeight = FontWeight.Bold)
            }
        }
    }
}