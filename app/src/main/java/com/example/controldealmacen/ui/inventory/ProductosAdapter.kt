package com.example.controldealmacen.ui.inventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.entities.ProductoEntity

class ProductosAdapter(
    private var productos: List<ProductoEntity>,
    private val onAddClick: (ProductoEntity) -> Unit,
    private val onRemoveClick: (ProductoEntity) -> Unit
) : RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProducto: ImageView = view.findViewById(R.id.img_producto)
        val tvNombre: TextView = view.findViewById(R.id.tv_nombre_producto)
        val tvStock: TextView = view.findViewById(R.id.tv_stock)
        val tvAvisoMinimo: TextView = view.findViewById(R.id.tv_aviso_minimo)
        val btnSumar: ImageButton = view.findViewById(R.id.btn_sumar)
        val btnRestar: ImageButton = view.findViewById(R.id.btn_restar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.tvNombre.text = producto.nombre
        holder.tvStock.text = "Cantidad: ${producto.cantidad}"

        // Mostrar aviso de stock bajo si tiene límite mínimo y la cantidad es menor o igual
        if (producto.cantidadMinima != null && producto.cantidad <= producto.cantidadMinima) {
            holder.tvAvisoMinimo.visibility = View.VISIBLE
        } else {
            holder.tvAvisoMinimo.visibility = View.GONE
        }

        // Imagen por defecto por ahora
        holder.imgProducto.setImageResource(R.drawable.ic_launcher_foreground)

        holder.btnSumar.setOnClickListener { onAddClick(producto) }
        holder.btnRestar.setOnClickListener { onRemoveClick(producto) }
    }

    override fun getItemCount(): Int = productos.size

    fun updateData(newProductos: List<ProductoEntity>) {
        productos = newProductos
        notifyDataSetChanged()
    }
}