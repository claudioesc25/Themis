package com.Duoc.Themis.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Duoc.Themis.model.NotaRequest
import com.Duoc.Themis.model.nota
import com.Duoc.Themis.repository.ThemisRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotasViewModel : ViewModel() {
    private val repository = ThemisRepository()

    private val _notas = MutableStateFlow<List<nota>>(emptyList())
    val notas: StateFlow<List<nota>> = _notas

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje

    fun cargarNotas() {
        viewModelScope.launch {
            try {
                _notas.value = repository.obtenerNotas()
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar: ${e.message}"
            }
        }
    }

    fun crearNota(titulo: String, contenido: String) {
        viewModelScope.launch {
            try {
                val r = repository.crearNota(NotaRequest(titulo.trim(), contenido.trim()))
                if (r.isSuccessful) cargarNotas()
                else _mensaje.value = "Error al crear: ${r.errorBody()?.string()}"
            } catch (e: Exception) {
                _mensaje.value = "Error de conexión: ${e.message}"
            }
        }
    }

    fun actualizarNota(id: Int, titulo: String, contenido: String) {
        viewModelScope.launch {
            try {
                val r = repository.actualizarNota(id, NotaRequest(titulo.trim(), contenido.trim()))
                if (r.isSuccessful) cargarNotas()
                else _mensaje.value = "Error al actualizar: ${r.errorBody()?.string()}"
            } catch (e: Exception) {
                _mensaje.value = "Error de conexión: ${e.message}"
            }
        }
    }

    fun eliminarNota(id: Int) {
        viewModelScope.launch {
            try {
                val r = repository.eliminarNota(id)
                if (r.isSuccessful) cargarNotas()
                else _mensaje.value = "Error al eliminar: ${r.errorBody()?.string()}"
            } catch (e: Exception) {
                _mensaje.value = "Error de conexión: ${e.message}"
            }
        }
    }
}