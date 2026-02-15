package com.team2813.scouting.scoutingapp2026

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

class MutableIntStateProvider: PreviewParameterProvider<MutableIntState> {
    override val values: Sequence<MutableIntState> = sequenceOf(mutableIntStateOf(0))
}

@Composable
@Preview(showBackground = true)
fun ApproxFuel(
    // Require a fuel counter when calling this function, but provide a default value for the preview
    @PreviewParameter(MutableIntStateProvider::class)
    fuelCounter: MutableIntState,
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
//    Text("This is the Endgame screen!")
    var selectedClimb by remember { mutableStateOf("None") }
    var selectedSide by remember { mutableStateOf("N/A") }
    var numCycles by remember {mutableStateOf(0)}
    var isFailed by remember { mutableStateOf(false) }

    Column {
        Text("Climb Level")

        listOf("None","L1","L2","L3").forEach { level ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (selectedClimb == level),
                    onClick = {
                        selectedClimb = level
                        if (level == "None") {
                            selectedSide = "N/A"
                        }
                    }
                )
                Text(text = level)
            }
        }

        if(selectedClimb != "None") {
            Text("Select Side")
            listOf("Left","Middle","Right").forEach { side ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = (selectedSide == side), onClick = {
                            selectedSide = side
                        })
                    Text(text = side)
                }
            }

            Text("Failed")
            Checkbox(
                checked = isFailed,
                onCheckedChange = {isFailed = it}
            )

        }


        Text(text = "Cycles")
        Row(verticalAlignment = Alignment.CenterVertically){
            Button(onClick = { if (numCycles > 0) numCycles--}){
                Text("-")
            }
        }

        Text(text = numCycles.toString())

        Button(onClick = {numCycles++}){
            Text("+")
        }

    }

}

@Composable
@Preview(
    showBackground = true,
    widthDp = 300,
    heightDp = 400
)
fun Quantitative() {
    var currentScreen by remember { mutableStateOf(0) }
    AnimatedContent(
        currentScreen, modifier = Modifier.fillMaxSize(),
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically)
        ) {
            when (targetScreen) {
                0 -> Prematch()
                1 -> Autonomous()
                2 -> Teleoperated()
                3 -> Endgame()
            }
            Button(onClick = {currentScreen++}) {
                Text("Next")
            }
            if (targetScreen > 0) {
                Button(onClick = {
                    // make sure that we don't go under 0
                    if (currentScreen > 0) {
                        currentScreen--
                    }
                }) {
                    Text("Previous")
                }
            }
        }
    }
}