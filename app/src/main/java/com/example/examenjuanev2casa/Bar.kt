package com.example.examenjuanev2casa

// Clase de datos (data class) que representa un Bar con sus atributos
// Se usa para almacenar y gestionar la información de los bares

data class Bar(
    var id: Int = 0, // Identificador único del bar (se inicializa en 0)
    var nombre_bar: String = "", // Nombre del bar
    var direccion: String = "", // Dirección del bar
    var valoracion: Float, // Valoración del bar (en escala flotante)
    var latitud: Float, // Coordenada de latitud para localización geográfica
    var longitud: Float, // Coordenada de longitud para localización geográfica
    var web_bar: String = "" // URL de la página web del bar
)
