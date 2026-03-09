package com.example.controldealmacen.ui.inventory // Fíjate que ahora estamos en inventory

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.controldealmacen.R

class ProductosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos) // Este XML lo he creado pero es el quie tienes que seguir editando tu bro

        val idUsuarioActivo = intent.getIntExtra("ID_USUARIO", -1)
        val rolUsuarioActivo = intent.getStringExtra("ROL_USUARIO") ?: "USUARIO"
        val nombreUsuarioActivo = intent.getStringExtra("NOMBRE_USUARIO") ?: "Desconocido"

        Toast.makeText(this, "Sesión: $nombreUsuarioActivo ($rolUsuarioActivo) - ID: $idUsuarioActivo", Toast.LENGTH_LONG).show()

        // Usa idUsuarioActivo para buscar lo de las 10 últimas interacciones
    }
}