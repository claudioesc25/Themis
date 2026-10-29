package com.Duoc.Themis.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.Duoc.Themis.ui.theme.AzulMarino
import com.Duoc.Themis.ui.theme.Dorado

@Composable
fun AvisoDialog(mensaje: String, alCerrar: () -> Unit) {
    AlertDialog(
        onDismissRequest = alCerrar,
        containerColor = AzulMarino,
        titleContentColor = Dorado,
        textContentColor = Color.White,
        title = { Text("Aviso") },
        text = { Text(mensaje) },
        confirmButton = {
            Button(
                onClick = alCerrar,
                colors = ButtonDefaults.buttonColors(containerColor = Dorado, contentColor = AzulMarino)
            ) {
                Text("Aceptar")
            }
        }
    )
}