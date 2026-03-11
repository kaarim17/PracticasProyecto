package com.example.controldealmacen.ui.inventory

import androidx.lifecycle.*
import com.example.controldealmacen.data.local.entities.HistorialEntity
import com.example.controldealmacen.data.local.entities.ProductoEntity
import com.example.controldealmacen.data.repository.HistorialRepository
import com.example.controldealmacen.data.repository.ProductosRepository
import kotlinx.coroutines.launch

class ProductosViewModel(
    private val productosRepository: ProductosRepository,
    private val historialRepository: HistorialRepository
) : ViewModel() {

    private val _productos = MutableLiveData<List<ProductoEntity>>()
    val productos: LiveData<List<ProductoEntity>> get() = _productos

    private var currentQuery: String = ""
    private var idUsuarioActivo: Int = -1

    /**
     * Carga los productos. 
     * Si no hay búsqueda, muestra todos pero con los 10 más recientes del usuario al principio.
     */
    fun cargarProductos(query: String = "", idUsuario: Int = -1) {
        if (idUsuario != -1) idUsuarioActivo = idUsuario
        currentQuery = query
        
        viewModelScope.launch {
            if (currentQuery.isEmpty()) {
                // 1. Obtener todos los productos habilitados
                val todos = productosRepository.getProductosHabilitados()
                
                // 2. Obtener los IDs de las últimas interacciones de ESTE usuario
                val recientes = historialRepository.getLastInteraccionesByPerfil(idUsuarioActivo)
                val idsRecientes = recientes.map { it.productoId }.distinct().take(10)
                
                // 3. Ordenar: Recientes primero (en orden de recencia), luego por nombre
                _productos.value = todos.sortedWith(compareBy({ 
                    val index = idsRecientes.indexOf(it.id)
                    if (index != -1) index else Int.MAX_VALUE 
                }, { it.nombre }))
                
            } else {
                // Búsqueda por nombre (filtro dinámico normal)
                _productos.value = productosRepository.searchProductos(currentQuery)
            }
        }
    }

    fun modificarStock(producto: ProductoEntity, cantidad: Int, perfilId: Int) {
        viewModelScope.launch {
            val nuevaCantidad = producto.cantidad + cantidad
            if (nuevaCantidad >= 0) {
                val productoActualizado = producto.copy(cantidad = nuevaCantidad)
                productosRepository.updateProducto(productoActualizado)
                
                // Registrar en historial
                val accion = if (cantidad > 0) "ALTA" else "BAJA"
                historialRepository.registrarInteraccion(
                    HistorialEntity(
                        perfilId = perfilId,
                        productoId = producto.id,
                        tipoAccion = accion,
                        cantidad = Math.abs(cantidad),
                        fechaHora = System.currentTimeMillis()
                    )
                )
                
                // Refrescar lista manteniendo el estado actual
                cargarProductos(currentQuery)
            }
        }
    }

    fun agregarNuevoProducto(nombre: String, stock: Int, minimo: Int?) {
        viewModelScope.launch {
            val nuevo = ProductoEntity(nombre = nombre, foto = "", cantidad = stock, cantidadMinima = minimo)
            productosRepository.insertProducto(nuevo)
            cargarProductos(currentQuery)
        }
    }
}

class ProductosViewModelFactory(
    private val productosRepo: ProductosRepository,
    private val historialRepo: HistorialRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductosViewModel(productosRepo, historialRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}