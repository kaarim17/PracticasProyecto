package com.example.controldealmacen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.controldealmacen.data.local.entities.AlbaranEntity

@Dao
interface AlbaranDao {
    @Insert
    suspend fun insert(albaran: AlbaranEntity)

    @Update
    suspend fun update(albaran: AlbaranEntity)

    @Query("SELECT * FROM albaranes")
    suspend fun getAlbaranes(): List<AlbaranEntity>

    // Filtra para mostrar en rojo los que no se han pagado
    @Query("SELECT * FROM albaranes WHERE pagado = 0")
    suspend fun getAlbaranesPendientes(): List<AlbaranEntity>

    // Busca todos los albaranes asociados a un proveedor usando el foreingKey
    @Query("SELECT * FROM albaranes WHERE proveedorId = :idProveedor")
    suspend fun getAlbaranesbyProveedor(idProveedor: Int): List<AlbaranEntity>
}