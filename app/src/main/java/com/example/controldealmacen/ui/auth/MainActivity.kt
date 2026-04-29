package com.example.controldealmacen.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.AppDatabase
import com.example.controldealmacen.data.local.entities.PerfilEntity
import com.example.controldealmacen.data.repository.PerfilRepository
import com.example.controldealmacen.ui.SessionManager
import com.example.controldealmacen.ui.inventory.ProductosActivity

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var adapter: PerfilAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

<<<<<<< HEAD
=======
        // Al volver al estado de reposo, limpiamos cualquier sesión previa
        SessionManager.clearSession()

>>>>>>> 073f78b984c6dd6157c0a1fe9d34e89bbdd38e5b
        val rvEmpleados = findViewById<RecyclerView>(R.id.rv_empleados)
        rvEmpleados.layoutManager = GridLayoutManager(this, 4)

        adapter = PerfilAdapter(emptyList()) { perfilSeleccionado ->
            if (perfilSeleccionado.rol == "ADMINISTRADOR") {
                mostrarDialogoContrasena(perfilSeleccionado)
            } else {
                iniciarSesion(perfilSeleccionado)
            }
        }
        rvEmpleados.adapter = adapter

        val database = AppDatabase.getDatabase(this)
        val repository = PerfilRepository(database.perfilDao())

        val factory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        viewModel.perfiles.observe(this) { listaPerfiles ->
            adapter.actualizarDatos(listaPerfiles)
        }

<<<<<<< HEAD
        val btnNuevoEmpleado = findViewById<android.widget.Button>(R.id.btn_nuevo_empleado)
        btnNuevoEmpleado.setOnClickListener {
            val intent = android.content.Intent(this, RegistroActivity::class.java)
=======
        findViewById<android.widget.Button>(R.id.btn_nuevo_empleado).setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
>>>>>>> 073f78b984c6dd6157c0a1fe9d34e89bbdd38e5b
            startActivity(intent)
        }
    }

    private fun mostrarDialogoContrasena(perfil: PerfilEntity) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Acceso Administrador")
        builder.setMessage("Introduce la contraseña para ${perfil.nombre}:")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        builder.setView(input)

<<<<<<< HEAD
        builder.setPositiveButton("Entrar") { dialog, _ ->
            val passwordIntroducida = input.text.toString()

=======
        builder.setPositiveButton("Entrar") { _, _ ->
            val passwordIntroducida = input.text.toString()
>>>>>>> 073f78b984c6dd6157c0a1fe9d34e89bbdd38e5b
            if (passwordIntroducida == perfil.contrasena) {
                SessionManager.isAdminAuthenticated = true
                iniciarSesion(perfil)
            } else {
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun iniciarSesion(perfil: PerfilEntity) {
        SessionManager.currentUserId = perfil.id
        SessionManager.currentUserRol = perfil.rol
        SessionManager.currentUserName = perfil.nombre

        Toast.makeText(this, "¡Bienvenido, ${perfil.nombre}!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, ProductosActivity::class.java)
        intent.putExtra("ID_USUARIO", perfil.id)
<<<<<<< HEAD
        intent.putExtra("ROL_USUARIO", perfil.rol)
        intent.putExtra("NOMBRE_USUARIO", perfil.nombre)

        startActivity(intent)

=======
        startActivity(intent)
>>>>>>> 073f78b984c6dd6157c0a1fe9d34e89bbdd38e5b
        finish()
    }
}
