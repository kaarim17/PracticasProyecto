package com.example.controldealmacen.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "historial",
    // Creamos las relaciones con las otras tablas
    foreignKeys = [
        ForeignKey(
            entity = PerfilEntity::class,
            parentColumns = ["id"],
            childColumns = ["perfilId"],
            onDelete = ForeignKey.CASCADE // Si se borra el perfil, se borra su historial
        ),
        ForeignKey(
            entity = ProductoEntity::class,
            parentColumns = ["id"],
            childColumns = ["productoId"],
            onDelete = ForeignKey.CASCADE // Si se borra el producto, se borra su historial
        )
    ],
    // Room recomienda añadir índices a las columnas que son claves foráneas para búsquedas más rápidas
    indices = [Index("perfilId"), Index("productoId")]
)
data class HistorialEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val perfilId: Int,        // ID del empleado que tocó la pantalla
    val productoId: Int,      // ID del producto que se modificó

    val tipoAccion: String,   // Guardaremos textos como "ALTA" o "BAJA"
    val cantidad: Int,        // Cuántas unidades se sumaron o restaron (ej: 2)
    val fechaHora: Long       // Marca de tiempo en milisegundos (para ordenar cuáles son los 10 más recientes)
)