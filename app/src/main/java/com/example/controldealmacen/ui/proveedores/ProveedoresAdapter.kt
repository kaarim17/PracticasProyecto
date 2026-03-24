    package com.example.controldealmacen.ui.proveedores

    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.TextView
    import androidx.recyclerview.widget.RecyclerView
    import com.example.controldealmacen.R
    import com.example.controldealmacen.data.local.entities.ProveedorEntity

    class ProveedoresAdapter(
        private var proveedores: List<ProveedorEntity>,
        private val onClick: (ProveedorEntity) -> Unit
    ) : RecyclerView.Adapter<ProveedoresAdapter.ProveedorViewHolder>() {

        class ProveedorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNombre: TextView = view.findViewById(R.id.tv_proveedor_nombre)
            val tvCif: TextView = view.findViewById(R.id.tv_proveedor_cif)
            val tvContacto: TextView = view.findViewById(R.id.tv_proveedor_contacto)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProveedorViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_proveedor, parent, false)
            return ProveedorViewHolder(view)
        }

        override fun onBindViewHolder(holder: ProveedorViewHolder, position: Int) {
            val proveedor = proveedores[position]

            holder.tvNombre.text = proveedor.nombre
            holder.tvCif.text = "CIF: ${proveedor.cif}"

            val tel = proveedor.telefono ?: "Sin telf"
            val email = proveedor.email ?: "Sin email"
            holder.tvContacto.text = "Tel: $tel | $email"

            holder.itemView.setOnClickListener {
                onClick(proveedor)
            }
        }

        override fun getItemCount(): Int = proveedores.size

        fun updateData(nuevaLista: List<ProveedorEntity>) {
            proveedores = nuevaLista
            notifyDataSetChanged()
        }
    }