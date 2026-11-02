package com.carlo_mederos.nav

data class Producto(
    val id: String,
    val nombre: String,
    val precio: Double
)

fun Double.comoPrecio(): String = "$" + String.format(java.util.Locale.US, "%.2f", this)
