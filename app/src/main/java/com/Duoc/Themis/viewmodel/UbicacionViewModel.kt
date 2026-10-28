package com.Duoc.Themis.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Duoc.Themis.repository.UbicacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UbicacionViewModel(private val repositorio: UbicacionRepository) : ViewModel() {

    private val _ubicacion = MutableStateFlow<Location?>(null)
    val ubicacion: StateFlow<Location?> = _ubicacion

    fun buscarUbicacion() {
        viewModelScope.launch {
            _ubicacion.value = repositorio.obtenerUbicacion()
        }
    }
}