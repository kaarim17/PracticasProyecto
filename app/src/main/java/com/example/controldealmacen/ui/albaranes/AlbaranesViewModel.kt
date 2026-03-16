package com.example.controldealmacen.ui.albaranes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.controldealmacen.data.local.AppDatabase
import com.example.controldealmacen.data.local.entities.AlbaranEntity
import com.example.controldealmacen.data.repository.AlbaranRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlbaranesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AlbaranRepository
    private val _albaranes = MutableLiveData<List<AlbaranEntity>>()
    val albaranes: LiveData<List<AlbaranEntity>> get() = _albaranes

    init {
        val albaranDao = AppDatabase.getDatabase(application).albaranDao()
        repository = AlbaranRepository(albaranDao)
    }
    fun cargarAlbaranes(filtro: String = "TODOS") {
        viewModelScope.launch(Dispatchers.IO) {
            val todosLosAlbaranes = repository.getAlbaranes()
            val ahora = System.currentTimeMillis()

            // 1 semana = 7 días * 24h * 60m * 60s * 1000ms
            val unMesEnMs = 30L * 24 * 60 * 60 * 1000
            val unaSemanaEnMs = 7L * 24 * 60 * 60 * 1000

            val listaFiltrada = when (filtro) {
                "MES" -> todosLosAlbaranes.filter { it.fechaPago != null && (ahora - it.fechaPago < unMesEnMs) }
                "SEMANA" -> todosLosAlbaranes.filter { it.fechaPago != null && (ahora - it.fechaPago < unaSemanaEnMs) }
                else -> todosLosAlbaranes // "TODOS"
            }

            _albaranes.postValue(listaFiltrada)
        }
    }

    fun guardarAlbaran(albaran: AlbaranEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAlbaran(albaran)
            cargarAlbaranes()
        }
    }

    fun actualizarAlbaran(albaran: AlbaranEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAlbaran(albaran)
            cargarAlbaranes()
        }
    }
}