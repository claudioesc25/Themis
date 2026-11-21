package com.Duoc.Themis.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Duoc.Themis.ui.theme.AzulMarino
import com.Duoc.Themis.ui.theme.Dorado
import com.Duoc.Themis.viewmodel.AuthViewModel

@Composable
fun PantallaLogin(
    viewModel: AuthViewModel,
    alIniciarSesion: () -> Unit,
    alVolver: () -> Unit
) {
    val cremaThemis = Color(0xFFFFFDF7)

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(cremaThemis)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoThemis(modifier = Modifier.size(104.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Dorado
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .width(42.dp)
                    .height(2.dp)
                    .background(Dorado.copy(alpha = 0.75f), RoundedCornerShape(50))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Accede a tu cuenta de Themis",
                style = MaterialTheme.typography.bodyMedium,
                color = AzulMarino.copy(alpha = 0.78f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            CampoFormulario(
                valor = formulario.correo,
                alCambiar = viewModel::onCorreoChange,
                etiqueta = "Correo",
                icono = Icons.Default.Email,
                error = errores.correo,
                tipoTeclado = androidx.compose.ui.text.input.KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(12.dp))

            CampoFormulario(
                valor = formulario.contrasena,
                alCambiar = viewModel::onContrasenaChange,
                etiqueta = "Contraseña",
                icono = Icons.Default.Lock,
                error = errores.contrasena,
                tipoTeclado = androidx.compose.ui.text.input.KeyboardType.Password,
                esContrasena = true
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = { viewModel.iniciarSesion(alIniciarSesion) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulMarino,
                    contentColor = Dorado
                ),
                contentPadding = PaddingValues(vertical = 14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Ingresar",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = alVolver,
                colors = ButtonDefaults.textButtonColors(contentColor = AzulMarino)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Volver",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }

        SnackbarHost(
            hostState = estadoSnackbar,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    aviso?.let { AvisoDialog(mensaje = it, alCerrar = viewModel::cerrarAviso) }
}