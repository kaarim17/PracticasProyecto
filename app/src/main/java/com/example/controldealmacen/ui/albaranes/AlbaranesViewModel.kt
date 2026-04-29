package com.example.controldealmacen.ui.albaranes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.controldealmacen.data.local.AppDatabase
import com.example.controldealmacen.data.local.entities.AlbaranConProveedor
import com.example.controldealmacen.data.local.entities.AlbaranEntity
import com.example.controldealmacen.data.repository.AlbaranRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class AlbaranesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AlbaranRepository
    private val _albaranesConProveedor = MutableLiveData<List<AlbaranConProveedor>>()
    val albaranesConProveedor: LiveData<List<AlbaranConProveedor>> get() = _albaranesConProveedor

    init {
        val albaranDao = AppDatabase.getDatabase(application).albaranDao()
        repository = AlbaranRepository(albaranDao)
    }

    fun cargarAlbaranes(filtro: String = "TODOS", fechaInicio: Long? = null, fechaFin: Long? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            // REQUISITO SPRINT 4: Usamos la relación para ver el nombre del proveedor
            val todosConProveedor = repository.getAlbaranesConProveedor()
            
            val listaFiltrada = when (filtro) {
                "MES" -> {
                    val cal = Calendar.getInstance().apply {
                        set(Calendar.DAY_OF_MONTH, 1)
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                    }
                    todosConProveedor.filter { it.albaran.fecha >= cal.timeInMillis }
                }
                "SEMANA" -> {
                    val cal = Calendar.getInstance().apply {
                        set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                    }
                    todosConProveedor.filter { it.albaran.fecha >= cal.timeInMillis }
                }
                "RANGO" -> {
                    if (fechaInicio != null && fechaFin != null) {
                        todosConProveedor.filter { it.albaran.fecha in fechaInicio..fechaFin }
                    } else {
                        todosConProveedor
                    }
                }
                else -> todosConProveedor
            }

            _albaranesConProveedor.postValue(listaFiltrada)
        }
    }

    fun guardarAlbaran(albaran: AlbaranEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAlbaran(albaran)
            cargarAlbaranes()
        }
    }

    fun marcarComoPagado(albaran: AlbaranEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            // REQUISITO SPRINT 4: Registrar marca temporal del pago
            val albaranActualizado = albaran.copy(
                pagado = true,
                fechaPago = System.currentTimeMillis()
            )
            repository.updateAlbaran(albaranActualizado)
            cargarAlbaranes()
        }
    }
}
