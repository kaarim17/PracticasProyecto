package com.example.controldealmacen.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class AlbaranConProveedor(
    @Embedded val albaran: AlbaranEntity,
    @Relation(
        parentColumn = "proveedorId",
        entityColumn = "id"
    )
    val proveedor: ProveedorEntity
)