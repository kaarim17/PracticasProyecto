package com.example.controldealmacen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.controldealmacen.data.local.entities.AlbaranConProveedor
import com.example.controldealmacen.data.local.entities.AlbaranEntity

@Dao
interface AlbaranDao {
    @Insert
    suspend fun insert(albaran: AlbaranEntity)

    @Update
    suspend fun update(albaran: AlbaranEntity)

    @Query("SELECT * FROM albaranes ORDER BY fecha DESC")
    suspend fun getAlbaranes(): List<AlbaranEntity>

    @Transaction
    @Query("SELECT * FROM albaranes ORDER BY fecha DESC")
    suspend fun getAlbaranesConProveedor(): List<AlbaranConProveedor>

    @Transaction
    @Query("SELECT * FROM albaranes WHERE id IN (:ids) ORDER BY fecha DESC")
    suspend fun getAlbaranesConProveedorByIds(ids: List<Int>): List<AlbaranConProveedor>

    // Filtra para mostrar en rojo los que no se han pagado
    @Query("SELECT * FROM albaranes WHERE pagado = 0 ORDER BY fecha DESC")
    suspend fun getAlbaranesPendientes(): List<AlbaranEntity>

    // Busca todos los albaranes asociados a un proveedor usando el foreingKey
    @Query("SELECT * FROM albaranes WHERE proveedorId = :idProveedor ORDER BY fecha DESC")
    suspend fun getAlbaranesbyProveedor(idProveedor: Int): List<AlbaranEntity>
}
