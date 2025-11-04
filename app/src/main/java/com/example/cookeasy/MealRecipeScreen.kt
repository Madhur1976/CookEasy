package com.example.cookeasy


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun MealRecipeScreen(
    mealSelected: String?
) {
    val recipeViewModel: MainViewModel = viewModel()
    val recipeState by recipeViewModel.RecipeState

    // 1. Use LaunchedEffect to fetch the data safely and only once.
    LaunchedEffect(mealSelected) {
        if (!mealSelected.isNullOrBlank()) {
            recipeViewModel.fetchRecipe(mealSelected)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 2. Check the state before trying to display the UI.
        when {
            recipeState.loading -> {
                CircularProgressIndicator() // Show a loading spinner.
            }

            recipeState.error != null -> {
                Text(text = "Error: ${recipeState.error}") // Show an error message.
            }

            recipeState.list.isNotEmpty() -> {
                // If the list is not empty, it's safe to display the recipe.
                val recipe = recipeState.list.first()
                RecipeDetailView(recipe = recipe)
            }
        }
    }
}

@Composable
fun RecipeDetailView(recipe: Meals) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Meal Image
        item {
            Image(
                painter = rememberAsyncImagePainter(model = recipe.strMealThumb),
                contentDescription = recipe.strMeal,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
        }

        // Meal Title and Category
        item {
            Text(
                text = recipe.strMeal,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${recipe.strArea} | ${recipe.strCategory}",
                style = MaterialTheme.typography.bodyMedium
            )
            if (!recipe.strTags.isNullOrBlank()) {
                Text(
                    text = "Tags: ${recipe.strTags}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
             /*if (!recipe.strYoutube.isNullOrBlank()) {
                 SelectionContainer {
                     Text(
                     text = "YouTube: ${recipe.strYoutube}",
                     style = MaterialTheme.typography.bodySmall
                 )  }

            }*/

          /*  if (!recipe.strYoutube.isNullOrBlank()) {
                val uriHandler = LocalUriHandler.current
                val linkText = "${recipe.strYoutube}"

                ClickableText(
                    text = AnnotatedString(
                        text = {
                            Row{
                                Text(text = "Youtube")
                                Text(text = recipe.strYoutube)

                            }
                        },
                        spanStyle = SpanStyle(
                            color = Color.Blue
                        )

                    ),

                    style = MaterialTheme.typography.bodySmall,
                    onClick = {
                        uriHandler.openUri(recipe.strYoutube)
                    }
                )
            }*/

            if (!recipe.strYoutube.isNullOrBlank()) {
                val uriHandler = LocalUriHandler.current
                // Use buildAnnotatedString to create text with different styles
                val annotatedLinkString = buildAnnotatedString {
                    append("Youtube: ") // The non-clickable part

                    // Tag the URL part so we can identify it in onClick
                    pushStringAnnotation(tag = "URL", annotation = recipe.strYoutube)

                    // Apply a link-like style only to the URL
                    withStyle(style = SpanStyle(
                        color = Color.Blue, // This is androidx.compose.ui.graphics.Color

                    )) {
                        append(recipe.strYoutube) // The clickable part
                    }

                    pop() // Pop the annotation
                }

                ClickableText(
                    text = annotatedLinkString,
                    style = MaterialTheme.typography.bodySmall,
                    onClick = { offset ->
                        // This ensures we only trigger the action when the URL part is clicked
                        annotatedLinkString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                            .firstOrNull()?.let { annotation ->
                                uriHandler.openUri(annotation.item) // Open the URI from the annotation
                            }
                    }
                )
            }



        }

        // Instructions Section
        item {
            Text(
                text = "Instructions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            Text(
                text = recipe.strInstructions,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}



