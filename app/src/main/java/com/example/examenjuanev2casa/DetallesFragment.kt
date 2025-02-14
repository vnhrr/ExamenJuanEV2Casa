package com.example.barapp

import android.content.Intent
import android.net.Uri
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

        // Obtener datos del bar desde los argumentos enviados
        arguments?.let { bundle ->
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
            Toast.makeText(requireContext(), "Bar eliminado", Toast.LENGTH_SHORT).show()
            activity?.supportFragmentManager?.popBackStack()
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
}
