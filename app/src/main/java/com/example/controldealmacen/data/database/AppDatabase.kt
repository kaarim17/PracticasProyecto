package com.example.controldealmacen.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.controldealmacen.data.database.dao.*
import com.example.controldealmacen.data.database.entities.*

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