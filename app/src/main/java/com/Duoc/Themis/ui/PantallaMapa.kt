package com.Duoc.Themis.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.Duoc.Themis.model.LUGARES
import com.Duoc.Themis.model.Lugar
import com.google.android.gms.location.FusedLocationProviderClient
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
    val azulThemis = Color(0xFF0A1F44)
    val doradoThemis = Color(0xFFD4AF37)
    val cremaThemis = Color(0xFFFFFDF7)

    val contexto = LocalContext.current

    fun calcularDistanciaMetros(origen: GeoPoint, lugar: Lugar): Float {
        val r = FloatArray(1)
        Location.distanceBetween(
            origen.latitude, origen.longitude, lugar.latitud, lugar.longitud, r
        )
        return r[0]
    }

    fun textoDistancia(metros: Float): String =
        if (metros < 1000) "${metros.toInt()} m"
        else String.format("%.1f km", metros / 1000f)

    fun buscarUbicacion(
        cliente: FusedLocationProviderClient,
        onUbicacion: (GeoPoint?) -> Unit
    ) {
        cliente.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener { loc ->
            if (loc != null) {
                onUbicacion(GeoPoint(loc.latitude, loc.longitude))
            } else {
                cliente.lastLocation.addOnSuccessListener { ultima ->
                    onUbicacion(ultima?.let { GeoPoint(it.latitude, it.longitude) })
                }.addOnFailureListener {
                    onUbicacion(null)
                }
            }
        }.addOnFailureListener {
            cliente.lastLocation.addOnSuccessListener { ultima ->
                onUbicacion(ultima?.let { GeoPoint(it.latitude, it.longitude) })
            }.addOnFailureListener {
                onUbicacion(null)
            }
        }
    }

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
    var buscandoUbicacion by remember { mutableStateOf(false) }

    val lanzadorPermisos = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        tienePermiso = resultado.values.any { it }
    }

    val clienteUbicacion = remember {
        LocationServices.getFusedLocationProviderClient(contexto)
    }

    fun solicitarUbicacionActual() {
        if (!tienePermiso) return
        buscandoUbicacion = true
        buscarUbicacion(
            cliente = clienteUbicacion,
            onUbicacion = { punto ->
                ubicacion = punto
                buscandoUbicacion = false
            }
        )
    }

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

    LaunchedEffect(tienePermiso) {
        if (tienePermiso) {
            solicitarUbicacionActual()
        } else {
            ubicacion = null
            buscandoUbicacion = false
        }
    }

    val cercanos: List<Pair<Lugar, Float>> = remember(ubicacion) {
        val u = ubicacion
        if (u == null) emptyList()
        else LUGARES
            .map { lugar -> lugar to calcularDistanciaMetros(u, lugar) }
            .sortedBy { par -> par.second }
            .take(8)
    }

    val mapView = remember {
        Configuration.getInstance().userAgentValue = contexto.packageName
        MapView(contexto).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(13.0)
            controller.setCenter(GeoPoint(-33.4489, -70.6693))
        }
    }

    val miUbicacion = remember {
        MyLocationNewOverlay(GpsMyLocationProvider(contexto), mapView)
    }

    LaunchedEffect(tienePermiso) {
        if (tienePermiso) miUbicacion.enableMyLocation()
        else miUbicacion.disableMyLocation()
    }

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

    LaunchedEffect(ubicacion) {
        ubicacion?.let {
            mapView.controller.setZoom(14.0)
            mapView.controller.animateTo(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cremaThemis)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f),
            color = cremaThemis,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
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

                Text(
                    text = "Cerca de mí",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = azulThemis
                )

                TextButton(
                    onClick = {
                        val punto = miUbicacion.myLocation ?: ubicacion
                        if (punto != null) {
                            mapView.controller.setZoom(16.0)
                            mapView.controller.animateTo(punto)
                        } else if (tienePermiso) {
                            solicitarUbicacionActual()
                        } else {
                            lanzadorPermisos.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = doradoThemis)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Mi ubicación", fontWeight = FontWeight.SemiBold)
                }
            }
        }

        AndroidView(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clipToBounds(),
            factory = { mapView },
            update = { mv ->
                mv.overlays.clear()
                cercanos.forEach { (lugar, _) ->
                    mv.overlays.add(Marker(mv).apply {
                        position = GeoPoint(lugar.latitud, lugar.longitud)
                        title = "${lugar.tipo.etiqueta}: ${lugar.nombre}"
                        snippet = lugar.direccion
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    })
                }
                mv.overlays.add(miUbicacion)
                mv.invalidate()
            }
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            color = cremaThemis,
            border = BorderStroke(1.dp, doradoThemis.copy(alpha = 0.7f)),
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(top = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Lugares jurídicos y bibliotecas",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = azulThemis
                    )
                    if (cercanos.isNotEmpty()) {
                        Text(
                            text = "${cercanos.size} cercanos",
                            style = MaterialTheme.typography.labelMedium,
                            color = azulThemis.copy(alpha = 0.7f)
                        )
                    }
                }

                when {
                    !tienePermiso -> {
                        Text(
                            text = "Activa el permiso de ubicación para calcular distancias y ver lugares cercanos.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = azulThemis.copy(alpha = 0.8f),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                        )
                    }

                    ubicacion == null || buscandoUbicacion -> {
                        Text(
                            text = "Obteniendo tu ubicación actual…",
                            style = MaterialTheme.typography.bodyMedium,
                            color = azulThemis.copy(alpha = 0.8f),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                        )
                    }

                    cercanos.isEmpty() -> {
                        Text(
                            text = "No hay lugares registrados cerca de tu posición.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = azulThemis.copy(alpha = 0.8f),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 220.dp),
                            contentPadding = PaddingValues(bottom = 8.dp)
                        ) {
                            items(
                                items = cercanos,
                                key = { it.first.nombre + it.first.direccion }
                            ) { (lugar, distancia) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            mapView.controller.setZoom(17.0)
                                            mapView.controller.animateTo(
                                                GeoPoint(lugar.latitud, lugar.longitud)
                                            )
                                        }
                                        .padding(horizontal = 16.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Surface(
                                                shape = RoundedCornerShape(50),
                                                color = azulThemis.copy(alpha = 0.08f)
                                            ) {
                                                Text(
                                                    text = lugar.tipo.etiqueta,
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                                    color = azulThemis,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Icon(
                                                imageVector = Icons.Default.LocationOn,
                                                contentDescription = null,
                                                tint = doradoThemis,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = textoDistancia(distancia),
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                                color = azulThemis
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = lugar.nombre,
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = azulThemis
                                        )

                                        Text(
                                            text = lugar.direccion,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = azulThemis.copy(alpha = 0.75f)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = doradoThemis,
                                        modifier = Modifier.clickable {
                                            val uri = Uri.parse(
                                                "geo:${lugar.latitud},${lugar.longitud}" +
                                                        "?q=${lugar.latitud},${lugar.longitud}(${Uri.encode(lugar.nombre)})"
                                            )
                                            contexto.startActivity(Intent(Intent.ACTION_VIEW, uri))
                                        }
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Cómo llegar",
                                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                                color = azulThemis
                                            )
                                        }
                                    }
                                }
                                HorizontalDivider(color = azulThemis.copy(alpha = 0.1f))
                            }
                        }
                    }
                }
            }
        }
    }
}