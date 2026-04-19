package com.team2813.scouting.scoutingapp2026

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "2026 Gear Heads Scouting App",
    ) {
        App()
    }
}
