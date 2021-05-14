package com.example.dieter.ui.screen.calculate.nutrients

import com.example.dieter.ui.component.TextFieldState

class IngredientSearchState : TextFieldState(validator = ::isIngredientValid, errorFor = ::ingredientValidationError)

private fun isIngredientValid(ingredient: String) = ingredient.length > 3

@Suppress("UNUSED_PARAMETER")
private fun ingredientValidationError(ingredient: String): String = "Wow, that's too short"
