package com.Duoc.Themis.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.Duoc.Themis.viewmodel.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DocumentosScreen(
    viewModel: AuthViewModel
) {
    val documentos by viewModel.documentos.collectAsState()
    val mensajeEstado by viewModel.mensajeEstado.collectAsState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var generandoId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        viewModel.cargarDocumentos()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Leyes y Articulos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 55.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (mensajeEstado.isNotEmpty()) {
                Text(
                    text = mensajeEstado,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(documentos, key = { it.id }) { doc ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = doc.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tipo: ${doc.tipo_documento}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = doc.contenido,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    scope.launch {
                                        generandoId = doc.id
                                        val ok = withContext(Dispatchers.IO) {
                                            PdfGenerador.guardarDocumento(context, doc)
                                        }
                                        generandoId = null
                                        snackbarHostState.showSnackbar(
                                            if (ok) {
                                                "PDF guardado en Descargas"
                                            } else {
                                                "Error al generar el PDF"
                                            }
                                        )
                                    }
                                },
                                enabled = generandoId == null,
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text(
                                    text = if (generandoId == doc.id) "Generando..." else "Generar PDF"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}