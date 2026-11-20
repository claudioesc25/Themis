package com.Duoc.Themis.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.Duoc.Themis.model.nota
import com.Duoc.Themis.viewmodel.NotasViewModel

@Composable
fun PantallaNotas(viewModel: NotasViewModel, alVolver: () -> Unit) {
    val notas by viewModel.notas.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    var notaEditando by remember { mutableStateOf<nota?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.cargarNotas() }

    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding().padding(16.dp)
    ) {
        Text("Mis notas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        if (mensaje.isNotBlank()) {
            Text(mensaje, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = { notaEditando = null; mostrarDialogo = true },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Nueva nota") }

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notas) { n ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(n.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text(n.contenido, style = MaterialTheme.typography.bodyMedium)
                        Row {
                            TextButton(onClick = { notaEditando = n; mostrarDialogo = true }) { Text("Editar") }
                            TextButton(onClick = { viewModel.eliminarNota(n.id) }) { Text("Eliminar") }
                        }
                    }
                }
            }
        }

        TextButton(onClick = alVolver) { Text("Volver") }
    }

    if (mostrarDialogo) {
        DialogoNota(
            inicial = notaEditando,
            alGuardar = { titulo, contenido ->
                val editando = notaEditando
                if (editando == null) viewModel.crearNota(titulo, contenido)
                else viewModel.actualizarNota(editando.id, titulo, contenido)
                mostrarDialogo = false
            },
            alCerrar = { mostrarDialogo = false }
        )
    }
}

@Composable
private fun DialogoNota(
    inicial: nota?,
    alGuardar: (String, String) -> Unit,
    alCerrar: () -> Unit
) {
    var titulo by remember { mutableStateOf(inicial?.titulo ?: "") }
    var contenido by remember { mutableStateOf(inicial?.contenido ?: "") }

    AlertDialog(
        onDismissRequest = alCerrar,
        title = { Text(if (inicial == null) "Nueva nota" else "Editar nota") },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Titulo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = contenido,
                    onValueChange = { contenido = it },
                    label = { Text("Contenido") },
                    minLines = 8,
                    maxLines = 8,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { if (titulo.isNotBlank()) alGuardar(titulo, contenido) }) {
                Text("Guardar")
            }
        },
        dismissButton = { TextButton(onClick = alCerrar) { Text("Cancelar") } }
    )
}