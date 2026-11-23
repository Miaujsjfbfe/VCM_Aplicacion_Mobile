package com.losdebuggers.oftapp.data

import dev.insforge.createInsforgeClient
import dev.insforge.auth.Auth
import dev.insforge.auth.auth

object InsforgeClient {
    val client = createInsforgeClient(
        baseURL = "https://9c9qzn7v.us-east.insforge.app/",
        anonKey = "ik_f57cccb589cea1fc5d63d420e55383e1"
    ) {
        install(Auth)
    }
}
