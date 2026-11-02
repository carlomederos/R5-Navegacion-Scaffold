package com.carlo_mederos.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carlo_mederos.nav.ui.theme.NavTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroProducto(
    idInicial: String,
    nombreInicial: String,
    precioInicial: String,
    esEdicion: Boolean,
    idDisponible: (String) -> Boolean,
    onRegistrar: (Producto) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var id by rememberSaveable { mutableStateOf(idInicial) }
    var nombre by rememberSaveable { mutableStateOf(nombreInicial) }
    var precio by rememberSaveable { mutableStateOf(precioInicial) }

    val precioNumero = precio.trim().toDoubleOrNull()
    val idRepetido = !esEdicion && id.isNotBlank() && !idDisponible(id.trim())
    val precioInvalido = precio.isNotBlank() && (precioNumero == null || precioNumero < 0)
    val formularioValido = id.isNotBlank() && !idRepetido &&
        nombre.isNotBlank() && precioNumero != null && !precioInvalido

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(if (esEdicion) "Editar producto" else "Registro de producto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    onRegistrar(Producto(id.trim(), nombre.trim(), precioNumero ?: 0.0))
                },
                enabled = formularioValido,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(24.dp)
            ) {
                Text(if (esEdicion) "Guardar cambios" else "Registrar")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = id,
                onValueChange = { id = it },
                label = { Text("ID") },
                singleLine = true,
                // Al editar, el ID no se cambia porque identifica al producto.
                enabled = !esEdicion,
                isError = idRepetido,
                supportingText = if (idRepetido) {
                    { Text("Ya existe un producto con este ID") }
                } else null,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                prefix = { Text("$") },
                singleLine = true,
                isError = precioInvalido,
                supportingText = if (precioInvalido) {
                    { Text("Escribe un precio válido, por ejemplo 99.50") }
                } else null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegistroProductoPreview() {
    NavTheme {
        RegistroProducto(
            idInicial = "",
            nombreInicial = "",
            precioInicial = "",
            esEdicion = false,
            idDisponible = { true },
            onRegistrar = {},
            onBack = {}
        )
    }
}
