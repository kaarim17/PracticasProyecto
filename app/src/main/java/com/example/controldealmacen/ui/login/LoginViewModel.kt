package com.example.controldealmacen.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.controldealmacen.data.local.entities.PerfilEntity
import com.example.controldealmacen.data.repository.PerfilRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: PerfilRepository) : ViewModel() {
    private val _perfiles = MutableLiveData<List<PerfilEntity>>()
    val perfiles: LiveData<List<PerfilEntity>> get() = _perfiles

    init {
        cargarPerfiles()
    }

    private fun cargarPerfiles() {
        // viewModelScope es una corrutina que se cancela sola si cerramos la pantalla
        viewModelScope.launch {
            val lista = repository.getPerfilesActivos()
            _perfiles.value = lista
        }
    }
}
class LoginViewModelFactory(private val repository: PerfilRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}