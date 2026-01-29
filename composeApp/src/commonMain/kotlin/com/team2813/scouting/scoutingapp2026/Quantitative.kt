package com.team2813.scouting.scoutingapp2026

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.Role.Companion.Checkbox
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration
import kotlin.time.TimeSource

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
    teleopData: TeleopData = remember { TeleopData() }) {
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
        modifier = modifier.fillMaxWidth().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {


        Row(modifier = modifier, verticalAlignment = Alignment.Top) {
            Column(

                modifier = Modifier.fillMaxWidth(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                    Text("Teleoperated", fontSize = 20.sp)
                //Text(stringResource(Res.string.teleoperated), fontSize = 20.sp)

                IncrementButton("Cycles", teleopData.Cycles, enabled = true)
                //IncrementButton(stringResource(Res.string.cycles), teleopData.Cycles, enabled = true)
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Herding", fontSize = 20.sp)
            //Text(stringResource(Res.string.herding), fontSize = 20.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(herding, onCheckedChange = { herding = !herding }
                    )

            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Incapacitated", fontSize = 20.sp)
            //Text(stringResource(Res.string.incapacitated), fontSize = 20.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    incapacitatedMark != null,
                    onCheckedChange = { curr ->
                        if (curr) {
                            incapacitatedMark =
                                TimeSource.Monotonic.markNow()

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

}

        @Composable
        @Preview(showBackground = true)
        fun Endgame() {
            Text("This is the Endgame screen!")
        }

        @Composable
        @Preview(showBackground = true)
        fun Quantitative() {
            var currentScreen by remember { mutableStateOf(2) }
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
                    Button(onClick = { currentScreen++ }) {
                        Text("Next")
                    }
                    Button(onClick = { currentScreen-- }) {
                        Text("Previous")
                    }
                }
            }
        }
