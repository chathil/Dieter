package com.example.dieter.ui.screen.calculate

import com.example.dieter.ui.component.TextFieldState

class MealNameState :
    TextFieldState(validator = ::isMealNameValid, errorFor = ::mealNameValidationError)

private fun isMealNameValid(name: String) = name.length > 3

@Suppress("UNUSED_PARAMETER")
private fun mealNameValidationError(name: String): String = "Wow, that's too short"
