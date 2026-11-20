package com.Duoc.Themis.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.Duoc.Themis.viewmodel.AuthViewModel
import com.Duoc.Themis.viewmodel.NotasViewModel

object Rutas {
    const val CARGA = "carga"
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val MENU = "menu"
    const val PERFIL = "perfil"
    const val NOTAS = "notas"
    const val DOCUMENTOS_XANO = "documentos_xano"
    const val CAMARA = "camara"
    const val MAPA = "mapa"
}

@Composable
fun Navegacion(
    authViewModel: AuthViewModel,
    notasViewModel: NotasViewModel
) {
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

        composable(Rutas.DOCUMENTOS_XANO) {
            DocumentosScreen(viewModel = authViewModel)
        }

        composable(Rutas.NOTAS) {
            PantallaNotas(
                viewModel = notasViewModel,
                alVolver = { controlador.popBackStack() }
            )
        }

        composable(Rutas.CAMARA) {
            PantallaCamara(alVolver = { controlador.popBackStack() })
        }

        composable(Rutas.MAPA) {
            PantallaMapa(alVolver = { controlador.popBackStack() })
        }
    }
}