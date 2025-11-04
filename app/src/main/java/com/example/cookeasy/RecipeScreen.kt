package com.example.cookeasy

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@Composable
fun RecipeScreen(
    modifier: Modifier = Modifier,
    onCardClick: (String, String) -> Unit

){
    val recipeViewModel: MainViewModel = viewModel()
    val viewState by recipeViewModel.categoriesState
    val mealState by recipeViewModel.mealsState
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        when{
            viewState.loading ->{
                CircularProgressIndicator(Modifier.align ( Alignment.Center ), color = Color.Cyan)
            }
            viewState.error != null ->{

                    Column(
                        modifier= Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {   Text("${viewState.error}")
                        Button(
                            onClick = {
                                recipeViewModel.fetchCategories()
                            },

                        ) {
                            Text("Retry")
                        }

                }
            }
            else ->{
                // Display Categories
                CategoryScreen(viewState.list, viewState,
                    onCardClick,
                )
            }
        }
    }
}

@Composable
fun CategoryScreen(

    categories: List<Category>,
    viewState: MainViewModel.recipeState,
    onCardClick: (String, String) -> Unit

){
    LazyVerticalGrid(GridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {
      items(categories){category->
          CategoryItem(category,viewState, onCardClick ,)
      }
    }
}



// Defines how each item looks like
@Composable
fun CategoryItem(

    category: Category,
    viewState: MainViewModel.recipeState,
    onCardClick: (String, String) -> Unit

)

{

    Card(
        onClick = {

            onCardClick(category.strCategoryDescription,category.strCategory)

        },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(16.dp), // rounded corners
        colors = CardDefaults.cardColors(containerColor = Color.White) // background
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(category.strCategoryThumb),
                contentDescription = category.strCategory,
                modifier = Modifier.fillMaxSize().aspectRatio(1F)
            )


            Text(
                text = category.strCategory,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }


}