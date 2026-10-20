package com.losdebuggers.oftapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = ColorPrincipal,
    secondary = ColorSecundario,
    background = ColorFondo,
    surface = ColorBlancoTarjetas,
    onPrimary = ColorBlancoTarjetas,
    onSecondary = ColorPrincipal,
    onBackground = ColorTextoPrincipal,
    onSurface = ColorTextoPrincipal
)

@Composable
fun OftAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}