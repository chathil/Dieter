package com.example.dieter.ui.screen.goal

import com.example.dieter.ui.component.TextFieldState

class AgeState : TextFieldState(validator = ::isAgeValid, errorFor = ::ageValidationError)

private fun isAgeValid(age: String) = (age.toIntOrNull() ?: 0 > 13) || (age.toIntOrNull() ?: 0 < 60)

@Suppress("UNUSED_PARAMETER")
private fun ageValidationError(age: String): String = "Might be too young or old"
