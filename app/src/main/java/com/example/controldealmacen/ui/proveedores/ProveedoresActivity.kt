package com.example.controldealmacen.ui.proveedores

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProveedoresActivity : AppCompatActivity() {

    private val viewModel: ProveedorViewModel by viewModels()
    private lateinit var adapter: ProveedoresAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proveedores)

        setupRecyclerView()
        setupObservers()
        setupFab()
    }

    override fun onResume() {
        super.onResume()
        // Recargamos la lista cada vez que volvemos a esta pantalla
        viewModel.cargarProveedores()
    }

    private fun setupRecyclerView() {
        val rvProveedores = findViewById<RecyclerView>(R.id.rv_proveedores)
        rvProveedores.layoutManager = LinearLayoutManager(this)

        // Inicializamos el adaptador vacío. Cuando hagan clic en un proveedor, avisamos
        adapter = ProveedoresAdapter(emptyList()) { proveedorSeleccionado ->
            Toast.makeText(this, "Has tocado: ${proveedorSeleccionado.nombre}", Toast.LENGTH_SHORT).show()
            // TODO: Más adelante, al tocar un proveedor, abriremos sus Albaranes
        }
        rvProveedores.adapter = adapter
    }

    private fun setupObservers() {
        // Observamos los cambios en la base de datos
        viewModel.proveedores.observe(this) { lista ->
            adapter.updateData(lista)
        }
    }

    private fun setupFab() {
        val fab = findViewById<FloatingActionButton>(R.id.fab_add_proveedor)
        fab.setOnClickListener {
            // Vamos a la pantalla que creamos antes para añadir un proveedor
            val intent = Intent(this, AddProveedorActivity::class.java)
            startActivity(intent)
        }
    }
}