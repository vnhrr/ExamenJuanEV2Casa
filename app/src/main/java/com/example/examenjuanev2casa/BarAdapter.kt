package com.example.examenjuanev2casa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



// Define la clase DiscosAdapter, que extiende RecyclerView.Adapter y especifica MyViewHolder como su ViewHolder.
class BaresAdapter(private val baresList: List<Bar>) : RecyclerView.Adapter<BaresAdapter.MyViewHolder>() {

    // Define la clase interna MyViewHolder, que extiende RecyclerView.ViewHolder.
    // Esta clase proporciona una referencia a las vistas de cada elemento de datos.
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Encuentra y almacena referencias a los elementos de la interfaz de usuario en el layout del ítem.
        var nombreBar: TextView = view.findViewById(R.id.tvNombreBarFL)
        var webBar: TextView = view.findViewById(R.id.tvWebBarFL)
    }

    // Crea nuevas vistas (invocadas por el layout manager).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Infla el layout del ítem de la lista (bar_item.xml) y lo pasa al constructor de MyViewHolder.
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.linea_lista_bares, parent, false)
        return MyViewHolder(itemView)
    }

    // Reemplaza el contenido de una vista (invocada por el layout manager).
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Obtiene el elemento de la lista de bares en esta posición.
        val bar = baresList[position]
        // Reemplaza el contenido de las vistas con los datos del elemento en cuestión.
        holder.nombreBar.text = bar.nombre_bar
        holder.webBar.text = bar.web_bar
    }

    // Devuelve el tamaño de la lista de datos (invocado por el layout manager).
    override fun getItemCount() = baresList.size
}

