package com.example.controldealmacen.ui.proveedores

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.controldealmacen.data.local.AppDatabase
import com.example.controldealmacen.data.local.entities.ProveedorEntity
import com.example.controldealmacen.data.repository.ProveedorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProveedorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProveedorRepository

    private val _proveedores = MutableLiveData<List<ProveedorEntity>>()
    val proveedores: LiveData<List<ProveedorEntity>> get() = _proveedores

    init {
        val proveedorDao = AppDatabase.getDatabase(application).proveedorDao()
        repository = ProveedorRepository(proveedorDao)
        cargarProveedores()
    }

    fun cargarProveedores() {
        viewModelScope.launch(Dispatchers.IO) {
            val lista = repository.getProveedoresActivos()
            _proveedores.postValue(lista)
        }
    }

    fun insertProveedor(proveedor: ProveedorEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertProveedor(proveedor)
            cargarProveedores()
        }
    }
}