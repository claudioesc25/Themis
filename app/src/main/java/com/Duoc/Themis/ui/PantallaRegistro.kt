package com.Duoc.Themis.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.Duoc.Themis.viewmodel.AuthViewModel

@Composable
fun PantallaRegistro(
    viewModel: AuthViewModel,
    alRegistrar: () -> Unit,
    alVolver: () -> Unit
) {
    val formulario by viewModel.formulario.collectAsState()
    val errores by viewModel.errores.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.limpiarFormulario()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoThemis(modifier = Modifier.size(96.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        CampoFormulario(
            valor = formulario.nombre,
            alCambiar = viewModel::onNombreChange,
            etiqueta = "Nombre",
            icono = Icons.Default.Person,
            error = errores.nombre
        )
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
        CampoFormulario(
            valor = formulario.confirmacion,
            alCambiar = viewModel::onConfirmacionChange,
            etiqueta = "Confirmar contraseña",
            icono = Icons.Default.Lock,
            error = errores.confirmacion,
            tipoTeclado = KeyboardType.Password,
            esContrasena = true
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = formulario.aceptaTerminos,
                onCheckedChange = viewModel::onTerminosChange
            )
            Text("Acepto los términos y condiciones")
        }
        errores.terminos?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { viewModel.registrar(alRegistrar) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
        TextButton(onClick = alVolver) {
            Text("Volver")
        }
    }
}