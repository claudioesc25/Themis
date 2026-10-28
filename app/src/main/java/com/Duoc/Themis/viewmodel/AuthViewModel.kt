package com.Duoc.Themis.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Duoc.Themis.model.ErroresFormulario
import com.Duoc.Themis.model.FormularioAuth
import com.Duoc.Themis.model.Usuario
import com.Duoc.Themis.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val PATRON_CORREO = Regex("^[\\w.-]+@[\\w.-]+\\.\\w+$")

class AuthViewModel(private val repositorio: UsuarioRepository) : ViewModel() {

    private val _formulario = MutableStateFlow(FormularioAuth())
    val formulario: StateFlow<FormularioAuth> = _formulario

    private val _errores = MutableStateFlow(ErroresFormulario())
    val errores: StateFlow<ErroresFormulario> = _errores

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso

    private val _registroExitoso = MutableStateFlow(false)
    val registroExitoso: StateFlow<Boolean> = _registroExitoso

    private val _nombreUsuario = MutableStateFlow("")
    val nombreUsuario: StateFlow<String> = _nombreUsuario

    fun onNombreChange(valor: String) {
        _formulario.update { it.copy(nombre = valor) }
        _errores.update { it.copy(nombre = validarNombre(valor)) }
    }

    fun onCorreoChange(valor: String) {
        _formulario.update { it.copy(correo = valor) }
        _errores.update { it.copy(correo = validarCorreo(valor)) }
    }

    fun onContrasenaChange(valor: String) {
        _formulario.update { it.copy(contrasena = valor) }
        _errores.update { it.copy(contrasena = validarContrasena(valor)) }
    }

    fun onConfirmacionChange(valor: String) {
        _formulario.update { it.copy(confirmacion = valor) }
        _errores.update { it.copy(confirmacion = validarConfirmacion(_formulario.value.contrasena, valor)) }
    }

    fun onTerminosChange(valor: Boolean) {
        _formulario.update { it.copy(aceptaTerminos = valor) }
        _errores.update { it.copy(terminos = validarTerminos(valor)) }
    }

    fun limpiarFormulario() {
        _formulario.value = FormularioAuth()
        _errores.value = ErroresFormulario()
    }

    fun cerrarAviso() {
        _aviso.value = null
    }

    fun consumirRegistroExitoso() {
        _registroExitoso.value = false
    }

    fun registrar(alExito: () -> Unit) {
        if (!validarRegistro()) return
        viewModelScope.launch {
            val formulario = _formulario.value
            val correo = formulario.correo.trim()
            if (repositorio.buscarPorCorreo(correo) != null) {
                _errores.update { it.copy(correo = "Este correo ya está registrado") }
                return@launch
            }
            repositorio.registrar(
                Usuario(
                    nombre = formulario.nombre.trim(),
                    correo = correo,
                    contrasena = formulario.contrasena
                )
            )
            _registroExitoso.value = true
            alExito()
        }
    }

    fun iniciarSesion(alExito: () -> Unit) {
        if (!validarInicioSesion()) return
        viewModelScope.launch {
            val formulario = _formulario.value
            val usuario = repositorio.buscarPorCorreo(formulario.correo.trim())
            if (usuario == null || usuario.contrasena != formulario.contrasena) {
                _aviso.value = "Correo o contraseña incorrectos"
                return@launch
            }
            _nombreUsuario.value = usuario.nombre
            alExito()
        }
    }

    private fun validarRegistro(): Boolean {
        val formulario = _formulario.value
        val nuevosErrores = ErroresFormulario(
            nombre = validarNombre(formulario.nombre),
            correo = validarCorreo(formulario.correo),
            contrasena = validarContrasena(formulario.contrasena),
            confirmacion = validarConfirmacion(formulario.contrasena, formulario.confirmacion),
            terminos = validarTerminos(formulario.aceptaTerminos)
        )
        _errores.value = nuevosErrores
        return nuevosErrores.sinErrores()
    }

    private fun validarInicioSesion(): Boolean {
        val formulario = _formulario.value
        val nuevosErrores = ErroresFormulario(
            correo = validarCorreo(formulario.correo),
            contrasena = validarContrasena(formulario.contrasena)
        )
        _errores.value = nuevosErrores
        return nuevosErrores.sinErrores()
    }

    private fun validarNombre(nombre: String): String? = when {
        nombre.isBlank() -> "El nombre es obligatorio"
        nombre.trim().length < 3 -> "El nombre debe tener al menos 3 caracteres"
        else -> null
    }

    private fun validarCorreo(correo: String): String? = when {
        correo.isBlank() -> "El correo es obligatorio"
        !correo.trim().matches(PATRON_CORREO) -> "El correo no tiene un formato válido"
        else -> null
    }

    private fun validarContrasena(contrasena: String): String? = when {
        contrasena.isBlank() -> "La contraseña es obligatoria"
        contrasena.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
        else -> null
    }

    private fun validarConfirmacion(contrasena: String, confirmacion: String): String? = when {
        confirmacion.isBlank() -> "Debes confirmar la contraseña"
        confirmacion != contrasena -> "Las contraseñas no coinciden"
        else -> null
    }

    private fun validarTerminos(aceptaTerminos: Boolean): String? =
        if (aceptaTerminos) null else "Debes aceptar los términos y condiciones"
}