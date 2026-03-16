package com.example.controldealmacen.ui.inventory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.controldealmacen.data.local.AppDatabase
import com.example.controldealmacen.data.local.entities.HistorialEntity
import com.example.controldealmacen.data.local.entities.ProductoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductosViewModel(application: Application) : AndroidViewModel(application) {

    private val database: AppDatabase = AppDatabase.getDatabase(application)
    private val productoDao = database.productoDao()
    private val historialDao = database.historialDao()

    private val _productos = MutableLiveData<List<ProductoEntity>>()
    val productos: LiveData<List<ProductoEntity>> get() = _productos

    fun cargarProductos(query: String = "", idUsuario: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val lista = if (query.isEmpty()) {
                productoDao.getProductosHabilitados()
            } else {

                productoDao.searchProductos("%$query%")
            }
            _productos.postValue(lista)
        }
    }
    fun modificarStock(producto: ProductoEntity, cambio: Int, idUsuario: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val nuevaCantidad = producto.cantidad + cambio

            if (nuevaCantidad >= 0) {
                val productoActualizado = producto.copy(cantidad = nuevaCantidad)
                productoDao.update(productoActualizado)

                val tipoMovimiento = if (cambio > 0) "ENTRADA" else "SALIDA"
                historialDao.insert(
                    HistorialEntity(
                        perfilId = idUsuario,
                        productoId = producto.id,
                        tipoAccion = tipoMovimiento,
                        cantidad = Math.abs(cambio),
                        fechaHora = System.currentTimeMillis()
                    )
                )
                cargarProductos("", idUsuario)
            }
        }
    }

    suspend fun getProductoById(id: Int): ProductoEntity? {
        return withContext(Dispatchers.IO) {
            productoDao.getProductoById(id)
        }
    }

    fun guardarEdicionProducto(producto: ProductoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            productoDao.update(producto)
        }
    }

    fun borrarProducto(producto: ProductoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            productoDao.delete(producto)
        }
    }
}