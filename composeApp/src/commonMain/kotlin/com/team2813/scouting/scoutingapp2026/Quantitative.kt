package com.team2813.scouting.scoutingapp2026

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.time.Duration
import kotlin.time.TimeSource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import scoutingapp2026.composeapp.generated.resources.Res
import scoutingapp2026.composeapp.generated.resources.auto_fuel_depot
import scoutingapp2026.composeapp.generated.resources.auto_fuel_neutral
import scoutingapp2026.composeapp.generated.resources.auto_fuel_outpost
import scoutingapp2026.composeapp.generated.resources.auto_shoot_preload
import scoutingapp2026.composeapp.generated.resources.matchNumber
import scoutingapp2026.composeapp.generated.resources.nameOfPerson
import scoutingapp2026.composeapp.generated.resources.teamNumber

@Composable
fun IncrementButton(
    text: String,
    count: MutableState<Int>,
    maximum: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
) {
    if (maximum <= 0) {
        throw IllegalArgumentException(
            "Expected a maximum greater than 0, but got: $maximum")
    }
    var currentCount by count

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text, fontSize = 20.sp)
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { currentCount -= 1 },
                    enabled = currentCount > 0 && enabled,
                    contentPadding = PaddingValues(0.dp),
                    shape = RectangleShape,
                    modifier = Modifier.size(30.dp)) {
                        Text("-", fontSize = 17.sp)
                    }
                Button(
                    onClick = { currentCount += 1 },
                    enabled = currentCount < maximum && enabled) {
                        Text("$currentCount", fontSize = 20.sp)
                    }
            }
    }
}

class MutableIntStateProvider : PreviewParameterProvider<MutableIntState> {
    override val values: Sequence<MutableIntState> =
        sequenceOf(mutableIntStateOf(0), mutableIntStateOf(1000))
}

@Composable
@Preview(showBackground = true)
fun ApproxFuel(
    // Require a fuel counter when calling this function, but provide a default
    // value for the preview
    @PreviewParameter(MutableIntStateProvider::class)
    fuelCounter: MutableIntState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val incValues = intArrayOf(1, 5, 10)
    var fuelNum: Int by fuelCounter
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement =
            Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally),
        modifier = modifier) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(70.dp)) {
                    for (value in incValues) {
                        Button(
                            onClick = { fuelNum = maxOf(fuelNum - value, 0) },
                            modifier =
                                Modifier.padding(vertical = 10.dp)
                                    .fillMaxWidth(),
                            enabled = enabled,
                            contentPadding = PaddingValues(vertical = 10.dp)) {
                                Text("-$value")
                            }
                    }
                }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AnimatedContent(
                    fuelNum,
                    transitionSpec = {
                        if (targetState < initialState) {
                            slideInVertically { it } togetherWith
                                slideOutVertically { -it }
                        } else {
                            slideInVertically { -it } togetherWith
                                slideOutVertically { it }
                        }
                    }) { fuelCount ->
                        Text(
                            "$fuelCount",
                            Modifier.width(35.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center)
                    }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(70.dp)) {
                    for (value in incValues) {
                        Button(
                            onClick = { fuelNum += value },
                            modifier =
                                Modifier.padding(vertical = 10.dp)
                                    .fillMaxWidth(),
                            enabled = enabled,
                            contentPadding = PaddingValues(vertical = 10.dp)) {
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
        var scoutingTeamNumber by remember {
            mutableStateOf("")
        } // Name of the Scouting Team #
        TextField(
            value = scoutingTeamNumber,
            onValueChange = { scoutingTeamNumber = it },
            singleLine = true,
            label = { Text(stringResource(Res.string.teamNumber)) },
            keyboardOptions =
                KeyboardOptions(keyboardType = KeyboardType.Number))
        var nameOfHuman by remember {
            mutableStateOf("")
        } // Name of the Human being that is scouting
        TextField(
            value = nameOfHuman,
            onValueChange = { nameOfHuman = it },
            singleLine = true,
            label = { Text(stringResource(Res.string.nameOfPerson)) })
        var matchNumber by remember {
            mutableStateOf("")
        } // the match that the Human is scouting
        TextField(
            value = matchNumber,
            onValueChange = { matchNumber = it },
            singleLine = true,
            label = { Text(stringResource(Res.string.matchNumber)) },
            keyboardOptions =
                KeyboardOptions(keyboardType = KeyboardType.Number))
    }
}

@Composable
@Preview(showBackground = true)
fun Autonomous(modifier: Modifier = Modifier) {
    val _approxFuel = remember { mutableIntStateOf(0) }
    var approxFuel by _approxFuel
    var shootPreload by remember { mutableStateOf(false) }
    val cycleNumber = remember { mutableStateOf(0) }

    Column(
        modifier = modifier,
        verticalArrangement =
            Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {
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
                    Checkbox(shootPreload, onCheckedChange = change)
                    Text(
                        stringResource(Res.string.auto_shoot_preload),
                        modifier = Modifier.clickable { change(!shootPreload) })
                }
                val fuelOutpost = remember { mutableStateOf(false) }
                val fuelDepot = remember { mutableStateOf(false) }
                val fuelNeutral = remember { mutableStateOf(false) }
                val stateArr =
                    arrayOf(
                        fuelNeutral to Res.string.auto_fuel_neutral,
                        fuelDepot to Res.string.auto_fuel_depot,
                        fuelOutpost to Res.string.auto_fuel_outpost)
                for ((mutableState, str) in stateArr) {
                    var state by mutableState
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            state,
                            onCheckedChange = { state = it },
                        )
                        Text(
                            stringResource(str),
                            modifier = Modifier.clickable { state = !state })
                    }
                }
            }

            ApproxFuel(_approxFuel, modifier = Modifier.fillMaxWidth())

            IncrementButton("Cycle counter", cycleNumber)
        }
}

data class TeleopData(
    val Cycles: MutableState<Int> = mutableStateOf(0),
    val Fuel: MutableState<Int> = mutableStateOf(0),
    val incapacitatedTime: MutableState<Duration> =
        mutableStateOf(Duration.ZERO),
    val Herding: MutableState<Boolean> = mutableStateOf(false),
)

@Composable
@Preview(showBackground = true)
fun Teleoperated(
    modifier: Modifier = Modifier,
    teleopData: TeleopData = remember { TeleopData() }
) {
    var cycles by teleopData.Cycles
    var fuel by teleopData.Fuel
    var herding by teleopData.Herding
    var incapacitatedMark: TimeSource.Monotonic.ValueTimeMark? by remember {
        mutableStateOf(null)
    }
    var incapacitatedTime by teleopData.incapacitatedTime
    DisposableEffect(teleopData) {
        onDispose {
            incapacitatedMark?.let { incapacitatedTime += it.elapsedNow() }
            incapacitatedMark = null
        }
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(10.dp, Alignment.CenterVertically)) {
            Row(modifier = modifier, verticalAlignment = Alignment.Top) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        Text("Teleoperated", fontSize = 20.sp)
                        // Text(stringResource(Res.string.teleoperated),
                        // fontSize = 20.sp)

                        IncrementButton(
                            "Cycles", teleopData.Cycles, enabled = true)
                        // IncrementButton(stringResource(Res.string.cycles),
                        // teleopData.Cycles, enabled = true)
                    }
            }
            val fuelAmount = mutableIntStateOf(0)
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ApproxFuel(fuelCounter = fuelAmount, enabled = true)
                }
            // Text(stringResource(Res.string.herding), fontSize = 20.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Herding", fontSize = 20.sp)
                Checkbox(herding, onCheckedChange = { herding = !herding })
            }
            // Text(stringResource(Res.string.incapacitated), fontSize = 20.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Incapacitated", fontSize = 20.sp)
                Checkbox(
                    incapacitatedMark != null,
                    onCheckedChange = { curr ->
                        if (curr) {
                            incapacitatedMark = TimeSource.Monotonic.markNow()
                        } else {
                            incapacitatedMark?.let {
                                incapacitatedTime += it.elapsedNow()
                            }
                            incapacitatedMark = null
                        }
                    })
            }
        }
}

@Composable
@Preview(showBackground = true)
fun Endgame() {
    Text("This is the Endgame screen!")
}

@Composable
@Preview(showBackground = true, widthDp = 300, heightDp = 400)
fun Quantitative() {
    var currentScreen by remember { mutableStateOf(2) }
    AnimatedContent(
        currentScreen,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = {
            if (targetState > initialState) {
                slideInHorizontally { width -> width } + fadeIn() togetherWith
                    slideOutHorizontally { width -> -width } + fadeOut()
            } else {
                slideInHorizontally { width -> -width } + fadeIn() togetherWith
                    slideOutHorizontally { width -> width } + fadeOut()
            }
        }) { targetScreen ->
            Box {
                val modifier = Modifier.fillMaxWidth().align(Alignment.Center)
                when (targetScreen) {
                    0 -> Prematch()
                    1 -> Autonomous(modifier)
                    2 -> Teleoperated()
                    3 -> Endgame()
                }
                Button(
                    onClick = { currentScreen++ },
                    modifier = Modifier.align(Alignment.BottomEnd)) {
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
                        modifier = Modifier.align(Alignment.BottomStart)) {
                            Text("Previous")
                        }
                }
            }
        }
}
