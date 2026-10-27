package com.losdebuggers.oftapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.losdebuggers.oftapp.model.Usuario
import com.losdebuggers.oftapp.ui.screens.MainScreen
import com.losdebuggers.oftapp.ui.screens.LoginScreen
import com.losdebuggers.oftapp.ui.theme.ColorPrincipal
import com.losdebuggers.oftapp.ui.theme.OftAppTheme
import com.losdebuggers.oftapp.viewmodel.ExamenViewModel
import com.losdebuggers.oftapp.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private val examenViewModel: ExamenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OftAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val usuario by loginViewModel.usuario.collectAsState()
                    val sesionVerificada by loginViewModel.sesionVerificada.collectAsState()

                    when {
                        !sesionVerificada -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                            CircularProgressIndicator(color = ColorPrincipal)
                        }
                        usuario == null -> LoginScreen(viewModel = loginViewModel)
                        else -> MainScreen(
                            usuario = usuario!!,
                            examenViewModel = examenViewModel,
                            onLogout = loginViewModel::cerrarSesion
                        )
                    }
                }
            }
        }
    }
}