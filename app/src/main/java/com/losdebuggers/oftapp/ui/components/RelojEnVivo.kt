package com.losdebuggers.oftapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RelojEnVivo(
    modifier: Modifier = Modifier,
    colorTexto: Color = Color.White,
    colorFondo: Color = Color.White.copy(alpha = 0.15f)
) {
    var horaActual by remember { mutableStateOf("") }
    var fechaActual by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val localeCl = Locale.Builder().setLanguage("es").setRegion("CL").build()
        val formatoHora = SimpleDateFormat("HH:mm:ss", localeCl)
        val formatoFecha = SimpleDateFormat("EEE, dd MMM", localeCl)
        while (true) {
            val ahora = Date()
            horaActual = formatoHora.format(ahora)
            fechaActual = formatoFecha.format(ahora).replaceFirstChar { it.uppercase() }
            delay(1000L)
        }
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = colorFondo
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.AccessTime,
                contentDescription = "Reloj en tiempo real",
                tint = colorTexto,
                modifier = Modifier.size(13.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "$horaActual hrs • $fechaActual",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorTexto
            )
        }
    }
}
