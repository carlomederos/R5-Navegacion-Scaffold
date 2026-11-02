package com.carlo_mederos.nav.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.carlo_mederos.nav.ListaProductos
import com.carlo_mederos.nav.Producto
import com.carlo_mederos.nav.ProductosViewModel
import com.carlo_mederos.nav.RegistroProducto

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    productosViewModel: ProductosViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = ListaRoute,
        modifier = modifier
    ) {
        composable<ListaRoute> { backStackEntry ->
            // Recibe el producto que devuelve la pantalla de registro (Registro -> Lista).
            val handle = backStackEntry.savedStateHandle
            val resultado by handle
                .getStateFlow<ArrayList<String>?>(PRODUCTO_REGISTRADO, null)
                .collectAsState()

            LaunchedEffect(resultado) {
                resultado?.let { (id, nombre, precio) ->
                    productosViewModel.guardar(Producto(id, nombre, precio.toDouble()))
                    handle.remove<ArrayList<String>>(PRODUCTO_REGISTRADO)
                }
            }

            ListaProductos(
                productos = productosViewModel.productos,
                // Botón "+": abre el registro vacío.
                onAgregar = { navController.navigate(RegistroRoute()) },
                // Tocar un producto: abre el registro pasando sus datos como argumentos (Lista -> Registro).
                onSeleccionar = { producto ->
                    navController.navigate(
                        RegistroRoute(
                            id = producto.id,
                            nombre = producto.nombre,
                            precio = producto.precio.toString()
                        )
                    )
                }
            )
        }

        composable<RegistroRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<RegistroRoute>()
            RegistroProducto(
                idInicial = route.id,
                nombreInicial = route.nombre,
                precioInicial = route.precio,
                esEdicion = route.esEdicion,
                idDisponible = { id -> !productosViewModel.existeId(id) },
                onRegistrar = { producto ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        PRODUCTO_REGISTRADO,
                        arrayListOf(producto.id, producto.nombre, producto.precio.toString())
                    )
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
