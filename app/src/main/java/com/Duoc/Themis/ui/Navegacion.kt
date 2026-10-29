package com.Duoc.Themis.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.Duoc.Themis.model.ARTICULOS_CHILE
import com.Duoc.Themis.model.LEYES_CHILE
import com.Duoc.Themis.viewmodel.AuthViewModel

object Rutas {
    const val CARGA = "carga"
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val MENU = "menu"
    const val ARTICULOS = "articulos"
    const val LEYES = "leyes"
    const val CAMARA = "camara"
    const val PERFIL = "perfil"
}

@Composable
fun Navegacion(authViewModel: AuthViewModel) {
    val controlador = rememberNavController()
    val usuarioActual by authViewModel.usuarioActual.collectAsState()

    NavHost(navController = controlador, startDestination = Rutas.CARGA) {
        composable(Rutas.CARGA) {
            PantallaCarga(alTerminar = {
                val destino = if (authViewModel.haySesion()) Rutas.MENU else Rutas.BIENVENIDA
                controlador.navigate(destino) {
                    popUpTo(Rutas.CARGA) { inclusive = true }
                }
            })
        }
        composable(Rutas.BIENVENIDA) {
            PantallaBienvenida(
                alIrALogin = { controlador.navigate(Rutas.LOGIN) },
                alIrARegistro = { controlador.navigate(Rutas.REGISTRO) }
            )
        }
        composable(Rutas.LOGIN) {
            PantallaLogin(
                viewModel = authViewModel,
                alIniciarSesion = {
                    controlador.navigate(Rutas.MENU) {
                        popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                    }
                },
                alVolver = { controlador.popBackStack() }
            )
        }
        composable(Rutas.REGISTRO) {
            PantallaRegistro(
                viewModel = authViewModel,
                alRegistrar = {
                    controlador.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.REGISTRO) { inclusive = true }
                    }
                },
                alVolver = { controlador.popBackStack() }
            )
        }
        composable(Rutas.MENU) {
            PantallaMenu(
                nombreUsuario = usuarioActual?.nombre ?: "",
                alNavegar = { ruta -> controlador.navigate(ruta) }
            )
        }
        composable(Rutas.PERFIL) {
            PantallaPerfil(
                usuario = usuarioActual,
                alCerrarSesion = {
                    authViewModel.cerrarSesion()
                    controlador.navigate(Rutas.BIENVENIDA) {
                        popUpTo(Rutas.MENU) { inclusive = true }
                    }
                },
                alVolver = { controlador.popBackStack() }
            )
        }
        composable(Rutas.ARTICULOS) {
            PantallaSeccion(
                titulo = "Artículos",
                documentos = ARTICULOS_CHILE,
                alVolver = { controlador.popBackStack() }
            )
        }
        composable(Rutas.LEYES) {
            PantallaSeccion(
                titulo = "Leyes",
                documentos = LEYES_CHILE,
                alVolver = { controlador.popBackStack() }
            )
        }
        composable(Rutas.CAMARA) {
            PantallaCamara(alVolver = { controlador.popBackStack() })
        }
    }
}