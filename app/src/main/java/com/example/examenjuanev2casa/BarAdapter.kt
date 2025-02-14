package com.example.examenjuanev2casa

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

// Adaptador personalizado para mostrar la lista de bares en un ListView
class BarAdapter(private val context: Context, private var baresList: List<Bar>) : BaseAdapter() {

    // Devuelve el número total de elementos en la lista
    override fun getCount(): Int = baresList.size

    // Devuelve el objeto Bar en la posición dada
    override fun getItem(position: Int): Any = baresList[position]

    // Devuelve el ID del objeto en la posición dada
    override fun getItemId(position: Int): Long = baresList[position].id.toLong()

    // Genera y devuelve la vista para cada elemento de la lista
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Si convertView es nulo, se infla el layout correspondiente
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.linea_lista_bares, parent, false)

        // Referencias a los TextView en el layout para mostrar la información del bar
        val nombreBar = view.findViewById<TextView>(R.id.tvNombreBarFL)
        val webBar = view.findViewById<TextView>(R.id.tvWebBarFL)

        // Obtener el bar actual de la lista y asignar los valores a los TextView
        val bar = baresList[position]
        nombreBar.text = bar.nombre_bar
        webBar.text = bar.web_bar

        return view
    }

    /**
     * Método para actualizar la lista de bares y notificar cambios en el adaptador
     * @param newList Nueva lista de bares a mostrar
     */
    fun updateList(newList: List<Bar>) {
        baresList = newList
        notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
    }
}
