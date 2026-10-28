package com.Duoc.Themis.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val EsquemaThemis = darkColorScheme(
    primary = Dorado,
    onPrimary = Negro,
    secondary = AzulMarino,
    onSecondary = Dorado,
    background = Negro,
    onBackground = Blanco,
    surface = Negro,
    onSurface = Blanco,
    outline = Dorado
)

@Composable
fun ThemisTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaThemis,
        typography = Typography,
        content = content
    )
}
