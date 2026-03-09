package com.example.controldealmacen.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase // Importante para el Callback
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
import kotlinx.coroutines.CoroutineScope // Importante para insertar sin bloquear pantalla
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                    "almacen_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Corrutinas para no bloquear el hilo principal de la app
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = database.perfilDao()

                        dao.insert(
                            PerfilEntity(nombre = "Admin Jefe", foto = "", rol = "ADMINISTRADOR", contrasena = "1234", correo = "admin@empresa.com", habilitado = true)
                        )
                        dao.insert(
                            PerfilEntity(nombre = "Ana Gómez", foto = "", rol = "USUARIO", contrasena = null, correo = null, habilitado = true)
                        )
                        // Este insert lo pongo en inactivo para comprobar que no sale en pantalla
                        dao.insert(
                            PerfilEntity(nombre = "Luis Temporal", foto = "", rol = "USUARIO", contrasena = null, correo = null, habilitado = false)
                        )
                    }
                }
            }
        }
    }
}