package com.example.controldealmacen.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class HistorialDetallado(
    @Embedded val historial: HistorialEntity,

    @Relation(
        parentColumn = "productoId",
        entityColumn = "id"
    )
    val producto: ProductoEntity,

    @Relation(
        parentColumn = "perfilId",
        entityColumn = "id"
    )
    val perfil: PerfilEntity
)