package com.example.controldealmacen.ui.login

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
import com.example.controldealmacen.ui.inventory.ProductosActivity

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var adapter: PerfilAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Preparamos el RecyclerView
        val rvEmpleados = findViewById<RecyclerView>(R.id.rv_empleados)
        // Ponemos 4 columnas para la cuadrícula
        rvEmpleados.layoutManager = GridLayoutManager(this, 4)

        // 2. Inicializamos el Adapter con la lógica de clics
        adapter = PerfilAdapter(emptyList()) { perfilSeleccionado ->
            if (perfilSeleccionado.rol == "ADMINISTRADOR") {
                // Si es admin, le pedimos la clave
                mostrarDialogoContrasena(perfilSeleccionado)
            } else {
                // Si es usuario normal, entra directo
                iniciarSesion(perfilSeleccionado)
            }
        }
        rvEmpleados.adapter = adapter

        // 3. Preparamos la Base de Datos y el Repositorio
        val database = AppDatabase.getDatabase(this)
        val repository = PerfilRepository(database.perfilDao())

        // 4. Instanciamos el ViewModel usando su Factory
        val factory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        // 5. Observamos los datos en tiempo real de la base de datos
        viewModel.perfiles.observe(this) { listaPerfiles ->
            adapter.actualizarDatos(listaPerfiles)
        }
    }
    private fun mostrarDialogoContrasena(perfil: PerfilEntity) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Acceso Administrador")
        builder.setMessage("Introduce la contraseña para ${perfil.nombre}:")

        // Creamos el campo de texto para la contraseña
        val input = EditText(this)
        // Configuramos para que salgan asteriscos (***)
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        builder.setView(input)

        // Botón de Entrar
        builder.setPositiveButton("Entrar") { dialog, _ ->
            val passwordIntroducida = input.text.toString()

            // Comprobamos si la contraseña es correcta
            if (passwordIntroducida == perfil.contrasena) {
                iniciarSesion(perfil)
            } else {
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón de Cancelar
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun iniciarSesion(perfil: PerfilEntity) {
        Toast.makeText(this, "¡Bienvenido, ${perfil.nombre}!", Toast.LENGTH_SHORT).show()

        // 1. Preparamos el viaje a la pantalla de tu compañero
        val intent = Intent(this, ProductosActivity::class.java)

        // 2. Le pasamos por el Intent los datos que tu compañero necesita para buscar en el Historial
        intent.putExtra("ID_USUARIO", perfil.id)
        intent.putExtra("ROL_USUARIO", perfil.rol)
        intent.putExtra("NOMBRE_USUARIO", perfil.nombre)

        // 3. Arrancamos la pantalla de productos
        startActivity(intent)

        // 4. Cerramos esta pantalla para que el botón "Atrás" de Android no les devuelva aquí sin más
        finish()
    }
}