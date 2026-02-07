package com.team2813.scouting.scoutingapp2026

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import scoutingapp2026.composeapp.generated.resources.Res
import scoutingapp2026.composeapp.generated.resources.auto_fuel_depot
import scoutingapp2026.composeapp.generated.resources.auto_fuel_neutral
import scoutingapp2026.composeapp.generated.resources.auto_fuel_outpost
import scoutingapp2026.composeapp.generated.resources.auto_shoot_preload

class MutableIntStateProvider: PreviewParameterProvider<MutableIntState> {
    override val values: Sequence<MutableIntState> = sequenceOf(mutableIntStateOf(0), mutableIntStateOf(1000))
}

@Composable
@Preview(showBackground = true)
fun ApproxFuel(
    // Require a fuel counter when calling this function, but provide a default value for the preview
    @PreviewParameter(MutableIntStateProvider::class)
    fuelCounter: MutableIntState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val incValues = intArrayOf(1, 5, 10)
    var fuelNum: Int by fuelCounter
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(70.dp)
        ) {
            for (value in incValues) {
                Button(
                    onClick = {
                        fuelNum = maxOf(fuelNum - value, 0)
                    },
                    modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth(),
                    enabled = enabled,
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(70.dp)
        ) {
            for (value in incValues) {
                Button(
                    onClick = {fuelNum += value},
                    modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth(),
                    enabled = enabled,
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
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
fun Autonomous(modifier: Modifier = Modifier) {
    val _approxFuel = remember { mutableIntStateOf(0) }
    var approxFuel by _approxFuel
    var shootPreload by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val change: (Boolean) -> Unit = {
                    shootPreload = it
                    if (it) {
                        approxFuel += 8
                    } else {
                        approxFuel = maxOf(approxFuel - 8, 0)
                    }
                }
                Checkbox(
                    shootPreload,
                    onCheckedChange = change
                )
                Text(
                    stringResource(Res.string.auto_shoot_preload),
                    modifier = Modifier.clickable { change (!shootPreload)}
                )
            }
            val fuelOutpost = remember { mutableStateOf(false) }
            val fuelDepot = remember { mutableStateOf(false) }
            val fuelNeutral = remember { mutableStateOf(false) }
            val stateArr = arrayOf(
                fuelNeutral to Res.string.auto_fuel_neutral,
                fuelDepot to Res.string.auto_fuel_depot,
                fuelOutpost to Res.string.auto_fuel_outpost
            )
            for ((mutableState, str) in stateArr) {
                var state by mutableState
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        state,
                        onCheckedChange = { state = it },
                    )
                    Text(
                        stringResource(str),
                        modifier = Modifier.clickable {
                            state = !state
                        }
                    )
                }
            }
        }

        ApproxFuel(_approxFuel, modifier = Modifier.fillMaxWidth())
    }
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
        Box {
            val modifier = Modifier.fillMaxWidth().align(Alignment.Center)
            when (targetScreen) {
                0 -> Prematch()
                1 -> Autonomous(modifier)
                2 -> Teleoperated()
                3 -> Endgame()
            }
            Button(
                onClick = {currentScreen++},
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Text("Next")
            }
            if (targetScreen > 0) {
                Button(
                    onClick = {
                        // make sure that we don't go under 0
                        if (currentScreen > 0) {
                            currentScreen--
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomStart)
                ) {
                    Text("Previous")
                }
            }
        }
    }
}