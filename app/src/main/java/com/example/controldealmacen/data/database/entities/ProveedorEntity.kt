package com.example.controldealmacen.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "proveedores")
data class ProveedorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Este es el ID al que apuntará el albarán

    val cif: String,
    val nombre: String,
    val telefono: String?, //Opcional
    val email: String?, //Opcional
    val habilitado: Boolean = true
)