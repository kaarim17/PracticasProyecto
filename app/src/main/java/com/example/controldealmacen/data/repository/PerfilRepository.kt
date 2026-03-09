package com.example.controldealmacen.data.repository

import com.example.controldealmacen.data.local.dao.PerfilDao
import com.example.controldealmacen.data.local.entities.PerfilEntity

class PerfilRepository(private val perfilDao: PerfilDao) {

    suspend fun insertPerfil(perfil: PerfilEntity) = perfilDao.insert(perfil)

    suspend fun getPerfilesActivos(): List<PerfilEntity> = perfilDao.getPerfilesActivos()
}