package com.example.controldealmacen.ui.inventory

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.AppDatabase
import com.example.controldealmacen.data.repository.HistorialRepository
import com.example.controldealmacen.data.repository.ProductosRepository
import com.example.controldealmacen.ui.auth.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.os.Handler
import android.os.Looper
import android.widget.Toast

class ProductosActivity : AppCompatActivity() {

    private lateinit var viewModel: ProductosViewModel
    private lateinit var adapter: ProductosAdapter
    private var idUsuarioActivo: Int = -1

    // Variables del TIMEOUT
    private val handlerTimeout = Handler(Looper.getMainLooper())
    private val tiempoInactividad = 10 * 60 * 1000L // 10 minutos
    //private val tiempoInactividad = 10 * 1000L // 10 segundos

    private val runnableCerrarSesion = Runnable {
        // Este código se ejecuta si el tiempo se agota
        Toast.makeText(this, "Sesión cerrada por inactividad", Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        idUsuarioActivo = intent.getIntExtra("ID_USUARIO", -1)
        
        if (idUsuarioActivo == -1) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        setupRecyclerView()
        setupViewModel()
        setupSearch()
        setupFab()
    }

    private fun setupViewModel() {
        val database = AppDatabase.getDatabase(this)
        val productosRepo = ProductosRepository(database.productoDao())
        val historialRepo = HistorialRepository(database.historialDao())
        val factory = ProductosViewModelFactory(productosRepo, historialRepo)
        
        viewModel = ViewModelProvider(this, factory)[ProductosViewModel::class.java]
        
        viewModel.productos.observe(this) { productos ->
            adapter.updateData(productos)
        }
        
        viewModel.cargarProductos(idUsuario = idUsuarioActivo)
    }

    private fun setupRecyclerView() {
        val rvProductos = findViewById<RecyclerView>(R.id.rv_productos)
        rvProductos.layoutManager = GridLayoutManager(this, 3)

        adapter = ProductosAdapter(
            emptyList(),
            onAddClick = { producto ->
                viewModel.modificarStock(producto, 1, idUsuarioActivo)
            },
            onRemoveClick = { producto ->
                viewModel.modificarStock(producto, -1, idUsuarioActivo)
            }
        )
        rvProductos.adapter = adapter
    }
    private fun setupSearch() {
        val etBuscar = findViewById<EditText>(R.id.et_buscar)
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.cargarProductos(s.toString(), idUsuarioActivo)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun setupFab() {
        val fab = findViewById<FloatingActionButton>(R.id.fab_add_producto)
        fab.setOnClickListener {
            // Abre la nueva pantalla para añadir producto
            val intent = Intent(this, AddProductoActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onUserInteraction() {
        super.onUserInteraction()
        reiniciarTemporizador()
    }
    override fun onResume() {
        super.onResume()
        reiniciarTemporizador()
    }
    override fun onPause() {
        super.onPause()
        handlerTimeout.removeCallbacks(runnableCerrarSesion)
    }
    private fun reiniciarTemporizador() {
        handlerTimeout.removeCallbacks(runnableCerrarSesion)
        handlerTimeout.postDelayed(runnableCerrarSesion, tiempoInactividad)
    }
}