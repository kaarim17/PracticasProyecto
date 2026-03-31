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
import java.util.Calendar

class AlbaranesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AlbaranRepository
    private val _albaranes = MutableLiveData<List<AlbaranEntity>>()
    val albaranes: LiveData<List<AlbaranEntity>> get() = _albaranes

    init {
        val albaranDao = AppDatabase.getDatabase(application).albaranDao()
        repository = AlbaranRepository(albaranDao)
    }

    fun cargarAlbaranes(filtro: String = "TODOS", fechaInicio: Long? = null, fechaFin: Long? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val todosLosAlbaranes = repository.getAlbaranes()
            val ahora = System.currentTimeMillis()

            val listaFiltrada = when (filtro) {
                "MES" -> {
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.DAY_OF_MONTH, 1)
                    cal.set(Calendar.HOUR_OF_DAY, 0)
                    cal.set(Calendar.MINUTE, 0)
                    cal.set(Calendar.SECOND, 0)
                    val inicioMes = cal.timeInMillis
                    todosLosAlbaranes.filter { it.fecha >= inicioMes }
                }
                "SEMANA" -> {
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
                    cal.set(Calendar.HOUR_OF_DAY, 0)
                    cal.set(Calendar.MINUTE, 0)
                    cal.set(Calendar.SECOND, 0)
                    val inicioSemana = cal.timeInMillis
                    todosLosAlbaranes.filter { it.fecha >= inicioSemana }
                }
                "RANGO" -> {
                    if (fechaInicio != null && fechaFin != null) {
                        todosLosAlbaranes.filter { it.fecha in fechaInicio..fechaFin }
                    } else {
                        todosLosAlbaranes
                    }
                }
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
