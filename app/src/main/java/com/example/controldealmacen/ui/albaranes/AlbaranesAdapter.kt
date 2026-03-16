package com.example.controldealmacen.ui.albaranes

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.entities.AlbaranEntity

class AlbaranesAdapter(
    private var albaranes: List<AlbaranEntity>,
    private val onAlbaranClick: (AlbaranEntity) -> Unit
) : RecyclerView.Adapter<AlbaranesAdapter.AlbaranViewHolder>() {

    class AlbaranViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvImporte: TextView = view.findViewById(R.id.tv_albaran_importe)
        val tvEstado: TextView = view.findViewById(R.id.tv_albaran_estado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbaranViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_albaran, parent, false)
        return AlbaranViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbaranViewHolder, position: Int) {
        val albaran = albaranes[position]

        holder.tvImporte.text = "${albaran.importe} €"

        if (albaran.pagado) {
            holder.tvEstado.text = "Pagado"
            holder.tvEstado.setTextColor(Color.parseColor("#4CAF50")) // Verde
        } else {
            holder.tvEstado.text = "Pendiente"
            holder.tvEstado.setTextColor(Color.parseColor("#F44336")) // Rojo
        }

        // Al hacer clic en la tarjeta, abriremos la foto en grande (lo pide el PDF)
        holder.itemView.setOnClickListener {
            onAlbaranClick(albaran)
        }
    }

    override fun getItemCount(): Int = albaranes.size

    fun updateData(newAlbaranes: List<AlbaranEntity>) {
        this.albaranes = newAlbaranes
        notifyDataSetChanged()
    }
}