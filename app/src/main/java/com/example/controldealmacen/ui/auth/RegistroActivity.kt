package com.example.controldealmacen.ui.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.AppDatabase
import com.example.controldealmacen.data.local.entities.PerfilEntity
import com.example.controldealmacen.ui.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistroActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val etNombre = findViewById<EditText>(R.id.et_registro_nombre)
        val etCorreo = findViewById<EditText>(R.id.et_registro_correo)
        val etPassword = findViewById<EditText>(R.id.et_registro_password)
        val rgRol = findViewById<RadioGroup>(R.id.rg_registro_rol)
        val rbAdmin = findViewById<RadioButton>(R.id.rb_rol_admin)
        val btnGuardar = findViewById<Button>(R.id.btn_guardar_registro)
        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val password = etPassword.text.toString().trim()

            val rolSeleccionado = if (rbAdmin.isChecked) "ADMINISTRADOR" else "USUARIO"

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (rolSeleccionado == "ADMINISTRADOR" && password.isEmpty()) {
                Toast.makeText(this, "Un administrador necesita contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            guardarUsuarioEnBD(nombre, correo, password, rolSeleccionado)
        }
    }

    private fun guardarUsuarioEnBD(nombre: String, correo: String, password: String, rol: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val database = AppDatabase.getDatabase(this@RegistroActivity)
            val perfilDao = database.perfilDao()

            val nuevoPerfil = PerfilEntity(
                nombre = nombre,
                foto = "", // La foto la dejaremos vacía por ahora
                rol = rol,
                contrasena = if (password.isNotEmpty()) password else null,
                correo = if (correo.isNotEmpty()) correo else null,
                habilitado = true // Por defecto entra activo
            )

            perfilDao.insert(nuevoPerfil)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@RegistroActivity, "Usuario registrado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
