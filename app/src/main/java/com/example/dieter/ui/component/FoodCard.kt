package com.example.dieter.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dieter.R
import com.example.dieter.data.source.domain.TodaysFoodModel
import com.example.dieter.ui.theme.DieterShapes
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
@OptIn(ExperimentalStdlibApi::class)
fun FoodCard(foodModel: TodaysFoodModel, modifier: Modifier = Modifier) {
    val consumedAt = SimpleDateFormat(
        "HH:mm",
        Locale.UK
    ).format(foodModel.consumedAt)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .defaultMinSize(38.dp, 38.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
        ) {
            Text(
                consumedAt,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(4.dp)
            )
        }
        Spacer(Modifier.size(8.dp))
        Image(
            painter = painterResource(id = R.drawable.fake_food),
            contentScale = ContentScale.Crop,
            contentDescription = "food picture",
            modifier = Modifier
                .size(64.dp)
                .background(Color.Transparent, DieterShapes.small)
                .clip(DieterShapes.small)
        )
        Spacer(Modifier.size(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                foodModel.type.toString().lowercase().capitalize(),
                style = MaterialTheme.typography.subtitle2
            )
            Text("${foodModel.cal} kcal", style = MaterialTheme.typography.caption)
        }
    }
}
