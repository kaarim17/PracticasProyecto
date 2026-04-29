package com.example.controldealmacen.ui.albaranes

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.entities.AlbaranConProveedor
import com.example.controldealmacen.ui.BaseActivity
import com.example.controldealmacen.ui.SessionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar

class AlbaranesActivity : BaseActivity() {

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
            startActivity(Intent(this, AddAlbaranActivity::class.java))
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
                toggleSelectionMode() 
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

        adapter = AlbaranesAdapter(emptyList()) { itemTocado ->
            mostrarDetalleAlbaran(itemTocado)
        }
        rvAlbaranes.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.albaranesConProveedor.observe(this) { lista ->
            adapter.updateData(lista)
        }
    }

    private fun setupFiltros() {
        findViewById<Button>(R.id.btn_filtro_todos).setOnClickListener { viewModel.cargarAlbaranes("TODOS") }
        findViewById<Button>(R.id.btn_filtro_mes).setOnClickListener { viewModel.cargarAlbaranes("MES") }
        findViewById<Button>(R.id.btn_filtro_semana).setOnClickListener { viewModel.cargarAlbaranes("SEMANA") }
        
        findViewById<Button>(R.id.btn_filtro_todos).setOnLongClickListener {
            mostrarSelectorRango()
            true
        }
    }

    private fun mostrarSelectorRango() {
        val cal = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            val inicio = Calendar.getInstance().apply { set(year, month, day, 0, 0, 0) }
            DatePickerDialog(this, { _, yearF, monthF, dayF ->
                val fin = Calendar.getInstance().apply { set(yearF, monthF, dayF, 23, 59, 59) }
                viewModel.cargarAlbaranes("RANGO", inicio.timeInMillis, fin.timeInMillis)
            }, year, month, day).show()
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun mostrarDetalleAlbaran(item: AlbaranConProveedor) {
        val albaran = item.albaran
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_add_albaran, null)
        
        view.findViewById<TextView>(R.id.tv_titulo_add_albaran)?.text = "Detalle de Albarán"
        view.findViewById<ImageView>(R.id.iv_albaran_foto).setImageURI(Uri.parse(albaran.foto))
        view.findViewById<EditText>(R.id.et_albaran_importe).apply {
            setText(albaran.importe.toString())
            isEnabled = false
        }
        view.findViewById<View>(R.id.sp_proveedores).visibility = View.GONE
        view.findViewById<Button>(R.id.btn_foto_albaran).visibility = View.GONE
        
        val btnGuardar = view.findViewById<Button>(R.id.btn_guardar_albaran)
        
        if (!albaran.pagado && SessionManager.currentUserRol == "ADMINISTRADOR") {
            btnGuardar.text = "MARCAR COMO PAGADO"
            btnGuardar.visibility = View.VISIBLE
        } else {
            btnGuardar.visibility = View.GONE
        }

        val dialog = builder.setView(view).create()

        btnGuardar.setOnClickListener {
            viewModel.marcarComoPagado(albaran)
            dialog.dismiss()
            Toast.makeText(this, "Albarán pagado", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }
}
