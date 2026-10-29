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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.Duoc.Themis.ui.theme.AzulMarino
import com.Duoc.Themis.ui.theme.Dorado

@Composable
fun PantallaBienvenida(alIrALogin: () -> Unit, alIrARegistro: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoThemis(modifier = Modifier.size(140.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "THEMIS",
            style = MaterialTheme.typography.headlineLarge,
            color = Dorado
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = alIrALogin,
            colors = ButtonDefaults.buttonColors(containerColor = AzulMarino, contentColor = Dorado),
            border = BorderStroke(2.dp, AzulMarino),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = alIrARegistro,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Dorado),
            border = BorderStroke(2.dp, AzulMarino),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
    }
}