package com.Duoc.Themis.ui

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun PantallaCamara(alVolver: () -> Unit) {
    val contexto = LocalContext.current
    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    var foto by remember { mutableStateOf<Bitmap?>(null) }

    val lanzadorCamara = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { exito ->
        val uri = fotoUri
        if (exito && uri != null) {
            val opciones = BitmapFactory.Options().apply { inSampleSize = 4 }
            contexto.contentResolver.openInputStream(uri)?.use { flujo ->
                foto = BitmapFactory.decodeStream(flujo, null, opciones)
            }
        }
    }

    val lanzadorPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            val archivo = File.createTempFile("foto_themis", ".jpg", contexto.cacheDir)
            val uri = FileProvider.getUriForFile(contexto, contexto.packageName + ".provider", archivo)
            fotoUri = uri
            lanzadorCamara.launch(uri)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        foto?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Foto tomada",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Button(onClick = { lanzadorPermiso.launch(Manifest.permission.CAMERA) }) {
            Text("Tomar foto")
        }
        TextButton(onClick = alVolver) {
            Text("Volver")
        }
    }
}