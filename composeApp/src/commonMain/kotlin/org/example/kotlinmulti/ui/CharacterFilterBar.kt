package org.example.kotlinmulti.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CharacterFilterBar(
    name: String, //texto de busqueda
    onNameChange: (String) -> Unit, //callback cuando cambia el texto
    status: String?, //estado seleccionado o null para todos
    onStatusChange: (String?) -> Unit //callback cuando cambia el estado
) {
    //estado de scroll para los chips
    val scrollState = rememberScrollState()

    //contenedor principal con estilo de tarjeta
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)//color de fondo gris claro
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //campo de texto para buscar por nombre
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                placeholder = { Text("Buscar por nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    //icono de lupa
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar"
                    )
                },
                //boton de limpiar texto
                trailingIcon = {
                    if (name.isNotEmpty()) {
                        IconButton(onClick = { onNameChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpiar"
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp)
                // Eliminamos la configuración de colores para usar los valores por defecto
            )

            //fila para selector de estado
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                //etiqueta fija
                Text(
                    text = "Estado:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(end = 8.dp)
                )

                //contenedor de chips con scroll horizontal
                Row(
                    modifier = Modifier
                        .horizontalScroll(scrollState)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    //chip para "Todos"
                    SimpleFilterChip(
                        selected = status == null,
                        onClick = { onStatusChange(null) },
                        label = "Todos"
                    )

                    //chip para "Vivo"
                    SimpleFilterChip(
                        selected = status == "Alive",
                        onClick = { onStatusChange("Alive") },
                        label = "Vivo",
                        dotColor = Color(0xFF4CAF50)//punto verde
                    )

                    //chip para "Muerto"
                    SimpleFilterChip(
                        selected = status == "Dead",
                        onClick = { onStatusChange("Dead") },
                        label = "Muerto",
                        dotColor = Color(0xFFF44336)//punto rojo
                    )

                    //chip para "Desconocido"
                    SimpleFilterChip(
                        selected = status == "unknown",
                        onClick = { onStatusChange("unknown") },
                        label = "Desconocido",
                        dotColor = Color(0xFF9E9E9E)//punto gris
                    )
                }
            }
        }
    }
}

//composable reutilizable para cada chip de filtro
@Composable
fun SimpleFilterChip(
    selected: Boolean, //si el chip esta activo
    onClick: () -> Unit, //accion al pulsar
    label: String, //texto del chip
    dotColor: Color? = null //color del punto si aplica
) {
    Surface(
        modifier = Modifier.height(36.dp),
        onClick = onClick,
        shape = RoundedCornerShape(50),//esquinas redondas
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.White,
        contentColor = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else Color.Black,
        border = BorderStroke(1.dp, Color.LightGray)//borde fino gris
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            //si hay dotColor, dibuja un punto
            if (dotColor != null) {
                Box(
                    Modifier
                        .size(8.dp)
                        .background(dotColor, CircleShape)
                )
            }
            //texto del chip
            Text(label)
        }
    }
}
