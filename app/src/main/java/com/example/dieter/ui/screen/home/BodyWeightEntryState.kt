package com.example.dieter.ui.screen.home

import com.example.dieter.ui.component.TextFieldState

class BodyWeightState : TextFieldState(validator = ::isBodyWeightValid, errorFor = ::bodyWeightValidationError)

private fun isBodyWeightValid(weight: String) = weight.toIntOrNull() ?: 0 > 40

// TODO: Might be offensive?
@Suppress("UNUSED_PARAMETER")
private fun bodyWeightValidationError(weight: String): String = "Bad input"
