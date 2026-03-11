package com.example.controldealmacen.data.repository

import com.example.controldealmacen.data.local.dao.HistorialDao
import com.example.controldealmacen.data.local.entities.HistorialEntity

class HistorialRepository (private val historialDao: HistorialDao) {

    suspend fun registrarInteraccion(interaccion: HistorialEntity) = historialDao.insert(interaccion)
    
    suspend fun getLastInteracciones(): List<HistorialEntity> = historialDao.getLastInteracciones()
    
    suspend fun getLastInteraccionesByPerfil(perfilId: Int): List<HistorialEntity> = 
        historialDao.getLastInteraccionesByPerfil(perfilId)

    suspend fun getInteraccionesByPerfil(idPerfil: Int): List<HistorialEntity> = 
        historialDao.getInteraccionesByPerfil(idPerfil)
}