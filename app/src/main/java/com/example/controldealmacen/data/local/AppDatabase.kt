package com.example.controldealmacen.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.controldealmacen.data.local.dao.AlbaranDao
import com.example.controldealmacen.data.local.dao.HistorialDao
import com.example.controldealmacen.data.local.dao.PerfilDao
import com.example.controldealmacen.data.local.dao.ProductoDao
import com.example.controldealmacen.data.local.dao.ProveedorDao
import com.example.controldealmacen.data.local.entities.AlbaranEntity
import com.example.controldealmacen.data.local.entities.HistorialEntity
import com.example.controldealmacen.data.local.entities.PerfilEntity
import com.example.controldealmacen.data.local.entities.ProductoEntity
import com.example.controldealmacen.data.local.entities.ProveedorEntity

@Database(
    entities = [
        ProductoEntity::class,
        PerfilEntity::class,
        AlbaranEntity::class,
        ProveedorEntity::class,
        HistorialEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Declaro todos los Daos
    abstract fun productoDao(): ProductoDao
    abstract fun perfilDao(): PerfilDao
    abstract fun proveedorDao(): ProveedorDao
    abstract fun albaranDao(): AlbaranDao
    abstract fun historialDao(): HistorialDao

    // SINGLETON
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "almacen_database" // El nombre del archivo interno de la base de datos
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}