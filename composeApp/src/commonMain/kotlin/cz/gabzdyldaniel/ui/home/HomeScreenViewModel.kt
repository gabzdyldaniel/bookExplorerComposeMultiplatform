package cz.gabzdyldaniel.ui.home

import cz.gabzdyldaniel.data.models.Volume
import cz.gabzdyldaniel.data.repository.BookRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel {
    val repository = BookRepository()
    private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val state = _state.asStateFlow()

    fun fetchBooks(author: String) {
        CoroutineScope(Dispatchers.Main).launch {
            _state.value = HomeScreenState.Loading
            try {
                val result = repository.fetchBooks(author)
                _state.value = HomeScreenState.Success(result.items ?: emptyList())
            } catch (e: Exception) {
                HomeScreenState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class HomeScreenState {
    object Loading : HomeScreenState()
    data class Success(val data: List<Volume>) : HomeScreenState()
    data class Error(val message: String) : HomeScreenState()
}