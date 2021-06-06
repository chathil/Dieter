package com.example.dieter

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.dieter.ui.screen.account.AccountViewModel
import com.example.dieter.ui.screen.add.ingredients.AddIngredientsScreen
import com.example.dieter.ui.screen.add.ingredients.AddIngredientsViewModel
import com.example.dieter.ui.screen.calculate.CalculateScreen
import com.example.dieter.ui.screen.calculate.CalculateViewModel
import com.example.dieter.ui.screen.goal.GoalScreen
import com.example.dieter.ui.screen.goal.GoalViewModel
import com.example.dieter.ui.screen.history.HistoryScreen
import com.example.dieter.ui.screen.history.HistoryViewModel
import com.example.dieter.ui.screen.home.HomeAccountGroup
import com.example.dieter.ui.screen.home.HomeViewModel
import com.example.dieter.ui.screen.search.ingredient.SearchIngredientScreen
import com.example.dieter.ui.screen.search.ingredient.SearchIngredientViewModel
import com.example.dieter.ui.screen.welcome.WelcomeScreen
import com.example.dieter.ui.screen.welcome.WelcomeViewModel
import com.example.dieter.ui.screen.workout.WorkoutScreen
import com.example.dieter.ui.screen.workout.WorkoutViewModel
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * Destinations used in the ([DieterApp]).
 */
object MainDestinations {
    const val WELCOME_ROUTE = "welcome"
    const val HOME_ROUTE = "home"
    const val ACCOUNT_ROUTE = "account"
    const val ADD_INGREDIENTS_ROUTE = "add_ingredients"
    const val SEARCH_INGREDIENT_ROUTE = "search_ingredient"
    const val CALCULATE_NUTRIENTS_ROUTE = "calculate_nutrients"
    const val SET_GOAL_ROUTE = "set_goal"
    const val HISTORY_ROUTE = "history"
    const val WORKOUT_ROUTE = "workout"
//    const val COURSE_DETAIL_ID_KEY = "courseId" /* For reference */
}

@OptIn(InternalCoroutinesApi::class)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    appState: DieterAppState,
    finishActivity: () -> Unit = {},
    welcomeShown: () -> Unit = {},
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.HOME_ROUTE,
    showWelcomeInitially: Boolean
) {

    val actions = remember(navController) { MainActions(navController) }

    NavHost(
        navController = navController,
        startDestination = if (showWelcomeInitially) MainDestinations.WELCOME_ROUTE else startDestination
    ) {

        composable(MainDestinations.WELCOME_ROUTE) {
            // Intercept back to Where-ever: make it finish the activity
            BackHandler {
                finishActivity()
            }
            val welcomeViewModel: WelcomeViewModel =
                viewModel(factory = HiltViewModelFactory(LocalContext.current, it))
            WelcomeScreen(
                welcomeViewModel = welcomeViewModel,
                welcomeFinished = {
                    actions.welcomeFinished()
                },
                navigateToHome = {
                    actions.toHome()
                    welcomeShown()
                }
            )
        }

        composable(MainDestinations.HOME_ROUTE) {
            // Intercept back to Welcome: make it finish the activity
            BackHandler {
                finishActivity()
            }
            val homeViewModel: HomeViewModel =
                viewModel(factory = HiltViewModelFactory(LocalContext.current, it))
            val accountViewModel: AccountViewModel =
                viewModel(factory = HiltViewModelFactory(LocalContext.current, it))
            HomeAccountGroup(
                homeViewModel = homeViewModel,
                accountViewModel = accountViewModel,
                onLogout = actions.toWelcome,
                history = actions.history,
                burnCalories = actions.workout,
                navigateToCalculateNutrients = actions.addIngredients,
                setGoal = actions.setGoal
            )
        }

        composable(MainDestinations.ADD_INGREDIENTS_ROUTE) {
            val addIngredientsViewModel: AddIngredientsViewModel = viewModel(
                factory = HiltViewModelFactory(
                    LocalContext.current, it
                )
            )
            AddIngredientsScreen(
                appState = appState,
                viewModel = addIngredientsViewModel,
                goUp = actions.upPress,
                calculateNutrients = actions.calculateNutrients,
                navigateToSearchIngredient = actions.searchIngredient,
            )
        }
        composable(MainDestinations.SEARCH_INGREDIENT_ROUTE) {
            val viewModel: SearchIngredientViewModel = viewModel(
                factory = HiltViewModelFactory(
                    LocalContext.current, it
                )
            )
            SearchIngredientScreen(
                appState = appState,
                viewModel = viewModel,
                goUp = actions.upPress
            )
        }

        composable(MainDestinations.CALCULATE_NUTRIENTS_ROUTE) {
            val viewModel: CalculateViewModel = viewModel(
                factory = HiltViewModelFactory(
                    LocalContext.current, it
                )
            )
            CalculateScreen(
                viewModel = viewModel,
                appState = appState,
                goUp = actions.upPress,
                save = {
                    actions.toHome()
                }
            )
        }

        composable(MainDestinations.SET_GOAL_ROUTE) {
            val viewModel: GoalViewModel = viewModel(
                factory = HiltViewModelFactory(
                    LocalContext.current, it
                )
            )
            GoalScreen(
                viewModel = viewModel,
                goUp = actions.upPress,
                goHome = actions.toHome
            )
        }

        composable(MainDestinations.HISTORY_ROUTE) {
            val viewModel: HistoryViewModel = viewModel(
                factory = HiltViewModelFactory(
                    LocalContext.current, it
                )
            )
            HistoryScreen(viewModel = viewModel, goUp = actions.toHome)
        }

        composable(MainDestinations.WORKOUT_ROUTE) {
            val viewModel: WorkoutViewModel =
                viewModel(factory = HiltViewModelFactory(LocalContext.current, it))
            WorkoutScreen(viewModel = viewModel, appState = appState, goUp = actions.upPress)
        }

        // Reference for navigation with parameters

//        navigation(
//            route = MainDestinations.COURSES_ROUTE,
//            startDestination = CourseTabs.FEATURED.route
//        ) {
//            courses(
//                onCourseSelected = actions.selectCourse,
//                onboardingComplete = onboardingComplete,
//                navController = navController,
//                modifier = modifier
//            )
//        }
//
//        composable(
//            "${MainDestinations.COURSE_DETAIL_ROUTE}/{$COURSE_DETAIL_ID_KEY}",
//            arguments = listOf(
//                navArgument(COURSE_DETAIL_ID_KEY) { type = NavType.LongType }
//            )
//        ) { backStackEntry ->
//            val arguments = requireNotNull(backStackEntry.arguments)
//            CourseDetails(
//                courseId = arguments.getLong(COURSE_DETAIL_ID_KEY),
//                selectCourse = actions.selectCourse,
//                upPress = actions.upPress
//            )
//        }
    }
}

/**
 * Models the navigation actions in the app.
 */
class MainActions(navController: NavHostController) {
    val welcomeFinished: () -> Unit = {
        navController.popBackStack()
    }
    val toWelcome: () -> Unit = {
        navController.navigate(MainDestinations.WELCOME_ROUTE)
    }
    val toHome: () -> Unit = {
        navController.navigate(MainDestinations.HOME_ROUTE)
    }
    val addIngredients: () -> Unit = {
        navController.navigate(MainDestinations.ADD_INGREDIENTS_ROUTE)
    }

    val searchIngredient: () -> Unit = {
        navController.navigate(MainDestinations.SEARCH_INGREDIENT_ROUTE)
    }

    val calculateNutrients: () -> Unit = {
        navController.navigate(MainDestinations.CALCULATE_NUTRIENTS_ROUTE)
    }

    val setGoal: () -> Unit = {
        navController.navigate(MainDestinations.SET_GOAL_ROUTE)
    }

    val history: () -> Unit = {
        navController.navigate(MainDestinations.HISTORY_ROUTE)
    }

    val workout: () -> Unit = {
        navController.navigate(MainDestinations.WORKOUT_ROUTE)
    }

    // For reference
//    val selectCourse: (Long) -> Unit = { courseId: Long ->
//        navController.navigate("${MainDestinations.COURSE_DETAIL_ROUTE}/$courseId")
//    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}
