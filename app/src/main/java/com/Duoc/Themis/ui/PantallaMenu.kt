package com.Duoc.Themis.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaMenu(nombreUsuario: String, alNavegar: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoThemis(modifier = Modifier.size(96.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Bienvenido, $nombreUsuario",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        BotonMenu(texto = "Artículos", alClic = { alNavegar(Rutas.ARTICULOS) })
        BotonMenu(texto = "Leyes", alClic = { alNavegar(Rutas.LEYES) })
        BotonMenu(texto = "Cámara", alClic = { alNavegar(Rutas.CAMARA) })
        BotonMenu(texto = "Mi usuario", alClic = { alNavegar(Rutas.PERFIL) })
    }
}

@Composable
private fun BotonMenu(texto: String, alClic: () -> Unit) {
    Button(
        onClick = alClic,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Text(texto)
    }
}