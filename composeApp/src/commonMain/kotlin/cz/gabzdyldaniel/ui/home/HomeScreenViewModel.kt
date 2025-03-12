package cz.gabzdyldaniel.ui.home

import cz.gabzdyldaniel.data.models.Volume
import cz.gabzdyldaniel.data.repository.BookRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel {
    private val repository = BookRepository()
    private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Initial)
    val state = _state.asStateFlow()

    private val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun fetchBooks(author: String) {
        if (author.isBlank()) {
            _state.value = HomeScreenState.Error("Zadejte jméno autora")
            return
        }

        viewModelScope.launch {
            try {
                println("ViewModel: Začínám hledat knihy")
                _state.value = HomeScreenState.Loading

                try {
                    val result = repository.fetchBooks(author)
                    println("ViewModel: Data získána, items=${result.items?.size}")

                    val items = result.items ?: emptyList()
                    if (items.isEmpty()) {
                        _state.value = HomeScreenState.Success(emptyList())
                        println("ViewModel: Prázdný seznam")
                    } else {
                        _state.value = HomeScreenState.Success(items)
                        println("ViewModel: Úspěch, ${items.size} položek")
                    }
                } catch (e: Exception) {
                    println("ViewModel: Chyba v repository: ${e.message}")
                    e.printStackTrace()
                    _state.value = HomeScreenState.Error("Chyba při načítání dat: ${e.message}")
                }
            } catch (e: Exception) {
                println("ViewModel: Neošetřená výjimka: ${e.message}")
                e.printStackTrace()
                _state.value = HomeScreenState.Error("Neočekávaná chyba: ${e.message}")
            }
        }
    }

    fun onCleared() {
        viewModelScope.cancel()
    }
}

sealed class HomeScreenState {
    data object Initial : HomeScreenState()
    data object Loading : HomeScreenState()
    data class Success(val data: List<Volume>) : HomeScreenState()
    data class Error(val message: String) : HomeScreenState()
}