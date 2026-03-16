package com.example.controldealmacen.ui.albaranes

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.entities.AlbaranEntity
import com.example.controldealmacen.data.local.entities.ProveedorEntity
import com.example.controldealmacen.ui.proveedores.ProveedorViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import java.io.File
import java.io.FileOutputStream

class AddAlbaranActivity : AppCompatActivity() {
    private val albaranesViewModel: AlbaranesViewModel by viewModels()
    private val proveedorViewModel: ProveedorViewModel by viewModels()

    private lateinit var spProveedores: Spinner
    private lateinit var etImporte: EditText
    private lateinit var swPagado: SwitchMaterial
    private lateinit var ivFoto: ImageView

    private var fotoUri: Uri? = null
    private var proveedoresList: List<ProveedorEntity> = emptyList()
    private var proveedorSeleccionado: ProveedorEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_albaran)

        initViews()
        setupSpinner()
    }

    private fun initViews() {
        spProveedores = findViewById(R.id.sp_proveedores)
        etImporte = findViewById(R.id.et_albaran_importe)
        swPagado = findViewById(R.id.sw_albaran_pagado)
        ivFoto = findViewById(R.id.iv_albaran_foto)

        findViewById<Button>(R.id.btn_foto_albaran).setOnClickListener {
            tomarFoto()
        }

        findViewById<Button>(R.id.btn_guardar_albaran).setOnClickListener {
            validarYGuardar()
        }
    }

    private fun setupSpinner() {
        proveedorViewModel.proveedores.observe(this) { lista ->
            proveedoresList = lista

            val nombresMostrados = lista.map { "${it.nombre} (CIF: ${it.cif})" }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nombresMostrados)
            spProveedores.adapter = adapter

            spProveedores.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    proveedorSeleccionado = proveedoresList[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    proveedorSeleccionado = null
                }
            }
        }
    }
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            ivFoto.setImageBitmap(imageBitmap)
            fotoUri = guardarImagenInterna(imageBitmap)
        }
    }

    private fun tomarFoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            takePictureLauncher.launch(takePictureIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "No se pudo abrir la cámara", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarImagenInterna(bitmap: Bitmap): Uri {
        val file = File(filesDir, "albaran_${System.currentTimeMillis()}.jpg")
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        out.flush()
        out.close()
        return Uri.fromFile(file)
    }
    private fun validarYGuardar() {
        val importeStr = etImporte.text.toString().trim()
        val pagado = swPagado.isChecked

        if (proveedorSeleccionado == null) {
            Toast.makeText(this, "Debes seleccionar un proveedor (Crea uno primero si no hay)", Toast.LENGTH_LONG).show()
            return
        }
        if (importeStr.isEmpty()) {
            Toast.makeText(this, "El importe es obligatorio", Toast.LENGTH_SHORT).show()
            return
        }
        if (fotoUri == null) {
            Toast.makeText(this, "La foto del albarán es obligatoria", Toast.LENGTH_SHORT).show()
            return
        }

        val importe = importeStr.toDoubleOrNull()
        if (importe == null) {
            Toast.makeText(this, "El importe no es válido", Toast.LENGTH_SHORT).show()
            return
        }

        val fechaDePago = if (pagado) System.currentTimeMillis() else null

        val nuevoAlbaran = AlbaranEntity(
            proveedorId = proveedorSeleccionado!!.id,
            foto = fotoUri.toString(),
            importe = importe,
            pagado = pagado,
            fechaPago = fechaDePago
        )

        albaranesViewModel.guardarAlbaran(nuevoAlbaran)

        Toast.makeText(this, "Albarán guardado con éxito", Toast.LENGTH_SHORT).show()
        finish()
    }
}