package com.example.examenjuanev2casa

// Importaciones necesarias para la funcionalidad de la aplicación
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class AñadirActivity : AppCompatActivity() {

    // Declaración de variables para los campos de entrada de datos
    private lateinit var etNombre: EditText
    private lateinit var etDireccion: EditText
    private lateinit var etLatitud: EditText
    private lateinit var etLongitud: EditText
    private lateinit var etWeb: EditText
    private lateinit var dbHandler: ManejoBBDD // Manejador de base de datos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir)

        // Inicialización de los EditText con sus respectivos IDs del layout
        etNombre = findViewById(R.id.etNombre)
        etDireccion = findViewById(R.id.etDireccion)
        etLatitud = findViewById(R.id.etLatitud)
        etLongitud = findViewById(R.id.etLongitud)
        etWeb = findViewById(R.id.etWeb)

        // Inicializar la base de datos
        dbHandler = ManejoBBDD(this)

        // Crear canal de notificación (necesario para Android 8.0+)
        crearCanalDeNotificacion()

        // Solicitar permisos de notificación en dispositivos con Android 13+
        solicitarPermisoNotificaciones()

        // Botón para añadir un bar a la base de datos
        val incluir = findViewById<Button>(R.id.btIncluir)
        incluir.setOnClickListener {
            if (addBar()) { // Si el bar se añade correctamente, se cierra la actividad
                finish()
            }
        }
    }

    /**
     * Método para agregar un nuevo bar a la base de datos
     * @return `true` si el bar se añadió correctamente, `false` si hubo un error
     */
    private fun addBar(): Boolean {
        val nombre = etNombre.text.toString()
        val direccion = etDireccion.text.toString()
        val latitud = etLatitud.text.toString()
        val longitud = etLongitud.text.toString()
        val web = etWeb.text.toString()

        // Verificar que todos los campos estén llenos antes de continuar
        if (nombre.isNotEmpty() && direccion.isNotEmpty() && latitud.isNotEmpty() && longitud.isNotEmpty() && web.isNotEmpty()) {
            val bar = Bar(
                nombre_bar = nombre,
                direccion = direccion,
                valoracion = 0f, // Valoración inicial en 0
                latitud = latitud.toFloat(),
                longitud = longitud.toFloat(),
                web_bar = web
            )

            val status = dbHandler.addBar(bar) // Llamada a la base de datos para añadir el bar

            if (status > -1) { // Si se ha insertado correctamente
                mostrarNotificacion(nombre) // Mostrar una notificación de éxito
                Toast.makeText(applicationContext, "Bar añadido correctamente", Toast.LENGTH_LONG).show()
                return true
            }
        } else {
            Toast.makeText(applicationContext, "Todos los datos son requeridos", Toast.LENGTH_LONG).show()
            return false
        }
        return false
    }

    /**
     * Solicitar permisos de notificación en Android 13+
     */
    private fun solicitarPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }

    /**
     * Crea el canal de notificaciones (Requerido para Android 8.0+)
     */
    private fun crearCanalDeNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nombre = "Canal de Bares"
            val descripcion = "Notificaciones cuando se añade un nuevo bar"
            val importancia = NotificationManager.IMPORTANCE_DEFAULT
            val canal = NotificationChannel("BAR_CHANNEL", nombre, importancia).apply {
                description = descripcion
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(canal) // Registrar el canal de notificaciones
            Log.d("Notificaciones", "✅ Canal de notificación creado correctamente")
        }
    }

    /**
     * Muestra una notificación cuando se añade un bar
     */
    private fun mostrarNotificacion(nombreBar: String) {
        Log.d("Notificaciones", "🔔 Intentando mostrar notificación para el bar: $nombreBar")

        // Crear intent para abrir la actividad principal al hacer clic en la notificación
        val intent = Intent(this, PrincipalActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Crear un PendingIntent para la notificación
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construcción de la notificación
        val builder = NotificationCompat.Builder(this, "BAR_CHANNEL")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Icono de la notificación
            .setContentTitle("Nuevo Bar Añadido 🍻")
            .setContentText("Se ha añadido \"$nombreBar\" a la lista.")
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Alta prioridad para mayor visibilidad
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Se elimina la notificación al hacer clic

        // Enviar la notificación si se tienen permisos
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(this@AñadirActivity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notify(1001, builder.build()) // ID de la notificación
                Log.d("Notificaciones", "✅ Notificación enviada con éxito")
            } else {
                Log.d("Notificaciones", "⚠️ No se tiene permiso para mostrar notificaciones")
            }
        }
    }
}
