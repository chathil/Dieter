package com.example.dieter.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Chip(
    modifier: Modifier = Modifier,
    colors: ChipColors = DieterDefaultChip.defaultColors(),
    selected: Boolean = false,
    start: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier.height(32.dp).padding(end = 4.dp).wrapContentWidth()
            .wrapContentHeight(Alignment.CenterVertically)
            .background(colors.backgroundColor(selected = selected).value, RoundedCornerShape(50))
            .border(2.dp, MaterialTheme.colors.primary, RoundedCornerShape(50)).padding(8.dp)
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.caption) {
            Spacer(modifier = Modifier.size(4.dp))
            start?.let {
                it()
                Spacer(modifier = Modifier.size(8.dp))
            }
            content()
            trailing?.let {
                Spacer(modifier = Modifier.size(8.dp))
                it()
            }
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}

class DieterDefaultChip {
    companion object {
        @Composable
        fun defaultColors(): ChipColors = DieterDefaultChipColors(
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.primary,
            selectedBackgroundColor = MaterialTheme.colors.primary,
            selectedContentColor = MaterialTheme.colors.background
        )
    }
}

@Immutable
private class DieterDefaultChipColors(
    private val backgroundColor: Color,
    private val contentColor: Color,
    private val selectedBackgroundColor: Color,
    private val selectedContentColor: Color
) : ChipColors {
    @Composable
    override fun backgroundColor(selected: Boolean): State<Color> {
        val backgroundColor by animateColorAsState(if (selected) selectedBackgroundColor else backgroundColor)
        return rememberUpdatedState(backgroundColor)
    }

    @Composable
    override fun contentColor(selected: Boolean): State<Color> {
        val contentColor by animateColorAsState(if (selected) selectedContentColor else contentColor)
        return rememberUpdatedState(contentColor)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DieterDefaultChipColors

        if (backgroundColor != other.backgroundColor) return false
        if (contentColor != other.contentColor) return false
        if (selectedBackgroundColor != other.selectedBackgroundColor) return false
        if (selectedContentColor != other.selectedContentColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = backgroundColor.hashCode()
        result = 31 * result + contentColor.hashCode()
        result = 31 * result + selectedBackgroundColor.hashCode()
        result = 31 * result + selectedContentColor.hashCode()
        return result
    }
}

@Stable
interface ChipColors {
    @Composable
    fun backgroundColor(selected: Boolean): State<Color>
    @Composable
    fun contentColor(selected: Boolean): State<Color>
}

@Preview
@Composable
fun ChipPreview() {
    Chip(content = { Text("Account", modifier = Modifier.padding(end = 4.dp)) })
}
