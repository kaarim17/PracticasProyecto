package com.example.controldealmacen.ui.inventory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.controldealmacen.data.local.AppDatabase
import com.example.controldealmacen.data.local.entities.HistorialDetallado
import com.example.controldealmacen.data.repository.HistorialRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class InformeMensualViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HistorialRepository

    private val _movimientos = MutableLiveData<List<HistorialDetallado>>()
    val movimientos: LiveData<List<HistorialDetallado>> get() = _movimientos

    private val _totalEntradas = MutableLiveData<Int>()
    val totalEntradas: LiveData<Int> get() = _totalEntradas

    private val _totalSalidas = MutableLiveData<Int>()
    val totalSalidas: LiveData<Int> get() = _totalSalidas

    init {
        val dao = AppDatabase.getDatabase(application).historialDao()
        repository = HistorialRepository(dao)
        cargarInformeDelMes()
    }

    private fun cargarInformeDelMes() {
        viewModelScope.launch(Dispatchers.IO) {
            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, 1)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            val inicioMes = cal.timeInMillis
            val lista = repository.getHistorialMensual(inicioMes)
            var entradas = 0
            var salidas = 0

            lista.forEach { item ->
                if (item.historial.tipoAccion == "ENTRADA" || item.historial.tipoAccion == "ALTA") {
                    entradas += item.historial.cantidad
                } else if (item.historial.tipoAccion == "SALIDA" || item.historial.tipoAccion == "BAJA") {
                    salidas += item.historial.cantidad
                }
            }
            _totalEntradas.postValue(entradas)
            _totalSalidas.postValue(salidas)
            _movimientos.postValue(lista)
        }
    }
}