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
        viewModel.cargarProveedores()
    }

    private fun setupRecyclerView() {
        val rvProveedores = findViewById<RecyclerView>(R.id.rv_proveedores)
        rvProveedores.layoutManager = LinearLayoutManager(this)

        adapter = ProveedoresAdapter(emptyList()) { proveedorSeleccionado ->
            Toast.makeText(this, "Has tocado: ${proveedorSeleccionado.nombre}", Toast.LENGTH_SHORT).show()
        }
        rvProveedores.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.proveedores.observe(this) { lista ->
            adapter.updateData(lista)
        }
    }

    private fun setupFab() {
        val fab = findViewById<FloatingActionButton>(R.id.fab_add_proveedor)
        fab.setOnClickListener {
            val intent = Intent(this, AddProveedorActivity::class.java)
            startActivity(intent)
        }
    }
}