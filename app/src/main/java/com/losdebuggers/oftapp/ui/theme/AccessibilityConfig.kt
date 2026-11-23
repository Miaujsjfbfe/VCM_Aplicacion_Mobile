package com.losdebuggers.oftapp.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class AccessibilityColors(
    val background: Color,
    val surface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val accent: Color,
    val border: Color
)

val StandardAccessibilityColors = AccessibilityColors(
    background = ColorFondo,
    surface = ColorBlancoTarjetas,
    textPrimary = ColorTextoPrincipal,
    textSecondary = ColorTextoSecundario,
    accent = ColorPrincipal,
    border = Color(0xFFE2E8F0)
)

val HighContrastAccessibilityColors = AccessibilityColors(
    background = Color.Black,
    surface = Color(0xFF121212),
    textPrimary = Color.White,
    textSecondary = Color(0xFFE0F2FE),
    accent = Color(0xFFFFD54F),
    border = Color(0xFFFFFFFF)
)

val LocalAccessibilityColors = compositionLocalOf { StandardAccessibilityColors }
val LocalHighContrastEnabled = compositionLocalOf { false }
