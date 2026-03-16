package com.example.controldealmacen.ui.albaranes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.entities.AlbaranEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AlbaranesActivity : AppCompatActivity() {

    private val viewModel: AlbaranesViewModel by viewModels()
    private lateinit var adapter: AlbaranesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albaranes)

        setupRecyclerView()
        setupFiltros()
        setupObservers()

        findViewById<FloatingActionButton>(R.id.fab_add_albaran).setOnClickListener {
            val intent = Intent(this, AddAlbaranActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Por defecto, según el PDF, cargamos los de este mes al entrar
        viewModel.cargarAlbaranes("MES")
    }

    private fun setupRecyclerView() {
        val rvAlbaranes = findViewById<RecyclerView>(R.id.rv_albaranes)
        rvAlbaranes.layoutManager = LinearLayoutManager(this)

        adapter = AlbaranesAdapter(emptyList()) { albaranTocado ->
            // Al hacer clic, cumplimos el requisito del PDF: Mostrar foto en grande
            mostrarFotoEnGrande(albaranTocado)
        }
        rvAlbaranes.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.albaranes.observe(this) { lista ->
            adapter.updateData(lista)
        }
    }

    private fun setupFiltros() {
        findViewById<Button>(R.id.btn_filtro_todos).setOnClickListener {
            viewModel.cargarAlbaranes("TODOS")
        }
        findViewById<Button>(R.id.btn_filtro_mes).setOnClickListener {
            viewModel.cargarAlbaranes("MES")
        }
        findViewById<Button>(R.id.btn_filtro_semana).setOnClickListener {
            viewModel.cargarAlbaranes("SEMANA")
        }
    }

    private fun mostrarFotoEnGrande(albaran: AlbaranEntity) {
        val imageView = ImageView(this)
        imageView.setImageURI(Uri.parse(albaran.foto))
        imageView.setPadding(16, 16, 16, 16)

        AlertDialog.Builder(this)
            .setTitle("Albarán de ${albaran.importe}€")
            .setView(imageView)
            .setPositiveButton("Cerrar", null)
            .show()
    }
}