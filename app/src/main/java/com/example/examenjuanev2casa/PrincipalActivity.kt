package com.example.examenjuanev2casa

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.barapp.DetallesFragment

class PrincipalActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbHelper = ManejoBBDD(this)
        dbHelper.openDatabase()

        val db = dbHelper.writableDatabase
        if (db.isOpen) {
            Log.d("Database", "✅ Base de datos abierta correctamente")
        } else {
            Log.e("Database", "❌ Error al abrir la base de datos")
        }


        // Configurar la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Bares Parquesol" // Titulo de la toolbar
        supportActionBar?.setDisplayShowTitleEnabled(true) // Muestra el título
        supportActionBar?.setIcon(R.mipmap.bar) // Icono en la Toolbar

        // Cargar el fragmento al iniciar la actividad (Opcional, puedes cargarlo cuando quieras)
        if (savedInstanceState == null) {
            loadFragment(DetallesFragment())
        }

        // Cargar el fragmento al iniciar la actividad (Opcional, puedes cargarlo cuando quieras)
        if (savedInstanceState == null) {
            loadFragmentLista(ListaFragment())
        }
    }

    private fun loadFragmentLista(listaFragment: ListaFragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragmentLista, listaFragment)
            .addToBackStack(null) // Agrega a la pila para poder volver atrás
            .commit()
    }

    // Función para reemplazar el fragmento en el contenedor
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragmentDetalles, fragment)
            .addToBackStack(null) // Agrega a la pila para poder volver atrás
            .commit()
    }

}