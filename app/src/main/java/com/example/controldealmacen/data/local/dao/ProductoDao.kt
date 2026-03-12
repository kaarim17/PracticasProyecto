package com.example.controldealmacen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.controldealmacen.data.local.entities.ProductoEntity

@Dao
interface ProductoDao {
    @Insert
    suspend fun insert(producto: ProductoEntity): Long

    @Update
    suspend fun update(producto: ProductoEntity)

    @Query("SELECT * FROM productos WHERE habilitado = 1")
    suspend fun getProductosHabilitados(): List<ProductoEntity>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun getProductoById(id: Int): ProductoEntity?

    @Query("SELECT * FROM productos WHERE id IN (:ids) AND habilitado = 1")
    suspend fun getProductosByIds(ids: List<Int>): List<ProductoEntity>

    @Query("SELECT * FROM productos WHERE nombre LIKE :query AND habilitado = 1")
    suspend fun searchProductos(query: String): List<ProductoEntity>

    @Query("SELECT * FROM productos WHERE nombre = :nombre LIMIT 1")
    suspend fun getProductoByNombre(nombre: String): ProductoEntity?
}
