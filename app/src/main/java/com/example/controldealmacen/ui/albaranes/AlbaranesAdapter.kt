package com.example.controldealmacen.ui.albaranes

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.example.controldealmacen.data.local.entities.AlbaranConProveedor

class AlbaranesAdapter(
    private var albaranesConProveedor: List<AlbaranConProveedor>,
    private val onAlbaranClick: (AlbaranConProveedor) -> Unit
) : RecyclerView.Adapter<AlbaranesAdapter.AlbaranViewHolder>() {

    private var selectionMode = false
    private val selectedIds = mutableSetOf<Int>()

    class AlbaranViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvProveedor: TextView = view.findViewById(R.id.tv_albaran_proveedor)
        val tvImporte: TextView = view.findViewById(R.id.tv_albaran_importe)
        val tvEstado: TextView = view.findViewById(R.id.tv_albaran_estado)
        val checkBox: CheckBox = view.findViewById(R.id.cb_seleccion_albaran)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbaranViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_albaran, parent, false)
        return AlbaranViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbaranViewHolder, position: Int) {
        val item = albaranesConProveedor[position]
        val albaran = item.albaran
        val proveedor = item.proveedor

        holder.tvProveedor.text = proveedor.nombre
        holder.tvImporte.text = "${albaran.importe} €"

        if (albaran.pagado) {
            holder.tvEstado.text = "PAGADO"
            holder.tvEstado.setTextColor(Color.parseColor("#4CAF50"))
            holder.tvEstado.setBackgroundResource(android.R.drawable.editbox_dropdown_light_frame)
        } else {
            holder.tvEstado.text = "PENDIENTE"
            holder.tvEstado.setTextColor(Color.parseColor("#F44336"))
            holder.tvEstado.setBackgroundResource(android.R.drawable.editbox_dropdown_dark_frame)
        }

        holder.checkBox.visibility = if (selectionMode) View.VISIBLE else View.GONE
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = selectedIds.contains(albaran.id)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedIds.add(albaran.id) else selectedIds.remove(albaran.id)
        }

        holder.itemView.setOnClickListener {
            if (selectionMode) {
                holder.checkBox.isChecked = !holder.checkBox.isChecked
            } else {
                onAlbaranClick(item)
            }
        }
    }

    override fun getItemCount(): Int = albaranesConProveedor.size

    fun updateData(newItems: List<AlbaranConProveedor>) {
        this.albaranesConProveedor = newItems
        notifyDataSetChanged()
    }

    fun setSelectionMode(enabled: Boolean) {
        selectionMode = enabled
        if (!enabled) selectedIds.clear()
        notifyDataSetChanged()
    }

    fun getSelectedIds(): List<Int> = selectedIds.toList()
}
