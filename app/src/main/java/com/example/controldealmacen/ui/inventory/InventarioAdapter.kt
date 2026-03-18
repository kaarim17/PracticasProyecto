package com.example.controldealmacen.ui.inventory

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.entities.ProductoEntity

class InventarioAdapter(
    private var productos: List<ProductoEntity>
) : RecyclerView.Adapter<InventarioAdapter.InventarioViewHolder>() {

    class InventarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tv_inv_nombre)
        val tvCantidad: TextView = view.findViewById(R.id.tv_inv_cantidad)
        val tvMinima: TextView = view.findViewById(R.id.tv_inv_minima)
        val tvEstado: TextView = view.findViewById(R.id.tv_inv_estado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inventario_fila, parent, false)
        return InventarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: InventarioViewHolder, position: Int) {
        val producto = productos[position]

        holder.tvNombre.text = producto.nombre
        holder.tvCantidad.text = producto.cantidad.toString()
        holder.tvMinima.text = producto.cantidadMinima?.toString() ?: "-"

        if (producto.habilitado) {
            holder.tvEstado.text = "Activo"
            holder.tvEstado.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            holder.tvEstado.text = "Deshab."
            holder.tvEstado.setTextColor(Color.parseColor("#F44336"))
        }

        // Resaltar en rojo si el stock es bajo
        if (producto.cantidadMinima != null && producto.cantidad <= producto.cantidadMinima) {
            holder.tvCantidad.setTextColor(Color.RED)
            holder.itemView.setBackgroundColor(Color.parseColor("#FFF0F0"))
        } else {
            holder.tvCantidad.setTextColor(Color.BLACK)
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    override fun getItemCount(): Int = productos.size

    fun updateData(newProductos: List<ProductoEntity>) {
        productos = newProductos
        notifyDataSetChanged()
    }
}
