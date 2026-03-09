package com.example.controldealmacen.data.repository

import com.example.controldealmacen.data.local.dao.AlbaranDao
import com.example.controldealmacen.data.local.entities.AlbaranEntity

class AlbaranRepository (private val albaranDao: AlbaranDao) {

    suspend fun insertAlbaran(albaran: AlbaranEntity) = albaranDao.insert(albaran)
    suspend fun updateAlbaran(albaran: AlbaranEntity) = albaranDao.update(albaran)
    suspend fun getAlbaranes(): List<AlbaranEntity> = albaranDao.getAlbaranes()
    suspend fun getAlbaranesPendientes(): List<AlbaranEntity> = albaranDao.getAlbaranesPendientes()
    suspend fun getAlbaranesbyProveedor(idProveedor: Int): List<AlbaranEntity> = albaranDao.getAlbaranesbyProveedor(idProveedor)
}