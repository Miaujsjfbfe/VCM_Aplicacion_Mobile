package com.losdebuggers.oftapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.losdebuggers.oftapp.model.Usuario
import com.losdebuggers.oftapp.ui.screens.ExamenesScreen
import com.losdebuggers.oftapp.ui.screens.InicioScreen
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

                    // La vista se decide por el estado de sesión guardado en SQLite:
                    // al cambiar `usuario`, Login deja de existir en la composición
                    // (no queda en el back stack, no se puede volver con "atrás").
                    when {
                        !sesionVerificada -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                            CircularProgressIndicator(color = ColorPrincipal)
                        }
                        usuario == null -> LoginScreen(viewModel = loginViewModel)
                        else -> AppAutenticada(
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

/** Contenido visible solo con sesión activa: Inicio y (por ahora) Exámenes. */
@Composable
private fun AppAutenticada(
    usuario: Usuario,
    examenViewModel: ExamenViewModel,
    onLogout: () -> Unit
) {
    var enExamenes by rememberSaveable { mutableStateOf(false) }
    BackHandler(enabled = enExamenes) { enExamenes = false } // atrás: Exámenes -> Inicio

    if (enExamenes) {
        ExamenesScreen(viewModel = examenViewModel, onExamenClick = { })
    } else {
        InicioScreen(
            usuario = usuario,
            onNavigateToExamenes = { enExamenes = true },
            onLogout = onLogout
        )
    }
}
