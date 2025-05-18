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

// Colores representativos de Rick and Morty
object RickAndMortyColors {
    val PortalGreen = Color(0xFF97CE4C)      // Verde del portal
    val DarkBlue = Color(0xFF24325F)         // Azul oscuro del espacio
    val LightBlue = Color(0xFF00B5CC)        // Azul claro del portal
    val Yellow = Color(0xFFF0E14A)           // Amarillo de las pupilas
    val BackgroundDark = Color(0xFF262C3A)   // Fondo oscuro
}

@Composable
fun CharacterInfoCard(character: Character) {
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
            // Sección superior: Imagen y Nombre/Estado
            Row(modifier = Modifier.height(150.dp)) {
                // Imagen (lado izquierdo) - Ancho fijo en lugar de proporcional
                Box(
                    modifier = Modifier
                        .width(120.dp)  // Ancho fijo para la imagen
                        .fillMaxHeight()
                ) {
                    KamelImage(
                        resource = asyncPainterResource(character.image),
                        contentDescription = character.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        onLoading = { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) },
                        onFailure = { Text("❌", modifier = Modifier.align(Alignment.Center)) }
                    )
                }

                // Nombre, Estado y Especie (lado derecho) - Con fondo azul oscuro de Rick and Morty
                Column(
                    modifier = Modifier
                        .fillMaxWidth()  // Ocupa el resto del espacio disponible
                        .fillMaxHeight()
                        .background(RickAndMortyColors.DarkBlue),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Nombre
                        Text(
                            text = character.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,  // Permitir hasta 2 líneas
                            overflow = TextOverflow.Ellipsis,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Estado
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

                        // Especie (Raza)
                        Spacer(modifier = Modifier.weight(1f))
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

            // Sección inferior: Información adicional - Con color verde portal de Rick and Morty
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(RickAndMortyColors.BackgroundDark)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Origen
                    InfoRow(title = "Origen:", value = character.origin.name, color = RickAndMortyColors.PortalGreen)

                    // Ubicación
                    InfoRow(title = "Ubicación:", value = character.location.name, color = RickAndMortyColors.LightBlue)

                    // Tipo (solo si existe)
                    if (character.type.isNotEmpty()) {
                        InfoRow(title = "Tipo:", value = character.type, color = RickAndMortyColors.Yellow)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(title: String, value: String, color: Color) {
    // Cambiamos de Row a Column para mostrar el título y valor en líneas separadas
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
            maxLines = 2,  // Permitir hasta 2 líneas para el valor
            overflow = TextOverflow.Ellipsis,
            color = Color.White
        )
    }
}

private fun getStatusColor(status: String): Color {
    return when (status.lowercase()) {
        "alive" -> RickAndMortyColors.PortalGreen  // Verde del portal para vivo
        "dead" -> Color(0xFFF44336)                // Rojo para muerto
        else -> Color(0xFF9E9E9E)                  // Gris para desconocido
    }
}
