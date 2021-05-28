package com.example.dieter.ui.screen.goal

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dieter.data.source.domain.GoalType
import com.example.dieter.ui.component.AppNameHeader
import com.example.dieter.ui.component.DieterDefaultButton
import com.example.dieter.ui.component.TextFieldError
import com.example.dieter.ui.component.TextFieldState
import com.example.dieter.ui.theme.DieterShapes
import com.example.dieter.ui.theme.DieterTheme
import com.example.dieter.ui.theme.GreenPrimary
import com.example.dieter.ui.theme.Purple500
import com.example.dieter.vo.DataState
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun GoalScreen(
    viewModel: GoalViewModel,
    goUp: () -> Unit = {},
    goHome: () -> Unit = {},
    userRepId: String
) {
    val pagerState = rememberPagerState(pageCount = 2)
    val heightState = remember { HeightState() }
    val weightState = remember { WeightState() }
    val ageState = remember { AgeState() }
    val targetWeightState = remember { WeightState() }
    val selectedGoal = remember { mutableStateOf<GoalType?>(null) }
    var isMale by remember { mutableStateOf<Boolean?>(null) }
    val savingGoalState by viewModel.saveGoalState.collectAsState()
    var savingGoalMessage by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .statusBarsPadding()
            .background(MaterialTheme.colors.background)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.size(24.dp))
        AppNameHeader()
        when (savingGoalState) {
            is DataState.Success -> goHome()
            is DataState.Error -> savingGoalMessage = "Something went wrong"
            is DataState.Loading -> savingGoalMessage = "Saving..."
            else -> {
            }
        }
        if (savingGoalMessage.isNotEmpty())
            Text(savingGoalMessage)
        HorizontalPager(
            state = pagerState,
            dragEnabled = false,
            modifier = Modifier.padding(16.dp)
        ) { page ->
            when (page) {
                0 -> Slide1(
                    selectedGoal = selectedGoal,
                    heightState = heightState,
                    weightState = weightState
                )
                1 -> Slide2(
                    ageState = ageState,
                    targetWeightState = targetWeightState,
                    onSexSelected = { isMale = it }
                )
            }
        }
        val scrollScope = rememberCoroutineScope()
        BottomNavigation(
            later = goUp,
            next = {
                when (pagerState.currentPage) {
                    0 -> scrollScope.launch {
                        pagerState.scrollToPage(1)
                    }
                    1 -> selectedGoal.value?.let {
                        viewModel.save(
                            userRepId = userRepId,
                            selectedGoal = it,
                            age = ageState.text.toInt(),
                            isMale = isMale ?: true,
                            heightState.text.toInt(),
                            weightState.text.toInt(),
                            targetWeightState.text.toInt()
                        )
                    }
                }
            },
            isNextEnabled = when (pagerState.currentPage) {
                0 -> (selectedGoal.value != null) and (weightState.text.isNotEmpty()) and (weightState.getError() == null) and (heightState.text.isNotEmpty()) and (heightState.getError() == null)
                1 -> (isMale != null) and (ageState.text.isNotEmpty()) and (ageState.getError() == null)
                else -> false
            }
        )
    }
}

@Composable
private fun Slide1(
    modifier: Modifier = Modifier,
    selectedGoal: MutableState<GoalType?>,
    weightState: WeightState = remember { WeightState() },
    heightState: HeightState = remember { HeightState() }
) {
    Column {
        Text("I want to...", style = MaterialTheme.typography.h6)
        Spacer(Modifier.size(12.dp))
        IWantToCard(
            color = MaterialTheme.colors.primary,
            title = "Loose weight",
            subTitle = "duh… write something",
            isSelected = selectedGoal.value == GoalType.LOOSE_WEIGHT,
            modifier = Modifier.clickable { selectedGoal.value = GoalType.LOOSE_WEIGHT }
        )
        Spacer(Modifier.size(8.dp))
        IWantToCard(
            color = GreenPrimary,
            title = "Maintain weight",
            subTitle = "this is a fake subtitle",
            isSelected = selectedGoal.value == GoalType.MAINTAIN_WEIGHT,
            modifier = Modifier.clickable { selectedGoal.value = GoalType.MAINTAIN_WEIGHT }
        )
        Spacer(Modifier.size(8.dp))
        IWantToCard(
            color = MaterialTheme.colors.primary,
            title = "Increase weight",
            subTitle = "lorem ipsum something...",
            isSelected = selectedGoal.value == GoalType.INCREASE_WEIGHT,
            modifier = Modifier.clickable { selectedGoal.value = GoalType.INCREASE_WEIGHT }
        )
        Spacer(Modifier.size(8.dp))
        BMICalc(heightState = heightState, weightState = weightState)
    }
}

@Composable
fun Slide2(
    onSexSelected: (Boolean) -> Unit,
    ageState: AgeState = remember { AgeState() },
    targetWeightState: WeightState = remember { WeightState() }
) {
    Column {
        Text(
            "Extra information for the best recommendation…", style = MaterialTheme.typography.h6
        )
        Spacer(Modifier.size(12.dp))
        Surface(
            border = BorderStroke(2.dp, MaterialTheme.colors.primary),
            modifier = Modifier
                .clip(DieterShapes.small)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Age", style = MaterialTheme.typography.subtitle2)
                Text(
                    "To help for a better calorie intake recommendation",
                    style = MaterialTheme.typography.caption
                )
                Age(ageState = ageState)
            }
        }
        TargetWeight(targetWeightState = targetWeightState)
        Spacer(Modifier.size(8.dp))
        SexCard(onSexSelected = onSexSelected)
    }
}

@Composable
private fun Age(
    modifier: Modifier = Modifier,
    ageState: TextFieldState = remember { AgeState() },
    imeAction: ImeAction = ImeAction.None,
) {
    TextField(
        value = ageState.text,
        onValueChange = {
            ageState.text = it
        },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                val focused = focusState == FocusState.Active
                ageState.onFocusChange(focused)
                if (!focused) {
                    ageState.enableShowErrors()
                }
            },
        isError = ageState.showErrors(),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = KeyboardType.Number
        ),
    )
    ageState.getError()?.let {
        TextFieldError(textError = it)
    }
}

@Composable
private fun IWantToCard(
    modifier: Modifier = Modifier,
    color: Color,
    title: String,
    subTitle: String,
    isSelected: Boolean = false
) {
    Surface(
        border = BorderStroke(2.dp, color),
        color = if (isSelected) color else MaterialTheme.colors.surface,
        modifier = modifier
            .clip(DieterShapes.small)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.subtitle2)
            Text(subTitle, style = MaterialTheme.typography.caption)
        }
    }
}

@Composable
private fun BMICalc(
    modifier: Modifier = Modifier,
    weightState: WeightState = remember { WeightState() },
    heightState: HeightState = remember { HeightState() }
) {
    Surface(
        border = BorderStroke(2.dp, Purple500),
        modifier = modifier
            .clip(DieterShapes.small)
            .fillMaxWidth()
    ) {
        Column {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Weight & Height", style = MaterialTheme.typography.subtitle2)
                Text(
                    "let’s use BMI calculator to help you decide",
                    style = MaterialTheme.typography.caption
                )
            }
            Row {
                HeightWeightField(
                    state = heightState,
                    modifier = Modifier.width(144.dp)
                ) {
                    Text("Height-cm")
                }
                Spacer(Modifier.size(16.dp))
                HeightWeightField(
                    state = weightState,
                    modifier = Modifier.width(144.dp)
                ) {
                    Text("Weight-cm")
                }
            }
        }
    }
}

@Composable
private fun HeightWeightField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    imeAction: ImeAction = ImeAction.None,
    label: @Composable (() -> Unit)?
) {
    Column {
        TextField(
            value = state.text,
            onValueChange = {
                state.text = it
            },
            label = {
                if (label != null) {
                    label()
                }
            },
            modifier = modifier
                .onFocusChanged { focusState ->
                    val focused = focusState == FocusState.Active
                    state.onFocusChange(focused)
                    if (!focused) {
                        state.enableShowErrors()
                    }
                },
            isError = state.showErrors(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction,
                keyboardType = KeyboardType.Number
            )
        )
        state.getError()?.let {
            TextFieldError(textError = it)
        }
    }
}

@Composable
private fun SexCard(modifier: Modifier = Modifier, onSexSelected: (Boolean) -> Unit = {}) {
    var isMale by remember { mutableStateOf<Boolean?>(null) }
    Surface(
        border = BorderStroke(2.dp, Purple500),
        modifier = modifier
            .clip(DieterShapes.small)
            .fillMaxWidth()
    ) {
        Column {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Sex", style = MaterialTheme.typography.subtitle2)
                Text(
                    "To help for a better calorie intake recommendation",
                    style = MaterialTheme.typography.caption
                )
            }
            Row {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(
                            if (isMale == true) MaterialTheme.colors.primary else MaterialTheme.colors.background,
                            DieterShapes.small
                        )
                        .border(2.dp, MaterialTheme.colors.primary, DieterShapes.small)
                        .clickable {
                            isMale = true
                            onSexSelected(true)
                        }
                ) {
                    Text(
                        "Male",
                        style = MaterialTheme.typography.button,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(
                            if (isMale == false) MaterialTheme.colors.primary else MaterialTheme.colors.background,
                            DieterShapes.small
                        )
                        .border(2.dp, MaterialTheme.colors.primary, DieterShapes.small)
                        .clickable {
                            isMale = false
                            onSexSelected(false)
                        }
                ) {
                    Text(
                        "Female",
                        style = MaterialTheme.typography.button,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavigation(
    modifier: Modifier = Modifier,
    currentPage: Int = 0,
    later: () -> Unit = {},
    next: () -> Unit = {},
    isNextEnabled: Boolean = true
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 46.dp, start = 16.dp, end = 16.dp, top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = later) {
            Text("Later")
        }
        OutlinedButton(
            onClick = next,
            enabled = isNextEnabled,
            modifier = Modifier
                .width(126.dp)
                .height(64.dp),
            border = BorderStroke(2.dp, MaterialTheme.colors.primary),
            colors = DieterDefaultButton.outlinedColors()
        ) {
            Crossfade(targetState = currentPage) { page ->
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (page) {
                        0 -> Text("Next")
                        1 -> Text("Done")
                    }
                    Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = "next")
                }
            }
        }
    }
}

@Preview
@Composable
private fun TargetWeight(targetWeightState: WeightState = remember { WeightState() }) {
    Surface(
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
        modifier = Modifier
            .clip(DieterShapes.small)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Target Weight", style = MaterialTheme.typography.subtitle2)
            Text(
                "Weight you're trying to achieve",
                style = MaterialTheme.typography.caption
            )
            HeightWeightField(
                state = targetWeightState,
                label = null,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun IWantToCardPreview() {
    DieterTheme {
        IWantToCard(
            color = MaterialTheme.colors.primary,
            title = "Hello There",
            subTitle = "It's me, a subtitle."
        )
    }
}

@Preview
@Composable
private fun BMICalcPreview() {
    DieterTheme {
        BMICalc()
    }
}

@Preview
@Composable
private fun Slide1Preview() {
    DieterTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            Slide1(selectedGoal = remember { mutableStateOf(GoalType.INCREASE_WEIGHT) })
        }
    }
}

@Preview
@Composable
private fun Slide2Preview() {
    DieterTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            Slide2(
                {},
                remember {
                    AgeState()
                }
            )
        }
    }
}
