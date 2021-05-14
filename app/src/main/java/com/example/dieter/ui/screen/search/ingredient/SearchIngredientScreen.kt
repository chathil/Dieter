package com.example.dieter.ui.screen.search.ingredient

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dieter.ui.component.TextFieldError
import com.example.dieter.ui.component.TextFieldState
import com.example.dieter.ui.component.UpButton
import com.example.dieter.ui.screen.calculate.nutrients.IngredientSearchState
import com.example.dieter.ui.theme.DieterTheme

@Composable
private fun SearchIngredientScreen(
    viewModel: SearchIngredientViewModel,
    goUp: () -> Unit = {},
    done: (a: String) -> Unit = {}
) {
    Column {
        Row {
            UpButton(goUp)
            Text("Search ingredient", style = MaterialTheme.typography.h6)
        }
        IngredientSearch(modifier = Modifier.padding(16.dp))
    }
}

@Composable
private fun IngredientSearch(
    modifier: Modifier = Modifier,
    ingredientState: TextFieldState = remember { IngredientSearchState() },
    imeAction: ImeAction = ImeAction.Search,
    onImeAction: () -> Unit = {}
) {
    OutlinedTextField(
        value = ingredientState.text,
        onValueChange = {
            ingredientState.text = it
        },
        label = {
            Text("Ingredient name", style = MaterialTheme.typography.body1)
        },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                val focused = focusState == FocusState.Active
                ingredientState.onFocusChange(focused)
                if (!focused) {
                    ingredientState.enableShowErrors()
                }
            },
        isError = ingredientState.showErrors(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(onSearch = { onImeAction() })
    )
    ingredientState.getError()?.let {
        TextFieldError(textError = it)
    }
}

@Preview
@Composable
private fun IngredientSearchPreview() {
    DieterTheme {
        Surface {
            IngredientSearch()
        }
    }
}
