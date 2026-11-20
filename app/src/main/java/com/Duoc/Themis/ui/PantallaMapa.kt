package com.Duoc.Themis.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.Duoc.Themis.model.LUGARES
import com.Duoc.Themis.model.Lugar
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@SuppressLint("MissingPermission")
@Composable
fun PantallaMapa(alVolver: () -> Unit) {
    val contexto = LocalContext.current

    var tienePermiso by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                contexto, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        contexto, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var ubicacion by remember { mutableStateOf<GeoPoint?>(null) }

    val lanzadorPermisos = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado -> tienePermiso = resultado.values.any { it } }

    LaunchedEffect(Unit) {
        if (!tienePermiso) {
            lanzadorPermisos.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Ubicación del teléfono (servicio del dispositivo, sin API key)
    LaunchedEffect(tienePermiso) {
        if (tienePermiso) {
            val cliente = LocationServices.getFusedLocationProviderClient(contexto)
            cliente.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { loc ->
                if (loc != null) {
                    ubicacion = GeoPoint(loc.latitude, loc.longitude)
                } else {
                    cliente.lastLocation.addOnSuccessListener { ultima ->
                        if (ultima != null) {
                            ubicacion = GeoPoint(ultima.latitude, ultima.longitude)
                        }
                    }
                }
            }
        }
    }

    // Lugares ordenados por distancia al teléfono (los 8 más cercanos)
    val cercanos: List<Pair<Lugar, Float>> = remember(ubicacion) {
        val u = ubicacion
        if (u == null) emptyList()
        else LUGARES
            .map { it to distanciaMetros(u, it) }
            .sortedBy { it.second }
            .take(8)
    }

    val mapView = remember {
        Configuration.getInstance().userAgentValue = contexto.packageName
        MapView(contexto).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(13.0)
            controller.setCenter(GeoPoint(-33.45, -70.66)) // Santiago por defecto
        }
    }

    // Punto azul con MI ubicación en vivo (sin API)
    val miUbicacion = remember {
        MyLocationNewOverlay(GpsMyLocationProvider(contexto), mapView)
    }

    // Activar/desactivar el seguimiento cuando hay o no permiso
    LaunchedEffect(tienePermiso) {
        if (tienePermiso) miUbicacion.enableMyLocation()
        else miUbicacion.disableMyLocation()
    }

    // Ciclo de vida del mapa
    DisposableEffect(contexto) {
        val dueno = contexto as? LifecycleOwner
        val observador = LifecycleEventObserver { _, evento ->
            when (evento) {
                Lifecycle.Event.ON_RESUME -> {
                    mapView.onResume()
                    if (tienePermiso) miUbicacion.enableMyLocation()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    miUbicacion.disableMyLocation()
                    mapView.onPause()
                }
                else -> {}
            }
        }
        dueno?.lifecycle?.addObserver(observador)
        onDispose {
            dueno?.lifecycle?.removeObserver(observador)
            mapView.onDetach()
        }
    }

    // Centrar el mapa en el usuario cuando se obtiene la ubicación
    LaunchedEffect(ubicacion) {
        ubicacion?.let {
            mapView.controller.setZoom(14.0)
            mapView.controller.animateTo(it)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f)
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = alVolver) { Text("Volver") }
            Text("Cerca de mí", style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = {
                val punto = miUbicacion.myLocation ?: ubicacion
                if (punto != null) {
                    mapView.controller.setZoom(16.0)
                    mapView.controller.animateTo(punto)
                }
            }) { Text("Mi ubicación") }
        }

        AndroidView(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clipToBounds(),
            factory = { mapView },
            update = { mv ->
                mv.overlays.clear()

                // Marcadores de bibliotecas y fiscalías cercanas
                cercanos.forEach { (lugar, _) ->
                    mv.overlays.add(Marker(mv).apply {
                        position = GeoPoint(lugar.latitud, lugar.longitud)
                        title = "${lugar.tipo.etiqueta}: ${lugar.nombre}"
                        snippet = lugar.direccion
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    })
                }
                // Mi ubicación va al final para quedar ENCIMA de todo
                mv.overlays.add(miUbicacion)
                mv.invalidate()
            }
        )

        // Panel blanco con los mensajes y la lista de lugares
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .navigationBarsPadding()
        ) {
            when {
                !tienePermiso -> Text(
                    "Activa el permiso de ubicación para ver lugares cercanos.",
                    modifier = Modifier.padding(16.dp)
                )
                ubicacion == null -> Text(
                    "Obteniendo tu ubicación…",
                    modifier = Modifier.padding(16.dp)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                items(cercanos) { (lugar, distancia) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                mapView.controller.setZoom(17.0)
                                mapView.controller.animateTo(
                                    GeoPoint(lugar.latitud, lugar.longitud)
                                )
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(lugar.nombre, style = MaterialTheme.typography.titleSmall, color = Color.Black)
                            Text(
                                "${lugar.tipo.etiqueta} · ${formatearDistancia(distancia)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.DarkGray
                            )
                            Text(
                                lugar.direccion,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.DarkGray
                            )
                        }
                        TextButton(onClick = {
                            // Abre la app de mapas del teléfono (sin API)
                            val uri = Uri.parse(
                                "geo:${lugar.latitud},${lugar.longitud}" +
                                        "?q=${lugar.latitud},${lugar.longitud}(${Uri.encode(lugar.nombre)})"
                            )
                            contexto.startActivity(Intent(Intent.ACTION_VIEW, uri))
                        }) { Text("Cómo llegar") }
                    }
                    HorizontalDivider()
                }
            }
        } // fin del panel blanco
    } // fin de la columna principal
}

private fun distanciaMetros(origen: GeoPoint, lugar: Lugar): Float {
    val r = FloatArray(1)
    Location.distanceBetween(
        origen.latitude, origen.longitude, lugar.latitud, lugar.longitud, r
    )
    return r[0]
}

private fun formatearDistancia(metros: Float): String =
    if (metros < 1000) "${metros.toInt()} m"
    else String.format("%.1f km", metros / 1000f)