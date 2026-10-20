package com.losdebuggers.oftapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.losdebuggers.oftapp.ui.screens.LoginScreen
import com.losdebuggers.oftapp.ui.theme.OftAppTheme
import com.losdebuggers.oftapp.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OftAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(
                        viewModel = loginViewModel,
                        onLoginSuccess = {
                            println("Login correcto -> Navegar al Home / InicioScreen")
                        }
                    )
                }
            }
        }
    }
}