package com.example.controldealmacen.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
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
import kotlinx.coroutines.CoroutineScope
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
    version = 2, // Subimos la versión porque hemos cambiado la entidad AlbaranEntity
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun perfilDao(): PerfilDao
    abstract fun proveedorDao(): ProveedorDao
    abstract fun albaranDao(): AlbaranDao
    abstract fun historialDao(): HistorialDao

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
                    .fallbackToDestructiveMigration() // Esto borrará los datos viejos y creará los nuevos con los ejemplos
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        // 1. Insertar Perfiles Iniciales
                        val perfilDao = database.perfilDao()
                        perfilDao.insert(PerfilEntity(nombre = "Admin Jefe", foto = "", rol = "ADMINISTRADOR", contrasena = "1234", correo = "admin@empresa.com"))
                        perfilDao.insert(PerfilEntity(nombre = "Ana Gómez", foto = "", rol = "USUARIO", contrasena = null, correo = null))

                        // 2. Insertar Productos Iniciales
                        val productoDao = database.productoDao()
                        productoDao.insert(ProductoEntity(nombre = "Coca-Cola 33cl", foto = "", cantidad = 24, cantidadMinima = 10))
                        productoDao.insert(ProductoEntity(nombre = "Agua Mineral 50cl", foto = "", cantidad = 5, cantidadMinima = 12))

                        // 3. Insertar Proveedores de ejemplo
                        val proveedorDao = database.proveedorDao()
                        proveedorDao.insert(ProveedorEntity(id = 1, cif = "B12345678", nombre = "Bebidas del Norte S.L.", telefono = "944001122", email = "ventas@bebidasnorte.com"))
                        proveedorDao.insert(ProveedorEntity(id = 2, cif = "A87654321", nombre = "Suministros Globales", telefono = "911223344", email = "contacto@suministros.es"))

                        // 4. Insertar Albaranes de ejemplo
                        val albaranDao = database.albaranDao()
                        val ahora = System.currentTimeMillis()
                        val unaSemana = 7L * 24 * 60 * 60 * 1000

                        albaranDao.insert(AlbaranEntity(proveedorId = 1, foto = "", importe = 450.50, pagado = true, fecha = ahora, fechaPago = ahora))
                        albaranDao.insert(AlbaranEntity(proveedorId = 1, foto = "", importe = 120.00, pagado = false, fecha = ahora, fechaPago = null))
                        albaranDao.insert(AlbaranEntity(proveedorId = 2, foto = "", importe = 890.99, pagado = true, fecha = ahora - (unaSemana / 2), fechaPago = ahora - (unaSemana / 2)))
                        albaranDao.insert(AlbaranEntity(proveedorId = 2, foto = "", importe = 300.25, pagado = false, fecha = ahora - (unaSemana / 2), fechaPago = null))
                    }
                }
            }
        }
    }
}
