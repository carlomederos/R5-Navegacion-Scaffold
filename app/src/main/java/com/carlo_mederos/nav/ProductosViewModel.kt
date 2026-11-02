package com.carlo_mederos.nav

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

/**
 * Guarda la lista de productos mientras la app está abierta.
 * Al estar en un ViewModel, la lista no se pierde al navegar ni al girar la pantalla.
 */
class ProductosViewModel : ViewModel() {

    private val _productos = mutableStateListOf(
        Producto(id = "001", nombre = "Teclado", precio = 450.0),
        Producto(id = "002", nombre = "Mouse", precio = 199.9),
        Producto(id = "003", nombre = "Monitor", precio = 3299.0)
    )
    val productos: List<Producto> get() = _productos

    /** Agrega el producto, o lo actualiza si ya existe uno con el mismo ID. */
    fun guardar(producto: Producto) {
        val indice = _productos.indexOfFirst { it.id == producto.id }
        if (indice >= 0) _productos[indice] = producto else _productos.add(producto)
    }

    fun existeId(id: String): Boolean = _productos.any { it.id == id }
}
