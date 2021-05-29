package com.example.dieter.ui.screen.history

import android.widget.CalendarView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.dieter.R
import com.example.dieter.data.source.domain.NutrientModel
import com.example.dieter.ui.component.FoodCard
import com.example.dieter.ui.component.NutrientBar
import com.example.dieter.ui.component.UpButton
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    goUp: () -> Unit = {},
    userRepId: String
) {

    val nutrients by viewModel.nutrients.collectAsState()
    val todaysFoods by viewModel.todaysFood.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            UpButton(goUp)
            Text(
                "History",
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .statusBarsPadding()
            )
            Spacer(Modifier.size(16.dp))
        }
        CustomCalendarView {
            viewModel.todayNutrient(userRepId, it)
            viewModel.todayFood(userRepId, it)
        }
        Spacer(modifier = Modifier.size(16.dp))
        HistorySection(
            title = "Nutrients",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        nutrients.subList(0, nutrients.size).forEach { nutrient ->
            NutrientBar(
                nutrient = NutrientModel(
                    nutrient.name,
                    nutrient.current,
                    nutrient.of,
                    nutrient.unit
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.size(12.dp))
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Filled.ExpandMore, contentDescription = "expand")
        }
        HistorySection(
            title = "Foods",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        todaysFoods.forEach {
            FoodCard(it, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.size(8.dp))
        }
        Spacer(Modifier.size(64.dp))
    }
}

@Composable
private fun HistorySection(modifier: Modifier = Modifier, title: String) {
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
fun CustomCalendarView(onDateSelected: (String) -> Unit) {
    // Adds view to Compose
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = { context ->
            CalendarView(ContextThemeWrapper(context, R.style.CalenderViewCustom))
        },
        update = { view ->
            view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val monthZeroed = "${month + 1}".padStart(2, '0')
                val dateZeroed = "$dayOfMonth".padStart(2, '0')
                onDateSelected("$dateZeroed-$monthZeroed-$year")
            }
        }
    )
}
