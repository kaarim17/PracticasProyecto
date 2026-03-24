package com.example.controldealmacen.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.entities.PerfilEntity

class GestionPerfilesActivity : AppCompatActivity() {

    private val viewModel: GestionPerfilesViewModel by viewModels()
    private lateinit var adapter: GestionPerfilesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_perfiles)

        val rvPerfiles = findViewById<RecyclerView>(R.id.rv_gestion_perfiles)
        val etBuscar = findViewById<EditText>(R.id.et_buscar_perfil)

        // Inicializamos el Adapter pasándole las funciones de actualizar y borrar
        adapter = GestionPerfilesAdapter(
            onPerfilUpdated = { perfilActualizado ->
                viewModel.actualizarPerfil(perfilActualizado)
            },
            onPerfilDeleted = { perfilABorrar ->
                confirmarBorrado(perfilABorrar)
            }
        )
        rvPerfiles.adapter = adapter

        // Observamos los cambios en la base de datos para actualizar la lista en tiempo real
        viewModel.perfiles.observe(this) { lista ->
            adapter.setPerfiles(lista)
        }

        // Buscador en tiempo real
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