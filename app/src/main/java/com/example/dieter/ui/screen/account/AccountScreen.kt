package com.example.dieter.ui.screen.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dieter.data.source.domain.GoalModel
import com.example.dieter.data.source.domain.GoalType
import com.example.dieter.ui.component.AppNameHeader
import com.example.dieter.ui.component.DieterDefaultButton
import com.example.dieter.ui.theme.DieterShapes
import com.example.dieter.ui.theme.DieterTheme
import com.example.dieter.vo.DataState
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.insets.statusBarsPadding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    backToWelcome: () -> Unit,
    onNewGoal: () -> Unit = {}
) {
    var goalModel by remember { mutableStateOf<GoalModel?>(null) }
    val goal by viewModel.goal.collectAsState()

    when (goal) {
        is DataState.Success ->
            goalModel = (goal as DataState.Success<GoalModel?>).data
        // it's impossible not to have target weight
        is DataState.Error -> {

        }
        is DataState.Loading -> {
        }
        is DataState.Empty -> {

        }
    }
    Scaffold {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
            )
            AppNameHeader()
            Spacer(Modifier.size(24.dp))
            AccountInfo(onLogout = {
                backToWelcome()
                viewModel.signOut()
            })
            Spacer(Modifier.size(24.dp))
            if (goalModel != null) {
                GoalInfo(goal = goalModel!!, onNewGoal = onNewGoal)
            }
        }
    }
}

@Composable
private fun AccountInfo(onLogout: () -> Unit = {}) {
    val user = Firebase.auth.currentUser

    Row(verticalAlignment = Alignment.Top) {
        Image(
            rememberGlidePainter(request = user?.photoUrl),
            contentDescription = "ava",
            Modifier
                .size(132.dp)
        )
        Spacer(Modifier.size(8.dp))
        Column {
            Text(
                user?.displayName ?: "Unknown",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6
            )
            Text(
                user?.email ?: "Unknown",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1
            )
            OutlinedButton(
                onClick = onLogout,
                colors = DieterDefaultButton.outlinedColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
            ) {
                Text("Logout")
            }
        }
    }
}

@Composable
private fun GoalInfo(modifier: Modifier = Modifier, goal: GoalModel, onNewGoal: () -> Unit) {
    val goalString = when (goal.target) {
        GoalType.LOOSE_WEIGHT -> "Loose Weight"
        GoalType.INCREASE_WEIGHT -> "Increase Weight"
        GoalType.MAINTAIN_WEIGHT -> "Maintain Weight"
    }
    val sex = if (goal.isMale) "Male" else "Female"
    Column(verticalArrangement = Arrangement.Center, modifier = modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Goal", style = MaterialTheme.typography.body1)
            TextButton(onClick = onNewGoal) {
                Text("New goal")
            }
        }
        Surface(
            border = BorderStroke(2.dp, MaterialTheme.colors.primary),
            shape = DieterShapes.small
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        goalString,
                        maxLines = 2,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .width(86.dp)
                            .padding(start = 8.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("${goal.weight}kg", style = MaterialTheme.typography.h6)
                        Spacer(Modifier.size(8.dp))
                        Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "arrow")
                        Spacer(Modifier.size(8.dp))
                        Text("${goal.targetWeight}kg", style = MaterialTheme.typography.h6)
                    }
                }
                Divider(Modifier.padding(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Height",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Text(
                        "${goal.height}cm",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Divider(Modifier.padding(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        "Age/Sex",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Text(
                        "${goal.age}/$sex",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AccountInfoPreview() {
    DieterTheme {
        AccountInfo()
    }
}

@Preview
@Composable
private fun GoalInfoPreview() {
    DieterTheme {
        GoalInfo(
            goal = GoalModel(
                GoalType.MAINTAIN_WEIGHT,
                56,
                false,
                145,
                235,
                56,
                System.currentTimeMillis()
            ),
            onNewGoal = {}
        )
    }
}
