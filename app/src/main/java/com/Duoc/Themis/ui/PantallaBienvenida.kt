package com.Duoc.Themis.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Duoc.Themis.ui.theme.AzulMarino
import com.Duoc.Themis.ui.theme.Dorado

@Composable
fun PantallaBienvenida(alIrALogin: () -> Unit, alIrARegistro: () -> Unit) {
    val cremaThemis = Color(0xFFFFFDF7)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cremaThemis)
            .systemBarsPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        LogoThemis(modifier = Modifier.size(144.dp))

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "THEMIS",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            ),
            color = Dorado
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .width(48.dp)
                .height(2.dp)
                .background(Dorado.copy(alpha = 0.75f), RoundedCornerShape(50))
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Normativa, apuntes y consulta jurídica",
            style = MaterialTheme.typography.bodyMedium,
            color = AzulMarino.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(44.dp))

        Button(
            onClick = alIrALogin,
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
                text = "Iniciar sesión",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            onClick = alIrARegistro,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = cremaThemis,
                contentColor = AzulMarino
            ),
            border = BorderStroke(1.5.dp, Dorado),
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

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Plataforma de estudio y consulta",
            style = MaterialTheme.typography.labelMedium,
            color = AzulMarino.copy(alpha = 0.55f),
            textAlign = TextAlign.Center
        )
    }
}