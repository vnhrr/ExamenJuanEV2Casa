package com.example.barapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.examenjuanev2casa.Bar
import com.example.examenjuanev2casa.ManejoBBDD
import com.example.examenjuanev2casa.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Fragmento que muestra los detalles de un bar, incluyendo su información y su ubicación en un mapa interactivo.
 */
class DetallesFragment : Fragment(), OnMapReadyCallback {

    // Variables para almacenar la información del bar seleccionado
    private var barId: String? = null // ID único del bar
    private var latitude: Double = 0.0 // Latitud del bar
    private var longitude: Double = 0.0 // Longitud del bar

    // Variables relacionadas con Google Maps
    private lateinit var mapView: MapView // Componente de la vista del mapa
    private var googleMap: GoogleMap? = null // Objeto que representa el mapa

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout para este fragmento y asociarlo a la vista
        val view = inflater.inflate(R.layout.fragment_detalles, container, false)

        // Referencias a los elementos de la interfaz de usuario
        val nameTextView = view.findViewById<TextView>(R.id.tvNombreBArFD) // Nombre del bar
        val webTextView = view.findViewById<TextView>(R.id.tvDetallesBarFD) // URL del bar
        val modifyButton = view.findViewById<Button>(R.id.btModBarFD) // Botón de modificar
        val rateButton = view.findViewById<Button>(R.id.btPuntBarFD) // Botón de puntuar
        val deleteButton = view.findViewById<Button>(R.id.btDelBarFD) // Botón de eliminar
        mapView = view.findViewById(R.id.mapView) // Inicialización del MapView

        // Obtener datos del bar seleccionados de los argumentos recibidos
        arguments?.let { bundle ->
            barId = bundle.getString("id") // ID del bar
            val barName = bundle.getString("nombre", "Nombre no disponible") // Nombre del bar
            val barWeb = bundle.getString("web", "Web no disponible") // URL del bar
            latitude = bundle.getFloat("latitud", 0f).toDouble() // Latitud
            longitude = bundle.getFloat("longitud", 0f).toDouble() // Longitud

            // Mostrar la información obtenida en los componentes de la UI
            nameTextView.text = barName
            webTextView.text = barWeb

            // Hacer que el enlace web sea clickeable y se abra en un navegador
            webTextView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(barWeb))
                startActivity(intent)
            }
        }

        // Inicializar el MapView y cargar el mapa cuando esté listo
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        // Configurar botones con sus respectivas acciones
        modifyButton.setOnClickListener {
            Toast.makeText(requireContext(), "Modificar bar (pendiente)", Toast.LENGTH_SHORT).show()
        }

        rateButton.setOnClickListener {
            Toast.makeText(requireContext(), "Puntuar bar (pendiente)", Toast.LENGTH_SHORT).show()
        }

        deleteButton.setOnClickListener {
            barId?.let { id -> eliminarBar(id) } // Llamar a la función para eliminar el bar
        }

        return view
    }

    /**
     * Método que se ejecuta cuando el mapa está listo para usarse.
     * Agrega un marcador en la ubicación del bar y mueve la cámara hacia él.
     */
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val location = LatLng(latitude, longitude)
        googleMap?.addMarker(MarkerOptions().position(location).title("Ubicación del Bar"))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    // Métodos del ciclo de vida del MapView para evitar errores en la visualización del mapa
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    /**
     * Método para eliminar un bar de la base de datos.
     * @param id ID del bar a eliminar.
     */
    private fun eliminarBar(id: String) {
        val dbHandler = ManejoBBDD(requireContext())
        val barId = id.toIntOrNull()

        if (barId != null) {
            val resultado = dbHandler.deleteBar(crearBar(barId, "", "", 0.0f, 0.0f))
            Log.d("Database", "Filas afectadas al eliminar: $resultado")

            if (resultado > 0) {
                // Notificar que el bar ha sido eliminado
                setFragmentResult("barEliminado", Bundle().apply { putString("id", id) })
                Toast.makeText(requireContext(), "✅ Bar eliminado con ID: $id", Toast.LENGTH_SHORT).show()
                activity?.supportFragmentManager?.popBackStack()
            } else {
                Toast.makeText(requireContext(), "⚠️ Error al eliminar el bar", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "❌ ID no válido", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Método auxiliar para crear un objeto Bar con valores por defecto.
     * @param id ID del bar.
     * @param nombre_bar Nombre del bar.
     * @param direccion Dirección del bar.
     * @param valoracion Valoración del bar.
     * @param latitud Latitud del bar.
     * @return Objeto Bar con valores predeterminados.
     */
    private fun crearBar(
        id: Int = 0,
        nombre_bar: String = "",
        direccion: String = "",
        valoracion: Float = 0.0f,
        latitud: Float = 0.0f
    ): Bar {
        return Bar(
            id = id,
            nombre_bar = nombre_bar,
            direccion = direccion,
            valoracion = valoracion,
            latitud = latitud,
            longitud = 0.0f, // Valor por defecto
            web_bar = "" // Valor por defecto
        )
    }
}
