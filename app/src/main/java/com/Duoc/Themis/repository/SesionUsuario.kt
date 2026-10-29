package com.Duoc.Themis.repository

import android.content.Context

class SesionUsuario(contexto: Context) {
    private val prefs = contexto.applicationContext
        .getSharedPreferences("themis_sesion", Context.MODE_PRIVATE)
    fun guardar(correo: String) {
        prefs.edit().putString(CLAVE_CORREO, correo).apply()
    }
    fun obtener(): String? = prefs.getString(CLAVE_CORREO, null)
    fun cerrar() {
        prefs.edit().remove(CLAVE_CORREO).apply()
    }
    private companion object {
        const val CLAVE_CORREO = "correo_sesion"
    }
}