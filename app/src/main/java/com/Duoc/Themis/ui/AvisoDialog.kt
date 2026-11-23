package com.Duoc.Themis.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.Duoc.Themis.ui.theme.AzulMarino
import com.Duoc.Themis.ui.theme.Dorado

@Composable
fun AvisoDialog(mensaje: String, alCerrar: () -> Unit) {
    AlertDialog(
        onDismissRequest = alCerrar,
        shape = RoundedCornerShape(16.dp),
        containerColor = AzulMarino,
        titleContentColor = Dorado,
        textContentColor = Color.White,
        title = {
            Text(
                text = "Aviso",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = alCerrar,
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