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
import kotlinx.coroutines.launch
import org.example.kotlinmulti.model.Character
import org.example.kotlinmulti.network.ApiClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen() {
    //Estado para filtro por nombre (texto de busqueda)
    var nameFilter by remember { mutableStateOf("") }
    //Estado para filtro por status (Alive, Dead, unknown o null para todos)
    var statusFilter by remember { mutableStateOf<String?>(null) }

    //Estado de la pagina actual
    var currentPage by remember { mutableStateOf(1) }
    //Total de paginas disponibles segun la respuesta de la API
    var totalPages by remember { mutableStateOf(0) }

    //Lista de personajes para mostrar en la pantalla
    var characters by remember { mutableStateOf<List<Character>>(emptyList()) }
    //Indicador de carga (si aun se esta obteniendo datos)
    var isLoading by remember { mutableStateOf(true) }
    //Indicador de error al cargar datos
    var hasError by remember { mutableStateOf(false) }

    //Tamano base de cada celda en el grid
    val cellSize = 300.dp  // Tamaño base para todas las plataformas

    //Scope de corrutinas para llamadas asincronas
    val scope = rememberCoroutineScope()

    //Funcion interna que carga una pagina especifica desde la API
    fun loadPage(page: Int) {
        scope.launch {
            isLoading = true//indicar que empieza la carga
            hasError = false//resetear indicador de error
            try {
                //Llamar a ApiClient.fetchPage con filtros
                val response = ApiClient.fetchPage(
                    page = page,
                    name = if (nameFilter.isBlank()) null else nameFilter,
                    status = statusFilter
                )
                //Actualizar lista de personajes y datos de paginacion
                characters = response.results
                totalPages = response.info.pages
                currentPage = page
            } catch (e: Exception) {
                //En caso de error, mostrar pantalla de error y limpiar lista
                println("Excepcion***********--------*******: ${e::class.simpleName} – ${e.message}")
                hasError = true
                characters = emptyList()
            } finally {
                //Terminar indicador de carga
                isLoading = false
            }
        }
    }

    //Cargar la primera pagina al inicio o cuando cambien los filtros
    LaunchedEffect(nameFilter, statusFilter) {
        loadPage(1)
    }

    //Estructura base de la pantalla con barra superior
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
            //Barra de filtros (nombre y status)
            CharacterFilterBar(
                name = nameFilter,
                onNameChange = { nameFilter = it },
                status = statusFilter,
                onStatusChange = { statusFilter = it }
            )

            Box(modifier = Modifier.fillMaxSize()) {
                //Mostrar el contenido principal cuando no esta cargando
                if (!isLoading) {
                    if (hasError) {
                        //Mostrar mensaje de error
                        ErrorMessage()
                    } else if (characters.isEmpty()) {
                        //Mostrar mensaje si no hay resultados
                        EmptyResultsMessage()
                    } else {
                        //Mostrar lista de personajes en un grid
                        Column {
                            // Grid de personajes
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = cellSize),
                                contentPadding = PaddingValues(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                items(characters) { character ->
                                    CharacterInfoCard(character)
                                }
                            }

                            //Controles de paginacion (primera, anterior, siguiente, ultima)
                            PaginationControls(
                                currentPage = currentPage,
                                totalPages = totalPages,
                                onPageChange = { loadPage(it) }
                            )
                        }
                    }
                }

                //Indicador de carga centrado
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

//Composable que muestra los botones de paginacion
@Composable
fun PaginationControls(
    currentPage: Int,//pagina actual
    totalPages: Int,//numero total de paginas
    onPageChange: (Int) -> Unit//callback para cambiar de pagina
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Boton para ir a la primera pagina
        Button(
            onClick = { onPageChange(1) },
            enabled = currentPage > 1,//habilitado solo si no estamos en la pagina 1
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text("<<")
        }

        //Boton para ir a la pagina anterior
        Button(
            onClick = { onPageChange(currentPage - 1) },
            enabled = currentPage > 1,//habilitado solo si hay pagina anterior
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text("<")
        }

        //Muestra la pagina actual y total de paginas
        Text(
            text = "$currentPage de $totalPages",
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        //Boton para ir a la pagina siguiente
        Button(
            onClick = { onPageChange(currentPage + 1) },
            enabled = currentPage < totalPages,//habilitado solo si no estamos en la ultima pagina
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text(">")
        }

        //Boton para ir a la ultima pagina
        Button(
            onClick = { onPageChange(totalPages) },
            enabled = currentPage < totalPages,//habilitado solo si hay pagina siguiente
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text(">>")
        }
    }
}

//Muestra un mensaje de error cuando falla la carga de personajes
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
            text = "¡Ups! Ocurrió un error al cargar los personajes",//mensaje principal
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Intenta de nuevo más tarde o verifica tu conexión a internet",//mensaje secundario
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

//Muestra un mensaje cuando no hay resultados que mostrar
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
            text = "No se encontraron personajes",//mensaje principal
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Intenta con otros filtros de búsqueda",//mensaje secundario
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}
