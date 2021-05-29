package com.example.dieter.ui.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dieter.data.source.domain.BodyWeightModel
import com.example.dieter.data.source.domain.FoodType
import com.example.dieter.data.source.domain.GoalModel
import com.example.dieter.data.source.domain.NutrientModel
import com.example.dieter.data.source.domain.SetBodyWeightModel
import com.example.dieter.data.source.domain.TodaysFoodModel
import com.example.dieter.ui.component.AppNameHeader
import com.example.dieter.ui.component.DieterDefaultButton
import com.example.dieter.ui.component.DieterVerticalBarChart
import com.example.dieter.ui.component.FoodCard
import com.example.dieter.ui.component.NutrientBar
import com.example.dieter.ui.component.TextFieldError
import com.example.dieter.ui.theme.AlphaNearTransparent
import com.example.dieter.ui.theme.AlphaReallyTransparent
import com.example.dieter.ui.theme.DieterShapes
import com.example.dieter.ui.theme.DieterTheme
import com.example.dieter.ui.theme.GreenPrimary
import com.example.dieter.utils.LocalSysUiController
import com.example.dieter.vo.DataState
import com.google.accompanist.insets.statusBarsPadding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    navigateToCalculateNutrients: () -> Unit = {},
    history: () -> Unit = {},
    reloadHome: () -> Unit = {},
    burnCalories: () -> Unit = {},
    setGoal: () -> Unit = {},
    temporaryId: String
) {
    LocalSysUiController.current.setStatusBarColor(
        MaterialTheme.colors.background.copy(
            AlphaNearTransparent
        )
    )

    var showTrialBanner by remember { mutableStateOf(true) }
    var showSignInBanner by remember { mutableStateOf(Firebase.auth.currentUser == null) }
    val goal by homeViewModel.goal.collectAsState()
    var showGoalBanner by remember { mutableStateOf(false) }
    val todaysFoods by homeViewModel.todaysFood.collectAsState()
    var showWeightEntry by remember { mutableStateOf(false) }
    val bodyWeightState = remember { BodyWeightState() }
    var targetWeight by remember { mutableStateOf(0) }
    val bodyWeightEntries by homeViewModel.bodyWeightEntries.collectAsState()
    val nutrients by homeViewModel.nutrients.collectAsState()

    when (goal) {
        is DataState.Success -> if ((goal as DataState.Success<GoalModel?>).data == null) {
            val unwrappedData = (goal as DataState.Success<GoalModel?>).data
            // it's impossible not to have target weight
            targetWeight = unwrappedData!!.targetWeight
            showGoalBanner = true
        }
        is DataState.Error -> {
            showGoalBanner = true
        }
        is DataState.Loading -> {
        }
        is DataState.Empty -> {
            showGoalBanner = true
        }
    }

    Box(contentAlignment = Alignment.BottomCenter) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.size(16.dp))
            HomeAppBar(modifier = Modifier.padding(horizontal = 16.dp), historyTapped = history)
            Spacer(Modifier.size(12.dp))
            if (showSignInBanner)
                SignInBanner(onClose = { showSignInBanner = false })
            if (showTrialBanner) {
                TrialBanner(onClose = { showTrialBanner = false })
            }

            if (showGoalBanner) {
                GoalBanner(
                    onClose = { showGoalBanner = false },
                    modifier = Modifier.clickable { setGoal() }
                )
            }
            Spacer(Modifier.size(12.dp))
            HomeSection(
                title = "Today's summary",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(Modifier.size(22.dp))
            if (nutrients.isNotEmpty()) {
                nutrients.subList(0, nutrients.size).forEachIndexed { idx, nutrient ->
                    NutrientBar(
                        nutrient = NutrientModel(
                            nutrient.name,
                            nutrient.current,
                            nutrient.of,
                            nutrient.unit
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    if (idx == 0) {
                        Spacer(Modifier.size(12.dp))
                        BurnCaloriesButton(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                    Spacer(Modifier.size(12.dp))
                }
            } else {
                Text("Nothing for now.")
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.ExpandMore, contentDescription = "expand")
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Body weight entry", style = MaterialTheme.typography.subtitle2)
                    IconButton(
                        onClick = { showWeightEntry = !showWeightEntry },
                        modifier = Modifier.fillMaxWidth(.25f)
                    ) {
                        Icon(
                            if (showWeightEntry) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = "new entry"
                        )
                    }
                }
                Divider(
                    Modifier
                        .padding(bottom = 12.dp)
                )
                if (showWeightEntry) {
                    Row(Modifier.fillMaxWidth()) {
                        BodyWeight(bodyWeightState = bodyWeightState)
                        Spacer(Modifier.size(8.dp))
                        OutlinedButton(
                            onClick = {
                                homeViewModel.newBodyWeight(
                                    temporaryId,
                                    SetBodyWeightModel(
                                        bodyWeightState.text.toInt(),
                                        targetWeight,
                                        Date()
                                    )
                                )
                                showWeightEntry = false
                            },
                            border = BorderStroke(2.dp, MaterialTheme.colors.primary),
                            colors = DieterDefaultButton.outlinedColors(),
                            modifier = Modifier
                                .width(92.dp)
                                .height(64.dp)
                        ) {
                            Text("Save")
                        }
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                }
            }

            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                Spacer(Modifier.size(16.dp))
                bodyWeightEntries.forEach {
                    BodyWeightBar(weightModelSet = it)
                    Spacer(Modifier.size(8.dp))
                }
                Spacer(Modifier.size(16.dp))
            }
            Spacer(Modifier.size(12.dp))
            HomeSection(
                title = "Food eaten today",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            todaysFoods.forEach {
                FoodCard(it, modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(Modifier.size(8.dp))
            }
            Spacer(Modifier.size(216.dp))
        }
        BottomBar(
            navigateHome = reloadHome,
            navigateToCalculateNutrients = navigateToCalculateNutrients,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 36.dp)
        )
    }
}

@Composable
private fun HomeAppBar(
    modifier: Modifier = Modifier,
    historyTapped: () -> Unit = {},
    notificationTapped: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = historyTapped) {
            Icon(imageVector = Icons.Rounded.Timeline, contentDescription = "history")
        }
        AppNameHeader()
        IconButton(onClick = notificationTapped) {
            Icon(imageVector = Icons.Rounded.Notifications, contentDescription = "notifications")
        }
    }
}

@Composable
private fun TrialBanner(modifier: Modifier = Modifier, onClose: () -> Unit = {}) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                2.dp,
                MaterialTheme.colors.primary
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier
                    .background(MaterialTheme.colors.primary.copy(AlphaReallyTransparent))
                    .padding(16.dp)
            ) {
                Text("Get premium free for a week", style = MaterialTheme.typography.subtitle2)
                Text(
                    "Just $9.99/month after. Cancel anytime",
                    style = MaterialTheme.typography.body2
                )
            }
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, "close")
            }
        }
    }
}

@Composable
private fun GoalBanner(modifier: Modifier = Modifier, onClose: () -> Unit = {}) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                2.dp,
                MaterialTheme.colors.primary
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier
                    .background(MaterialTheme.colors.primary.copy(AlphaReallyTransparent))
                    .padding(16.dp)
            ) {
                Text(
                    "Looks like you haven’t set a goal",
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    "set it now so we could help you",
                    style = MaterialTheme.typography.body2
                )
            }
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, "close")
            }
        }
    }
}

@Composable
private fun SignInBanner(modifier: Modifier = Modifier, onClose: () -> Unit = {}) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                2.dp,
                MaterialTheme.colors.primary
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier
                    .background(MaterialTheme.colors.primary.copy(AlphaReallyTransparent))
                    .padding(16.dp)
            ) {
                Text("Sign In", style = MaterialTheme.typography.subtitle2)
                Text(
                    "to save & sync data across devices.",
                    style = MaterialTheme.typography.body2
                )
            }
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, "close")
            }
        }
    }
}

@Composable
private fun HomeSection(modifier: Modifier = Modifier, title: String) {
    Column(
        modifier = modifier
            .wrapContentHeight()
    ) {
        Text(title, style = MaterialTheme.typography.subtitle2)
        Divider(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )
    }
}

@Composable
private fun BurnCaloriesButton(modifier: Modifier = Modifier) {
    Button(
        onClick = { /*TODO*/ },
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        contentPadding = PaddingValues(0.dp),
        shape = DieterShapes.medium,
        colors = DieterDefaultButton.burnCaloriesColors(),
        elevation = null
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(192 / 256f)
                    .fillMaxHeight()
                    .background(
                        GreenPrimary,
                        DieterShapes.medium
                    )
                    .clip(DieterShapes.medium)
            )
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Burn Calories")
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("192/ 256 kcal")
                    Spacer(modifier.size(16.dp))
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = "burn calories",
                        modifier = Modifier.background(
                            color = Color.White,
                            shape = CircleShape
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun BodyWeightBar(weightModelSet: BodyWeightModel, modifier: Modifier = Modifier) {
    val entriedAt = SimpleDateFormat(
        "dd/MM",
        Locale.UK
    ).format(weightModelSet.date)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.height(202.dp), contentAlignment = Alignment.BottomCenter) {
            DieterVerticalBarChart(
                progress = (weightModelSet.current / weightModelSet.target.toFloat()),
                label = weightModelSet.current.toString()
            )
        }
        Spacer(Modifier.size(8.dp))
        Text(entriedAt, style = MaterialTheme.typography.caption)
    }
}

@Composable
private fun BodyWeight(
    modifier: Modifier = Modifier,
    bodyWeightState: BodyWeightState = remember { BodyWeightState() },
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    TextField(
        value = bodyWeightState.text,
        onValueChange = {
            bodyWeightState.text = it
        },
        label = {
            Text("Body weight in kg", style = MaterialTheme.typography.body1)
        },
        modifier = modifier
            .fillMaxWidth(.75f)
            .onFocusChanged { focusState ->
                val focused = focusState == FocusState.Active
                bodyWeightState.onFocusChange(focused)
                if (!focused) {
                    bodyWeightState.enableShowErrors()
                }
            },
        isError = bodyWeightState.showErrors(),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(onDone = { onImeAction() })
    )
    bodyWeightState.getError()?.let {
        TextFieldError(textError = it)
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    initiallySelected: Int = 0,
    navigateHome: () -> Unit = {},
    navigateToCalculateNutrients: () -> Unit = {},
    navigateToAccount: () -> Unit = {}
) {
    var selected by remember { mutableStateOf(initiallySelected) }
    Surface(
        elevation = 8.dp,
        shape = DieterShapes.medium,
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .background(MaterialTheme.colors.background)
        ) {
            val selectedColor = MaterialTheme.colors.primary
            IconButton(
                onClick = {
                    selected = 0
                    navigateHome()
                }
            ) {
                val homeIcon = Icons.Filled.Home
                Icon(
                    imageVector = homeIcon,
                    tint = if (selected == 0) selectedColor else homeIcon.tintColor,
                    contentDescription = "home"
                )
            }
            IconButton(
                onClick = {
                    selected = 1
                    navigateToCalculateNutrients()
                }
            ) {
                val eatIcon = Icons.Filled.RestaurantMenu
                Icon(
                    imageVector = eatIcon,
                    tint = if (selected == 1) selectedColor else eatIcon.tintColor,
                    contentDescription = "take a picture",
                    modifier = Modifier.size(48.dp)
                )
            }
            IconButton(
                onClick = {
                    selected = 2
                    navigateToAccount()
                }
            ) {
                val faceIcon = Icons.Filled.Face
                Icon(
                    imageVector = faceIcon,
                    tint = if (selected == 2) selectedColor else faceIcon.tintColor,
                    contentDescription = "account"
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeAppBarPreview() {
    DieterTheme {
        Surface {
            HomeAppBar()
        }
    }
}

@Preview
@Composable
fun BannerPreview() {
    DieterTheme {
        Column {
            SignInBanner()
            TrialBanner()
            GoalBanner()
        }
    }
}

@Preview
@Composable
fun HomeSectionPreview() {
    DieterTheme {
        Surface {
            HomeSection(title = "Today's summary")
        }
    }
}

@Preview
@Composable
fun NutrientBarPreview() {
    DieterTheme {
        Surface {
            NutrientBar(NutrientModel("Calorie", 1437f, 2000, "kcal"))
        }
    }
}

@Preview
@Composable
fun BurnCaloriesButtonPreview() {
    DieterTheme {
        Surface {
            BurnCaloriesButton()
        }
    }
}

@Preview
@Composable
fun BodyWeightEntryPreview() {
    DieterTheme {
        Surface {
            Row {
                Spacer(Modifier.size(8.dp))
                BodyWeightBar(BodyWeightModel("", 50, 70, Date()))
                Spacer(Modifier.size(8.dp))
                BodyWeightBar(BodyWeightModel("", 55, 70, Date()))
                Spacer(Modifier.size(8.dp))
                BodyWeightBar(BodyWeightModel("", 60, 70, Date()))
                Spacer(Modifier.size(8.dp))
                BodyWeightBar(BodyWeightModel("", 61, 70, Date()))
                Spacer(Modifier.size(8.dp))
                BodyWeightBar(BodyWeightModel("", 67, 70, Date()))
                Spacer(Modifier.size(8.dp))
                BodyWeightBar(BodyWeightModel("", 70, 70, Date()))
                Spacer(Modifier.size(8.dp))
            }
        }
    }
}

@Preview
@Composable
private fun FoodCardPreview() {
    DieterTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                FoodCard(
                    TodaysFoodModel(
                        "",
                        FoodType.BREAKFAST,
                        "Egg Sandwich",
                        "fake_food.jpg",
                        15,
                        Date(1619289713000)
                    ),
                )
                Spacer(Modifier.size(8.dp))
                FoodCard(
                    TodaysFoodModel(
                        "",
                        FoodType.BREAKFAST,
                        "Egg Sandwich",
                        "fake_food.jpg",
                        15,
                        Date(1619289713000)
                    ),
                )
                Spacer(Modifier.size(8.dp))
                FoodCard(
                    TodaysFoodModel(
                        "",
                        FoodType.BREAKFAST,
                        "Egg Sandwich",
                        "fake_food.jpg",
                        15,
                        Date(1619289713000)
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun BottomBarPreview() {
    DieterTheme {
        BottomBar()
    }
}
