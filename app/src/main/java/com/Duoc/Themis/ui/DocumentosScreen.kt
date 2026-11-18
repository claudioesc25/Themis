package com.Duoc.Themis.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.Duoc.Themis.viewmodel.ThemisViewModel

@Composable
fun DocumentosScreen(viewModel: ThemisViewModel = ThemisViewModel()) {
    val documentos by viewModel.documentos.collectAsState()
    val mensajeEstado by viewModel.mensajeEstado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Documentos Legales (Xano)",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Muestra el mensaje de éxito o error de las peticiones POST
        if (mensajeEstado.isNotEmpty()) {
            Text(text = mensajeEstado, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Botón para probar una inserción (POST)
        Button(onClick = {
            viewModel.registrarUsuario("Diego", "diego@duoc.cl", "123456")
        }) {
            Text(text = "Probar Registro de Usuario (POST)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista para mostrar los documentos que vienen de Xano (GET)
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(documentos) { doc ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = doc.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Tipo: ${doc.tipodocumento}", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = doc.contenido, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}