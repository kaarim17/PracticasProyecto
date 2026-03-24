package com.example.controldealmacen.data.repository

import com.example.controldealmacen.data.local.dao.PerfilDao
import com.example.controldealmacen.data.local.entities.PerfilEntity

class PerfilRepository(private val perfilDao: PerfilDao) {

    suspend fun insertPerfil(perfil: PerfilEntity) = perfilDao.insert(perfil)

    suspend fun updatePerfil(perfil: PerfilEntity) = perfilDao.update(perfil)

    suspend fun deletePerfil(perfil: PerfilEntity) = perfilDao.delete(perfil)

    suspend fun getPerfilesActivos(): List<PerfilEntity> = perfilDao.getPerfilesActivos()

    suspend fun getAllPerfilesOrdenados(): List<PerfilEntity> = perfilDao.getAllPerfilesOrdenados()

    suspend fun searchPerfiles(query: String): List<PerfilEntity> = perfilDao.searchPerfiles("%$query%")
}