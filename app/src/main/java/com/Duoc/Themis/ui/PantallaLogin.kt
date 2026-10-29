package com.Duoc.Themis.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.Duoc.Themis.viewmodel.AuthViewModel

@Composable
fun PantallaLogin(
    viewModel: AuthViewModel,
    alIniciarSesion: () -> Unit,
    alVolver: () -> Unit
) {
    val formulario by viewModel.formulario.collectAsState()
    val errores by viewModel.errores.collectAsState()
    val aviso by viewModel.aviso.collectAsState()
    val registroExitoso by viewModel.registroExitoso.collectAsState()
    val estadoSnackbar = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.limpiarFormulario()
    }

    LaunchedEffect(registroExitoso) {
        if (registroExitoso) {
            estadoSnackbar.showSnackbar("¡Registro exitoso! Ya puedes iniciar sesión")
            viewModel.consumirRegistroExitoso()
        }
    }

    Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoThemis(modifier = Modifier.size(96.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))
            CampoFormulario(
                valor = formulario.correo,
                alCambiar = viewModel::onCorreoChange,
                etiqueta = "Correo",
                icono = Icons.Default.Email,
                error = errores.correo,
                tipoTeclado = KeyboardType.Email
            )
            CampoFormulario(
                valor = formulario.contrasena,
                alCambiar = viewModel::onContrasenaChange,
                etiqueta = "Contraseña",
                icono = Icons.Default.Lock,
                error = errores.contrasena,
                tipoTeclado = KeyboardType.Password,
                esContrasena = true
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.iniciarSesion(alIniciarSesion) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar")
            }
            TextButton(onClick = alVolver) {
                Text("Volver")
            }
        }
        SnackbarHost(
            hostState = estadoSnackbar,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    aviso?.let { AvisoDialog(mensaje = it, alCerrar = viewModel::cerrarAviso) }
}