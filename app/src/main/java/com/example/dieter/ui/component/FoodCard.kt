package com.example.dieter.ui.component

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.dieter.R
import com.example.dieter.data.source.domain.TodaysFoodModel
import com.example.dieter.ui.theme.DieterShapes
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

@SuppressLint("DefaultLocale")
@Composable
@OptIn(
    ExperimentalStdlibApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class
)
fun FoodCard(
    modifier: Modifier = Modifier,
    foodModel: TodaysFoodModel,
    deletable: Boolean = false,
    onDelete: () -> Unit = {}
) {
    val consumedAt = SimpleDateFormat(
        "HH:mm",
        Locale.UK
    ).format(foodModel.consumedAt)
    val squareSize = 64.dp

    val swipeableState = rememberSwipeableState(initialValue = 0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to states
    var showConfirmation by remember { mutableStateOf(false) }
    Box(
        modifier
            .background(MaterialTheme.colors.primary)
            .swipeable(
                enabled = deletable,
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { showConfirmation = true },
                modifier = Modifier
                    .size(64.dp)
                    .padding(start = 8.dp)
            ) {
                Icon(imageVector = Icons.Filled.DeleteForever, contentDescription = "delete")
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(MaterialTheme.colors.surface)
            ) {
                Spacer(Modifier.size(8.dp))
                Text("Are you sure?", style = MaterialTheme.typography.subtitle2)
                Row {
                    IconButton(onClick = { showConfirmation = false }, modifier.size(64.dp)) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "cancel")
                    }
                    IconButton(onClick = onDelete, modifier.size(64.dp)) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = "yes")
                    }
                }
            }
            Spacer(Modifier.size(16.dp))
        }
        AnimatedVisibility(
            visible = !showConfirmation,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background)
                    .padding(horizontal = 16.dp)
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
    }
}
