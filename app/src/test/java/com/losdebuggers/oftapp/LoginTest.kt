package com.losdebuggers.oftapp

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

fun validarLogin(correo: String, clave: String): Boolean {
    return correo == "admin@test.cl" && clave == "1234"
}

class LoginTest {

    @Test
    fun loginCorrecto() {
        assertTrue(validarLogin("admin@test.cl", "1234"))
    }

    @Test
    fun claveIncorrecta() {
        assertFalse(validarLogin("admin@test.cl", "0000"))
    }

    @Test
    fun camposVacios() {
        assertFalse(validarLogin("", ""))
    }
}
