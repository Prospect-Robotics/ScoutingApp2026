package com.team2813.scouting.scoutingapp2026

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val darkColorScheme = darkColorScheme()
private val lightColorScheme = lightColorScheme()

@Composable
fun GearHeadsTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (isDarkMode) {
            darkColorScheme
        } else {
            lightColorScheme
        }
    MaterialTheme(colorScheme = colorScheme, content = content)
}
