package com.example.controldealmacen.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.controldealmacen.data.database.entities.PerfilEntity

@Dao
interface PerfilDao {
    @Insert
    suspend fun insert(perfil: PerfilEntity)

    @Query("SELECT * FROM perfiles WHERE habilitado = 1")
    suspend fun obtenerPerfilesActivos(): List<PerfilEntity>
}