package com.example.controldealmacen.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.controldealmacen.data.database.entities.ProductoEntity

@Dao
interface ProductoDao {
    @Insert
    suspend fun insert(producto: ProductoEntity)

    @Update
    suspend fun update(producto: ProductoEntity)

    @Query("SELECT * FROM productos WHERE habilitado = 1")
    suspend fun obtenerProductosHabilitados(): List<ProductoEntity>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun obtenerProductoPorId(id: Int): ProductoEntity?
}