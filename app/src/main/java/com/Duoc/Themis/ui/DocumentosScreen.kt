package com.Duoc.Themis.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.Duoc.Themis.viewmodel.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DocumentosScreen(
    viewModel: AuthViewModel,
    alVolver: (() -> Unit)? = null
) {
    val azulThemis = Color(0xFF0A1F44)
    val doradoThemis = Color(0xFFD4AF37)
    val superficieClara = Color(0xFFFFFDF7)

    val documentos by viewModel.documentos.collectAsState()
    val mensajeEstado by viewModel.mensajeEstado.collectAsState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var generandoId by remember { mutableStateOf<Int?>(null) }
    var textoBusqueda by rememberSaveable { mutableStateOf("") }
    var tipoSeleccionado by rememberSaveable { mutableStateOf<String?>(null) }
    val expandidos = remember { mutableStateMapOf<Int, Boolean>() }

    LaunchedEffect(Unit) {
        viewModel.cargarDocumentos()
    }

    val tiposDisponibles = remember(documentos) {
        documentos
            .map { it.tipo_documento.trim() }
            .filter { it.isNotEmpty() }
            .distinct()
    }

    val documentosFiltrados = remember(documentos, textoBusqueda, tipoSeleccionado) {
        val consulta = textoBusqueda.trim().lowercase()
        documentos.filter { doc ->
            val coincideTipo = tipoSeleccionado == null ||
                    doc.tipo_documento.trim().equals(tipoSeleccionado, ignoreCase = true)

            val coincideTexto = consulta.isEmpty() ||
                    doc.titulo.lowercase().contains(consulta) ||
                    doc.contenido.lowercase().contains(consulta) ||
                    doc.tipo_documento.lowercase().contains(consulta)

            coincideTipo && coincideTexto
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Leyes y Articulos",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = azulThemis,
                modifier = Modifier.padding(top = 36.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("Buscar ley, artículo o palabra clave...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = azulThemis
                    )
                },
                trailingIcon = {
                    if (textoBusqueda.isNotEmpty()) {
                        IconButton(onClick = { textoBusqueda = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar búsqueda",
                                tint = azulThemis
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = azulThemis,
                    unfocusedBorderColor = doradoThemis,
                    focusedContainerColor = superficieClara,
                    unfocusedContainerColor = superficieClara
                )
            )

            if (tiposDisponibles.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = tipoSeleccionado == null,
                        onClick = { tipoSeleccionado = null },
                        label = { Text("Todos") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = azulThemis,
                            selectedLabelColor = doradoThemis
                        )
                    )
                    tiposDisponibles.forEach { tipo ->
                        FilterChip(
                            selected = tipoSeleccionado == tipo,
                            onClick = {
                                tipoSeleccionado = if (tipoSeleccionado == tipo) null else tipo
                            },
                            label = { Text(tipo) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = azulThemis,
                                selectedLabelColor = doradoThemis
                            )
                        )
                    }
                }
            }

            if (mensajeEstado.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = mensajeEstado,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (documentosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (documentos.isEmpty()) {
                            "Cargando documentos..."
                        } else {
                            "No se encontraron resultados con ese filtro."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = azulThemis.copy(alpha = 0.75f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(documentosFiltrados, key = { it.id }) { doc ->
                        val estaExpandido = expandidos[doc.id] ?: false
                        val esTextoLargo = doc.contenido.length > 180

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = superficieClara
                            ),
                            border = BorderStroke(1.dp, doradoThemis.copy(alpha = 0.75f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = azulThemis.copy(alpha = 0.08f)
                                ) {
                                    Text(
                                        text = doc.tipo_documento,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = azulThemis,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = doc.titulo,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = azulThemis
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = doc.contenido,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = azulThemis.copy(alpha = 0.88f),
                                    maxLines = if (estaExpandido || !esTextoLargo) Int.MAX_VALUE else 4,
                                    overflow = TextOverflow.Ellipsis
                                )

                                if (esTextoLargo) {
                                    TextButton(
                                        onClick = { expandidos[doc.id] = !estaExpandido },
                                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (estaExpandido) "Ver menos" else "Ver más",
                                            color = azulThemis,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

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
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = doradoThemis,
                                        contentColor = azulThemis
                                    ),
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text(
                                        text = if (generandoId == doc.id) "Generando..." else "Generar PDF",
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (alVolver != null) {
                Spacer(modifier = Modifier.height(4.dp))
                TextButton(onClick = alVolver) {
                    Text(
                        text = "Volver",
                        color = azulThemis
                    )
                }
            }
        }
    }
}