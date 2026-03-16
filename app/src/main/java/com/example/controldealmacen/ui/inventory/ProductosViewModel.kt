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


    // --- 1. LAS DOS FUNCIONES QUE FALTABAN PARA EL ERROR ROJO ---

    // Esta función sirve tanto para cargar todos los productos como para buscarlos
    fun cargarProductos(query: String = "", idUsuario: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val lista = if (query.isEmpty()) {
                // Asumo que tu DAO tiene una función para obtener todos.
                // Si se llama distinto, cámbialo aquí.
                productoDao.getProductosHabilitados()
            } else {
                // Asumo que tienes una función para buscar por nombre
                productoDao.searchProductos("%$query%")
            }
            _productos.postValue(lista)
        }
    }

    // Esta función suma o resta stock y lo guarda en el historial
    fun modificarStock(producto: ProductoEntity, cambio: Int, idUsuario: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val nuevaCantidad = producto.cantidad + cambio

            // Solo modificamos si el stock no se va a quedar en negativo
            if (nuevaCantidad >= 0) {
                val productoActualizado = producto.copy(cantidad = nuevaCantidad)
                productoDao.update(productoActualizado)

                // Registramos el movimiento en el historial
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

                // Recargamos la lista para que la pantalla se actualice sola
                cargarProductos("", idUsuario)
            }
        }
    }


    // --- 2. FUNCIONES PARA EDITAR/BORRAR (Las que ya teníamos) ---

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