package com.example.controldealmacen.ui.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.entities.PerfilEntity

class PerfilAdapter(
    private var listaPerfiles: List<PerfilEntity>,
    private val onClick: (PerfilEntity) -> Unit
) : RecyclerView.Adapter<PerfilAdapter.PerfilViewHolder>() {

    class PerfilViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tv_nombre_empleado)
        val imgFoto: ImageView = view.findViewById(R.id.img_perfil_foto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerfilViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_perfil, parent, false)
        return PerfilViewHolder(view)
    }

    override fun getItemCount(): Int = listaPerfiles.size

    override fun onBindViewHolder(holder: PerfilViewHolder, position: Int) {
        val perfil = listaPerfiles[position]

        holder.tvNombre.text = perfil.nombre

        holder.imgFoto.setImageResource(R.drawable.ic_launcher_foreground)

        holder.itemView.setOnClickListener {
            onClick(perfil)
        }
    }
    fun actualizarDatos(nuevaLista: List<PerfilEntity>) {
        this.listaPerfiles = nuevaLista
        notifyDataSetChanged() // Esto avisa a la pantalla de que hay fotos nuevas
    }
}