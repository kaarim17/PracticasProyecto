package com.example.controldealmacen.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "perfiles")
data class PerfilEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val foto: String,
    val rol: String, //"USUARIO" o "ADMINISTRADOR"
    val contrasena: String?, // Opcional (solo para admins)
    val correo: String?,     // Opcional (solo para admins)
    val habilitado: Boolean = true
)