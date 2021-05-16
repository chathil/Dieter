package com.example.dieter.ui.screen.search.ingredient

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dieter.DieterAppState
import com.example.dieter.R
import com.example.dieter.data.source.domain.IngredientModel
import com.example.dieter.ui.component.TextFieldError
import com.example.dieter.ui.component.TextFieldState
import com.example.dieter.ui.component.UpButton
import com.example.dieter.ui.screen.calculate.nutrients.IngredientSearchState
import com.example.dieter.ui.theme.DieterShapes
import com.example.dieter.ui.theme.DieterTheme
import com.example.dieter.vo.DataState
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.InternalCoroutinesApi

@OptIn(InternalCoroutinesApi::class)
@Composable
fun SearchIngredientScreen(
    viewModel: SearchIngredientViewModel,
    goUp: () -> Unit = {},
    done: (a: String) -> Unit = {},
    appState: DieterAppState
) {
    val ingredientsState by viewModel.state.collectAsState()
    val ingredientSearchState = remember { IngredientSearchState() }
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            UpButton(goUp)
            Text(
                "Search ingredient",
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .statusBarsPadding()
            )
        }
        IngredientSearch(
            modifier = Modifier.padding(16.dp),
            ingredientState = ingredientSearchState,
            onImeAction = {
                viewModel.searchIngredient(ingredientSearchState.text)
            }
        )
        when (ingredientsState) {
            is DataState.Success -> {
                (ingredientsState as DataState.Success<List<IngredientModel>>).data.forEach {
                    IngredientCard(
                        ingredientModel = it,
                        modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                    )
                }
                Spacer(Modifier.size(64.dp))
            }
            is DataState.Error -> {
                Text("Something Went Wrong")
                Log.e("SearchIngredientScreen", "SearchIngredientScreen: ${(ingredientsState as DataState.Error).exception}")
            }
            is DataState.Empty -> {
                Text("No Result")
            }
            is DataState.Loading -> {
                Text("Loading")
            }
        }
    }
}

@Composable
private fun IngredientCard(modifier: Modifier = Modifier, ingredientModel: IngredientModel) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        if (ingredientModel.image.isNullOrEmpty()) {
            Spacer(
                modifier = Modifier
                    .size(62.dp)
                    .clip(DieterShapes.medium)
                    .background(MaterialTheme.colors.primary)
            )
        } else {
            Image(
                painter = rememberGlidePainter(
                    request = ingredientModel.image,
                    previewPlaceholder = R.drawable.ic_launcher_background
                ),
                contentDescription = "image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(62.dp)
                    .clip(DieterShapes.medium)
            )
        }
        Spacer(Modifier.size(8.dp))
        Text(
            ingredientModel.label,
            style = MaterialTheme.typography.subtitle2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
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

@Preview
@Composable
private fun IngredientCardPreview() {
    DieterTheme {
        Row(modifier = Modifier.wrapContentSize()) {
            IngredientCard(
                ingredientModel = IngredientModel(
                    ")",
                    "Broccoli",
                    IngredientModel.NutrientSnippet(9f, 10f, 2f, 39f, 39f),
                    "Meal",
                    "Meal Label",
                    emptyList(),
                    null
                )
            )
        }
    }
}
