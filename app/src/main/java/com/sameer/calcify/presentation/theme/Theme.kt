package com.sameer.calcify.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CalcifyColorScheme = darkColorScheme(
    primary = CalcifyAccent,
    onPrimary = CalcifyBackground,
    secondary = CalcifyAccent,
    background = CalcifyBackground,
    onBackground = CalcifyText,
    surface = CalcifySurface,
    onSurface = CalcifyText
)

@Composable
fun CalcifyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CalcifyColorScheme,
        content = content
    )
}
