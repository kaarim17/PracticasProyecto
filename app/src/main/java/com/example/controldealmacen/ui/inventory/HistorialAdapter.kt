package com.example.controldealmacen.ui.inventory

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.entities.HistorialDetallado
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistorialAdapter(private var movimientos: List<HistorialDetallado>) :
    RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder>() {

    class HistorialViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvProducto: TextView = view.findViewById(R.id.tv_historial_producto)
        val tvUsuarioFecha: TextView = view.findViewById(R.id.tv_historial_usuario_fecha)
        val tvCantidad: TextView = view.findViewById(R.id.tv_historial_cantidad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historial, parent, false)
        return HistorialViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistorialViewHolder, position: Int) {
        val item = movimientos[position]

        holder.tvProducto.text = item.producto.nombre

        // Formatear la fecha para que se lea como "22/04/2026 14:30"
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fecha = sdf.format(Date(item.historial.fechaHora))

        holder.tvUsuarioFecha.text = "Por: ${item.perfil.nombre} - $fecha"

        if (item.historial.tipoAccion == "ENTRADA" || item.historial.tipoAccion == "ALTA") {
            holder.tvCantidad.text = "+${item.historial.cantidad}"
            holder.tvCantidad.setTextColor(Color.parseColor("#4CAF50")) // Verde
        } else {
            holder.tvCantidad.text = "-${item.historial.cantidad}"
            holder.tvCantidad.setTextColor(Color.parseColor("#F44336")) // Rojo
        }
    }

    override fun getItemCount(): Int = movimientos.size

    fun updateData(nuevosMovimientos: List<HistorialDetallado>) {
        this.movimientos = nuevosMovimientos
        notifyDataSetChanged()
    }
}