package com.team2813.scouting.scoutingapp2026.backend

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlin.time.Duration

class AutoData {
    val shootPreload = mutableStateOf(false)
    val fuelOutpost = mutableStateOf(false)
    val fuelDepot = mutableStateOf(false)
    val fuelNeutral = mutableStateOf(false)
    val climb = mutableStateOf(false)
    val cycleNumber = mutableIntStateOf(0)
    val approxFuel = mutableIntStateOf(0)
}

class TeleopData {
    val Cycles = mutableIntStateOf(0)
    val Fuel = mutableIntStateOf(0)
    val incapacitatedTime = mutableStateOf(Duration.ZERO)
    val Herding = mutableStateOf(false)
}

class PreMatchData {
    val scoutingTeamNumber = mutableStateOf("")
    val nameOfHuman = mutableStateOf("")
    val matchNumber = mutableStateOf("")
}

class QuantitativeViewModel : ViewModel() {
    val autoData = AutoData()
    val teleopData = TeleopData()
    val preMatchData = PreMatchData()
}