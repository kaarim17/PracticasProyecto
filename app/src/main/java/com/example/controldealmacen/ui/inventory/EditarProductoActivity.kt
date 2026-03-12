package com.example.controldealmacen.ui.inventory

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.AppDatabase
import com.example.controldealmacen.data.local.entities.ProductoEntity
import com.example.controldealmacen.data.repository.HistorialRepository
import com.example.controldealmacen.data.repository.ProductosRepository
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditarProductoActivity : AppCompatActivity() {

    private var productoId: Int = -1
    private lateinit var productoActual: ProductoEntity
    private lateinit var viewModel: ProductosViewModel // <-- Añadimos el ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_producto)

        productoId = intent.getIntExtra("ID_PRODUCTO", -1)

        if (productoId == -1) {
            Toast.makeText(this, "Error al cargar el producto", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupViewModel() // <-- Instanciamos el ViewModel igual que en las otras pantallas
        cargarDatosProducto()

        findViewById<Button>(R.id.btn_guardar_edicion).setOnClickListener {
            guardarCambios()
        }
        findViewById<Button>(R.id.btn_borrar_producto).setOnClickListener {
            mostrarDialogoBorrar()
        }
    }

    private fun setupViewModel() {
        val database = AppDatabase.getDatabase(this)
        val productosRepo = ProductosRepository(database.productoDao())
        val historialRepo = HistorialRepository(database.historialDao())
        val factory = ProductosViewModelFactory(productosRepo, historialRepo)
        viewModel = ViewModelProvider(this, factory)[ProductosViewModel::class.java]
    }

    private fun cargarDatosProducto() {
        // Ahora usamos el lifecycleScope solo para esperar al ViewModel
        lifecycleScope.launch {
            val producto = viewModel.getProductoById(productoId)

            if (producto != null) {
                productoActual = producto
                findViewById<EditText>(R.id.et_editar_nombre).setText(producto.nombre)
                findViewById<EditText>(R.id.et_editar_cantidad).setText(producto.cantidad.toString())

                if (producto.cantidadMinima != null) {
                    findViewById<EditText>(R.id.et_editar_minimo).setText(producto.cantidadMinima.toString())
                }
                findViewById<SwitchMaterial>(R.id.sw_editar_habilitado).isChecked = producto.habilitado
            } else {
                Toast.makeText(this@EditarProductoActivity, "Producto no encontrado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun guardarCambios() {
        val nuevoNombre = findViewById<EditText>(R.id.et_editar_nombre).text.toString().trim()
        val nuevaCantidadStr = findViewById<EditText>(R.id.et_editar_cantidad).text.toString()
        val nuevoMinimoStr = findViewById<EditText>(R.id.et_editar_minimo).text.toString()
        val estaHabilitado = findViewById<SwitchMaterial>(R.id.sw_editar_habilitado).isChecked

        if (nuevoNombre.isEmpty() || nuevaCantidadStr.isEmpty()) {
            Toast.makeText(this, "Nombre y cantidad son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevaCantidad = nuevaCantidadStr.toIntOrNull() ?: 0
        val nuevoMinimo = nuevoMinimoStr.toIntOrNull()

        // Creamos la copia
        val productoActualizado = productoActual.copy(
            nombre = nuevoNombre,
            cantidad = nuevaCantidad,
            cantidadMinima = nuevoMinimo,
            habilitado = estaHabilitado
        )

        // Se lo pasamos al ViewModel para que él se encargue de la base de datos
        viewModel.guardarEdicionProducto(productoActualizado)

        Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show()
        finish() // Salimos de la pantalla
    }

    private fun mostrarDialogoBorrar() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Borrar Producto")
        builder.setMessage("¿Estás seguro de que deseas eliminar '${productoActual.nombre}'? Esta acción no se puede deshacer.")

        builder.setPositiveButton("Sí, borrar") { _, _ ->
            borrarProductoDeBD()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun borrarProductoDeBD() {
        // CAMBIO: Usamos productoActual en lugar de productoActualizado
        viewModel.borrarProducto(productoActual)

        Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
        finish()
    }
}