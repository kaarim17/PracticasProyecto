package com.example.controldealmacen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.controldealmacen.data.local.entities.HistorialEntity

@Dao
interface HistorialDao {
    @Insert
    suspend fun insert(interaccion: HistorialEntity)

    @Query("SELECT * FROM historial ORDER BY fechaHora DESC LIMIT 10")
    suspend fun getLastInteracciones(): List<HistorialEntity>

    @Query("SELECT * FROM historial WHERE perfilId = :perfilId ORDER BY fechaHora DESC LIMIT 10")
    suspend fun getLastInteraccionesByPerfil(perfilId: Int): List<HistorialEntity>

    @Query("SELECT * FROM historial WHERE perfilId = :idPerfil ORDER BY fechaHora DESC")
    suspend fun getInteraccionesByPerfil(idPerfil: Int): List<HistorialEntity>
}