package com.example.controldealmacen.data.repository

import com.example.controldealmacen.data.local.dao.*
import com.example.controldealmacen.data.local.entities.*

class AlmacenRepository(
    private val productoDao: ProductoDao,
    private val perfilDao: PerfilDao,
    private val proveedorDao: ProveedorDao,
    private val albaranDao: AlbaranDao,
    private val historialDao: HistorialDao
) {
    // Productos
    suspend fun insertProducto(producto: ProductoEntity) = productoDao.insert(producto)
    suspend fun updateProducto(producto: ProductoEntity) = productoDao.update(producto)
    suspend fun getProductosHabilitados(): List<ProductoEntity> = productoDao.getProductosHabilitados()
    suspend fun getProductoById(id: Int): ProductoEntity? = productoDao.getProductoById(id)

    // Historial
    suspend fun registrarInteraccion(interaccion: HistorialEntity) = historialDao.insert(interaccion)
    suspend fun getLastInteracciones(): List<HistorialEntity> = historialDao.getLastInteracciones()
    suspend fun getInteraccionesByPerfil(idPerfil: Int): List<HistorialEntity> = historialDao.getInteraccionesByPerfil(idPerfil)

    // Albaranes
    suspend fun insertAlbaran(albaran: AlbaranEntity) = albaranDao.insert(albaran)
    suspend fun updateAlbaran(albaran: AlbaranEntity) = albaranDao.update(albaran)
    suspend fun getAlbaranes(): List<AlbaranEntity> = albaranDao.getAlbaranes()
    suspend fun getAlbaranesPendientes(): List<AlbaranEntity> = albaranDao.getAlbaranesPendientes()
    suspend fun getAlbaranesbyProveedor(idProveedor: Int): List<AlbaranEntity> = albaranDao.getAlbaranesbyProveedor(idProveedor)

    // Perfiles
    suspend fun insertPerfil(perfil: PerfilEntity) = perfilDao.insert(perfil)
    suspend fun getPerfilesActivos(): List<PerfilEntity> = perfilDao.getPerfilesActivos()

    // Proveedores
    suspend fun insertProveedor(proveedor: ProveedorEntity) = proveedorDao.insert(proveedor)
    suspend fun updateProveedor(proveedor: ProveedorEntity) = proveedorDao.update(proveedor)
    suspend fun getProveedoresActivos(): List<ProveedorEntity> = proveedorDao.getProveedoresActivos()
    suspend fun getProveedorById(id: Int): ProveedorEntity? = proveedorDao.getProveedorById(id)
}