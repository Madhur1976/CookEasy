package com.example.cookeasy


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    init {
        _fetchCategories()
    }
    private val _categoriesState = mutableStateOf(recipeState())

    val categoriesState: State<recipeState> = _categoriesState

    private fun _fetchCategories(){
        viewModelScope.launch {
            try{
                val response = recipeService.getCategories()
                _categoriesState.value = _categoriesState.value.copy(
                    loading = false,
                    list = response.categories,
                    error = null
                )
            }
            catch(e: Exception){
               _categoriesState.value = categoriesState.value.copy(
                   loading = false,
                   error = "An error occurred while fetching categories"
               )
            }
        }
    }
    data class recipeState(
        var loading: Boolean =true,
        val list: List<Category> = emptyList(),
        val error: String? = null
    )

}