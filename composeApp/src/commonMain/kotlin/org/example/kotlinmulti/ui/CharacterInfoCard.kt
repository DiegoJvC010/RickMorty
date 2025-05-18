package org.example.kotlinmulti.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.example.kotlinmulti.model.Character

//Colores usados en la tarjeta de personaje
object RickAndMortyColors {
    val PortalGreen = Color(0xFF97CE4C)    //Verde del portal
    val DarkBlue = Color(0xFF24325F)       //Azul oscuro de fondo
    val LightBlue = Color(0xFF00B5CC)      //Azul claro de seccion inferior
    val Yellow = Color(0xFFF0E14A)         //Amarillo para texto de especie
    val BackgroundDark = Color(0xFF262C3A) //Fondo oscuro seccion inferior
}

@Composable
fun CharacterInfoCard(character: Character) {
    // Tarjeta principal que envuelve el contenido
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        )
    ) {
        Column {
            //Primera seccion: imagen y datos principales
            Row(modifier = Modifier.height(150.dp)) {
                //Imagen (lado izquierdo) - Ancho fijo
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .fillMaxHeight()
                ) {
                    //Carga de imagen asyncrona con KamelImage
                    KamelImage(
                        resource = asyncPainterResource(character.image),
                        contentDescription = character.name,//descripcion para accesibilidad
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop, //recorta para llenar contenedor
                        onLoading = {
                            //Indicador de carga centrado
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)
                            ) },
                        onFailure = {
                            //Mostrar X si falla la carga
                            Text("❌", modifier = Modifier.align(Alignment.Center)
                            ) }
                    )
                }

                //Nombre, Estado y Especie (lado derecho)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(RickAndMortyColors.DarkBlue),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        //Nombre del personaje
                        Text(
                            text = character.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,  //Permitir hasta 2 líneas
                            overflow = TextOverflow.Ellipsis,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        //Fila con punto de color y estado
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                Modifier
                                    .size(8.dp)
                                    .background(getStatusColor(character.status), CircleShape)
                            )
                            Text(
                                text = character.status,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        //Especie (Raza) alineada al final
                        Text(
                            text = character.species,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = RickAndMortyColors.Yellow,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }

            //Segunda seccion: origen, ubicacion y tipo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(RickAndMortyColors.BackgroundDark)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    //Origen
                    InfoRow(
                        title = "Origen:",
                        value = character.origin.name,
                        color = RickAndMortyColors.PortalGreen
                    )

                    //Ubicacion
                    InfoRow(
                        title = "Ubicación:",
                        value = character.location.name,
                        color = RickAndMortyColors.LightBlue
                    )

                    //Tipo (solo si existe)
                    if (character.type.isNotEmpty()) {
                        InfoRow(
                            title = "Tipo:",
                            value = character.type,
                            color = RickAndMortyColors.Yellow
                        )
                    }
                }
            }
        }
    }
}

//Composable para mostrar un par titulo-valor en columna
@Composable
private fun InfoRow(title: String, value: String, color: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Color.White
        )
    }
}

//Determina el color segun estado
private fun getStatusColor(status: String): Color {
    return when (status.lowercase()) {
        "alive" -> RickAndMortyColors.PortalGreen// Verde para vivo
        "dead" -> Color(0xFFF44336)//Rojo para muerto
        else -> Color(0xFF9E9E9E)//Gris para desconocido
    }
}
