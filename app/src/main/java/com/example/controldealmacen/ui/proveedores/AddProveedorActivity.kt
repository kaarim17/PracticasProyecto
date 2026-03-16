package com.example.controldealmacen.ui.proveedores

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.entities.ProveedorEntity

class AddProveedorActivity : AppCompatActivity() {

    private val viewModel: ProveedorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_proveedor) // Asegúrate de tener este layout

        findViewById<Button>(R.id.btn_guardar_proveedor).setOnClickListener {
            guardarProveedor()
        }
    }

    private fun guardarProveedor() {
        val cif = findViewById<EditText>(R.id.et_proveedor_cif).text.toString().trim()
        val nombre = findViewById<EditText>(R.id.et_proveedor_nombre).text.toString().trim()
        val telefono = findViewById<EditText>(R.id.et_proveedor_telefono).text.toString().trim()
        val email = findViewById<EditText>(R.id.et_proveedor_email).text.toString().trim()

        if (cif.isEmpty() || nombre.isEmpty()) {
            Toast.makeText(this, "El CIF y el Nombre son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val telFinal = if (telefono.isNotEmpty()) telefono else null
        val emailFinal = if (email.isNotEmpty()) email else null

        val nuevoProveedor = ProveedorEntity(
            cif = cif,
            nombre = nombre,
            telefono = telFinal,
            email = emailFinal,
            habilitado = true
        )

        viewModel.insertProveedor(nuevoProveedor)

        Toast.makeText(this, "Proveedor guardado con éxito", Toast.LENGTH_SHORT).show()
        finish()
    }
}