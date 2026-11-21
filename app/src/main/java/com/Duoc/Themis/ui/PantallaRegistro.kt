package com.Duoc.Themis.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Duoc.Themis.ui.theme.AzulMarino
import com.Duoc.Themis.ui.theme.Dorado
import com.Duoc.Themis.viewmodel.AuthViewModel

@Composable
fun PantallaRegistro(
    viewModel: AuthViewModel,
    alRegistrar: () -> Unit,
    alVolver: () -> Unit
) {
    val cremaThemis = Color(0xFFFFFDF7)

    val formulario by viewModel.formulario.collectAsState()
    val errores by viewModel.errores.collectAsState()
    val mensajeEstado by viewModel.mensajeEstado.collectAsState()
    val registroExitoso by viewModel.registroExitoso.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.limpiarFormulario()
    }

    if (registroExitoso) {
        AlertDialog(
            onDismissRequest = {
                viewModel.consumirRegistroExitoso()
                alRegistrar()
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = AzulMarino,
            titleContentColor = Dorado,
            textContentColor = Color.White,
            title = {
                Text(
                    text = "Usuario creado",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = mensajeEstado.ifBlank {
                        "¡Usuario creado con éxito! Ya puedes iniciar sesión."
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.consumirRegistroExitoso()
                        alRegistrar()
                    },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Dorado.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Dorado,
                        contentColor = AzulMarino
                    )
                ) {
                    Text(
                        text = "Aceptar",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cremaThemis)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoThemis(modifier = Modifier.size(96.dp))

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Crear cuenta",
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
            text = "Regístrate para guardar tus apuntes y consultas",
            style = MaterialTheme.typography.bodyMedium,
            color = AzulMarino.copy(alpha = 0.78f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        CampoFormulario(
            valor = formulario.nombre,
            alCambiar = viewModel::onNombreChange,
            etiqueta = "Nombre",
            icono = Icons.Default.Person,
            error = errores.nombre
        )

        Spacer(modifier = Modifier.height(10.dp))

        CampoFormulario(
            valor = formulario.correo,
            alCambiar = viewModel::onCorreoChange,
            etiqueta = "Correo",
            icono = Icons.Default.Email,
            error = errores.correo,
            tipoTeclado = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(10.dp))

        CampoFormulario(
            valor = formulario.contrasena,
            alCambiar = viewModel::onContrasenaChange,
            etiqueta = "Contraseña",
            icono = Icons.Default.Lock,
            error = errores.contrasena,
            tipoTeclado = KeyboardType.Password,
            esContrasena = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        CampoFormulario(
            valor = formulario.confirmacion,
            alCambiar = viewModel::onConfirmacionChange,
            etiqueta = "Confirmar contraseña",
            icono = Icons.Default.Lock,
            error = errores.confirmacion,
            tipoTeclado = KeyboardType.Password,
            esContrasena = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.onTerminosChange(!formulario.aceptaTerminos) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = formulario.aceptaTerminos,
                onCheckedChange = viewModel::onTerminosChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = AzulMarino,
                    uncheckedColor = AzulMarino.copy(alpha = 0.65f),
                    checkmarkColor = Dorado
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Acepto los términos y condiciones",
                style = MaterialTheme.typography.bodyMedium,
                color = AzulMarino.copy(alpha = 0.88f)
            )
        }

        errores.terminos?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        Button(
            onClick = {
                viewModel.registrarUsuario(
                    nombre = formulario.nombre,
                    correo = formulario.correo,
                    contrasenia = formulario.contrasena
                )
            },
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
                text = "Registrarse",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }

        if (mensajeEstado.isNotBlank() && !registroExitoso) {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = mensajeEstado,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
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
}