package com.example.controldealmacen.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val foto: String,
    val cantidad: Int,
    val cantidadMinima: Int?,
    val habilitado: Boolean = true
)