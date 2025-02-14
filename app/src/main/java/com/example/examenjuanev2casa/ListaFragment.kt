package com.example.examenjuanev2casa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.barapp.DetallesFragment

// Fragmento que muestra la lista de bares almacenados en la base de datos
class ListaFragment : Fragment() {

    // Variables para manejar la base de datos, la lista y el adaptador
    private lateinit var dbHandler: ManejoBBDD // Manejador de la base de datos
    private lateinit var listViewBares: ListView // ListView para mostrar la lista de bares
    private lateinit var adapter: BarAdapter // Adaptador para gestionar la visualización de los bares

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout asociado a este fragmento
        val view = inflater.inflate(R.layout.fragment_lista, container, false)

        // Inicializar la base de datos
        dbHandler = ManejoBBDD(requireContext())

        // Referencias a elementos de la interfaz de usuario
        listViewBares = view.findViewById(R.id.listViewBares)
        val tituloLista = view.findViewById<TextView>(R.id.tvTListaBares)
        val botonAñadir = view.findViewById<Button>(R.id.btAñadirBarFL)

        // Asignar título a la lista
        tituloLista.text = "Lista de bares"

        // Configurar el botón para añadir un nuevo bar
        botonAñadir.setOnClickListener {
            startActivity(Intent(requireContext(), AñadirActivity::class.java))
        }

        // Escuchar si un bar ha sido eliminado en `DetallesFragment` y actualizar la lista
        setFragmentResultListener("barEliminado") { _, _ ->
            Log.d("Database", "🔄 Recibida la señal de eliminación. Actualizando lista...")
            actualizarLista()
        }

        // Configurar la acción al hacer clic en un elemento de la lista
        listViewBares.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedBar = adapter.getItem(position) as Bar // Obtener el bar seleccionado

            // Crear un nuevo fragmento de detalles y pasarle los datos del bar seleccionado
            val detallesFragment = DetallesFragment().apply {
                arguments = Bundle().apply {
                    putString("id", selectedBar.id.toString()) // Pasar ID
                    putString("nombre", selectedBar.nombre_bar) // Pasar nombre
                    putFloat("latitud", selectedBar.latitud) // Pasar latitud
                    putFloat("longitud", selectedBar.longitud) // Pasar longitud
                    putString("web", selectedBar.web_bar) // Pasar URL web
                }
            }

            // Reemplazar el fragmento actual con el fragmento de detalles y añadir a la pila de retroceso
            parentFragmentManager.beginTransaction()
                .replace(R.id.containerFragmentDetalles, detallesFragment)
                .addToBackStack(null)
                .commit()
        }

        // Cargar la lista de bares al iniciar
        actualizarLista()

        return view
    }

    /**
     * Se ejecuta cuando el fragmento se reanuda.
     * Se usa para asegurar que la lista siempre esté actualizada.
     */
    override fun onResume() {
        super.onResume()
        Log.d("Database", "🔄 onResume llamado. Forzando actualización de la lista...")
        actualizarLista()
    }

    /**
     * Método para actualizar la lista de bares con los datos de la base de datos.
     */
    private fun actualizarLista() {
        val baresLista = dbHandler.getAllBares() // Obtener todos los bares de la base de datos

        Log.d("Database", "📋 Total de bares obtenidos después de actualización: ${baresLista.size}")

        if (::adapter.isInitialized) {
            adapter.updateList(baresLista) // Actualizar la lista en el adaptador
            adapter.notifyDataSetChanged() // Notificar al adaptador que los datos han cambiado
            listViewBares.invalidateViews() // Forzar redibujado de la lista para reflejar los cambios
        } else {
            adapter = BarAdapter(requireContext(), baresLista) // Crear un nuevo adaptador
            listViewBares.adapter = adapter // Asignar el adaptador a la ListView
        }
    }
}
