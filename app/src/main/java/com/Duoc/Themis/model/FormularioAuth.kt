package com.Duoc.Themis.model
    data class FormularioAuth(
        val nombre: String = "",
        val correo: String = "",
        val contrasena: String = "",
        val confirmacion: String = "",
        val aceptaTerminos: Boolean = false
    )

    data class ErroresFormulario(
        val nombre: String? = null,
        val correo: String? = null,
        val contrasena: String? = null,
        val confirmacion: String? = null,
        val terminos: String? = null
    ) {
        fun sinErrores(): Boolean =
            listOf(nombre, correo, contrasena, confirmacion, terminos).all { it == null }

}