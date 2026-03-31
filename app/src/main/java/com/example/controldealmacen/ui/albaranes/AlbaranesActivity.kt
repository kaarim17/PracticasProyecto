package com.example.controldealmacen.ui.albaranes

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.entities.AlbaranEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar

class AlbaranesActivity : AppCompatActivity() {

    private val viewModel: AlbaranesViewModel by viewModels()
    private lateinit var adapter: AlbaranesAdapter
    private var isSelectionMode = false

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

        findViewById<Button>(R.id.btn_modo_informe).setOnClickListener {
            toggleSelectionMode()
        }

        findViewById<FloatingActionButton>(R.id.fab_confirmar_informe).setOnClickListener {
            val selectedIds = adapter.getSelectedIds()
            if (selectedIds.isNotEmpty()) {
                val intent = Intent(this, ResumenInformeActivity::class.java)
                intent.putIntegerArrayListExtra("SELECTED_IDS", ArrayList(selectedIds))
                startActivity(intent)
                toggleSelectionMode() // Salir del modo selección tras generar
            } else {
                Toast.makeText(this, "Selecciona al menos un albarán", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleSelectionMode() {
        isSelectionMode = !isSelectionMode
        adapter.setSelectionMode(isSelectionMode)
        
        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add_albaran)
        val fabConfirm = findViewById<FloatingActionButton>(R.id.fab_confirmar_informe)
        val btnInforme = findViewById<Button>(R.id.btn_modo_informe)

        if (isSelectionMode) {
            fabAdd.visibility = View.GONE
            fabConfirm.visibility = View.VISIBLE
            btnInforme.text = "CANCELAR SELECCIÓN"
        } else {
            fabAdd.visibility = View.VISIBLE
            fabConfirm.visibility = View.GONE
            btnInforme.text = "GENERAR INFORME"
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.cargarAlbaranes("MES")
    }

    private fun setupRecyclerView() {
        val rvAlbaranes = findViewById<RecyclerView>(R.id.rv_albaranes)
        rvAlbaranes.layoutManager = LinearLayoutManager(this)

        adapter = AlbaranesAdapter(emptyList()) { albaranTocado ->
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
        
        // Añadimos clic largo en "Todos" o un botón específico para rango
        findViewById<Button>(R.id.btn_filtro_todos).setOnLongClickListener {
            mostrarSelectorRango()
            true
        }
    }

    private fun mostrarSelectorRango() {
        val cal = Calendar.getInstance()
        
        // Seleccionar Fecha Inicio
        DatePickerDialog(this, { _, year, month, day ->
            val inicio = Calendar.getInstance()
            inicio.set(year, month, day, 0, 0, 0)
            
            // Seleccionar Fecha Fin
            DatePickerDialog(this, { _, yearF, monthF, dayF ->
                val fin = Calendar.getInstance()
                fin.set(yearF, monthF, dayF, 23, 59, 59)
                
                viewModel.cargarAlbaranes("RANGO", inicio.timeInMillis, fin.timeInMillis)
                Toast.makeText(this, "Filtrando rango seleccionado", Toast.LENGTH_SHORT).show()
                
            }, year, month, day).apply {
                setTitle("Fecha Fin")
                show()
            }
            
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).apply {
            setTitle("Fecha Inicio")
            show()
        }
    }

    private fun mostrarFotoEnGrande(albaran: AlbaranEntity) {
        val imageView = ImageView(this)
        try {
            imageView.setImageURI(Uri.parse(albaran.foto))
            imageView.setPadding(16, 16, 16, 16)

            AlertDialog.Builder(this)
                .setTitle("Albarán de ${albaran.importe}€")
                .setView(imageView)
                .setPositiveButton("Cerrar", null)
                .show()
        } catch (e: Exception) {
            Toast.makeText(this, "No se puede cargar la imagen", Toast.LENGTH_SHORT).show()
        }
    }
}
