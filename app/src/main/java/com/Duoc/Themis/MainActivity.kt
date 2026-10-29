package com.Duoc.Themis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.Duoc.Themis.model.AppDatabase
import com.Duoc.Themis.repository.UsuarioRepository
import com.Duoc.Themis.ui.Navegacion
import com.Duoc.Themis.ui.theme.ThemisTheme
import com.Duoc.Themis.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {

    private val baseDatos by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "themis_db").build()
    }

    private val authViewModel: AuthViewModel by viewModels {
        viewModelFactory {
            initializer { AuthViewModel(UsuarioRepository(baseDatos.usuarioDao())) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThemisTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navegacion(authViewModel)
                }
            }
        }
    }
}