package com.example.dieter.ui.screen.home

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dieter.MainDestinations
import com.example.dieter.R
import com.example.dieter.application.DieterApplication
import com.example.dieter.data.source.domain.BodyWeightModel
import com.example.dieter.data.source.domain.BurnCalorieModel
import com.example.dieter.data.source.domain.FoodType
import com.example.dieter.data.source.domain.GoalModel
import com.example.dieter.data.source.domain.NutrientModel
import com.example.dieter.data.source.domain.NutrientType
import com.example.dieter.data.source.domain.SetBodyWeightModel
import com.example.dieter.data.source.domain.TodaysFoodModel
import com.example.dieter.ui.component.AppNameHeader
import com.example.dieter.ui.component.DieterDefaultButton
import com.example.dieter.ui.component.DieterVerticalBarChart
import com.example.dieter.ui.component.FoodCard
import com.example.dieter.ui.component.NutrientBar
import com.example.dieter.ui.component.TextFieldError
import com.example.dieter.ui.screen.account.AccountScreen
import com.example.dieter.ui.screen.account.AccountViewModel
import com.example.dieter.ui.theme.AlphaNearTransparent
import com.example.dieter.ui.theme.AlphaReallyTransparent
import com.example.dieter.ui.theme.DieterShapes
import com.example.dieter.ui.theme.DieterTheme
import com.example.dieter.ui.theme.GreenPrimary
import com.example.dieter.utils.LocalSysUiController
import com.example.dieter.vo.DataState
import com.google.accompanist.insets.statusBarsPadding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeAccountGroup(
    homeViewModel: HomeViewModel = viewModel(),
    accountViewModel: AccountViewModel,
    navigateToCalculateNutrients: () -> Unit = {},
    history: () -> Unit = {},
    burnCalories: () -> Unit = {},
    setGoal: () -> Unit = {},
    onLogout: () -> Unit
) {
    var toShow by remember {
        mutableStateOf(MainDestinations.HOME_ROUTE)
    }
    val scaffoldState = rememberScaffoldState()
    val snackbarCoroutineScope = rememberCoroutineScope()
    var showSyncDoneBar by remember { mutableStateOf<Boolean?>(false) }
    var showLoginDoneBar by remember { mutableStateOf<Boolean?>(false) }
    var errorSnackBar by remember { mutableStateOf<Boolean?>(false) }
    val goal by homeViewModel.goal.collectAsState()
    var showGoalBanner by remember { mutableStateOf(false) }
    var goalModel by remember { mutableStateOf<GoalModel?>(null) }
    val scrollState = rememberScrollState()
    var lastScrollIndex by remember { mutableStateOf(0) }
    var scrollUp by remember { mutableStateOf(true) }

    scrollUp = scrollState.value > lastScrollIndex
    if ((scrollState.value - lastScrollIndex).absoluteValue > 112)
        lastScrollIndex = scrollState.value

    when (goal) {
        is DataState.Success ->
            if ((goal as DataState.Success<GoalModel?>).data?.isMale != null) {
                val unwrappedData = (goal as DataState.Success<GoalModel?>).data
                // it's impossible not to have target weight
                goalModel = unwrappedData!!
                showGoalBanner = false
            }
        is DataState.Error -> {
            errorSnackBar = true
            showGoalBanner = true
        }
        is DataState.Loading -> {
        }
        is DataState.Empty -> {
            showGoalBanner = true
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
        if (showLoginDoneBar == true) {
            snackbarCoroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar("Welcome ${Firebase.auth.currentUser.displayName}")
            }
            showLoginDoneBar = null
        }
        if (errorSnackBar == true) {
            snackbarCoroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar("Somethings not right")
            }
            errorSnackBar = null
        }
        if (showSyncDoneBar == true) {
            snackbarCoroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar("Data are synced")
            }
            showSyncDoneBar = null
        }
        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {

            Crossfade(targetState = toShow) { screen ->
                if (screen == MainDestinations.HOME_ROUTE) {
                    HomeScreen(
                        homeViewModel = homeViewModel,
                        history = history,
                        burnCalories = burnCalories,
                        goal = goalModel,
                        setGoal = setGoal,
                        showGoalBanner = showGoalBanner,
                        onGoalClose = {
                            showGoalBanner = false
                        },
                        error = {
                            if (errorSnackBar == false) {
                                errorSnackBar = true
                            }
                        },
                        syncDone = {
                            if (showSyncDoneBar == false)
                                showSyncDoneBar = true
                        },
                        signInDone = {
                            if (showLoginDoneBar == false) {
                                showLoginDoneBar = true
                            }
                        },
                        modifier = Modifier
                            .statusBarsPadding()
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    )
                } else if (screen == MainDestinations.ACCOUNT_ROUTE) {
                    AccountScreen(
                        viewModel = accountViewModel,
                        onNewGoal = {
                            snackbarCoroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("Not yet implemented")
                            }
                        },
                        backToWelcome = onLogout
                    )
                }
            }

            AnimatedVisibility(
                visible = !scrollUp,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                BottomBar(
                    navigateHome = { toShow = MainDestinations.HOME_ROUTE },
                    navigateToCalculateNutrients = navigateToCalculateNutrients,
                    navigateToAccount = {
                        if (Firebase.auth.currentUser == null)
                            snackbarCoroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("Not signed in")
                            }
                        else
                            toShow = MainDestinations.ACCOUNT_ROUTE
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 36.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(),
    history: () -> Unit = {},
    burnCalories: () -> Unit = {},
    setGoal: () -> Unit = {},
    signInDone: () -> Unit = {},
    syncDone: () -> Unit = {},
    error: () -> Unit = {},
    goal: GoalModel?,
    showGoalBanner: Boolean,
    onGoalClose: () -> Unit = {},
) {
    LocalSysUiController.current.setStatusBarColor(
        MaterialTheme.colors.background.copy(
            AlphaNearTransparent
        )
    )

    var showTrialBanner by remember { mutableStateOf(true) }
    var showSignInBanner by remember { mutableStateOf(Firebase.auth.currentUser == null) }
    val todaysFoods by homeViewModel.todaysFood.collectAsState()
    var showWeightEntry by remember { mutableStateOf(false) }
    val bodyWeightState = remember { BodyWeightState() }
    val bodyWeightEntries by homeViewModel.bodyWeightEntries.collectAsState()
    val nutrients by homeViewModel.nutrients.collectAsState()
    val calories by homeViewModel.calories.collectAsState()
    val loginState by homeViewModel.loginState.collectAsState()
    val linkDeviceSuccessState by homeViewModel.linkDeviceSuccess.collectAsState()
    var expand by remember {
        mutableStateOf(false)
    }

    // TODO: 31/05/21: Snackbar shown repeatedly
    // if the app is still open after signin, meaning it's still in the same session.
    // SUCCESS will be called and snackbar will show again
    when (loginState) {
        is DataState.Success -> {
            val uid = (loginState as DataState.Success<FirebaseUser>).data.uid
            homeViewModel.linkUserDevice(uid)
            signInDone()
            showSignInBanner = false
        }
        is DataState.Error -> {
            error()
        }
        is DataState.Loading -> {
        }
        is DataState.Empty -> {
        }
    }

    if (linkDeviceSuccessState) {
        syncDone()
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.size(16.dp))
        HomeAppBar(modifier = Modifier.padding(horizontal = 16.dp), historyTapped = history)
        Spacer(Modifier.size(12.dp))
        AnimatedVisibility(visible = showSignInBanner) {
            SignInBanner(
                onClose = { showSignInBanner = false },
                login = { token ->
                    homeViewModel.authWithGoogle(token)
                }
            )
        }
        AnimatedVisibility(visible = showTrialBanner) {
            TrialBanner(onClose = { showTrialBanner = false })
        }
        AnimatedVisibility(visible = showGoalBanner) {
            GoalBanner(
                onClose = onGoalClose,
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

        Column(
            modifier = Modifier.animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (nutrients.isNotEmpty()) {
                val mapped = nutrients.map { it.name to it }.toMap()
                val top5 = mutableListOf<NutrientModel>()
                mapped[NutrientType.ENERC_KCAL.nutrientName]?.let { top5.add(it) }
                mapped[NutrientType.CHOCDF.nutrientName]?.let { top5.add(it) }
                mapped[NutrientType.FAT.nutrientName]?.let { top5.add(it) }
                mapped[NutrientType.FIBTG.nutrientName]?.let { top5.add(it) }
                mapped[NutrientType.PROCNT.nutrientName]?.let { top5.add(it) }
                val toShow: List<NutrientModel> =
                    if (expand) listOf(top5, nutrients - top5).flatten() else top5

                toShow.forEachIndexed { idx, nutrient ->
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
                        BurnCaloriesButton(
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                            burnCalorieModel = calories,
                            onClick = burnCalories
                        )
                    }
                    Spacer(Modifier.size(12.dp))
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.healthy_food),
                    contentDescription = "eat",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(212.dp)
                )
                Text(
                    "Tap the middle button of the floating bar to start",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            if (nutrients.isNotEmpty()) {
                IconButton(onClick = { expand = !expand }) {
                    Icon(
                        imageVector = if (expand) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = "expand"
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp)
                .animateContentSize()
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
                        if (showWeightEntry or bodyWeightEntries.isEmpty()) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = "new entry"
                    )
                }
            }
            Divider(
                Modifier
                    .padding(bottom = 12.dp)
            )
            if (showWeightEntry or bodyWeightEntries.isEmpty()) {
                Row(Modifier.fillMaxWidth()) {
                    BodyWeight(bodyWeightState = bodyWeightState)
                    Spacer(Modifier.size(8.dp))
                    OutlinedButton(
                        onClick = {
                            homeViewModel.newBodyWeight(
                                SetBodyWeightModel(
                                    bodyWeightState.text.toInt(),
                                    goal?.targetWeight ?: 1, // TODO: comeback to this later
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

        if (bodyWeightEntries.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .height(232.dp)
            ) {
                Spacer(Modifier.size(16.dp))
                bodyWeightEntries.forEach {
                    BodyWeightBar(
                        weightModelSet = it,
                        onDelete = {
                            homeViewModel.deleteBodyWeight(it)
                        }
                    )
                    Spacer(Modifier.size(8.dp))
                }
                Spacer(Modifier.size(16.dp))
            }
        } else {
            Text(
                "Nothings here, start by adding it.",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        Spacer(Modifier.size(12.dp))
        if (todaysFoods.isNotEmpty()) {
            HomeSection(
                title = "Food eaten today",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            todaysFoods.forEach {
                FoodCard(
                    foodModel = it, deletable = true,
                    onDelete = {
                        homeViewModel.deleteTodaysFood(it)
                    }
                )
                Spacer(Modifier.size(8.dp))
            }
        }
        Spacer(Modifier.size(216.dp))
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
            Icon(
                imageVector = Icons.Rounded.Notifications,
                contentDescription = "notifications"
            )
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
                Text(
                    "2-months trials activated",
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    "Enjoy our premium features",
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
private fun SignInBanner(
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {},
    login: (String) -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                2.dp,
                MaterialTheme.colors.primary
            )
    ) {
        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("LoginPopup", "firebaseAuthWithGoogle:" + account.id)
                    // it's save to use bang operator here
                    login(account.idToken!!)
                } catch (e: ApiException) {
                    Log.w("LoginPopup", "Google sign in failed", e)
                    // temporary error message
                    Toast.makeText(
                        DieterApplication.applicationContext(),
                        "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(stringResource(id = R.string.default_web_client_id))
            .requestEmail()
            .build()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier
                    .background(MaterialTheme.colors.primary.copy(AlphaReallyTransparent))
                    .padding(16.dp)
                    .clickable {
                        val googleSignInClient =
                            DieterApplication
                                .applicationContext()
                                ?.let { GoogleSignIn.getClient(it, gso) }
                        launcher.launch(googleSignInClient?.signInIntent)
                    }
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
private fun BurnCaloriesButton(
    modifier: Modifier = Modifier,
    burnCalorieModel: DataState<BurnCalorieModel>,
    onClick: () -> Unit = {}
) {
    var formattedValue by remember { mutableStateOf("") }
    var burnToBurn = Pair(1, 1)
    when (burnCalorieModel) {
        is DataState.Success -> {
            formattedValue =
                "${burnCalorieModel.data.burned}/ ${burnCalorieModel.data.toBurn} kcal"
            burnToBurn = Pair(burnCalorieModel.data.burned, burnCalorieModel.data.toBurn)
        }
        is DataState.Loading -> formattedValue = "Loading"
        is DataState.Error -> formattedValue = "Somethings wrong"
        is DataState.Empty -> formattedValue = "-/-"
    }
    Button(
        onClick = onClick,
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
            val progress = if (burnToBurn.second <= 0) {
                burnToBurn.first / 1f
            } else burnToBurn.first / burnToBurn.second.toFloat()
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(progress)
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
                    Text(formattedValue)
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
private fun BodyWeightBar(
    modifier: Modifier = Modifier,
    weightModelSet: BodyWeightModel,
    onDelete: () -> Unit = {}
) {
    val entriedAt = SimpleDateFormat(
        "dd/MM",
        Locale.UK
    ).format(weightModelSet.date)
    var showDelete by remember { mutableStateOf(false) }
    val offsetAnimControl by animateIntOffsetAsState(
        targetValue = IntOffset(
            0,
            if (showDelete) 0 else 16
        )
    )

    val progress = if (weightModelSet.target <= 0) {
        weightModelSet.current / 1f
    } else weightModelSet.current / weightModelSet.target.toFloat()

    Column {
        IconButton(
            onClick = {
                showDelete = false
                onDelete()
            },
            modifier
                .width(46.dp)
                .height(if (showDelete) 64.dp else 0.dp)
                .offset { offsetAnimControl }
        ) {
            Icon(imageVector = Icons.Filled.DeleteForever, contentDescription = "cancel")
        }

        val offsetAnimBar by animateIntOffsetAsState(
            targetValue = IntOffset(
                0,
                if (showDelete) 16 else 0
            )
        )

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .offset { offsetAnimBar }
                .clickable { showDelete = !showDelete },
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.height(202.dp), contentAlignment = Alignment.BottomCenter) {
                DieterVerticalBarChart(
                    progress = (progress),
                    label = weightModelSet.current.toString()
                )
            }
            Spacer(Modifier.size(8.dp))
            Text(
                entriedAt,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.height(16.dp)
            )
        }
    }
}

@Composable
private fun BodyWeight(
    modifier: Modifier = Modifier,
    bodyWeightState: BodyWeightState = remember { BodyWeightState() },
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    Column {
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
            SignInBanner(onClose = {}, login = {})
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
            BurnCaloriesButton(burnCalorieModel = DataState.Empty)
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
                BodyWeightBar(weightModelSet = BodyWeightModel("", 50, 70, Date()))
                Spacer(Modifier.size(8.dp))
                BodyWeightBar(weightModelSet = BodyWeightModel("", 55, 70, Date()))
                Spacer(Modifier.size(8.dp))
                BodyWeightBar(weightModelSet = BodyWeightModel("", 60, 70, Date()))
                Spacer(Modifier.size(8.dp))
                BodyWeightBar(weightModelSet = BodyWeightModel("", 61, 70, Date()))
                Spacer(Modifier.size(8.dp))
                BodyWeightBar(weightModelSet = BodyWeightModel("", 67, 70, Date()))
                Spacer(Modifier.size(8.dp))
                BodyWeightBar(weightModelSet = BodyWeightModel("", 70, 70, Date()))
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
                    foodModel = TodaysFoodModel(
                        "",
                        FoodType.BREAKFAST,
                        "Egg Sandwich",
                        "fake_food.jpg",
                        15,
                        Date(1619289713000)
                    )
                )
                Spacer(Modifier.size(8.dp))
                FoodCard(
                    foodModel = TodaysFoodModel(
                        "",
                        FoodType.BREAKFAST,
                        "Egg Sandwich",
                        "fake_food.jpg",
                        15,
                        Date(1619289713000)
                    )
                )
                Spacer(Modifier.size(8.dp))
                FoodCard(
                    foodModel = TodaysFoodModel(
                        "",
                        FoodType.BREAKFAST,
                        "Egg Sandwich",
                        "fake_food.jpg",
                        15,
                        Date(1619289713000)
                    )
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
