package com.example.controldealmacen.ui.inventory

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.example.controldealmacen.ui.auth.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductosActivity : AppCompatActivity() {

    private val viewModel: ProductosViewModel by viewModels()
    private lateinit var adapter: ProductosAdapter
    private var idUsuarioActivo: Int = -1

    private val handlerTimeout = Handler(Looper.getMainLooper())
    private val tiempoInactividad = 10 * 60 * 1000L

    private val runnableCerrarSesion = Runnable {
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
        setupObservers()
        setupSearch()
        setupFab()
    }

    private fun setupObservers() {
        viewModel.productos.observe(this) { productos ->
            if (::adapter.isInitialized) {
                adapter.updateData(productos)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        reiniciarTemporizador()
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
            val intent = Intent(this, AddProductoActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuarioActivo)
            startActivity(intent)
        }

        fab.setOnLongClickListener {
            val opciones = arrayOf("Ver Inventario (Excel)", "Gestión de Proveedores", "Gestión de Albaranes")

            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Menú Avanzado")
                .setItems(opciones) { _, opcionElegida ->
                    when (opcionElegida) {
                        0 -> startActivity(Intent(this, InventarioActivity::class.java))
                        1 -> startActivity(Intent(this, com.example.controldealmacen.ui.proveedores.ProveedoresActivity::class.java))
                        2 -> startActivity(Intent(this, com.example.controldealmacen.ui.albaranes.AlbaranesActivity::class.java))
                    }
                }
                .show()
            true
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
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
