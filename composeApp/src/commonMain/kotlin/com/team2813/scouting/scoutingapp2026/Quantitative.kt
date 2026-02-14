package com.team2813.scouting.scoutingapp2026

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import scoutingapp2026.composeapp.generated.resources.Res
import scoutingapp2026.composeapp.generated.resources.matchNumber
import scoutingapp2026.composeapp.generated.resources.nameOfPerson
import scoutingapp2026.composeapp.generated.resources.teamNumber

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
    Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
        var scoutingTeamNumber by remember { mutableStateOf("") } // Name of the Scouting Team #
        TextField(
            value = scoutingTeamNumber,
            onValueChange = { scoutingTeamNumber = it },
            singleLine = true,
            label = {
                Text(stringResource(Res.string.teamNumber))
            },
            keyboardOptions = KeyboardOptions(keyboardType= KeyboardType.Number)
        )
        var nameOfHuman by remember { mutableStateOf("") } // Name of the Human being that is scouting
        TextField(
            value = nameOfHuman,
            onValueChange = { nameOfHuman = it },
            singleLine = true,
            label = {
                Text(stringResource(Res.string.nameOfPerson))
            }
        )
        var matchNumber by remember { mutableStateOf("") } // the match that the Human is scouting
        TextField(
            value = matchNumber,
            onValueChange = { matchNumber = it },
            singleLine = true,
            label = {
                Text(stringResource(Res.string.matchNumber))
            },
            keyboardOptions = KeyboardOptions(keyboardType= KeyboardType.Number)
        )

    }
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