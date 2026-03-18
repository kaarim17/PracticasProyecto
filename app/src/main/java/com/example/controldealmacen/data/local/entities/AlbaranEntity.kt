package com.example.controldealmacen.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "albaranes",
    foreignKeys = [
        ForeignKey(
            entity = ProveedorEntity::class,
            parentColumns = ["id"],
            childColumns = ["proveedorId"],
            onDelete = ForeignKey.Companion.RESTRICT
        )
    ],
    indices = [Index("proveedorId")]
)
data class AlbaranEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val proveedorId: Int,
    val foto: String,
    val importe: Double,
    val pagado: Boolean = false,
    val fecha: Long, // Fecha de creación del albarán
    val fechaPago: Long? // Fecha en la que se marcó como pagado
)
