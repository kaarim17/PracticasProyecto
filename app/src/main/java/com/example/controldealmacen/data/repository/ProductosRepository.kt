package com.example.controldealmacen.data.repository

import com.example.controldealmacen.data.local.dao.ProductoDao
import com.example.controldealmacen.data.local.entities.ProductoEntity

class ProductosRepository (private val productoDao: ProductoDao) {

    suspend fun insertProducto(producto: ProductoEntity) = productoDao.insert(producto)
    suspend fun updateProducto(producto: ProductoEntity) = productoDao.update(producto)
    suspend fun deleteProducto(producto: ProductoEntity) = productoDao.delete(producto)
    suspend fun getProductosHabilitados(): List<ProductoEntity> = productoDao.getProductosHabilitados()
    suspend fun getProductoById(id: Int): ProductoEntity? = productoDao.getProductoById(id)
    suspend fun getProductosByIds(ids: List<Int>): List<ProductoEntity> = productoDao.getProductosByIds(ids)
    suspend fun searchProductos(query: String): List<ProductoEntity> = productoDao.searchProductos("%$query%")
    suspend fun getProductoByNombre(nombre: String): ProductoEntity? = productoDao.getProductoByNombre(nombre)
}
