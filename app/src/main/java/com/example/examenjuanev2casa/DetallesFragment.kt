package com.example.barapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.examenjuanev2casa.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

// Clase DetailFragment que extiende de Fragment e implementa OnMapReadyCallback (para cargar el mapa)
class DetallesFragment : Fragment(), OnMapReadyCallback {

    // Variables para almacenar la latitud y la longitud del bar
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    // Variable para el MapView que mostrará el mapa en el fragmento
    private lateinit var mapView: MapView

    // Variable para manejar el GoogleMap una vez que esté listo
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento y asignarlo a una variable de vista
        val view = inflater.inflate(R.layout.fragment_detalles, container, false)


        // Obtener referencias de los elementos de la interfaz
        val nameTextView = view.findViewById<TextView>(R.id.tvNombreBArFD)
        val detailsTextView = view.findViewById<TextView>(R.id.tvDetallesBarFD)
        val modifyButton = view.findViewById<Button>(R.id.btModBarFD)
        val rateButton = view.findViewById<Button>(R.id.btPuntBarFD)
        val deleteButton = view.findViewById<Button>(R.id.btDelBarFD)
        mapView = view.findViewById(R.id.mapView) // Inicializar el MapView

        // Obtener datos del bar desde los argumentos enviados por el Activity o Fragment anterior
        arguments?.let { bundle ->
            // Extraer el nombre del bar (si no existe, se asigna "Nombre no disponible")
            val barName = bundle.getString("bar_name", "Nombre no disponible")

            // Extraer la descripción del bar (si no existe, se asigna "Sin descripción")
            val barDescription = bundle.getString("bar_description", "Sin descripción")

            // Extraer la latitud y longitud (si no existen, se asigna 0.0 por defecto)
            latitude = bundle.getDouble("latitude", 41.63193259287489)
            longitude = bundle.getDouble("longitude", -4.7587432528516125)

            // Mostrar los datos obtenidos en los TextView de la interfaz
            nameTextView.text = barName
            detailsTextView.text = barDescription
        }

        // Inicializar el MapView y cargar el mapa cuando esté listo
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this) // Llama a onMapReady cuando el mapa esté disponible

        // Configurar botón "Modificar" (aquí se puede agregar la lógica para modificar datos del bar)
        modifyButton.setOnClickListener {
            Toast.makeText(requireContext(), "Modificar bar (pendiente)", Toast.LENGTH_SHORT).show()
        }

        // Configurar botón "Puntuar" (aquí se puede agregar la lógica para puntuar el bar)
        rateButton.setOnClickListener {
            Toast.makeText(requireContext(), "Puntuar bar (pendiente)", Toast.LENGTH_SHORT).show()
        }

        // Configurar botón "Eliminar" (aquí se eliminaría el bar y se regresaría a la lista)
        deleteButton.setOnClickListener {
            Toast.makeText(requireContext(), "Bar eliminado", Toast.LENGTH_SHORT).show()
            activity?.supportFragmentManager?.popBackStack() // Vuelve atrás en la navegación
        }

        return view // Retorna la vista ya configurada
    }

    /**
     * Método que se ejecuta cuando el mapa está listo para usarse.
     * Aquí se configura la posición en el mapa con un marcador.
     */
    override fun onMapReady(map: GoogleMap) {
        googleMap = map // Se asigna la instancia del mapa

        // Crear una ubicación basada en la latitud y longitud obtenidas
        val location = LatLng(latitude, longitude)

        // Agregar un marcador en la ubicación del bar
        googleMap?.addMarker(MarkerOptions().position(location).title("Ubicación del Bar"))

        // Mover la cámara al marcador con un zoom adecuado
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    /**
     * Manejo del ciclo de vida del MapView
     * Android requiere que estos métodos sean llamados para evitar errores y fugas de memoria.
     */

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
}
