package com.example.cookeasy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cookeasy.ui.theme.CookEasyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CookEasyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                 AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(
        navController =  navController,
        startDestination = "homeScreen"
    ){
        composable ("homeScreen"){
            RecipeScreen (
                onCardClick = {description,categorySelected->
                    navController.navigate("descriptionScreen/$description/$categorySelected")
                }
            )
        }
         composable ("descriptionScreen/{description}/{categorySelected}"){
             val description = it.arguments?.getString("description")
             val categorySelected = it.arguments?.getString("categorySelected")
            CategoryDescriptionScreen (
               description = description,
                categorySelected = categorySelected,
                navigateToRecipeScreen = {mealSelected->
                    navController.navigate("recipeScreen/$mealSelected")
                }
            )
        }

        composable("recipeScreen/{mealSelected}") {
            val mealSelected = it.arguments?.getString("mealSelected")
            MealRecipeScreen(
                mealSelected = mealSelected
            )
        }


    }
}

