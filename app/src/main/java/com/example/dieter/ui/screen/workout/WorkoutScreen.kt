package com.example.dieter.ui.screen.workout

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dieter.DieterAppState
import com.example.dieter.data.source.domain.BurnCalorieModel
import com.example.dieter.data.source.domain.WorkoutModel
import com.example.dieter.service.CountdownService
import com.example.dieter.ui.component.DieterDefaultButton
import com.example.dieter.ui.component.DieterProgressBar
import com.example.dieter.ui.component.UpButton
import com.example.dieter.ui.theme.DieterShapes
import com.example.dieter.ui.theme.DieterTheme
import com.example.dieter.ui.theme.GreenPrimary
import com.example.dieter.vo.DataState
import com.google.accompanist.insets.statusBarsPadding
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

@Composable
fun WorkoutScreen(
    viewModel: WorkoutViewModel,
    goUp: () -> Unit = {},
    appState: DieterAppState
) {
    val isTicking by viewModel.isTicking.collectAsState()
    val todos by viewModel.todos.collectAsState()
    val todo by appState.activeCounterState.collectAsState()
    val finished by appState.workoutDone.collectAsState()
    var current by remember { mutableStateOf<Pair<WorkoutModel, Float>?>(null) }
    val context = LocalContext.current
    val saveWorkoutState by viewModel.saveWorkoutState.collectAsState()
    var showSummary by remember { mutableStateOf(false) }
    var saveWorkoutMessage by remember { mutableStateOf("") }
    var sigint by remember { mutableStateOf(false) }
    val calories by viewModel.calories.collectAsState()
    val toAdd by viewModel.toAdd.collectAsState()

    when (saveWorkoutState) {
        is DataState.Success -> showSummary = true
        is DataState.Error -> saveWorkoutMessage = "Something went wrong"
        is DataState.Loading -> if ((saveWorkoutState as DataState.Loading<Boolean>).data != null) saveWorkoutMessage =
            "Loading"
        else -> {
        }
    }

    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
        if (finished) {
            viewModel.tick(false)
            sigint = true
            viewModel.clearTodos()
            Log.d("WorkoutScreen", "WorkoutScreen: $finished")
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .animateContentSize()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                UpButton(goUp)
                Text(
                    "Burn calories",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .statusBarsPadding()
                )
                Text(saveWorkoutMessage)
            }
            Spacer(modifier = Modifier.size(24.dp))
            CalorieBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                burnCalorieModel = calories,
                toAdd = toAdd
            )
            Spacer(modifier = Modifier.size(28.dp))

            viewModel.workoutModels.forEach {
                val dones by viewModel.dones.collectAsState()

                WorkoutCard(
                    item = Pair(it, todos[it] ?: 0),
                    enabled = !isTicking,
                    isDone = dones.contains(it),
                    decrease = {
                        viewModel.dec(it)
                    },
                    increase = {
                        viewModel.inc(it)
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                )

                if (todo?.workout == it) {

                    val progress = (todo!!.now) / (todo!!.peak).toFloat()
                    current = Pair(it, progress)
                    if (progress in 0.0f..0.1f)
                        viewModel.dones(it)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        if (!sigint) {
                            viewModel.tick(true)
                        }
                        Spacer(Modifier.size(16.dp))
                        Text(toSecond(todo!!.now), style = MaterialTheme.typography.caption)
                        Spacer(Modifier.size(8.dp))
                        DieterProgressBar(
                            progress = progress
                        )
                        Text(toSecond(todo!!.peak), style = MaterialTheme.typography.caption)
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(112.dp)
                .padding(horizontal = 16.dp)
        ) {
            val intent = Intent(context, CountdownService::class.java)
            if (isTicking)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row {
                        StopTimerButton(
                            onStop = {
                                sigint = true
                                viewModel.tick(false)
                                context.stopService(intent)
                                // push whatever progress is to firebase
                                viewModel.saveBurnedCalories(
                                    Pair(
                                        current?.first,
                                        current?.second?.times(
                                            current?.first?.calorieBurnedPerMinute ?: 0
                                        )?.minus(
                                            current?.first?.calorieBurnedPerMinute ?: 0
                                        )?.absoluteValue
                                    ),
                                    todo?.now
                                )
                            }
                        )
                        CancelWorkoutButton(
                            onCancel = {
                                sigint = true
                                viewModel.tick(false)
                                context.stopService(intent)
                                viewModel.clearTodos()
                                goUp()
                            }
                        )
                    }

                    PauseTimerButton(
                        onPause = {
                            Toast.makeText(
                                context,
                                "You can't pause yet sorry:)",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    )
                }
            else
                StartTimerButton(
                    enabled = todos.isNotEmpty(),
                    onStart = {
                        viewModel.tick(true)
                        intent.putExtra("todos", HashMap(todos))
                        intent.putExtra("user_rep_id", viewModel.userRepId)
                        context.startService(intent)
                    }
                )
            Spacer(modifier = Modifier.size(36.dp))
        }
    }
}

private fun toSecond(time: Long): String {
    return String.format(
        "%d:%d",
        TimeUnit.MILLISECONDS.toMinutes(time),
        TimeUnit.MILLISECONDS.toSeconds(time) -
            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
    )
}

@Composable
private fun WorkoutCard(
    modifier: Modifier = Modifier,
    item: Pair<WorkoutModel, Int>,
    isDone: Boolean = false,
    enabled: Boolean = true,
    increase: () -> Unit = {},
    decrease: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                item.first.name,
                style = MaterialTheme.typography.h6,
                color = if (isDone) GreenPrimary else MaterialTheme.colors.onSurface
            )
            Text(
                "~${item.first.calorieBurnedPerMinute} kcal/ 1mnts",
                style = MaterialTheme.typography.caption
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.wrapContentSize()) {
            IconButton(onClick = decrease, enabled = enabled) {
                Icon(
                    imageVector = Icons.Filled.Remove,
                    contentDescription = "decrease"
                )
            }
            Text("${item.second}mnts")
            IconButton(onClick = increase, enabled = enabled) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "increase"
                )
            }
        }
    }
}

@Composable
private fun CalorieBar(
    modifier: Modifier = Modifier,
    burnCalorieModel: DataState<BurnCalorieModel>,
    toAdd: Float,
) {
    var formattedValue by remember { mutableStateOf("") }
    var burnToBurn = Pair(0, 0)

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
            val animProgress = if (burnToBurn.second == 0) {
                (burnToBurn.first + toAdd) / 1f
            } else {
                (burnToBurn.first + toAdd) / burnToBurn.second
            }
            val animToAdd: Float by animateFloatAsState(targetValue = animProgress)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(animToAdd)
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colors.primary,
                        DieterShapes.medium
                    )
                    .clip(DieterShapes.medium)
            )
            val progress = if (burnToBurn.second == 0) {
                burnToBurn.first / 1f
            } else {
                burnToBurn.first / burnToBurn.second.toFloat()
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .background(
                        GreenPrimary,
                        DieterShapes.medium
                    )
                    .clip(DieterShapes.medium)
                    .animateContentSize()
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
                    if (toAdd > 0f) {
                        Text("${burnToBurn.first} + ${toAdd.toInt()}/ ${burnToBurn.second} kcal")
                    } else {
                        Text(formattedValue)
                    }
                    Spacer(modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun StartTimerButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onStart: () -> Unit = {}
) {
    OutlinedButton(
        enabled = enabled,
        onClick = onStart,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
        colors = DieterDefaultButton.outlinedColors()
    ) {
        Text("Start timer")
    }
}

@Composable
private fun StopTimerButton(modifier: Modifier = Modifier, onStop: () -> Unit = {}) {
    TextButton(onClick = onStop) {
        Text("Stop")
    }
}

@Composable
private fun CancelWorkoutButton(modifier: Modifier = Modifier, onCancel: () -> Unit = {}) {
    TextButton(onClick = onCancel) {
        Text("Cancel")
    }
}

@Composable
private fun PauseTimerButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onPause: () -> Unit = {}
) {
    OutlinedButton(
        onClick = onPause,
        enabled = enabled,
        modifier = Modifier
            .width(126.dp)
            .height(64.dp),
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
        colors = DieterDefaultButton.outlinedColors()
    ) {
        Text("Pause")
    }
}

@Preview
@Composable
private fun WorkoutCardPreview() {
    DieterTheme {
        Surface {
            WorkoutCard(item = Pair(WorkoutModel("1", "Pushup", 100), 1))
        }
    }
}

@Preview
@Composable
private fun CalorieBarPreview() {
    DieterTheme {
        CalorieBar(burnCalorieModel = DataState.Empty, toAdd = 10f)
    }
}

@Preview
@Composable
private fun ButtonsPreview() {
    DieterTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            Column {
                StartTimerButton(enabled = false)
                PauseTimerButton()
                StopTimerButton()
            }
        }
    }
}
