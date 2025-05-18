package org.example.kotlinmulti.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import kotlinx.coroutines.launch
import org.example.kotlinmulti.model.Character
import org.example.kotlinmulti.network.ApiClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen() {
    // Estados de filtro
    var nameFilter by remember { mutableStateOf("") }
    var statusFilter by remember { mutableStateOf<String?>(null) }

    // Estados de paginación
    var currentPage by remember { mutableStateOf(1) }
    var totalPages by remember { mutableStateOf(0) }

    // Lista de personajes y estado de carga
    var characters by remember { mutableStateOf<List<Character>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    // Determinar el tamaño de celda basado en el número de columnas deseado
    // En pantallas más grandes queremos menos columnas para que las tarjetas sean más anchas
    val cellSize = 300.dp  // Tamaño base para todas las plataformas

    val scope = rememberCoroutineScope()

    // Función para cargar una página específica
    fun loadPage(page: Int) {
        scope.launch {
            isLoading = true
            hasError = false
            try {
                val response = ApiClient.fetchPage(
                    page = page,
                    name = if (nameFilter.isBlank()) null else nameFilter,
                    status = statusFilter
                )
                characters = response.results
                totalPages = response.info.pages
                currentPage = page
            } catch (e: Exception) {
                hasError = true
                characters = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    // Cargar la primera página al inicio o cuando cambien los filtros
    LaunchedEffect(nameFilter, statusFilter) {
        loadPage(1)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rick and Morty Explorer") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CharacterFilterBar(
                name = nameFilter,
                onNameChange = { nameFilter = it },
                status = statusFilter,
                onStatusChange = { statusFilter = it }
            )

            Box(modifier = Modifier.fillMaxSize()) {
                // Mostrar el contenido principal cuando no está cargando
                if (!isLoading) {
                    if (hasError) {
                        ErrorMessage()
                    } else if (characters.isEmpty()) {
                        EmptyResultsMessage()
                    } else {
                        Column {
                            // Grid de personajes con tamaño fijo para las celdas
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = cellSize),
                                contentPadding = PaddingValues(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                items(characters) { character ->
                                    CharacterInfoCard(character)
                                }
                            }

                            // Controles de paginación
                            PaginationControls(
                                currentPage = currentPage,
                                totalPages = totalPages,
                                onPageChange = { loadPage(it) }
                            )
                        }
                    }
                }

                // Indicador de carga
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun PaginationControls(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón para ir a la primera página
        Button(
            onClick = { onPageChange(1) },
            enabled = currentPage > 1,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text("<<")
        }

        // Botón para página anterior
        Button(
            onClick = { onPageChange(currentPage - 1) },
            enabled = currentPage > 1,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text("<")
        }

        // Indicador de página actual
        Text(
            text = "$currentPage de $totalPages",
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Botón para página siguiente
        Button(
            onClick = { onPageChange(currentPage + 1) },
            enabled = currentPage < totalPages,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text(">")
        }

        // Botón para ir a la última página
        Button(
            onClick = { onPageChange(totalPages) },
            enabled = currentPage < totalPages,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text(">>")
        }
    }
}

@Composable
fun ErrorMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¡Ups! Ocurrió un error al cargar los personajes",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Intenta de nuevo más tarde o verifica tu conexión a internet",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EmptyResultsMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No se encontraron personajes",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Intenta con otros filtros de búsqueda",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}
