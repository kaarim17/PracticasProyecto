package com.example.controldealmacen.data.repository

import androidx.room.Dao
import com.example.controldealmacen.data.local.dao.ProveedorDao
import com.example.controldealmacen.data.local.entities.ProveedorEntity

class ProveedorRepository (private val proveedorDao: ProveedorDao) {

    suspend fun insertProveedor(proveedor: ProveedorEntity) = proveedorDao.insert(proveedor)
    suspend fun updateProveedor(proveedor: ProveedorEntity) = proveedorDao.update(proveedor)
    suspend fun getProveedoresActivos(): List<ProveedorEntity> = proveedorDao.getProveedoresActivos()
    suspend fun getProveedorById(id: Int): ProveedorEntity? = proveedorDao.getProveedorById(id)
}