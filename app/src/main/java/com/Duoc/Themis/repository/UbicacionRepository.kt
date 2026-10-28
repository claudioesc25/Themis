package com.Duoc.Themis.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

class UbicacionRepository(contexto: Context) {

    private val clienteUbicacion = LocationServices.getFusedLocationProviderClient(contexto)

    @SuppressLint("MissingPermission")
    suspend fun obtenerUbicacion(): Location? =
        clienteUbicacion.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
}