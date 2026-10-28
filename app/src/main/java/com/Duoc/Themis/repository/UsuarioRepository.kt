package com.Duoc.Themis.repository

import com.Duoc.Themis.model.Usuario
import com.Duoc.Themis.model.UsuarioDao

class UsuarioRepository(private val dao: UsuarioDao) {
    suspend fun registrar(usuario: Usuario) = dao.insertar(usuario)
    suspend fun buscarPorCorreo(correo: String) = dao.buscarPorCorreo(correo)
}