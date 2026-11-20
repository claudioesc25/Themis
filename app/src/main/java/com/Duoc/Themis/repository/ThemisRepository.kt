
package com.Duoc.Themis.repository

import com.Duoc.Themis.model.*
import com.Duoc.Themis.remote.RetrofitInstance
import retrofit2.Response

class ThemisRepository {

    // Documentos
    suspend fun obtenerDocumentos(): List<Documento> =
        RetrofitInstance.api.obtenerDocumentos()

    suspend fun crearDocumento(
        body: DocumentoRequest
    ): Response<Documento> =
        RetrofitInstance.api.crearDocumento(body)

    // Usuarios
    suspend fun obtenerUsuarios(): List<UsuarioThemis> =
        RetrofitInstance.api.obtenerUsuarios()

    // Registro mediante Xano
    suspend fun registrarUsuario(
        body: UsuarioRequest
    ): Response<AuthResponse> =
        RetrofitInstance.api.registrarUsuario(body)

    // Inicio de sesión mediante Xano
    suspend fun iniciarSesion(
        body: AuthRequest
    ): Response<AuthResponse> =
        RetrofitInstance.api.iniciarSesion(body)

    // Categorías y notas
    suspend fun obtenerCategorias(): List<Categoria> =
        RetrofitInstance.api.obtenerCategorias()

    suspend fun obtenerNotas(): List<nota> =
        RetrofitInstance.api.obtenerNotas()
    suspend fun crearNota(body: NotaRequest): Response<nota> =
        RetrofitInstance.api.crearNota(body)

    suspend fun actualizarNota(id: Int, body: NotaRequest): Response<nota> =
        RetrofitInstance.api.actualizarNota(id, body)

    suspend fun eliminarNota(id: Int): Response<Unit> =
        RetrofitInstance.api.eliminarNota(id)

}

