package com.example.controldealmacen.ui.auth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.entities.PerfilEntity
import com.google.android.material.switchmaterial.SwitchMaterial

class GestionPerfilesAdapter(
    private val onPerfilUpdated: (PerfilEntity) -> Unit,
    private val onPerfilDeleted: (PerfilEntity) -> Unit
) : RecyclerView.Adapter<GestionPerfilesAdapter.PerfilViewHolder>() {

    private var perfiles = listOf<PerfilEntity>()

    fun setPerfiles(nuevaLista: List<PerfilEntity>) {
        perfiles = nuevaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerfilViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gestion_perfil, parent, false)
        return PerfilViewHolder(view)
    }

    override fun onBindViewHolder(holder: PerfilViewHolder, position: Int) {
        val perfil = perfiles[position]
        holder.bind(perfil)
    }

    override fun getItemCount(): Int = perfiles.size

    inner class PerfilViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tv_gestion_nombre)
        private val tvRol: TextView = itemView.findViewById(R.id.tv_gestion_rol)
        private val swHabilitado: SwitchMaterial = itemView.findViewById(R.id.sw_habilitado)
        private val btnBorrar: ImageButton = itemView.findViewById(R.id.btn_borrar_perfil)

        fun bind(perfil: PerfilEntity) {
            tvNombre.text = perfil.nombre
            tvRol.text = "Rol: ${perfil.rol}"

            // Quitamos el listener temporalmente para que no salte al recargar la vista
            swHabilitado.setOnCheckedChangeListener(null)
            swHabilitado.isChecked = perfil.habilitado
            swHabilitado.text = if (perfil.habilitado) "Activo" else "Deshabilitado"

            swHabilitado.setOnCheckedChangeListener { _, isChecked ->
                val perfilActualizado = perfil.copy(habilitado = isChecked)
                onPerfilUpdated(perfilActualizado)
            }

            btnBorrar.setOnClickListener {
                onPerfilDeleted(perfil)
            }
        }
    }
}