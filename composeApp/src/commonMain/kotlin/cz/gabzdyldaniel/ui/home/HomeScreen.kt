package cz.gabzdyldaniel.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.gabzdyldaniel.data.models.Volume

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Autor") },
            placeholder = { Text("Zadej jméno autora...") },
            trailingIcon = {
                IconButton(onClick = { viewModel.fetchBooks(searchQuery) }) {
                    Icon(Icons.Default.Search, contentDescription = "Hledat")
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { viewModel.fetchBooks(searchQuery) }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Content based on state
        when (state) {
            is HomeScreenState.Initial -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Zadejte jméno autora a stiskněte vyhledat",
                        style = MaterialTheme.typography.body1
                    )
                }
            }

            is HomeScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is HomeScreenState.Success -> {
                val books = (state as HomeScreenState.Success).data
                if (books.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Žádné knihy nebyly nalezeny")
                    }
                } else {
                    LazyColumn {
                        items(books) { book ->
                            BookItem(book)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            is HomeScreenState.Error -> {
                val errorMessage = (state as HomeScreenState.Error).message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Chyba: $errorMessage",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.fetchBooks(searchQuery) }) {
                            Text("Zkusit znovu")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookItem(book: Volume) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title
            Text(
                text = book.volumeInfo.title ?: "Neznámý název",
                style = MaterialTheme.typography.h6,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Authors
            Text(
                text = "Autor: ${book.volumeInfo.authors?.joinToString(", ") ?: "Neznámý autor"}",
                style = MaterialTheme.typography.body2
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description (if available)
            book.volumeInfo.description?.let { description ->
                Text(
                    text = description,
                    style = MaterialTheme.typography.body2,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Published date
            book.volumeInfo.publishedDate?.let {
                Text(
                    text = "Vydáno: $it",
                    style = MaterialTheme.typography.caption
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}