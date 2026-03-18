package com.example.controldealmacen.ui.inventory

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InventarioActivity : AppCompatActivity() {

    private lateinit var adapter: InventarioAdapter
    private lateinit var rvInventario: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventario)

        setupRecyclerView()
        setupFiltros()
        cargarDatos("TODOS")
    }

    private fun setupRecyclerView() {
        rvInventario = findViewById(R.id.rv_inventario)
        rvInventario.layoutManager = LinearLayoutManager(this)
        adapter = InventarioAdapter(emptyList())
        rvInventario.adapter = adapter
    }

    private fun setupFiltros() {
        findViewById<Button>(R.id.btn_filter_all).setOnClickListener { cargarDatos("TODOS") }
        findViewById<Button>(R.id.btn_filter_low_stock).setOnClickListener { cargarDatos("BAJO_STOCK") }
        findViewById<Button>(R.id.btn_filter_disabled).setOnClickListener { cargarDatos("DESHABILITADOS") }
    }

    private fun cargarDatos(filtro: String) {
        val productoDao = AppDatabase.getDatabase(this).productoDao()

        lifecycleScope.launch {
            val lista = withContext(Dispatchers.IO) {
                when (filtro) {
                    "BAJO_STOCK" -> productoDao.getProductosStockBajo()
                    "DESHABILITADOS" -> productoDao.getProductosDeshabilitados()
                    else -> productoDao.getAllProductos()
                }
            }
            adapter.updateData(lista)
        }
    }
}
