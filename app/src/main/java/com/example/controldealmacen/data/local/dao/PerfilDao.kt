package com.example.controldealmacen.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.controldealmacen.data.local.entities.PerfilEntity

@Dao
interface PerfilDao {
    @Insert
    suspend fun insert(perfil: PerfilEntity)

    @Update
    suspend fun update(perfil: PerfilEntity)

    @Delete
    suspend fun delete(perfil: PerfilEntity)

    @Query("SELECT * FROM perfiles WHERE habilitado = 1")
    suspend fun getPerfilesActivos(): List<PerfilEntity>

    @Query("SELECT * FROM perfiles ORDER BY habilitado DESC, nombre ASC")
    suspend fun getAllPerfilesOrdenados(): List<PerfilEntity>

    @Query("SELECT * FROM perfiles WHERE nombre LIKE :query ORDER BY habilitado DESC, nombre ASC")
    suspend fun searchPerfiles(query: String): List<PerfilEntity>
}