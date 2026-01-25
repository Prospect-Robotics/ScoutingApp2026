package com.team2813.scouting.scoutingapp2026

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview(showBackground = true)
fun Prematch() {
    Text("This is the Prematch screen!")
}

@Composable
@Preview(showBackground = true)
fun Autonomous() {
    Text("This is the Autonomous screen!")
}

@Composable
@Preview(showBackground = true)
fun Teleoperated() {
    Text("This is the Teleoperated screen!")
}

@Composable
@Preview(showBackground = true)
fun Endgame() {
    Text("This is the Endgame screen!")
}

@Composable
@Preview(showBackground = true)
fun Quantitative() {
    var currentScreen by remember { mutableStateOf(0) }
    AnimatedContent(
        currentScreen, modifier = Modifier.size(300.dp, 400.dp),
        transitionSpec = {
            if (targetState > initialState) {
                slideInHorizontally { width -> width } +
                        fadeIn() togetherWith
                        slideOutHorizontally { width -> -width } +
                        fadeOut()
            } else {
                slideInHorizontally { width -> -width } +
                        fadeIn() togetherWith
                        slideOutHorizontally { width -> width } +
                        fadeOut()
            }
        }
    ) { targetScreen ->
        Column {
            when (targetScreen) {
                0 -> Prematch()
                1 -> Autonomous()
                2 -> Teleoperated()
                3 -> Endgame()
            }
            Button(onClick = {currentScreen++}) {
                Text("Next")
            }
            Button(onClick = {currentScreen--}) {
                Text("Previous")
            }
        }
    }
}