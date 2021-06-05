package com.example.dieter.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.dieter.data.source.domain.NutrientModel
import com.example.dieter.utils.roundTo

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
            Text("${nutrient.current.roundTo(1)}/${nutrient.of} ${nutrient.unit}")
        }
        val progress = if (nutrient.of <= 0) {
            nutrient.current / 1f
        } else nutrient.current / nutrient.of.toFloat()
        DieterProgressBar(progress = progress)
    }
}
