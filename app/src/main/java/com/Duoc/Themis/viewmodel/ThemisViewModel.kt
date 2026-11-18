package com.Duoc.Themis.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Duoc.Themis.model.*
import com.Duoc.Themis.repository.ThemisRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThemisViewModel : ViewModel() {
    private val repository = ThemisRepository()

    // Estado para guardar la lista de documentos que vienen de Xano (GET)
    private val _documentos = MutableStateFlow<List<Documento>>(emptyList())
    val documentos: StateFlow<List<Documento>> = _documentos

    // Estado para mensajes de éxito o error en la UI
    private val _mensajeEstado = MutableStateFlow<String>("")
    val mensajeEstado: StateFlow<String> = _mensajeEstado

    init {
        cargarDocumentos()
    }

    // Método GET para traer los documentos desde Xano
    fun cargarDocumentos() {
        viewModelScope.launch {
            try {
                _documentos.value = repository.obtenerDocumentos()
            } catch (e: Exception) {
                _documentos.value = emptyList()
            }
        }
    }

    // Método POST para registrar un usuario en Xano
    fun registrarUsuario(nombre: String, correo: String, contrasenia: String) {
        viewModelScope.launch {
            try {
                val req = UsuarioRequest(nombre = nombre, correo = correo, contraseña = contrasenia)
                val response = repository.registrarUsuario(req)
                if (response.isSuccessful) {
                    _mensajeEstado.value = "¡Usuario registrado con éxito en Xano (POST)!"
                } else {
                    _mensajeEstado.value = "Error al registrar el usuario"
                }
            } catch (e: Exception) {
                _mensajeEstado.value = "Excepción: ${e.message}"
            }
        }
    }
}