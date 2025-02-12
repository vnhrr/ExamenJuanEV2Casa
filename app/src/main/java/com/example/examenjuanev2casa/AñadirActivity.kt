package com.example.examenjuanev2casa

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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

        // Inicializar el controlador de la base de datos
        dbHandler = ManejoBBDD(this)

        val incluir = findViewById<Button>(R.id.btIncluir)
        incluir.setOnClickListener{
            if (addBar()){
                Toast.makeText(applicationContext, "Bar añadido", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(applicationContext, "Ocurrio un error", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
    * Método para agregar un nuevo Gato a la base de datos.
    * @return `true` si el gato se agregó correctamente, `false` si hubo un error.
    */
    private fun addBar(): Boolean {
        val nombre = etNombre.text.toString()
        val direccion = etDireccion.text.toString()
        val latitud = etLatitud.text.toString()
        val longitud = etLongitud.text.toString()
        val web = etWeb.text.toString()

        // Verificar que los campos no estén vacíos
        if (nombre.isNotEmpty() && direccion.isNotEmpty() && latitud.isNotEmpty() && longitud.isNotEmpty() && web.isNotEmpty()) {
            val bar = Bar(nombre_bar = nombre, direccion = direccion, valoracion = 0f, latitud = latitud.toFloat(), longitud = longitud.toFloat(), web_bar = web.toString())
            val status = dbHandler.addBar(bar)

            if (status > -1) {
                Toast.makeText(applicationContext, "Bar añadido correctamente", Toast.LENGTH_LONG).show()
                return true
            }
        } else {
            Toast.makeText(applicationContext, "Todos los datos son requeridos", Toast.LENGTH_LONG).show()
            return false
        }
        return false
    }
}