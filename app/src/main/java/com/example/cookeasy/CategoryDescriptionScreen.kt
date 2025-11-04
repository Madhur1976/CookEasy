package com.example.cookeasy

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun CategoryDescriptionScreen(
    description: String?,
    categorySelected:String?,
    navigateToRecipeScreen: (String)-> Unit
) {
    val MealViewModel: MainViewModel = viewModel()
    val mealState by MealViewModel.mealsState
    MealViewModel.fetchMeals(categorySelected)


       /* Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
//                .verticalScroll(rememberScrollState())
        ) {
            Text("Description:", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            if (description != null) {
                Text(description.replace("\\[.*?\\]".toRegex(), ""))
            } else {
                Text("No Description ")
            }*/

//Spacer(Modifier.height(8.dp))


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // First static item = description
        item {
            Text("Description:", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            if (description != null) {
                Text(description.replace("\\[.*?\\]".toRegex(), ""))
            } else {
                Text("No Description ")
            }
            Spacer(Modifier.height(8.dp))
        }

        // Then the meal list
        items(mealState.list) { meal ->
            mealCard(meal,navigateToRecipeScreen)
        }
    }

}




@Composable
fun mealCard(
    meal: Meal,
    navigateToRecipeScreen: (String)-> Unit

){
    Card(

        onClick = {
            navigateToRecipeScreen(meal.strMeal)
        },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            ,
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(16.dp), // rounded corners
        colors = CardDefaults.cardColors(containerColor = Color.White) // background
    ){
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(meal.strMealThumb),
                contentDescription = meal.strMeal,
                modifier = Modifier.fillMaxSize().aspectRatio(1F)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = meal.strMeal,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
