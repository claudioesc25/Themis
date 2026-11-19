
package com.Duoc.Themis.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Duoc.Themis.model.Documento
import com.Duoc.Themis.model.ErroresFormulario
import com.Duoc.Themis.model.FormularioAuth
import com.Duoc.Themis.model.Usuario
import com.Duoc.Themis.model.UsuarioRequest
import com.Duoc.Themis.model.AuthRequest
import com.Duoc.Themis.repository.SesionUsuario
import com.Duoc.Themis.repository.ThemisRepository
import com.Duoc.Themis.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val PATRON_CORREO =
    Regex("^[\\w.-]+@[\\w.-]+\\.\\w+$")

class AuthViewModel(
    private val repositorio: UsuarioRepository,
    private val sesion: SesionUsuario
) : ViewModel() {

    private val _documentos =
        MutableStateFlow<List<Documento>>(emptyList())

    private val _mensajeEstado =
        MutableStateFlow("")

    val mensajeEstado: StateFlow<String> = _mensajeEstado

    private val repository = ThemisRepository()

    private val _formulario =
        MutableStateFlow(FormularioAuth())

    val formulario: StateFlow<FormularioAuth> = _formulario

    private val _errores =
        MutableStateFlow(ErroresFormulario())

    val errores: StateFlow<ErroresFormulario> = _errores

    private val _aviso =
        MutableStateFlow<String?>(null)

    val aviso: StateFlow<String?> = _aviso

    private val _registroExitoso =
        MutableStateFlow(false)

    val registroExitoso: StateFlow<Boolean> = _registroExitoso

    private val _usuarioActual =
        MutableStateFlow<Usuario?>(null)

    val usuarioActual: StateFlow<Usuario?> = _usuarioActual

    init {
        // Recuperar usuario de la sesión guardada
        sesion.obtener()?.let { correo ->
            viewModelScope.launch {
                _usuarioActual.value =
                    repositorio.buscarPorCorreo(correo)
            }
        }
    }

    fun haySesion(): Boolean =
        sesion.obtener() != null

    fun cerrarSesion() {
        sesion.cerrar()
        _usuarioActual.value = null
        limpiarFormulario()
    }

    fun onNombreChange(valor: String) {
        _formulario.update {
            it.copy(nombre = valor)
        }

        _errores.update {
            it.copy(nombre = validarNombre(valor))
        }
    }

    fun onCorreoChange(valor: String) {
        _formulario.update {
            it.copy(correo = valor)
        }

        _errores.update {
            it.copy(correo = validarCorreo(valor))
        }
    }

    fun onContrasenaChange(valor: String) {
        _formulario.update {
            it.copy(contrasena = valor)
        }

        _errores.update {
            it.copy(contrasena = validarContrasena(valor))
        }
    }

    fun onConfirmacionChange(valor: String) {
        _formulario.update {
            it.copy(confirmacion = valor)
        }

        _errores.update {
            it.copy(
                confirmacion = validarConfirmacion(
                    _formulario.value.contrasena,
                    valor
                )
            )
        }
    }

    fun onTerminosChange(valor: Boolean) {
        _formulario.update {
            it.copy(aceptaTerminos = valor)
        }

        _errores.update {
            it.copy(terminos = validarTerminos(valor))
        }
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

    // INICIAR SESIÓN CON XANO
    fun iniciarSesion(alExito: () -> Unit) {
        if (!validarInicioSesion()) return

        viewModelScope.launch {
            val formulario = _formulario.value

            try {
                val request = AuthRequest(
                    correo = formulario.correo.trim(),
                    contrasena = formulario.contrasena
                )

                val response = repository.iniciarSesion(request)

                if (response.isSuccessful) {
                    val token = response.body()?.authToken

                    if (token.isNullOrBlank()) {
                        _aviso.value = "Xano no devolvió un token válido"
                        return@launch
                    }

                    sesion.guardar(formulario.correo.trim())
                    sesion.guardarToken(token)

                    // La autenticación de Xano fue exitosa.
                    alExito()

                    // Buscamos el perfil local sin bloquear el ingreso.
                    try {
                        _usuarioActual.value =
                            repositorio.buscarPorCorreo(formulario.correo.trim())
                    } catch (e: Exception) {
                        _usuarioActual.value = null
                    }

                } else {
                    _aviso.value = "Error ${response.code()}: ${response.errorBody()?.string()}"
                }

            } catch (e: Exception) {
                _aviso.value = "Error de conexión: ${e.message}"
            }
        }
    }

    // VALIDAR REGISTRO
    private fun validarRegistro(): Boolean {

        val formulario = _formulario.value

        val nuevosErrores = ErroresFormulario(
            nombre = validarNombre(formulario.nombre),
            correo = validarCorreo(formulario.correo),
            contrasena = validarContrasena(formulario.contrasena),
            confirmacion = validarConfirmacion(
                formulario.contrasena,
                formulario.confirmacion
            ),
            terminos = validarTerminos(formulario.aceptaTerminos)
        )

        _errores.value = nuevosErrores

        return nuevosErrores.sinErrores()
    }

    // VALIDAR LOGIN
    private fun validarInicioSesion(): Boolean {
        val formulario = _formulario.value

        val nuevosErrores = ErroresFormulario(
            correo = validarCorreo(formulario.correo),
            contrasena = validarContrasena(formulario.contrasena)
        )

        _errores.value = nuevosErrores

        return nuevosErrores.sinErrores()
    }

    // VALIDACIONES
    private fun validarNombre(nombre: String): String? =
        when {
            nombre.isBlank() ->
                "El nombre es obligatorio"

            nombre.trim().length < 3 ->
                "El nombre debe tener al menos 3 caracteres"

            else -> null
        }

    private fun validarCorreo(correo: String): String? =
        when {
            correo.isBlank() ->
                "El correo es obligatorio"

            !correo.trim().matches(PATRON_CORREO) ->
                "El correo no tiene un formato válido"

            else -> null
        }

    private fun validarContrasena(contrasena: String): String? =
        when {
            contrasena.isBlank() ->
                "La contraseña es obligatoria"

            contrasena.length < 6 ->
                "La contraseña debe tener al menos 6 caracteres"

            else -> null
        }

    private fun validarConfirmacion(
        contrasena: String,
        confirmacion: String
    ): String? =
        when {
            confirmacion.isBlank() ->
                "Debes confirmar la contraseña"

            confirmacion != contrasena ->
                "Las contraseñas no coinciden"

            else -> null
        }

    private fun validarTerminos(
        aceptaTerminos: Boolean
    ): String? =
        if (aceptaTerminos) null
        else "Debes aceptar los términos y condiciones"

    // REGISTRAR USUARIO EN XANO
    fun registrarUsuario(
        nombre: String,
        correo: String,
        contrasenia: String
    ) {
        if (!validarRegistro()) return
        viewModelScope.launch {
            try {

                val request = UsuarioRequest(
                    nombre = nombre.trim(),
                    correo = correo.trim(),
                    contrasena = contrasenia
                )

                val response =
                    repository.registrarUsuario(request)

                if (response.isSuccessful) {
                    val token = response.body()?.authToken

                    if (!token.isNullOrBlank()) {
                        sesion.guardar(correo.trim())
                        sesion.guardarToken(token)
                    }

                    _mensajeEstado.value =
                        "¡Usuario registrado con éxito en Xano!"

                    _registroExitoso.value = true

                } else {
                    val error = response.errorBody()?.string()

                    _mensajeEstado.value =
                        "Error al registrar: ${error ?: response.code()}"
                }

            } catch (e: Exception) {
                _mensajeEstado.value =
                    "Error de conexión: ${e.message}"
            }
        }
    }
}
