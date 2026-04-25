package com.team2813.scouting.scoutingapp2026

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.team2813.scouting.scoutingapp2026.backend.AllData
import com.team2813.scouting.scoutingapp2026.backend.AllianceColor
import com.team2813.scouting.scoutingapp2026.backend.EventViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import scoutingapp2026.composeapp.generated.resources.Res
import scoutingapp2026.composeapp.generated.resources.alliance
import scoutingapp2026.composeapp.generated.resources.back
import scoutingapp2026.composeapp.generated.resources.driverStation
import scoutingapp2026.composeapp.generated.resources.dsNum
import scoutingapp2026.composeapp.generated.resources.eventCode
import scoutingapp2026.composeapp.generated.resources.eventName

@Composable
fun CompSelection(
    navController: NavHostController,
    eventViewModel: EventViewModel = koinViewModel()
) {
    val allData by eventViewModel.allData.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(eventViewModel) { eventViewModel.load() }
    Box(modifier = Modifier.fillMaxSize()) {
        Button(
            { navController.popBackStack() },
            modifier = Modifier.align(Alignment.TopStart)) {
                Text(stringResource(Res.string.back))
            }
        Column(modifier = Modifier.align(Alignment.Center)) {
            CompSelector { eventCode, allianceColor, dsNum ->
                coroutineScope.launch {
                    eventViewModel.setEvent(eventCode, allianceColor, dsNum)
                }
            }
            if (allData.eventCode != null) {
                HorizontalDivider()
                CompSelectInternal(allData)
            }
        }
    }
}

@Composable
@Preview
private fun CompSelectorPreview() {
    var data: AllData by remember { mutableStateOf(AllData()) }
    Column {
        CompSelector { code, alliance, dsNum ->
            data =
                AllData(
                    eventName = "Unknown",
                    eventCode = code,
                    allianceColor = alliance,
                    driverStationNum = dsNum,
                )
        }
        if (data.eventCode != null) {
            HorizontalDivider()
            CompSelectInternal(data)
        }
    }
}

@Composable
private fun CompSelector(
    setComp: (String, AllianceColor, Int) -> Unit,
) {
    var eventCode by remember { mutableStateOf("") }
    var allianceColor by remember { mutableStateOf(AllianceColor.Blue) }
    var dsNum by remember { mutableIntStateOf(1) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxWidth()) {
            TextField(
                eventCode,
                { eventCode = it },
                label = { Text(stringResource(Res.string.eventCode)) },
                placeholder = { Text("CASNV") },
                singleLine = true)
            Text(stringResource(Res.string.alliance))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.selectableGroup()) {
                    for (value in AllianceColor.entries) {
                        val selected = allianceColor == value
                        val select = { allianceColor = value }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier =
                                Modifier.selectable(
                                    selected, onClick = select)) {
                                RadioButton(selected, onClick = select)
                                Text(value.value)
                            }

                        Spacer(modifier = Modifier.width(5.dp))
                    }
                }
            Text(stringResource(Res.string.dsNum))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.selectableGroup()) {
                    for (value in 1..3) {
                        val selected = dsNum == value
                        val select = { dsNum = value }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier =
                                Modifier.selectable(
                                    selected, onClick = select)) {
                                RadioButton(
                                    selected = selected, onClick = select)
                                Text("$value")
                            }
                        Spacer(modifier = Modifier.width(5.dp))
                    }
                }
            Button(onClick = { setComp(eventCode, allianceColor, dsNum) }) {
                Text("Fetch")
            }
        }
}

@Composable
private fun CompSelectInternal(allData: AllData) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(0.dp, Alignment.CenterVertically)) {
            Text(
                stringResource(Res.string.eventName),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text("${allData.eventName}")

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                stringResource(Res.string.eventCode),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text("${allData.eventCode}")

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                stringResource(Res.string.driverStation),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text("${allData.allianceColor.value} ${allData.driverStationNum}")
        }
}
