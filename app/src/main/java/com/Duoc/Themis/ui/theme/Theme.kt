package com.Duoc.Themis.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EsquemaThemis = lightColorScheme(
    primary = Dorado,
    onPrimary = AzulMarino,
    secondary = AzulMarino,
    onSecondary = Dorado,
    background = Cafe,
    onBackground = AzulMarino,
    surface = Cafe,
    onSurface = AzulMarino,
    outline = AzulMarino
)

@Composable
fun ThemisTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaThemis,
        typography = Typography,
        content = content
    )
}
