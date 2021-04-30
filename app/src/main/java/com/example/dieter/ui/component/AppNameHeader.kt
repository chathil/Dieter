package com.example.dieter.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dieter.ui.theme.DieterLightColors
import com.example.dieter.ui.theme.DieterTheme

@Composable
fun AppNameHeader() {
    Surface {
        Row {
            Box(Modifier.size(28.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val trianglePath = Path().apply {
                        val size = 28.dp.toPx()
                        moveTo(size / 2f, 0f)
                        lineTo(size, size)
                        lineTo(0f, size)
                    }
                    drawPath(
                        path = trianglePath,
                        color = DieterLightColors.primary
                    )
                }
            }
            Spacer(Modifier.size(8.dp))
            Text("dieter", style = MaterialTheme.typography.h6)
        }
    }
}

@Preview
@Composable
fun AppNameHeaderPreview() {
    DieterTheme {
        AppNameHeader()
    }
}
