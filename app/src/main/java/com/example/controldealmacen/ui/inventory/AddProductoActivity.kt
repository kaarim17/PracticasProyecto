package com.example.controldealmacen.ui.inventory

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.AppDatabase
import com.example.controldealmacen.data.local.entities.HistorialEntity
import com.example.controldealmacen.data.local.entities.ProductoEntity
import com.example.controldealmacen.data.repository.HistorialRepository
import com.example.controldealmacen.data.repository.ProductosRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.*

class AddProductoActivity : AppCompatActivity() {

    private lateinit var viewModel: ProductosViewModel
    private var fotoUri: Uri? = null
    private var idUsuarioActivo: Int = -1

    private lateinit var ivFoto: ImageView
    private lateinit var etNombre: EditText
    private lateinit var etCantidad: EditText
    private lateinit var etCantidadMinima: EditText
    private lateinit var cbHabilitado: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_producto)

        idUsuarioActivo = intent.getIntExtra("ID_USUARIO", -1)

        setupViewModel()
        initViews()
    }

    private fun setupViewModel() {
        val database = AppDatabase.getDatabase(this)
        val productosRepo = ProductosRepository(database.productoDao())
        val historialRepo = HistorialRepository(database.historialDao())
        val factory = ProductosViewModelFactory(productosRepo, historialRepo)
        viewModel = ViewModelProvider(this, factory)[ProductosViewModel::class.java]
    }

    private fun initViews() {
        ivFoto = findViewById(R.id.iv_producto_foto)
        etNombre = findViewById(R.id.et_nombre)
        etCantidad = findViewById(R.id.et_cantidad)
        etCantidadMinima = findViewById(R.id.et_cantidad_minima)
        cbHabilitado = findViewById(R.id.cb_habilitado)

        findViewById<Button>(R.id.btn_tomar_foto).setOnClickListener {
            tomarFoto()
        }

        findViewById<Button>(R.id.btn_guardar_producto).setOnClickListener {
            validarYGuardar()
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            ivFoto.setImageBitmap(imageBitmap)
            fotoUri = guardarImagenInterna(imageBitmap)
        }
    }

    private fun tomarFoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            takePictureLauncher.launch(takePictureIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "No se pudo abrir la cámara", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarImagenInterna(bitmap: Bitmap): Uri {
        val file = File(filesDir, "producto_${System.currentTimeMillis()}.jpg")
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        out.flush()
        out.close()
        return Uri.fromFile(file)
    }

    private fun validarYGuardar() {
        val nombre = etNombre.text.toString().trim()
        val cantidadStr = etCantidad.text.toString().trim()
        val cantidadMinimaStr = etCantidadMinima.text.toString().trim()
        val habilitado = cbHabilitado.isChecked

        if (fotoUri == null) {
            Toast.makeText(this, "La foto es obligatoria", Toast.LENGTH_SHORT).show()
            return
        }
        if (nombre.isEmpty()) {
            Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
            return
        }
        if (cantidadStr.isEmpty()) {
            Toast.makeText(this, "La cantidad es obligatoria", Toast.LENGTH_SHORT).show()
            return
        }

        val cantidad = cantidadStr.toInt()
        val cantidadMinima = if (cantidadMinimaStr.isNotEmpty()) cantidadMinimaStr.toInt() else null

        lifecycleScope.launch {
            val database = AppDatabase.getDatabase(this@AddProductoActivity)
            val existingProduct = database.productoDao().getProductoByNombre(nombre)

            if (existingProduct != null) {
                mostrarAdvertenciaDuplicado(nombre, fotoUri.toString(), cantidad, cantidadMinima, habilitado)
            } else {
                guardarProducto(nombre, fotoUri.toString(), cantidad, cantidadMinima, habilitado)
            }
        }
    }

    private fun mostrarAdvertenciaDuplicado(nombre: String, foto: String, cantidad: Int, min: Int?, hab: Boolean) {
        AlertDialog.Builder(this)
            .setTitle("Producto Duplicado")
            .setMessage("Ya existe un producto con el nombre '$nombre'. ¿Deseas añadirlo de todas formas?")
            .setPositiveButton("Sí, añadir") { _, _ ->
                guardarProducto(nombre, foto, cantidad, min, hab)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun guardarProducto(nombre: String, foto: String, cantidad: Int, min: Int?, hab: Boolean) {
        lifecycleScope.launch {
            val database = AppDatabase.getDatabase(this@AddProductoActivity)
            val producto = ProductoEntity(
                nombre = nombre,
                foto = foto,
                cantidad = cantidad,
                cantidadMinima = min,
                habilitado = hab
            )
            val idNuevo = database.productoDao().insert(producto).toInt()

            // Registrar interacción inicial para que aparezca como "reciente"
            database.historialDao().insert(
                HistorialEntity(
                    perfilId = idUsuarioActivo,
                    productoId = idNuevo,
                    tipoAccion = "ALTA",
                    cantidad = cantidad,
                    fechaHora = System.currentTimeMillis()
                )
            )

            Toast.makeText(this@AddProductoActivity, "Producto guardado con éxito", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
