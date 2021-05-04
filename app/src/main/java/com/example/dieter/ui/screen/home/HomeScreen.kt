package com.example.dieter.ui.screen.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dieter.data.source.domain.BodyWeightModel
import com.example.dieter.data.source.domain.NutrientModel
import com.example.dieter.ui.component.AppNameHeader
import com.example.dieter.ui.component.DieterDefaultButton
import com.example.dieter.ui.component.DieterProgressBar
import com.example.dieter.ui.component.DieterVerticalBarChart
import com.example.dieter.ui.theme.AlphaNearTransparent
import com.example.dieter.ui.theme.AlphaReallyTransparent
import com.example.dieter.ui.theme.DieterShapes
import com.example.dieter.ui.theme.DieterTheme
import com.example.dieter.ui.theme.GreenPrimary
import com.example.dieter.utils.LocalSysUiController
import com.google.accompanist.insets.statusBarsPadding
import java.util.Date

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel()
) {
    LocalSysUiController.current.setStatusBarColor(
        MaterialTheme.colors.background.copy(
            AlphaNearTransparent
        )
    )
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
        HomeAppBar(modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.size(12.dp))
        TrialBanner(modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.size(12.dp))
        HomeSection(title = "Today's summary", modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.size(22.dp))
        homeViewModel.nutrients.subList(0, 5).forEachIndexed { idx, nutrient ->
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

        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Filled.ExpandMore, contentDescription = "expand")
        }
        HomeSection(title = "Body weight entry", modifier = Modifier.padding(horizontal = 16.dp))
        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            Spacer(Modifier.size(16.dp))
            homeViewModel.bodyWeightEntries.forEach {
                BodyWeightBar(weightModel = it)
                Spacer(Modifier.size(8.dp))
            }
            Spacer(Modifier.size(16.dp))
        }
    }
}

@Composable
fun HomeAppBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Rounded.Timeline, contentDescription = "history")
        AppNameHeader()
        Icon(imageVector = Icons.Rounded.Notifications, contentDescription = "notifications")
    }
}

@Composable
fun TrialBanner(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                2.dp,
                MaterialTheme.colors.primary,
                DieterShapes.medium
            )
            .clip(DieterShapes.medium)
    ) {
        Column(
            Modifier
                .background(MaterialTheme.colors.primary.copy(AlphaReallyTransparent))
                .padding(horizontal = 12.dp, vertical = 16.dp)
        ) {
            Text("Get premium free for a week", style = MaterialTheme.typography.subtitle2)
            Text("Just $9.99/month after. Cancel anytime", style = MaterialTheme.typography.body2)
        }
    }
}

@Composable
fun HomeSection(title: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(title, style = MaterialTheme.typography.subtitle2)
        Divider(modifier.padding(bottom = 12.dp))
    }
}

@Composable
fun NutrientBar(nutrient: NutrientModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(nutrient.name)
            Text("${nutrient.current}/${nutrient.of} ${nutrient.unit}")
        }
        DieterProgressBar(progress = (nutrient.current / nutrient.of.toFloat()))
    }
}

@Composable
fun BurnCaloriesButton(modifier: Modifier = Modifier) {
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
fun BodyWeightBar(weightModel: BodyWeightModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.height(202.dp), contentAlignment = Alignment.BottomCenter) {
            Log.d("BodyWeightBar", " ${(weightModel.current / weightModel.target.toFloat())}")
            DieterVerticalBarChart(progress = (weightModel.current / weightModel.target.toFloat()))
        }
        Spacer(Modifier.size(8.dp))
        Text("12/10", style = MaterialTheme.typography.caption)
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
fun TrialBannerPreview() {
    DieterTheme {
        TrialBanner()
    }
}

@Preview
@Composable
fun HomeSectionPreview() {
    DieterTheme {
        Surface {
            HomeSection("Today's summary")
        }
    }
}

@Preview
@Composable
fun NutrientBarPreview() {
    DieterTheme {
        Surface {
            NutrientBar(NutrientModel("Calorie", 1437, 2000, "kcal"))
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
            BodyWeightBar(BodyWeightModel(67, 70, Date()))
        }
    }
}
