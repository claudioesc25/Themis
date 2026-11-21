package com.Duoc.Themis.viewmodel

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import com.Duoc.Themis.repository.SesionUsuario
import com.Duoc.Themis.model.ErroresFormulario
import com.Duoc.Themis.model.FormularioAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Proxy

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    private fun crearSesionFalsa(): SesionUsuario {
        val memoria = mutableMapOf<String, Any?>()
        val editorFalso = Proxy.newProxyInstance(
            SharedPreferences.Editor::class.java.classLoader,
            arrayOf(SharedPreferences.Editor::class.java)
        ) { proxy, method, args ->
            when (method.name) {
                "putString" -> { memoria[args[0] as String] = args[1]; proxy }
                "remove" -> { memoria.remove(args[0] as String); proxy }
                "apply", "commit" -> Unit
                else -> proxy
            }
        } as SharedPreferences.Editor

        val prefsFalsas = Proxy.newProxyInstance(
            SharedPreferences::class.java.classLoader,
            arrayOf(SharedPreferences::class.java)
        ) { _, method, args ->
            when (method.name) {
                "getString" -> memoria[args[0] as String] as? String ?: (args[1] as? String)
                "edit" -> editorFalso
                else -> null
            }
        } as SharedPreferences

        // ContextWrapper envuelve el contexto sin usar Proxy sobre una clase abstracta
        val contextoFalso = object : ContextWrapper(null) {
            override fun getApplicationContext(): Context = this
            override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences = prefsFalsas
        }

        return SesionUsuario(contextoFalso)
    }

    // PRUEBA 1: Validación de errores en el registro
    @Test
    fun `al ingresar datos invalidos en registro se generan mensajes de error y se bloquea el envio`() = runTest {
        // Preparación (Arrange)
        val viewModel = AuthViewModel(sesion = crearSesionFalsa())

        // Acción (Act)
        viewModel.onNombreChange("Cl")
        viewModel.onCorreoChange("correo-invalido.cl")
        viewModel.onContrasenaChange("123")
        viewModel.onConfirmacionChange("456")
        viewModel.onTerminosChange(false)
        viewModel.registrarUsuario("Cl", "correo-invalido.cl", "123")

        // Comprobación (Assert)
        val errores = viewModel.errores.value
        assertEquals("El nombre debe tener al menos 3 caracteres", errores.nombre)
        assertEquals("El correo no tiene un formato válido", errores.correo)
        assertEquals("La contraseña debe tener al menos 6 caracteres", errores.contrasena)
        assertEquals("Las contraseñas no coinciden", errores.confirmacion)
        assertEquals("Debes aceptar los términos y condiciones", errores.terminos)
        assertFalse(viewModel.registroExitoso.value)
    }

    // PRUEBA 2: Cierre de sesión y limpieza de estados
    @Test
    fun `cerrarSesion elimina los datos guardados y limpia el formulario`() = runTest {
        // Preparación (Arrange)
        val sesionFalsa = crearSesionFalsa()
        sesionFalsa.guardar("claudio@duocuc.cl")
        val viewModel = AuthViewModel(sesion = sesionFalsa)
        viewModel.onCorreoChange("claudio@duocuc.cl")

        // Acción (Act)
        viewModel.cerrarSesion()

        // Comprobación (Assert)
        assertFalse(viewModel.haySesion())
        assertNull(viewModel.usuarioActual.value)
        assertEquals("", viewModel.formulario.value.correo)
    }
}