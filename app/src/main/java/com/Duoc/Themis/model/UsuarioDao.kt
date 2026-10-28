package com.Duoc.Themis.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
    interface UsuarioDao {
        @Insert
        suspend fun insertar(usuario: Usuario)

        @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
        suspend fun buscarPorCorreo(correo: String): Usuario?

}