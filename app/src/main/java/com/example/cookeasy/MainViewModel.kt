package com.example.cookeasy


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    init {
        fetchCategories()
    }
    private val _categoriesState = mutableStateOf(recipeState())
    private val _mealState = mutableStateOf(mealState())
    private val _recipeState = mutableStateOf(mealRecipeState())


    val categoriesState: State<recipeState> = _categoriesState
    val mealsState: State<mealState> = _mealState
    val RecipeState: State<mealRecipeState> = _recipeState
     fun fetchCategories(){
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

    fun fetchMeals(categorySelected: String?){
        viewModelScope.launch {
            try{
                val mealResponse = mealService.getMeals(categorySelected = categorySelected)
                _mealState.value = _mealState.value.copy(
                    loading = false,
                    list = mealResponse.meals,
                    error = null
                )
            }
            catch(e: Exception){
                _mealState.value = mealsState.value.copy(
                    loading = false,
                    error = "An error occurred while fetching categories"
                )
            }
        }
    }

    fun fetchRecipe(mealSelected: String?){
        viewModelScope.launch {
            try{
                val recipeResponse = RecipeService.getrecipe(mealSelected)

                _recipeState.value = _recipeState.value.copy(
                    loading = false,
                    list = recipeResponse.meals,
                    error=null
                )

            }catch(e: Exception){
                _recipeState.value = _recipeState.value.copy(
                    loading = false,

                    error=null
                )
            }
        }
    }



    data class recipeState(
        var loading: Boolean =true,
        val list: List<Category> = emptyList(),
        val error: String? = null,

    )

     data class mealState(
         var loading: Boolean =true,
         val list: List<Meal> = emptyList(),
         val error: String? = null,

    )

data class mealRecipeState(
    var loading: Boolean =true,
    val list: List<Meals> = emptyList(),
    val error: String? = null,

    )






}