package com.example.controldealmacen.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.controldealmacen.data.local.AppDatabase
import com.example.controldealmacen.data.local.entities.PerfilEntity
import com.example.controldealmacen.data.repository.PerfilRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GestionPerfilesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PerfilRepository

    private val _perfiles = MutableLiveData<List<PerfilEntity>>()
    val perfiles: LiveData<List<PerfilEntity>> get() = _perfiles

    init {
        val dao = AppDatabase.getDatabase(application).perfilDao()
        repository = PerfilRepository(dao)
        cargarPerfiles()
    }

    fun cargarPerfiles() {
        viewModelScope.launch(Dispatchers.IO) {
            _perfiles.postValue(repository.getAllPerfilesOrdenados())
        }
    }

    fun buscarPerfiles(query: String) {
        if (query.isBlank()) {
            cargarPerfiles()
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                _perfiles.postValue(repository.searchPerfiles(query))
            }
        }
    }

    fun actualizarPerfil(perfil: PerfilEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePerfil(perfil)
            cargarPerfiles() // Recargamos la lista para ver el cambio
        }
    }

    fun borrarPerfil(perfil: PerfilEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePerfil(perfil)
            cargarPerfiles()
        }
    }
}