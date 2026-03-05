package com.example.controldealmacen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.controldealmacen.data.local.entities.PerfilEntity

@Dao
interface PerfilDao {
    @Insert
    suspend fun insert(perfil: PerfilEntity)

    @Query("SELECT * FROM perfiles WHERE habilitado = 1")
    suspend fun getPerfilesActivos(): List<PerfilEntity>
}