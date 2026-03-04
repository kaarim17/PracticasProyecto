package com.example.controldealmacen.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.controldealmacen.data.database.entities.AlbaranEntity

@Dao
interface AlbaranDao {
    @Insert
    suspend fun insert(albaran: AlbaranEntity)

    @Update
    suspend fun update(albaran: AlbaranEntity) // Ideal para cuando lo marques como pagado

    @Query("SELECT * FROM albaranes")
    suspend fun obtenerTodosLosAlbaranes(): List<AlbaranEntity>

    // Filtra para mostrar en rojo o con un aviso los que no se han pagado
    @Query("SELECT * FROM albaranes WHERE pagado = 0")
    suspend fun obtenerAlbaranesPendientes(): List<AlbaranEntity>

    // Busca todos los albaranes asociados a un proveedor específico gracias a la clave foránea
    @Query("SELECT * FROM albaranes WHERE proveedorId = :idProveedor")
    suspend fun obtenerAlbaranesPorProveedor(idProveedor: Int): List<AlbaranEntity>
}