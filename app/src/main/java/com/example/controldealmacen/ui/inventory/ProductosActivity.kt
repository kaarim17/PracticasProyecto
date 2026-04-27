package com.example.controldealmacen.ui.inventory

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldealmacen.R
import com.example.controldealmacen.ui.BaseActivity
import com.example.controldealmacen.ui.SessionManager
import com.example.controldealmacen.ui.albaranes.AlbaranesActivity
import com.example.controldealmacen.ui.auth.GestionPerfilesActivity
import com.example.controldealmacen.ui.auth.MainActivity
import com.example.controldealmacen.ui.proveedores.ProveedoresActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class ProductosActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val viewModel: ProductosViewModel by viewModels()
    private lateinit var adapter: ProductosAdapter
    private lateinit var drawerLayout: DrawerLayout
    private var idUsuarioActivo: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        idUsuarioActivo = intent.getIntExtra("ID_USUARIO", -1)

        if (idUsuarioActivo == -1) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setupToolbar()
        setupNavigationDrawer()
        setupRecyclerView()
        setupObservers()
        setupSearch()
        setupFab()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Diario - Hola, ${SessionManager.currentUserName}"
    }

    private fun setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, findViewById(R.id.toolbar),
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        
        navView.setNavigationListener(this)

        // Ocultar sección privada si no es admin
        val menu = navView.menu
        if (SessionManager.currentUserRol != "ADMINISTRADOR") {
            menu.findItem(R.id.group_admin).isVisible = false
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_inventario -> startActivity(Intent(this, InventarioActivity::class.java))
            R.id.nav_perfiles -> {
                if (SessionManager.isAdminAuthenticated) {
                    startActivity(Intent(this, GestionPerfilesActivity::class.java))
                } else {
                    Toast.makeText(this, "Acceso restringido", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.nav_proveedores -> startActivity(Intent(this, ProveedoresActivity::class.java))
            R.id.nav_albaranes -> startActivity(Intent(this, AlbaranesActivity::class.java))
            R.id.nav_logout -> {
                SessionManager.clearSession()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupObservers() {
        viewModel.productos.observe(this) { productos ->
            if (::adapter.isInitialized) {
                adapter.updateData(productos)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.cargarProductos(idUsuario = idUsuarioActivo)
    }

    private fun setupRecyclerView() {
        val rvProductos = findViewById<RecyclerView>(R.id.rv_productos)
        rvProductos.layoutManager = GridLayoutManager(this, 3)

        adapter = ProductosAdapter(
            emptyList(),
            onAddClick = { producto -> viewModel.modificarStock(producto, 1, idUsuarioActivo) },
            onRemoveClick = { producto -> viewModel.modificarStock(producto, -1, idUsuarioActivo) }
        )
        rvProductos.adapter = adapter
    }

    private fun setupSearch() {
        findViewById<EditText>(R.id.et_buscar).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.cargarProductos(s.toString(), idUsuarioActivo)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupFab() {
        findViewById<FloatingActionButton>(R.id.fab_add_producto).setOnClickListener {
            val intent = Intent(this, AddProductoActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuarioActivo)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}

// Extensión necesaria para el listener
fun NavigationView.setNavigationListener(listener: NavigationView.OnNavigationItemSelectedListener) {
    this.setNavigationItemSelectedListener(listener)
}
