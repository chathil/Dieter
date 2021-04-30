package com.example.dieter.ui.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ButtonColors
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import com.example.dieter.ui.theme.AlphaNearTransparent
import com.example.dieter.ui.theme.AlphaReallyTransparent
import com.example.dieter.ui.theme.DieterDarkColors
import com.example.dieter.ui.theme.DieterLightColors
import com.example.dieter.ui.theme.GreenPrimary

object DieterDefaultButton {
    @Composable
    fun defaultColors(
        backgroundColor: Color =
            if (isSystemInDarkTheme()) DieterDarkColors.primary
            else DieterLightColors.primary.copy(AlphaReallyTransparent),
        contentColor: Color = Color.Black,
        disabledBackgroundColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            .compositeOver(MaterialTheme.colors.surface),
        disabledContentColor: Color = MaterialTheme.colors.onSurface
            .copy(alpha = ContentAlpha.disabled)
    ): ButtonColors = DieterDefaultButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor
    )

    @Composable
    fun outlinedColors(
        backgroundColor: Color =
            if (isSystemInDarkTheme()) DieterDarkColors.primary.copy(AlphaReallyTransparent)
            else DieterLightColors.primary.copy(AlphaReallyTransparent),
        contentColor: Color = if (isSystemInDarkTheme()) DieterDarkColors.primary else DieterLightColors.primary,
        disabledBackgroundColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            .compositeOver(MaterialTheme.colors.surface),
        disabledContentColor: Color = MaterialTheme.colors.onSurface
            .copy(alpha = ContentAlpha.disabled)
    ): ButtonColors = DieterDefaultButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor
    )

    @Composable
    fun burnCaloriesColors(
        backgroundColor: Color = GreenPrimary.copy(AlphaNearTransparent),
        contentColor: Color = Color.Black,
        disabledBackgroundColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            .compositeOver(MaterialTheme.colors.surface),
        disabledContentColor: Color = MaterialTheme.colors.onSurface
            .copy(alpha = ContentAlpha.disabled)
    ): ButtonColors = DieterDefaultButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor
    )
}

@Immutable
private class DieterDefaultButtonColors(
    private val backgroundColor: Color,
    private val contentColor: Color,
    private val disabledBackgroundColor: Color,
    private val disabledContentColor: Color
) : ButtonColors {
    @Composable
    override fun backgroundColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) backgroundColor else disabledBackgroundColor)
    }

    @Composable
    override fun contentColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) contentColor else disabledContentColor)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DieterDefaultButtonColors

        if (backgroundColor != other.backgroundColor) return false
        if (contentColor != other.contentColor) return false
        if (disabledBackgroundColor != other.disabledBackgroundColor) return false
        if (disabledContentColor != other.disabledContentColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = backgroundColor.hashCode()
        result = 31 * result + contentColor.hashCode()
        result = 31 * result + disabledBackgroundColor.hashCode()
        result = 31 * result + disabledContentColor.hashCode()
        return result
    }
}
