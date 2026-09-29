package com.losdebuggers.oftapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.losdebuggers.oftapp.ui.screens.ExamenesScreen
import com.losdebuggers.oftapp.ui.theme.OftAppTheme
import com.losdebuggers.oftapp.viewmodel.ExamenViewModel

class MainActivity : ComponentActivity() {

    // Instanciamos el ViewModel
    private val examenViewModel: ExamenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OftAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llamamos a la pantalla de Exámenes
                    ExamenesScreen(
                        viewModel = examenViewModel,
                        onExamenClick = { id ->
                            // Por ahora solo imprime en consola al hacer clic
                            println("Examen seleccionado ID: $id")
                        }
                    )
                }
            }
        }
    }
}