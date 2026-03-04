package com.example.controldealmacen.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.controldealmacen.data.database.entities.HistorialEntity

@Dao
interface HistorialDao {
    // Registra un nuevo movimiento (alta o baja de producto)
    @Insert
    suspend fun insert(interaccion: HistorialEntity)

    // La consulta para el Diario
    // Ordena por fecha descendente y coge solo 10.
    @Query("SELECT * FROM historial ORDER BY fechaHora DESC LIMIT 10")
    suspend fun obtenerUltimasInteracciones(): List<HistorialEntity>

    // Por si un usuario quiere ver solo sus propios movimientos
    @Query("SELECT * FROM historial WHERE perfilId = :idPerfil ORDER BY fechaHora DESC")
    suspend fun obtenerInteraccionesPorPerfil(idPerfil: Int): List<HistorialEntity>
}