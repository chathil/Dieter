package com.example.dieter.ui.component

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Chip(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    start: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier.height(32.dp).padding(end = 4.dp).wrapContentWidth()
            .wrapContentHeight(Alignment.CenterVertically)
            .border(2.dp, color, RoundedCornerShape(50)).padding(8.dp)
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

@Preview
@Composable
fun ChipPreview() {
    Chip(content = { Text("Account", modifier = Modifier.padding(end = 4.dp)) })
}
