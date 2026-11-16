package com.losdebuggers.oftapp

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ExampleJUnit5Test {

    @Test
    fun testAddition() {
        val sum = 2 + 2
        assertEquals(4, sum)
    }

    @ParameterizedTest
    @ValueSource(strings = ["Tecnólogo", "Oftalmólogo", "Paciente", "Admin"])
    fun testValidRoles(role: String) {
        val isValid = role in listOf("Tecnólogo", "Oftalmólogo", "Paciente", "Admin")
        assertEquals(true, isValid)
    }
}
