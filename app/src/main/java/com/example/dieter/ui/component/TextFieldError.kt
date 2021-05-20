package com.example.dieter.ui.component

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

/**
 * To be removed when [TextField]s support error
 */
@Composable
fun TextFieldError(textError: String) {
    Text(
        text = textError,
        style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.error)
    )
}
