package com.Duoc.Themis.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.Duoc.Themis.model.Usuario

@Composable
fun PantallaPerfil(
    usuario: Usuario?,
    alCerrarSesion: () -> Unit,
    alVolver: () -> Unit
) {
    val azulThemis = Color(0xFF0A1F44)
    val doradoThemis = Color(0xFFD4AF37)
    val superficieClara = Color(0xFFFFFDF7)

    val nombreFormateado = usuario?.nombre
        ?.trim()
        ?.takeIf { it.isNotEmpty() }
        ?.replaceFirstChar { it.uppercase() }
        ?: "-"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado horizontal tipo credencial
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = azulThemis,
                border = BorderStroke(1.5.dp, doradoThemis),
                modifier = Modifier.size(60.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = doradoThemis,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "Mi usuario",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = azulThemis
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Ficha clara con barra lateral dorada
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = superficieClara,
            border = BorderStroke(1.dp, doradoThemis.copy(alpha = 0.8f))
        ) {
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .fillMaxHeight()
                        .background(azulThemis)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 18.dp, vertical = 16.dp)
                ) {
                    CampoPerfil(
                        etiqueta = "NOMBRE",
                        valor = nombreFormateado,
                        colorEtiqueta = doradoThemis,
                        colorValor = azulThemis
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = azulThemis.copy(alpha = 0.12f)
                    )
                    CampoPerfil(
                        etiqueta = "CORREO ELECTRÓNICO",
                        valor = usuario?.correo ?: "-",
                        colorEtiqueta = doradoThemis,
                        colorValor = azulThemis
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = azulThemis.copy(alpha = 0.12f)
                    )
                    CampoPerfil(
                        etiqueta = "TÉRMINOS Y CONDICIONES",
                        valor = "Aceptados",
                        colorEtiqueta = doradoThemis,
                        colorValor = azulThemis
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = alCerrarSesion,
            colors = ButtonDefaults.buttonColors(
                containerColor = doradoThemis,
                contentColor = azulThemis
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Cerrar sesión",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }

        TextButton(onClick = alVolver) {
            Text(
                text = "Volver",
                color = azulThemis
            )
        }
    }
}

@Composable
private fun CampoPerfil(
    etiqueta: String,
    valor: String,
    colorEtiqueta: Color,
    colorValor: Color
) {
    Column {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = colorEtiqueta
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = colorValor
        )
    }
}