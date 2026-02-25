package com.team2813.scouting.scoutingapp2026.backend

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class QuantitativeViewModel : ViewModel() {
    val shootPreload = mutableStateOf(false)
    val fuelOutpost = mutableStateOf(false)
    val fuelDepot = mutableStateOf(false)
    val fuelNeutral = mutableStateOf(false)
    val climb = mutableStateOf(false)

    val cycleNumber = mutableIntStateOf(0)
    val approxFuel = mutableIntStateOf(0)
}