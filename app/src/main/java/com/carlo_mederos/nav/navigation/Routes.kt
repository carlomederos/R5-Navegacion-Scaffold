package com.carlo_mederos.nav.navigation

import kotlinx.serialization.Serializable

/**
 * Rutas disponibles en la app.
 */
@Serializable
data object ListaRoute

/**
 * Pantalla de registro. Si se abre con argumentos (al tocar un producto de la lista),
 * los campos aparecen llenos para editarlo. Con el botón "+" se abre vacía.
 */
@Serializable
data class RegistroRoute(
    val id: String = "",
    val nombre: String = "",
    val precio: String = ""
) {
    val esEdicion: Boolean get() = id.isNotEmpty()
}

/** Clave usada para devolver el producto registrado a la lista. */
const val PRODUCTO_REGISTRADO = "producto_registrado"
