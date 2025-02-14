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

class ListaFragment : Fragment() {

    private lateinit var dbHandler: ManejoBBDD
    private lateinit var listViewBares: ListView
    private lateinit var adapter: BarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lista, container, false)

        // Inicializar base de datos
        dbHandler = ManejoBBDD(requireContext())

        // Referencias a UI
        listViewBares = view.findViewById(R.id.listViewBares)
        val tituloLista = view.findViewById<TextView>(R.id.tvTListaBares)
        val botonAñadir = view.findViewById<Button>(R.id.btAñadirBarFL)

        tituloLista.text = "Lista de bares"

        // Configurar botón "Añadir"
        botonAñadir.setOnClickListener {
            startActivity(Intent(requireContext(), AñadirActivity::class.java))
        }

        // 🔥 Escuchar si un bar ha sido eliminado en `DetallesFragment`
        setFragmentResultListener("barEliminado") { _, _ ->
            Log.d("Database", "🔄 Recibida la señal de eliminación. Actualizando lista...")
            actualizarLista()
        }

        // 💡 Detectar clic en un bar y enviar datos a `DetallesFragment`
        listViewBares.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedBar = adapter.getItem(position) as Bar

            val detallesFragment = DetallesFragment().apply {
                arguments = Bundle().apply {
                    putString("id", selectedBar.id.toString())
                    putString("nombre", selectedBar.nombre_bar)
                    putFloat("latitud", selectedBar.latitud)
                    putFloat("longitud", selectedBar.longitud)
                    putString("web", selectedBar.web_bar)
                }
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.containerFragmentDetalles, detallesFragment)
                .addToBackStack(null)
                .commit()
        }

        // Cargar lista de bares
        actualizarLista()

        return view
    }

    override fun onResume() {
        super.onResume()
        Log.d("Database", "🔄 onResume llamado. Forzando actualización de la lista...")
        actualizarLista() // Asegurar que la lista se actualiza al volver
    }

    private fun actualizarLista() {
        val baresLista = dbHandler.getAllBares()

        Log.d("Database", "📋 Total de bares obtenidos después de actualización: ${baresLista.size}")

        if (::adapter.isInitialized) {
            adapter.updateList(baresLista)
            adapter.notifyDataSetChanged() // 🔥 Notificar cambios al Adapter
            listViewBares.invalidateViews() // 🔄 Forzar redibujado de la lista
        } else {
            adapter = BarAdapter(requireContext(), baresLista)
            listViewBares.adapter = adapter
        }
    }
}
