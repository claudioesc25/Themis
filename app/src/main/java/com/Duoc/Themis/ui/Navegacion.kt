package com.Duoc.Themis.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
}

@Composable
fun Navegacion(authViewModel: AuthViewModel) {
    val controlador = rememberNavController()
    val nombreUsuario by authViewModel.nombreUsuario.collectAsState()

    NavHost(navController = controlador, startDestination = Rutas.CARGA) {
        composable(Rutas.CARGA) {
            PantallaCarga(alTerminar = {
                controlador.navigate(Rutas.BIENVENIDA) {
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
                nombreUsuario = nombreUsuario,
                alNavegar = { ruta -> controlador.navigate(ruta) }
            )
        }
        composable(Rutas.ARTICULOS) {
            PantallaSeccion(titulo = "Articulos", alVolver = { controlador.popBackStack() })
        }
        composable(Rutas.LEYES) {
            PantallaSeccion(titulo = "Leyes", alVolver = { controlador.popBackStack() })
        }
        composable(Rutas.CAMARA) {
            PantallaCamara(alVolver = { controlador.popBackStack() })
        }
    }
}