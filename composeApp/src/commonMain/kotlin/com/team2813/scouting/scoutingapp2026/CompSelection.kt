package com.team2813.scouting.scoutingapp2026

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.team2813.scouting.scoutingapp2026.backend.AllData
import com.team2813.scouting.scoutingapp2026.backend.AllianceColor
import com.team2813.scouting.scoutingapp2026.backend.EventViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import scoutingapp2026.composeapp.generated.resources.Res
import scoutingapp2026.composeapp.generated.resources.TBAConfirmation
import scoutingapp2026.composeapp.generated.resources.alliance
import scoutingapp2026.composeapp.generated.resources.back
import scoutingapp2026.composeapp.generated.resources.driverStation
import scoutingapp2026.composeapp.generated.resources.dsNum
import scoutingapp2026.composeapp.generated.resources.eventCode
import scoutingapp2026.composeapp.generated.resources.eventName
import scoutingapp2026.composeapp.generated.resources.no
import scoutingapp2026.composeapp.generated.resources.openTBAConfirmation
import scoutingapp2026.composeapp.generated.resources.yes

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
            CompSelector { eventCode, allianceColor, dsNum, forceRefresh ->
                coroutineScope.launch {
                    eventViewModel.setEvent(
                        eventCode, allianceColor, dsNum, forceRefresh)
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
        CompSelector { code, alliance, dsNum, _ ->
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
    setComp: (String, AllianceColor, Int, Boolean) -> Unit,
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
            var showConfirmation by remember { mutableStateOf(false) }
            val haptics = LocalHapticFeedback.current
            val buttonColors = ButtonDefaults.buttonColors()
            // This surface + the composition local provider is to try and
            // emulate an actual `Button` composable
            // We can't use one of those because we need to handle the long
            // click.
            // To do that, we use combinedClickable, which doesn't work on
            // buttons (the button eats the input; it's too tasty)
            Surface(
                modifier =
                    Modifier.combinedClickable(
                            interactionSource = null,
                            indication = LocalIndication.current,
                            onClick = {
                                haptics.performHapticFeedback(
                                    HapticFeedbackType.LongPress)
                                setComp(eventCode, allianceColor, dsNum, false)
                            },
                            onLongClickLabel =
                                stringResource(Res.string.openTBAConfirmation),
                            onLongClick = { showConfirmation = true },
                        )
                        .semantics { role = Role.Button },
                color = buttonColors.containerColor,
                contentColor = buttonColors.contentColor,
                shape = ButtonDefaults.shape) {
                    val textStyle =
                        LocalTextStyle.current.merge(
                            MaterialTheme.typography.labelLarge)
                    CompositionLocalProvider(
                        LocalTextStyle provides textStyle,
                        LocalContentColor provides buttonColors.contentColor) {
                            Row(
                                Modifier.defaultMinSize(
                                        minWidth = ButtonDefaults.MinWidth,
                                        minHeight = ButtonDefaults.MinHeight,
                                    )
                                    .padding(ButtonDefaults.ContentPadding),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text("Fetch")
                            }
                        }
                }
            if (showConfirmation) {
                val dismiss = { showConfirmation = false }
                val accept = {
                    setComp(eventCode, allianceColor, dsNum, true)
                    dismiss()
                }
                Dialog(
                    onDismissRequest = { dismiss() },
                ) {
                    Card(
                        Modifier.fillMaxWidth().padding(16.dp),
                        shape = RoundedCornerShape(16.dp)) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment =
                                    Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    stringResource(Res.string.TBAConfirmation),
                                    modifier = Modifier.padding(16.dp),
                                    textAlign = TextAlign.Center)
                                Row {
                                    Button(
                                        onClick = dismiss,
                                        modifier = Modifier.padding(8.dp),
                                    ) {
                                        Text(stringResource(Res.string.no))
                                    }
                                    Button(
                                        onClick = accept,
                                        modifier = Modifier.padding(8.dp),
                                    ) {
                                        Text(stringResource(Res.string.yes))
                                    }
                                }
                            }
                        }
                }
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
            ProvideTextStyle(TextStyle(textAlign = TextAlign.Center)) {
                Text(
                    stringResource(Res.string.eventName),
                    fontWeight = FontWeight.Bold,
                )
                Text("${allData.eventName}")

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    stringResource(Res.string.eventCode),
                    fontWeight = FontWeight.Bold,
                )
                Text("${allData.eventCode}")

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    stringResource(Res.string.driverStation),
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "${allData.allianceColor.value} ${allData.driverStationNum}")
            }
        }
}
