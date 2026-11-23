package com.losdebuggers.oftapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class LoginUITest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun botonVisible() {
        rule.onNodeWithText("Iniciar sesión").assertIsDisplayed()
    }

    @Test
    fun loginCorrecto() {
        rule.onNodeWithTag("correo").performTextInput("admin@test.cl")
        rule.onNodeWithTag("clave").performTextInput("1234")
        rule.onNodeWithText("Iniciar sesión").performClick()
    }

    @Test
    fun loginIncorrecto() {
        rule.onNodeWithText("Iniciar sesión").performClick()
    }
}
