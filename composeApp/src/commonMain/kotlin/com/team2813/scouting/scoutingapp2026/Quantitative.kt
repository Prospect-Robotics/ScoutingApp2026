package com.team2813.scouting.scoutingapp2026

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.team2813.scouting.scoutingapp2026.backend.QuantitativeViewModel
import kotlin.time.TimeSource
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import org.koin.compose.viewmodel.koinViewModel
import scoutingapp2026.composeapp.generated.resources.*

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
fun Prematch(
    modifier: Modifier = Modifier,
    viewModel: QuantitativeViewModel = koinViewModel()
) {
    val preMatchData = viewModel.preMatchData
    Column(modifier, verticalArrangement = Arrangement.spacedBy(30.dp)) {
        var scoutingTeamNumber by
            preMatchData.scoutingTeamNumber // Name of the Scouting Team #
        TextField(
            value = scoutingTeamNumber,
            onValueChange = { scoutingTeamNumber = it },
            singleLine = true,
            label = { Text(stringResource(Res.string.teamNumber)) },
            keyboardOptions =
                KeyboardOptions(keyboardType = KeyboardType.Number))
        var nameOfHuman by
            preMatchData.nameOfHuman // Name of the Human being that is scouting
        TextField(
            value = nameOfHuman,
            onValueChange = { nameOfHuman = it },
            singleLine = true,
            label = { Text(stringResource(Res.string.nameOfPerson)) })
        var matchNumber by
            preMatchData.matchNumber // the match that the Human is scouting
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
fun Autonomous(
    modifier: Modifier = Modifier,
    viewModel: QuantitativeViewModel = koinViewModel()
) {
    val autoData = viewModel.autoData
    var approxFuel by autoData.approxFuel
    var shootPreload by autoData.shootPreload

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
                val stateArr =
                    arrayOf(
                        autoData.fuelNeutral to Res.string.auto_fuel_neutral,
                        autoData.fuelDepot to Res.string.auto_fuel_depot,
                        autoData.fuelOutpost to Res.string.auto_fuel_outpost,
                        autoData.climb to Res.string.climb)
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
            ApproxFuel(autoData.approxFuel, modifier = Modifier.fillMaxWidth())

            IncrementButton("Cycle counter", autoData.cycleNumber)
        }
}

@Composable
@Preview(showBackground = true)
fun Teleoperated(
    modifier: Modifier = Modifier,
    viewModel: QuantitativeViewModel = koinViewModel(),
) {
    val teleopData = viewModel.teleopData
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
                        IncrementButton(
                            stringResource(Res.string.cycles),
                            teleopData.Cycles,
                            enabled = true)
                    }
            }
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ApproxFuel(fuelCounter = teleopData.Fuel, enabled = true)
                }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(Res.string.herding), fontSize = 20.sp)
                Checkbox(herding, onCheckedChange = { herding = !herding })
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(Res.string.incap), fontSize = 20.sp)
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
fun Endgame(modifier: Modifier = Modifier) {
    //    Text("This is the Endgame screen!")
    var selectedClimb by remember { mutableStateOf("None") }
    var selectedSide by remember { mutableStateOf("N/A") }
    var numCycles by remember { mutableStateOf(0) }
    var isFailed by remember { mutableStateOf(false) }

    Column(modifier) {
        Text("Climb Level")

        listOf("None", "L1", "L2", "L3").forEach { level ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (selectedClimb == level),
                    onClick = {
                        selectedClimb = level
                        if (level == "None") {
                            selectedSide = "N/A"
                        }
                    })
                Text(text = level)
            }
        }

        if (selectedClimb != "None") {
            Text("Select Side")
            listOf("Left", "Middle", "Right").forEach { side ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = (selectedSide == side),
                        onClick = { selectedSide = side })
                    Text(text = side)
                }
            }

            Text("Failed")
            Checkbox(checked = isFailed, onCheckedChange = { isFailed = it })
        }

        Text(text = "Cycles")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { if (numCycles > 0) numCycles-- }) { Text("-") }
        }

        Text(text = numCycles.toString())

        Button(onClick = { numCycles++ }) { Text("+") }
    }
}

@Composable
fun climbTimer() {
    var seconds by remember { mutableStateOf(0) }
    var running by remember { mutableStateOf(false) }

    LaunchedEffect(running) {
        while (running) {
            kotlinx.coroutines.delay(1000)
            seconds++
        }
    }

    Column {
        Text("$seconds")
        Button(onClick = { running = !running }) {
            Text(if (running) "Stop" else "Start")
        }
        Button(onClick = { seconds = 0 }) { Text("Reset") }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 300, heightDp = 400)
fun Quantitative(navController: NavHostController) {
    var currentScreen by remember { mutableStateOf(0) }
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
            Box(Modifier.fillMaxSize()) {
                val modifier = Modifier.fillMaxWidth().align(Alignment.Center)
                when (targetScreen) {
                    0 -> Prematch(modifier)
                    1 -> Autonomous(modifier)
                    2 -> Teleoperated(modifier)
                    3 -> Endgame(modifier)
                }
                if (targetScreen < 3) {
                    Button(
                        onClick = {
                            // make sure that we don't go over 3
                            if (currentScreen < 3) {
                                currentScreen++
                            }
                        },
                        modifier = Modifier.align(Alignment.TopEnd)) {
                            Text(stringResource(Res.string.next))
                        }
                } else {
                    val coroutineScope = rememberCoroutineScope()
                    val viewModel: QuantitativeViewModel = koinViewModel()
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.submit(navController)
                            }
                        },
                        modifier = Modifier.align(Alignment.TopEnd)) {
                            Text(stringResource(Res.string.submit))
                        }
                }

                if (targetScreen > 0) {
                    Button(
                        onClick = {
                            // make sure that we don't go under 0
                            if (currentScreen > 0) {
                                currentScreen--
                            }
                        },
                        modifier = Modifier.align(Alignment.TopStart)) {
                            Text(stringResource(Res.string.prev))
                        }
                }
            }
        }
}
