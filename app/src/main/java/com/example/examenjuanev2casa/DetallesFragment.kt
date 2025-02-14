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

// Clase DetailFragment que extiende de Fragment e implementa OnMapReadyCallback (para cargar el mapa)
class DetallesFragment : Fragment(), OnMapReadyCallback {

    private var barId: String? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detalles, container, false)

        // Obtener referencias de los elementos de la UI
        val nameTextView = view.findViewById<TextView>(R.id.tvNombreBArFD)
        val webTextView = view.findViewById<TextView>(R.id.tvDetallesBarFD)
        val modifyButton = view.findViewById<Button>(R.id.btModBarFD)
        val rateButton = view.findViewById<Button>(R.id.btPuntBarFD)
        val deleteButton = view.findViewById<Button>(R.id.btDelBarFD)
        mapView = view.findViewById(R.id.mapView) // Inicializar el MapView

        // Recibir los datos del bar seleccionado
        arguments?.let { bundle ->
            barId = bundle.getString("id") // ⚡ Guarda el ID
            val barName = bundle.getString("nombre", "Nombre no disponible")
            val barWeb = bundle.getString("web", "Web no disponible")
            latitude = bundle.getFloat("latitud", 0f).toDouble()
            longitude = bundle.getFloat("longitud", 0f).toDouble()

            // Mostrar los datos obtenidos en los TextView
            nameTextView.text = barName
            webTextView.text = barWeb

            // 💡 Hacer la web clickeable y abrir en el navegador
            webTextView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(barWeb))
                startActivity(intent)
            }
        }

        // Inicializar el MapView y cargar el mapa cuando esté listo
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        // Configurar botones
        modifyButton.setOnClickListener {
            Toast.makeText(requireContext(), "Modificar bar (pendiente)", Toast.LENGTH_SHORT).show()
        }

        rateButton.setOnClickListener {
            Toast.makeText(requireContext(), "Puntuar bar (pendiente)", Toast.LENGTH_SHORT).show()
        }

        deleteButton.setOnClickListener {
            barId?.let { id -> eliminarBar(id) }
        }

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val location = LatLng(latitude, longitude)

        googleMap?.addMarker(MarkerOptions().position(location).title("Ubicación del Bar"))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

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

    private fun eliminarBar(id: String) {
        val dbHandler = ManejoBBDD(requireContext()) // Instanciar la base de datos

        // Convertir el ID a Int
        val barId = id.toIntOrNull()

        if (barId != null) {
            val resultado = dbHandler.deleteBar(crearBar(barId, "", "", 0.0f, 0.0f)) // Solo el ID es relevante

            Log.d("Database", "Filas afectadas al eliminar: $resultado") // Verificar cuántas filas fueron eliminadas

            if (resultado > 0) {
                // Notificar que se ha eliminado un bar
                setFragmentResult("barEliminado", Bundle().apply {
                    putString("id", id)
                })

                Toast.makeText(requireContext(), "✅ Bar eliminado con ID: $id", Toast.LENGTH_SHORT).show()

                // Volver a la lista
                activity?.supportFragmentManager?.popBackStack()
            } else {
                Toast.makeText(requireContext(), "⚠️ Error al eliminar el bar", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "❌ ID no válido", Toast.LENGTH_SHORT).show()
        }
    }

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
            longitud = 0.0f,  // Valor por defecto
            web_bar = ""      // Valor por defecto
        )
    }

}
