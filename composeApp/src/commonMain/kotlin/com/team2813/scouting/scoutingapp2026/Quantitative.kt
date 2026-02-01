package com.team2813.scouting.scoutingapp2026

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview(showBackground = true)
fun ApproxFuel(
    fuelCounter: MutableState<Int> = remember { mutableIntStateOf(0) },
    enabled: Boolean = true
) {
    val incValues = intArrayOf(1, 5, 10)
    var fuelNum: Int by fuelCounter
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (value in incValues) {
                Button({
                    fuelNum -= value
                    if (fuelNum < 0) {
                        fuelNum = 0
                    }
                } , enabled = enabled) {
                    Text("-$value")
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                fuelNum,
                transitionSpec = {
                    if (targetState < initialState) {
                        slideInVertically { it } togetherWith slideOutVertically { -it }
                    } else {
                        slideInVertically { -it } togetherWith slideOutVertically { it }
                    }
                }
            ) { fuelCount ->
                Text(
                    "$fuelCount",
                    Modifier.width(35.dp),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (value in incValues) {
                Button({fuelNum += value}, enabled = enabled) {
                    Text("+$value")
                }
            }
        }
    }
}

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