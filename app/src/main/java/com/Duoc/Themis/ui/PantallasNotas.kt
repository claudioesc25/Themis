package com.Duoc.Themis.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.Duoc.Themis.model.nota
import com.Duoc.Themis.viewmodel.NotasViewModel

@Composable
fun PantallaNotas(viewModel: NotasViewModel, alVolver: () -> Unit) {
    val azulThemis = Color(0xFF0A1F44)
    val doradoThemis = Color(0xFFD4AF37)
    val cremaThemis = Color(0xFFFFFDF7)

    val notas by viewModel.notas.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    var notaEditando by remember { mutableStateOf<nota?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var filtroTexto by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) { viewModel.cargarNotas() }

    val notasFiltradas = remember(notas, filtroTexto) {
        val consulta = filtroTexto.trim().lowercase()
        if (consulta.isEmpty()) notas
        else notas.filter { n ->
            n.titulo.lowercase().contains(consulta) ||
                    n.contenido.lowercase().contains(consulta)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cremaThemis)
            .systemBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(doradoThemis, RoundedCornerShape(50))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Cuaderno de estudio",
                style = MaterialTheme.typography.labelLarge,
                color = azulThemis.copy(alpha = 0.75f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Encabezado azul con contador y botón "+"
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = azulThemis,
            border = BorderStroke(1.dp, doradoThemis)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Mis Apuntes",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = "Notas y resúmenes jurídicos",
                        style = MaterialTheme.typography.bodySmall,
                        color = doradoThemis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = doradoThemis.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, doradoThemis.copy(alpha = 0.55f))
                    ) {
                        Text(
                            text = "${notas.size}",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = doradoThemis,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        onClick = {
                            notaEditando = null
                            mostrarDialogo = true
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = doradoThemis,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Nueva nota",
                                tint = azulThemis,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Buscador / filtro de apuntes
        OutlinedTextField(
            value = filtroTexto,
            onValueChange = { filtroTexto = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            placeholder = {
                Text(
                    text = "Filtrar apuntes por título o texto...",
                    color = azulThemis.copy(alpha = 0.5f)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = azulThemis
                )
            },
            trailingIcon = {
                if (filtroTexto.isNotEmpty()) {
                    IconButton(onClick = { filtroTexto = "" }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Limpiar filtro",
                            tint = azulThemis
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = azulThemis,
                unfocusedTextColor = azulThemis,
                focusedBorderColor = azulThemis,
                unfocusedBorderColor = doradoThemis,
                focusedContainerColor = cremaThemis,
                unfocusedContainerColor = cremaThemis,
                cursorColor = azulThemis
            )
        )

        if (mensaje.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (notasFiltradas.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = cremaThemis,
                    border = BorderStroke(1.dp, doradoThemis.copy(alpha = 0.7f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = doradoThemis,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (notas.isEmpty()) {
                                "No tienes apuntes todavía"
                            } else {
                                "Sin coincidencias para \"$filtroTexto\""
                            },
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = azulThemis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (notas.isEmpty()) {
                                "Toca el botón \"+\" para guardar resúmenes, artículos o ideas."
                            } else {
                                "Prueba con otra palabra o limpia el filtro."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = azulThemis.copy(alpha = 0.75f)
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(notasFiltradas, key = { it.id }) { n ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = cremaThemis),
                        border = BorderStroke(1.dp, doradoThemis.copy(alpha = 0.75f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Box(
                                modifier = Modifier
                                    .width(32.dp)
                                    .height(3.dp)
                                    .background(doradoThemis, RoundedCornerShape(50))
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = n.titulo,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = azulThemis
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = n.contenido,
                                style = MaterialTheme.typography.bodyMedium,
                                color = azulThemis.copy(alpha = 0.88f)
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 10.dp),
                                color = azulThemis.copy(alpha = 0.1f)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        notaEditando = n
                                        mostrarDialogo = true
                                    },
                                    colors = ButtonDefaults.textButtonColors(contentColor = azulThemis)
                                ) {
                                    Text("Editar", fontWeight = FontWeight.SemiBold)
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                TextButton(
                                    onClick = { viewModel.eliminarNota(n.id) },
                                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Text("Eliminar", fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        TextButton(
            onClick = alVolver,
            colors = ButtonDefaults.textButtonColors(contentColor = azulThemis)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Volver", fontWeight = FontWeight.SemiBold)
        }
    }

    if (mostrarDialogo) {
        DialogoNota(
            inicial = notaEditando,
            azulThemis = azulThemis,
            doradoThemis = doradoThemis,
            cremaThemis = cremaThemis,
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
    azulThemis: Color,
    doradoThemis: Color,
    cremaThemis: Color,
    alGuardar: (String, String) -> Unit,
    alCerrar: () -> Unit
) {
    var titulo by remember(inicial) { mutableStateOf(inicial?.titulo ?: "") }
    var contenido by remember(inicial) { mutableStateOf(inicial?.contenido ?: "") }

    AlertDialog(
        onDismissRequest = alCerrar,
        shape = RoundedCornerShape(16.dp),
        containerColor = cremaThemis,
        tonalElevation = 0.dp,
        title = {
            Column {
                Text(
                    text = if (inicial == null) "Nueva nota" else "Editar nota",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = azulThemis
                )
                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider(color = doradoThemis.copy(alpha = 0.6f))
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    supportingText = {
                        if (titulo.isBlank()) {
                            Text(
                                text = "Ingresa un título para guardar",
                                color = azulThemis.copy(alpha = 0.6f)
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = azulThemis,
                        unfocusedTextColor = azulThemis,
                        focusedLabelColor = azulThemis,
                        unfocusedLabelColor = azulThemis.copy(alpha = 0.7f),
                        focusedBorderColor = azulThemis,
                        unfocusedBorderColor = doradoThemis,
                        cursorColor = azulThemis
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = contenido,
                    onValueChange = { contenido = it },
                    label = { Text("Contenido") },
                    minLines = 4,
                    maxLines = 6,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = azulThemis,
                        unfocusedTextColor = azulThemis,
                        focusedLabelColor = azulThemis,
                        unfocusedLabelColor = azulThemis.copy(alpha = 0.7f),
                        focusedBorderColor = azulThemis,
                        unfocusedBorderColor = doradoThemis,
                        cursorColor = azulThemis
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (titulo.isNotBlank()) {
                        alGuardar(titulo.trim(), contenido.trim())
                    }
                },
                enabled = titulo.isNotBlank(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = doradoThemis,
                    contentColor = azulThemis,
                    disabledContainerColor = doradoThemis.copy(alpha = 0.35f),
                    disabledContentColor = azulThemis.copy(alpha = 0.4f)
                )
            ) {
                Text("Guardar", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = alCerrar,
                colors = ButtonDefaults.textButtonColors(contentColor = azulThemis.copy(alpha = 0.75f))
            ) {
                Text("Cancelar")
            }
        }
    )
}