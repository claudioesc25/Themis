package com.Duoc.Themis.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PantallaMenu(nombreUsuario: String, alNavegar: (String) -> Unit) {
    val doradoThemis = Color(0xFFD4AF37)
    val azulThemis = Color(0xFF0A1F44)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoThemis(modifier = Modifier.size(96.dp))
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Bienvenido, $nombreUsuario",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Selecciona una opción",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))

        BotonMenu(
            texto = "Leyes y Artículos",
            icono = Icons.Default.Menu,
            alClic = { alNavegar(Rutas.DOCUMENTOS_XANO) }
        )
        BotonMenu(
            texto = "Cámara",
            icono = Icons.Default.Add,
            alClic = { alNavegar(Rutas.CAMARA) }
        )
        BotonMenu(
            texto = "Mapa",
            icono = Icons.Default.LocationOn,
            alClic = { alNavegar(Rutas.MAPA) }
        )
        BotonMenu(
            texto = "Mis Apuntes",
            icono = Icons.Default.Info,
            alClic = { alNavegar(Rutas.NOTAS) }
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            color = azulThemis.copy(alpha = 0.15f)
        )

        BotonMenu(
            texto = "Mi usuario",
            icono = Icons.Default.Person,
            alClic = { alNavegar(Rutas.PERFIL) },
            colorFondo = doradoThemis,
            colorTexto = azulThemis,
            colorBorde = azulThemis
        )
    }
}

@Composable
private fun BotonMenu(
    texto: String,
    icono: ImageVector,
    alClic: () -> Unit,
    colorFondo: Color = MaterialTheme.colorScheme.secondary,
    colorTexto: Color = MaterialTheme.colorScheme.primary,
    colorBorde: Color = MaterialTheme.colorScheme.primary
) {
    Button(
        onClick = alClic,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorFondo,
            contentColor = colorTexto
        ),
        border = BorderStroke(1.dp, colorBorde),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}