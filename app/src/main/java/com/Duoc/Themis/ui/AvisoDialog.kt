package com.Duoc.Themis.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AvisoDialog(mensaje: String, alCerrar: () -> Unit) {
    AlertDialog(
        onDismissRequest = alCerrar,
        containerColor = MaterialTheme.colorScheme.secondary,
        titleContentColor = MaterialTheme.colorScheme.primary,
        textContentColor = MaterialTheme.colorScheme.onBackground,
        title = { Text("Aviso") },
        text = { Text(mensaje) },
        confirmButton = {
            Button(onClick = alCerrar) { Text("Aceptar") }
        }
    )
}