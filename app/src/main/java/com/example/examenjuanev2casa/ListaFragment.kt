package com.example.examenjuanev2casa

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

class ListaFragment : Fragment() {

    private var dbHandler: ManejoBBDD? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento
        val view = inflater.inflate(R.layout.fragment_lista, container, false)

        // Referencias a los elementos de la UI
        val listViewBares = view.findViewById<ListView>(R.id.listViewBares)
        val tituloLista = view.findViewById<TextView>(R.id.tvTListaBares)
        val botonAñadir = view.findViewById<Button>(R.id.btAñadirBarFL)


        // Configurar la lista (Ejemplo de datos)
        val bares_lista = viewBares()
        val adapter = android.widget.ArrayAdapter(requireContext(), R.layout.linea_lista_bares, bares_lista)
        listViewBares.adapter = adapter

        tituloLista.text = "Lista de bares"

        // Configurar evento del botón "Añadir"
        botonAñadir.setOnClickListener {
            startActivity(Intent(requireContext(), AñadirActivity::class.java))
        }

        return view
    }

    // Método para obtener los nombres de los bares desde la base de datos y devolverlos como List<String>
    private fun viewBares(): List<String> {
        val baresList = dbHandler?.getAllBares() // Obtiene los bares desde la base de datos
        if (baresList != null) {
            return baresList.map { it.nombre_bar }
        } // Devuelve solo los nombres de los bares
    }
}
