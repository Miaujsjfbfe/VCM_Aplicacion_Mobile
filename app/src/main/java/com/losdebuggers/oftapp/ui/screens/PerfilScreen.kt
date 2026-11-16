package com.losdebuggers.oftapp.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losdebuggers.oftapp.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

// ---------- Paleta del diseño nuevo (puedes moverla a ui/theme/Color.kt) ----------
private val VerdeOscuro = Color(0xFF004D40)
private val VerdeMedio = Color(0xFF00695C)
private val FondoPantalla = Color(0xFFF1F4F4)
private val FondoItem = Color(0xFFEAF3F0)
private val TextoPrincipal = Color(0xFF1A1C1C)
private val TextoSecundario = Color(0xFF5F6B68)
private val PrevisionFondo = Color(0xFFE6ECFB)
private val PrevisionTexto = Color(0xFF2A3F9E)
private val TelefonoFondo = Color(0xFFCDEFE0)
private val TelefonoIcono = Color(0xFF1B7F5C)
private val CorreoFondo = Color(0xFFDCE8FA)
private val CorreoIcono = Color(0xFF2F5DA8)
private val CiudadFondo = Color(0xFFFFEFC2)
private val CiudadIcono = Color(0xFFD9822B)
private val LogoutFondo = Color(0xFFDDEDE8)
private val LogoutTexto = Color(0xFFBA1A1A)
private val FooterTexto = Color(0xFF0F766E)
private val Blanco = Color.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    usuario: Usuario,
    onVolver: () -> Unit = {},
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { context.getSharedPreferences("perfil_prefs", Context.MODE_PRIVATE) }
    val claveUsuario = remember(usuario.rut) { usuario.rut.filter { it.isLetterOrDigit() } }

    // Datos de respaldo mientras no estén en el modelo Usuario
    val previsionTexto = "Fonasa B"
    val emailTexto = "roberto.gomez@email.com"
    val ciudadTexto = "Santiago, Chile"

    // Estado editable (se guarda en SharedPreferences)
    var telefono by remember {
        mutableStateOf(prefs.getString("telefono_$claveUsuario", null) ?: "+56 9 8765 4321")
    }
    var fotoPath by remember { mutableStateOf(prefs.getString("foto_$claveUsuario", null)) }
    var mostrarEdicion by remember { mutableStateOf(false) }

    // Carga la foto fuera del hilo principal
    val fotoBitmap by produceState<ImageBitmap?>(initialValue = null, fotoPath) {
        value = fotoPath?.let { path ->
            withContext(Dispatchers.IO) { cargarBitmap(path) }?.asImageBitmap()
        }
    }

    // Selector de fotos de la galería
    val selectorFoto = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val anterior = fotoPath
                val nuevo = withContext(Dispatchers.IO) { guardarFoto(context, uri, claveUsuario) }
                if (nuevo != null) {
                    fotoPath = nuevo
                    prefs.edit().putString("foto_$claveUsuario", nuevo).apply()
                    anterior?.let { withContext(Dispatchers.IO) { File(it).delete() } }
                }
            }
        }
    }
    val abrirSelector = {
        selectorFoto.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    Scaffold(
        containerColor = FondoPantalla,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mi Perfil",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Blanco
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Blanco
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VerdeOscuro)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. TARJETA PRINCIPAL: foto, nombre, RUT, previsión y botón editar
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Blanco),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AvatarPerfil(
                        bitmap = fotoBitmap,
                        tamano = 112.dp,
                        onCamaraClick = abrirSelector
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = usuario.nombre,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "RUT: ${usuario.rut}",
                        fontSize = 14.sp,
                        color = TextoSecundario
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(color = PrevisionFondo, shape = RoundedCornerShape(50)) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = PrevisionTexto
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Previsión: $previsionTexto",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrevisionTexto
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedButton(
                        onClick = { mostrarEdicion = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, VerdeMedio),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = FondoItem,
                            contentColor = VerdeMedio
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Editar Perfil",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // 2. TARJETA: DATOS DE CONTACTO
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Blanco),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(start = 4.dp, bottom = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBox,
                            contentDescription = null,
                            tint = VerdeOscuro,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Datos de Contacto",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoPrincipal
                        )
                    }

                    DatoContactoItem(
                        icono = Icons.Default.Phone,
                        fondoIcono = TelefonoFondo,
                        colorIcono = TelefonoIcono,
                        etiqueta = "Teléfono",
                        valor = telefono
                    )
                    DatoContactoItem(
                        icono = Icons.Default.Email,
                        fondoIcono = CorreoFondo,
                        colorIcono = CorreoIcono,
                        etiqueta = "Correo Electrónico",
                        valor = emailTexto
                    )
                    DatoContactoItem(
                        icono = Icons.Default.LocationOn,
                        fondoIcono = CiudadFondo,
                        colorIcono = CiudadIcono,
                        etiqueta = "Ciudad",
                        valor = ciudadTexto
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 3. BOTÓN DE CERRAR SESIÓN
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LogoutFondo,
                    contentColor = LogoutTexto
                ),
                shape = RoundedCornerShape(28.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar Sesión",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Text(
                text = "Clínica Visión Clara",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = FooterTexto
            )
        }
    }

    if (mostrarEdicion) {
        EdicionPerfilSheet(
            fotoBitmap = fotoBitmap,
            telefonoActual = telefono,
            onCambiarFoto = abrirSelector,
            onGuardar = { nuevoTelefono ->
                telefono = nuevoTelefono
                prefs.edit().putString("telefono_$claveUsuario", nuevoTelefono).apply()
                mostrarEdicion = false
            },
            onCerrar = { mostrarEdicion = false }
        )
    }
}

// ---------------------------------------------------------------------------
// AVATAR con insignia de cámara
// ---------------------------------------------------------------------------
@Composable
private fun AvatarPerfil(
    bitmap: ImageBitmap?,
    tamano: Dp,
    onCamaraClick: () -> Unit
) {
    Box(modifier = Modifier.size(tamano)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(FondoItem)
                .border(3.dp, FondoItem, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Sin foto de perfil",
                    tint = VerdeMedio,
                    modifier = Modifier.size(tamano * 0.55f)
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(34.dp)
                .clip(CircleShape)
                .background(VerdeOscuro)
                .border(2.dp, Blanco, CircleShape)
                .clickable(onClick = onCamaraClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Cambiar foto",
                tint = Blanco,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// ---------------------------------------------------------------------------
// ITEM DE CONTACTO
// ---------------------------------------------------------------------------
@Composable
private fun DatoContactoItem(
    icono: ImageVector,
    fondoIcono: Color,
    colorIcono: Color,
    etiqueta: String,
    valor: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = FondoItem),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = fondoIcono
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = colorIcono,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = etiqueta,
                    fontSize = 11.sp,
                    color = TextoSecundario
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = valor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// HOJA INFERIOR DE EDICIÓN (foto + teléfono)
// ---------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EdicionPerfilSheet(
    fotoBitmap: ImageBitmap?,
    telefonoActual: String,
    onCambiarFoto: () -> Unit,
    onGuardar: (String) -> Unit,
    onCerrar: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var digitos by remember { mutableStateOf(extraerDigitos(telefonoActual)) }

    val telefonoValido = digitos.length == 9 && digitos.startsWith("9")
    val mostrarError = digitos.isNotEmpty() && !telefonoValido

    ModalBottomSheet(
        onDismissRequest = onCerrar,
        sheetState = sheetState,
        containerColor = Blanco
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Editar Perfil",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )

            Spacer(modifier = Modifier.height(20.dp))

            AvatarPerfil(bitmap = fotoBitmap, tamano = 96.dp, onCamaraClick = onCambiarFoto)

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onCambiarFoto) {
                Text(
                    text = "Cambiar foto",
                    color = VerdeMedio,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = digitos,
                onValueChange = { digitos = extraerDigitos(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Número de teléfono") },
                prefix = { Text("+56 ") },
                singleLine = true,
                isError = mostrarError,
                supportingText = {
                    Text(
                        if (mostrarError) "Debe tener 9 dígitos y comenzar con 9"
                        else "Ej: 987654321"
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VerdeMedio,
                    focusedLabelColor = VerdeMedio,
                    cursorColor = VerdeMedio
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCerrar,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, VerdeMedio)
                ) {
                    Text("Cancelar", color = VerdeMedio, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { onGuardar(formatearTelefono(digitos)) },
                    enabled = telefonoValido,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeOscuro,
                        contentColor = Blanco
                    )
                ) {
                    Text("Guardar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// UTILIDADES
// ---------------------------------------------------------------------------

/** Deja solo los 9 dígitos del celular (quita +56, espacios y símbolos). */
private fun extraerDigitos(texto: String): String {
    val d = texto.filter { it.isDigit() }
    val sinPais = if (d.startsWith("56") && d.length > 9) d.drop(2) else d
    return sinPais.take(9)
}

/** 987654321 -> +56 9 8765 4321 */
private fun formatearTelefono(nueveDigitos: String): String =
    "+56 ${nueveDigitos[0]} ${nueveDigitos.substring(1, 5)} ${nueveDigitos.substring(5)}"

/** Copia la imagen elegida a almacenamiento interno (el URI del selector es temporal). */


/** Decodifica la foto reducida y corrige la rotación EXIF. */

