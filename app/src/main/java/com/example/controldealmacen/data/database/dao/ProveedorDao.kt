package com.example.controldealmacen.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.controldealmacen.data.database.entities.ProveedorEntity

@Dao
interface ProveedorDao {
    @Insert
    suspend fun insert(proveedor: ProveedorEntity)

    @Update
    suspend fun update(proveedor: ProveedorEntity)

    // Obtenemos solo los proveedores con los que seguimos trabajando
    @Query("SELECT * FROM proveedores WHERE habilitado = 1")
    suspend fun obtenerProveedoresActivos(): List<ProveedorEntity>

    @Query("SELECT * FROM proveedores WHERE id = :id")
    suspend fun obtenerProveedorPorId(id: Int): ProveedorEntity?
}