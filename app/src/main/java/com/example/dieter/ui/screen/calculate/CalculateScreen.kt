package com.example.dieter.ui.screen.calculate

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dieter.DieterAppState
import com.example.dieter.data.source.domain.FoodType
import com.example.dieter.data.source.domain.NutrientType
import com.example.dieter.ui.component.Chip
import com.example.dieter.ui.component.DieterDefaultButton
import com.example.dieter.ui.component.TextFieldError
import com.example.dieter.ui.component.TextFieldState
import com.example.dieter.ui.component.UpButton
import com.example.dieter.ui.theme.AlphaReallyTransparent
import com.example.dieter.ui.theme.DieterShapes
import com.example.dieter.ui.theme.DieterTheme
import com.example.dieter.ui.theme.YellowPrimary
import com.example.dieter.utils.roundTo
import com.example.dieter.vo.DataState
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.insets.statusBarsPadding
import java.util.Locale

@Composable
fun CalculateScreen(
    viewModel: CalculateViewModel,
    goUp: () -> Unit = {},
    save: () -> Unit = {},
    appState: DieterAppState
) {
    val mealNameState = remember { MealNameState() }
    var foodType by remember { mutableStateOf<FoodType?>(null) }
    val saveFoodState by viewModel.saveFoodState.collectAsState()
    var progress by remember { mutableStateOf("") }
    val ingredients by appState.ingredientsState.collectAsState()
    val summary by viewModel.summaryState.collectAsState()
    val cautions by viewModel.cautions.collectAsState()
    val eachIngredients by viewModel.state.collectAsState()
    val uri by appState.photoUri.collectAsState()

    if (eachIngredients.isNullOrEmpty()) {
        viewModel.details(ingredients)
    }
    when (saveFoodState) {
        is DataState.Success -> save()
        is DataState.Loading -> if ((saveFoodState as DataState.Loading<Boolean>).data != null) progress =
            "Loading..."
        is DataState.Error -> progress = "Error..."
        is DataState.Empty -> {
        }
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                UpButton(goUp)
                Text(
                    "Nutrients Info",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .statusBarsPadding()
                )
                Spacer(Modifier.size(16.dp))
                Text(progress)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.size(16.dp))
                if (uri != null) {
                    Image(
                        contentScale = ContentScale.Inside,
                        painter = rememberGlidePainter(request = appState.photoUri),
                        modifier = Modifier
                            .size(86.dp)
                            .background(MaterialTheme.colors.primary),
                        contentDescription = "pic of food"
                    )
                } else {
                    Spacer(
                        Modifier
                            .size(86.dp)
                            .background(MaterialTheme.colors.primary)
                    )
                }
                Spacer(Modifier.size(8.dp))
                MealName(mealNameState = mealNameState, modifier = Modifier.padding(end = 16.dp))
            }
            Text(
                "This is a...",
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.padding(16.dp)
            )
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                Spacer(Modifier.size(16.dp))
                FoodType.values().forEach {
                    Chip(
                        selected = it == foodType,
                        content = {
                            Text(it.toString().toLowerCase(Locale.ROOT).capitalize(Locale.ROOT))
                        },
                        modifier = Modifier.clickable {
                            foodType = it
                        }
                    )
                }
                Spacer(Modifier.size(16.dp))
            }

            if (cautions.isNotEmpty())
                CautionCard(cautions = cautions)

            Spacer(Modifier.size(16.dp))
            if (summary.isEmpty())
                Text(
                    "No nutrients info available :(",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            else
                NutrientList(modifier = Modifier.padding(horizontal = 16.dp), nutrients = summary)
            Spacer(Modifier.size(16.dp))
            Text(
                "Nutrients for each ingredients",
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.size(8.dp))
            eachIngredients.forEach { (t, u) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(t.label, style = MaterialTheme.typography.subtitle2)
                    // TODO: Replace with real weight value
                    Text("-", style = MaterialTheme.typography.caption)
                }
                when (u) {
                    is DataState.Success -> {
                        if (u.data.totalNutrients.isEmpty()) {
                            Text(
                                "No nutrients info available :(",
                                style = MaterialTheme.typography.subtitle1,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        } else {
                            NutrientList(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                nutrients = u.data.totalNutrients
                            )
                        }
                    }
                    else -> { /*TODO: Do something*/
                    }
                }
            }
            Spacer(Modifier.size(112.dp))
        }
        SaveButton(
            modifier = Modifier.padding(bottom = 36.dp),
            enabled = (foodType != null) && mealNameState.text.isNotEmpty() && summary.isNotEmpty(),
            onClick = {
                viewModel.saveToFirebase(mealNameState.text, foodType!!)
            }
        )
    }
}

@Composable
private fun SaveButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
        colors = DieterDefaultButton.outlinedColors(),
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 16.dp)
    ) {
        Text("Save")
    }
}

@Composable
private fun MealName(
    modifier: Modifier = Modifier,
    mealNameState: TextFieldState = remember { MealNameState() },
    imeAction: ImeAction = ImeAction.None,
    onImeAction: () -> Unit = {}
) {
    Column {
        TextField(
            value = mealNameState.text,
            onValueChange = {
                mealNameState.text = it
            },
            label = {
                Text("Meal name")
            },
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    val focused = focusState == FocusState.Active
                    mealNameState.onFocusChange(focused)
                    if (!focused) {
                        mealNameState.enableShowErrors()
                    }
                },
            isError = mealNameState.showErrors(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
            keyboardActions = KeyboardActions(onSearch = { onImeAction() })
        )

        mealNameState.getError()?.let {
            TextFieldError(textError = it)
        }
    }
}

@Composable
private fun NutrientList(
    modifier: Modifier = Modifier,
    nutrients: Map<NutrientType, Float?> = emptyMap()
) {
    var expand by remember { mutableStateOf(false) }

    val top5 = mutableMapOf<NutrientType, Float?>()
    nutrients[NutrientType.ENERC_KCAL]?.let { top5.put(NutrientType.ENERC_KCAL, it) }
    nutrients[NutrientType.CHOCDF]?.let { top5.put(NutrientType.CHOCDF, it) }
    nutrients[NutrientType.FAT]?.let { top5.put(NutrientType.FAT, it) }
    nutrients[NutrientType.FIBTG]?.let { top5.put(NutrientType.FIBTG, it) }
    nutrients[NutrientType.PROCNT]?.let { top5.put(NutrientType.PROCNT, it) }

    val reordered =
        mapOf<NutrientType, Float?>().plus(nutrients - top5) as Map<NutrientType, Float?>

    Column(
        modifier = modifier.animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        top5.forEach { (t, u) ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(t.nutrientName, style = MaterialTheme.typography.caption)
                Text("${u?.roundTo(1)}${t.nutrientUnit} ", style = MaterialTheme.typography.caption)
            }
        }
        if (expand)
            reordered.forEach { (t, u) ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(t.nutrientName, style = MaterialTheme.typography.caption)
                    Text(
                        "${u?.roundTo(1)}${t.nutrientUnit} ",
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        IconButton(onClick = { expand = !expand }) {
            Icon(
                imageVector = if (expand) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "expand"
            )
        }
    }
}

@Composable
private fun CautionCard(modifier: Modifier = Modifier, cautions: Set<String>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                YellowPrimary.copy(alpha = AlphaReallyTransparent),
                shape = DieterShapes.small
            )
    ) {
        Icon(
            imageVector = Icons.Rounded.Warning,
            contentDescription = "caution",
            tint = YellowPrimary,
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp)
        )
        Column {
            Text("Cautions", style = MaterialTheme.typography.subtitle1)
            val cautionsString = cautions.joinToString {
                it.toLowerCase(Locale.ROOT).capitalize(Locale.ROOT)
            }.replace('_', ' ', true)
            Text(cautionsString, style = MaterialTheme.typography.body2)
        }
    }
}

@Preview
@Composable
private fun CautionCardPreview() {
    DieterTheme {
        CautionCard(cautions = setOf("Hello", "This", "Is", "Cautions"))
    }
}

@Preview
@Composable
private fun NutrientListPreview() {
    DieterTheme {
        NutrientList(
            nutrients = mapOf(
                NutrientType.ENERC_KCAL to 100f,
                NutrientType.CHOCDF to 5f,
                NutrientType.FAT to 5f,
                NutrientType.FIBTG to 10f,
                NutrientType.PROCNT to 10f
            )
        )
    }
}

@Preview
@Composable
private fun SaveButtonPreview() {
    DieterTheme {
        SaveButton()
    }
}
