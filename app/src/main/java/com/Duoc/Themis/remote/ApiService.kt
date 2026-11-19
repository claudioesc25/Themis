package com.Duoc.Themis.remote
import com.Duoc.Themis.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("themis_categoria")
    suspend fun obtenerCategorias(): List<Categoria>

    @GET("themis_categoria/{id}")
    suspend fun obtenerCategoriaPorId(@Path("id") id: Int): Categoria

    @POST("themis_categoria")
    suspend fun crearCategoria(@Body body: CategoriaRequest): Response<Categoria>

    @PUT("themis_categoria/{id}")
    suspend fun actualizarCategoria(@Path("id") id: Int, @Body body: CategoriaRequest): Response<Categoria>

    @DELETE("themis_categoria/{id}")
    suspend fun eliminarCategoria(@Path("id") id: Int): Response<Unit>

    @GET("themis_legal_document")
    suspend fun obtenerDocumentos(): List<Documento>

    @GET("themis_legal_document/{id}")
    suspend fun obtenerDocumentoPorId(@Path("id") id: Int): Documento

    @POST("themis_legal_document")
    suspend fun crearDocumento(@Body body: DocumentoRequest): Response<Documento>

    @PUT("themis_legal_document/{id}")
    suspend fun actualizarDocumento(@Path("id") id: Int, @Body body: DocumentoRequest): Response<Documento>

    @DELETE("themis_legal_document/{id}")
    suspend fun eliminarDocumento(@Path("id") id: Int): Response<Unit>

    @GET("themis_nota")
    suspend fun obtenerNotas(): List<nota>

    @GET("themis_nota/{id}")
    suspend fun obtenerNotaPorId(@Path("id") id: Int): nota

    @POST("themis_nota")
    suspend fun crearNota(@Body body: NotaRequest): Response<nota>

    @PUT("themis_nota/{id}")
    suspend fun actualizarNota(@Path("id") id: Int, @Body body: NotaRequest): Response<nota>

    @DELETE("themis_nota/{id}")
    suspend fun eliminarNota(@Path("id") id: Int): Response<Unit>

    @GET("themis_usuario")
    suspend fun obtenerUsuarios(): List<UsuarioThemis>

    @GET("themis_usuario/{id}")
    suspend fun obtenerUsuarioPorId(@Path("id") id: Int): UsuarioThemis


    @PUT("themis_usuario/{id}")
    suspend fun actualizarUsuario(@Path("id") id: Int, @Body body: UsuarioRequest): Response<UsuarioThemis>

    @DELETE("themis_usuario/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Int): Response<Unit>

    @POST("auth/signup")
    suspend fun registrarUsuario(@Body body: UsuarioRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun iniciarSesion(@Body body: AuthRequest): Response<AuthResponse>
}
