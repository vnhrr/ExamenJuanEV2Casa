package com.example.examenjuanev2casa

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class FragmentMap : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap
    private var lati: Double = 0.0
    private var longi: Double = 0.0
    private var mapaListo = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detalles, container, false)

        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapaListo = true

        // Habilitar controles de zoom y gestos
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true

        // Establecer la ubicación predeterminada en Valladolid
        val ubicacion = LatLng(41.63193259287489, -4.7587432528516125)
        mMap.addMarker(MarkerOptions().position(ubicacion).title("Ubicación por defecto"))

        // Ajustar el zoom para que la ubicación sea visible correctamente
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f))

        // Cambiar el tipo de mapa a híbrido
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
    }

    /**
     * Método para actualizar la ubicación del marcador en el mapa.
     */
    fun actualizarTexto(datos1: Double, datos2: Double) {
        if (!mapaListo) {
            println("⚠️ Mapa aún no está listo. Ignorando actualización.")
            return
        }

        lati = datos1
        longi = datos2

        // Validar que las coordenadas sean correctas y evitar que se ubiquen en el Golfo de Guinea
        if (lati == 0.0 || longi == 0.0) {
            println("⚠️ Error: Coordenadas inválidas, usando Valladolid por defecto")
            lati = 41.63193259287489
            longi = -4.7587432528516125
        }

        val nuevaUbicacion = LatLng(lati, longi)

        // Limpiar el mapa antes de agregar nuevos marcadores
        mMap.clear()

        // Crear icono personalizado
        val iconoPersonalizado = BitmapDescriptorFactory.fromBitmap(
            resizeMapIcon("pngegg", 100, 100)
        )

        // Agregar marcador en la nueva ubicación
        mMap.addMarker(
            MarkerOptions()
                .position(nuevaUbicacion)
                .title("Nueva ubicación")
                .icon(iconoPersonalizado)
        )

        // Mover la cámara con animación
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(nuevaUbicacion, 15f))

        // Agregar otro marcador adicional en IES Julián Marías
        val iesjulian = LatLng(41.6320851, -4.7590656)
        mMap.addMarker(MarkerOptions().position(iesjulian).title("IES Julián Marías"))
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

    /**
     * Método para redimensionar el icono del marcador en el mapa.
     */
    private fun resizeMapIcon(iconName: String, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(
            resources,
            resources.getIdentifier(iconName, "drawable", requireContext().packageName)
        )
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }
}
