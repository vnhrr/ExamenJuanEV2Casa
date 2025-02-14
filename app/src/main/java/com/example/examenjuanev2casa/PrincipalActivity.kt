package com.example.examenjuanev2casa

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.barapp.DetallesFragment

// Actividad principal que gestiona la interfaz de usuario y la navegación entre fragmentos
class PrincipalActivity : AppCompatActivity() {

    private lateinit var spinnerBares: Spinner // Spinner para seleccionar bares
    private lateinit var dbHelper: ManejoBBDD // Manejador de base de datos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = ManejoBBDD(this) // Inicializar la base de datos
        verificarBaseDeDatos() // Verificar que la base de datos está funcionando correctamente

        // Configurar la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Bares Parquesol" // Título de la toolbar
        supportActionBar?.setDisplayShowTitleEnabled(true) // Muestra el título
        supportActionBar?.setDisplayShowHomeEnabled(true) // Habilitar el icono
        supportActionBar?.setLogo(R.mipmap.bar) // Añadir el icono a la toolbar

        // Inicializar Spinner
        spinnerBares = findViewById(R.id.spinnerBares)
        cargarBaresEnSpinner() // Cargar la lista de bares en el Spinner

        // Cargar los fragmentos solo si no están ya cargados
        if (savedInstanceState == null) {
            loadFragmentLista(ListaFragment()) // Cargar el fragmento de la lista de bares
            loadFragment(DetallesFragment()) // Cargar el fragmento de detalles de un bar
        }

        // Manejar la selección de bares en el Spinner
        spinnerBares.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val baresLista = dbHelper.getAllBares()
                if (position < baresLista.size) {
                    val barSeleccionado = baresLista[position]
                    cargarDetallesBar(barSeleccionado) // Actualizar DetallesFragment con el bar seleccionado
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se realiza ninguna acción si no se selecciona nada
            }
        }
    }

    /**
     * Verifica si la base de datos está abierta y funcionando correctamente
     */
    private fun verificarBaseDeDatos() {
        val db = dbHelper.writableDatabase
        if (db.isOpen) {
            Log.d("Database", "✅ Base de datos abierta correctamente")
        } else {
            Log.e("Database", "❌ Error al abrir la base de datos")
        }
    }

    /**
     * Carga los bares desde la base de datos y los muestra en el Spinner
     */
    private fun cargarBaresEnSpinner() {
        val baresLista = dbHelper.getAllBares() // Obtener lista de bares desde la BD

        if (baresLista.isNotEmpty()) {
            val nombresBares = baresLista.map { it.nombre_bar } // Obtener nombres de los bares

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresBares)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinnerBares.adapter = adapter // Asignar adaptador al Spinner
            Log.d("Database", "🔄 Spinner actualizado con ${nombresBares.size} bares")
        } else {
            Log.d("Database", "⚠️ No hay bares en la base de datos")
        }
    }

    /**
     * Carga el fragmento de la lista de bares en su contenedor
     */
    private fun loadFragmentLista(listaFragment: ListaFragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragmentLista, listaFragment)
            .addToBackStack(null) // Agregar a la pila para navegación
            .commit()
    }

    /**
     * Carga un fragmento en el contenedor de detalles
     */
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragmentDetalles, fragment)
            .addToBackStack(null) // Agregar a la pila para navegación
            .commit()
    }

    /**
     * Carga y actualiza `DetallesFragment` con los datos del bar seleccionado en el Spinner
     * @param bar Bar seleccionado para mostrar detalles
     */
    private fun cargarDetallesBar(bar: Bar) {
        val detallesFragment = DetallesFragment().apply {
            arguments = Bundle().apply {
                putString("id", bar.id.toString())
                putString("nombre", bar.nombre_bar)
                putFloat("latitud", bar.latitud)
                putFloat("longitud", bar.longitud)
                putString("web", bar.web_bar)
            }
        }

        loadFragment(detallesFragment) // Cargar el fragmento con los nuevos datos del bar seleccionado
    }
}

