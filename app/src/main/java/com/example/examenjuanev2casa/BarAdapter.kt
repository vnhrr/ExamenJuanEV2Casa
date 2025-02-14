package com.example.examenjuanev2casa

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class BarAdapter(private val context: Context, private var baresList: List<Bar>) : BaseAdapter() {

    override fun getCount(): Int = baresList.size

    override fun getItem(position: Int): Any = baresList[position]

    override fun getItemId(position: Int): Long = baresList[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.linea_lista_bares, parent, false)

        // Referencias a los TextView en el layout
        val nombreBar = view.findViewById<TextView>(R.id.tvNombreBarFL)
        val webBar = view.findViewById<TextView>(R.id.tvWebBarFL)

        // Obtener el bar actual y asignar los datos
        val bar = baresList[position]
        nombreBar.text = bar.nombre_bar
        webBar.text = bar.web_bar

        return view
    }

    fun updateList(newList: List<Bar>) {
        baresList = newList
        notifyDataSetChanged()
    }
}
