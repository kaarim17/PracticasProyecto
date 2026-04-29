package com.example.controldealmacen.ui.inventory

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R

class InformeMensualActivity : AppCompatActivity() {

    private val viewModel: InformeMensualViewModel by viewModels()
    private lateinit var adapter: HistorialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informe_mensual)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        val rvHistorial = findViewById<RecyclerView>(R.id.rv_historial_mensual)
        adapter = HistorialAdapter(emptyList())
        rvHistorial.adapter = adapter
    }

    private fun setupObservers() {
        val tvEntradas = findViewById<TextView>(R.id.tv_total_entradas)
        val tvSalidas = findViewById<TextView>(R.id.tv_total_salidas)

        // Escuchamos los totales
        viewModel.totalEntradas.observe(this) { entradas ->
            tvEntradas.text = "Entradas: $entradas"
        }

        viewModel.totalSalidas.observe(this) { salidas ->
            tvSalidas.text = "Salidas: $salidas"
        }

        // Escuchamos la lista de movimientos
        viewModel.movimientos.observe(this) { lista ->
            adapter.updateData(lista)
        }
    }
}