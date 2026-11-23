package com.losdebuggers.oftapp.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OftAppBackendApplication

fun main(args: Array<String>) {
    runApplication<OftAppBackendApplication>(*args)
}
