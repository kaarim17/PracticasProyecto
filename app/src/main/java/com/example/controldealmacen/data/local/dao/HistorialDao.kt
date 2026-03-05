package com.example.controldealmacen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.controldealmacen.data.local.entities.HistorialEntity

@Dao
interface HistorialDao {
    // Registra un nuevo movimiento (alta o baja de producto)
    @Insert
    suspend fun insert(interaccion: HistorialEntity)

    // La consulta para el Diario
    // Ordena por fecha descendente y los 10 ultimos
    @Query("SELECT * FROM historial ORDER BY fechaHora DESC LIMIT 10")
    suspend fun getLastInteracciones(): List<HistorialEntity>

    // Si un usuario quiere ver sus propios movimientos
    @Query("SELECT * FROM historial WHERE perfilId = :idPerfil ORDER BY fechaHora DESC")
    suspend fun getInteraccionesByPerfil(idPerfil: Int): List<HistorialEntity>
}