package com.example.dieter.ui.screen.goal

import com.example.dieter.ui.component.TextFieldState

class WeightState : TextFieldState(validator = ::isWeightValid, errorFor = ::weightValidationError)

private fun isWeightValid(weight: String) = weight.toIntOrNull() ?: 0 > 40

// TODO: Might be offensive?
@Suppress("UNUSED_PARAMETER")
private fun weightValidationError(weight: String): String = "Bad input"

class HeightState : TextFieldState(validator = ::isHeightValid, errorFor = ::heightValidationError)

private fun isHeightValid(height: String) = height.toIntOrNull() ?: 0 > 120

// TODO: Might be offensive?
@Suppress("UNUSED_PARAMETER")
private fun heightValidationError(height: String): String = "Bad input"

class TargetWeightState : TextFieldState(validator = ::isWeightValid, errorFor = ::weightValidationError)
