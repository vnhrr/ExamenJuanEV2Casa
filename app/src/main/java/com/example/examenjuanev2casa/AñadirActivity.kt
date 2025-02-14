package com.example.examenjuanev2casa

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

    private lateinit var etNombre: EditText
    private lateinit var etDireccion: EditText
    private lateinit var etLatitud: EditText
    private lateinit var etLongitud: EditText
    private lateinit var etWeb: EditText
    private lateinit var dbHandler: ManejoBBDD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir)

        etNombre = findViewById(R.id.etNombre)
        etDireccion = findViewById(R.id.etDireccion)
        etLatitud = findViewById(R.id.etLatitud)
        etLongitud = findViewById(R.id.etLongitud)
        etWeb = findViewById(R.id.etWeb)

        // Inicializar la base de datos
        dbHandler = ManejoBBDD(this)

        // Crear canal de notificación
        crearCanalDeNotificacion()

        // Solicitar permisos de notificación en Android 13+
        solicitarPermisoNotificaciones()

        val incluir = findViewById<Button>(R.id.btIncluir)
        incluir.setOnClickListener {
            if (addBar()) {
                finish()
            }
        }
    }

    /**
     * Método para agregar un nuevo Bar a la base de datos.
     * @return `true` si el bar se agregó correctamente, `false` si hubo un error.
     */
    private fun addBar(): Boolean {
        val nombre = etNombre.text.toString()
        val direccion = etDireccion.text.toString()
        val latitud = etLatitud.text.toString()
        val longitud = etLongitud.text.toString()
        val web = etWeb.text.toString()

        // Verificar que los campos no estén vacíos
        if (nombre.isNotEmpty() && direccion.isNotEmpty() && latitud.isNotEmpty() && longitud.isNotEmpty() && web.isNotEmpty()) {
            val bar = Bar(
                nombre_bar = nombre,
                direccion = direccion,
                valoracion = 0f,
                latitud = latitud.toFloat(),
                longitud = longitud.toFloat(),
                web_bar = web
            )

            val status = dbHandler.addBar(bar)

            if (status > -1) {
                // 🔥 Mostrar notificación de éxito
                mostrarNotificacion(nombre)

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
            manager?.createNotificationChannel(canal)

            Log.d("Notificaciones", "✅ Canal de notificación creado correctamente")
        }
    }

    /**
     * Muestra una notificación cuando se añade un bar
     */
    private fun mostrarNotificacion(nombreBar: String) {
        Log.d("Notificaciones", "🔔 Intentando mostrar notificación para el bar: $nombreBar")

        val intent = Intent(this, PrincipalActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, "BAR_CHANNEL")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Asegúrate de que tienes un ícono válido
            .setContentTitle("Nuevo Bar Añadido 🍻")
            .setContentText("Se ha añadido \"$nombreBar\" a la lista.")
            .setPriority(NotificationCompat.PRIORITY_HIGH) // 🔥 Prioridad ALTA para asegurarse de que se vea
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Se elimina al hacer clic

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
