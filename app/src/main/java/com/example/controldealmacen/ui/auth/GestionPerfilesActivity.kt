package com.example.controldealmacen.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.entities.PerfilEntity
import com.example.controldealmacen.ui.BaseActivity

class GestionPerfilesActivity : BaseActivity() {

    private val viewModel: GestionPerfilesViewModel by viewModels()
    private lateinit var adapter: GestionPerfilesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_perfiles)

        val rvPerfiles = findViewById<RecyclerView>(R.id.rv_gestion_perfiles)
        val etBuscar = findViewById<EditText>(R.id.et_buscar_perfil)

        adapter = GestionPerfilesAdapter(
            onPerfilUpdated = { perfilActualizado ->
                viewModel.actualizarPerfil(perfilActualizado)
            },
            onPerfilDeleted = { perfilABorrar ->
                confirmarBorrado(perfilABorrar)
            }
        )
        rvPerfiles.adapter = adapter

        viewModel.perfiles.observe(this) { lista ->
            adapter.setPerfiles(lista)
        }

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.buscarPerfiles(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun confirmarBorrado(perfil: PerfilEntity) {
        AlertDialog.Builder(this)
            .setTitle("Borrar Empleado")
            .setMessage("¿Estás seguro de que deseas borrar a ${perfil.nombre} de forma permanente?")
            .setPositiveButton("Borrar") { _, _ ->
                viewModel.borrarPerfil(perfil)
                Toast.makeText(this, "Empleado borrado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
