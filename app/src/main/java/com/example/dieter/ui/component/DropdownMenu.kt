package com.example.dieter.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dieter.data.source.domain.MeasureModel
import com.example.dieter.ui.theme.DieterShapes
import com.example.dieter.ui.theme.DieterTheme

@Composable
fun MeasurementDropdown(
    modifier: Modifier = Modifier,
    measurements: List<MeasureModel> = emptyList()
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    if (selectedText.isEmpty())
        selectedText = measurements.firstOrNull()?.label ?: "No Units Found"

    val icon = if (expanded)
        Icons.Filled.ExpandLess
    else
        Icons.Filled.ExpandMore

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                Icon(icon, "contentDescription", Modifier.clickable { expanded = !expanded })
            },
            maxLines = 1
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            measurements.forEach { label ->
                DropdownMenuItem(
                    onClick = {
                        selectedText = label.label
                        expanded = false
                    }
                ) {
                    Text(text = label.label)
                }
            }
        }
    }
}

@Preview
@Composable
private fun DropdownMenuPreview() {
    DieterTheme {
        Column {
            Row {
                MeasurementDropdown()
            }
            Spacer(Modifier.size(32.dp))
            Row(
                modifier = Modifier
                    .width(192.dp)
                    .wrapContentHeight()
                    .border(2.dp, MaterialTheme.colors.primary, DieterShapes.small)
            ) {
                Spacer(Modifier.size(4.dp))
                MeasurementDropdown(Modifier.padding(bottom = 4.dp))
                Spacer(Modifier.size(4.dp))
                OutlinedTextField(
                    value = "999",
                    onValueChange = { },
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Spacer(Modifier.size(4.dp))
            }
        }
    }
}
