package com.example.controldealmacen.ui.albaranes

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.AppDatabase
import com.example.controldealmacen.data.repository.AlbaranRepository
import com.example.controldealmacen.ui.BaseActivity
import kotlinx.coroutines.launch

class ResumenInformeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumen_informe)

        val selectedIds = intent.getIntegerArrayListExtra("SELECTED_IDS") ?: arrayListOf()
        
        if (selectedIds.isEmpty()) {
            finish()
            return
        }

        cargarDatosInforme(selectedIds)

        findViewById<Button>(R.id.btn_cerrar_informe).setOnClickListener {
            finish()
        }
    }

    private fun cargarDatosInforme(ids: List<Int>) {
        val albaranDao = AppDatabase.getDatabase(this).albaranDao()
        val repository = AlbaranRepository(albaranDao)

        lifecycleScope.launch {
            val listaConProveedor = repository.getAlbaranesConProveedorByIds(ids)

            var totalImporte = 0.0
            var pagados = 0
            var noPagados = 0
            val proveedoresInfo = mutableSetOf<String>()

            listaConProveedor.forEach { item ->
                totalImporte += item.albaran.importe
                if (item.albaran.pagado) pagados++ else noPagados++
                
                proveedoresInfo.add("${item.proveedor.nombre} (CIF: ${item.proveedor.cif})")
            }

            findViewById<TextView>(R.id.tv_total_importe).text = "Total Importe: %.2f €".format(totalImporte)
            findViewById<TextView>(R.id.tv_pagados).text = "Albaranes Pagados: $pagados"
            findViewById<TextView>(R.id.tv_no_pagados).text = "Albaranes sin Pagar: $noPagados"
            
            findViewById<TextView>(R.id.tv_lista_proveedores).text = proveedoresInfo.joinToString("\n")
        }
    }
}
