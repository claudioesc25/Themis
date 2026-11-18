package com.Duoc.Themis.model
// 1. Categoría
data class Categoria(
    val id: Int,
    val nombre: String,
    val descripcion: String
)

// 2. Documento Legal (con tus campos exactos)
data class Documento(
    val id: Int,
    val titulo: String,
    val tipodocumento: String,
    val contenido: String,
    val fecha_actualizacion: String,
    val fecha_publicacion: String,
    val categoria_id: Int
)

// 3. Nota Legal (con tus campos exactos y relación con documento)
data class nota(
    val id: Int,
    val titulo: String,
    val contenido: String,
    val documento_id: Int // Relación con documento legal
)

// 4. Usuario (con tus campos exactos)
data class UsuarioThemis(
    val id: Int,
    val nombre: String,
    val correo: String,
    val contraseña: String
)

// --- Modelos para peticiones POST / PUT (Request) ---

data class CategoriaRequest(
    val nombre: String,
    val descripcion: String
)

data class DocumentoRequest(
    val titulo: String,
    val tipodocumento: String,
    val contenido: String,
    val fecha_actualizacion: String,
    val fecha_publicacion: String,
    val categoria_id: Int
)

data class NotaRequest(
    val titulo: String,
    val contenido: String,
    val documento_id: Int
)

data class UsuarioRequest(
    val nombre: String,
    val correo: String,
    val contraseña: String)
