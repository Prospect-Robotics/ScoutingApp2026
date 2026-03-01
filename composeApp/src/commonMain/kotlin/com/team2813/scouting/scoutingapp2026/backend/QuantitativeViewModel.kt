package com.team2813.scouting.scoutingapp2026.backend

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.team2813.scouting.scoutingapp2026.Home
import com.team2813.scouting.scoutingapp2026.QRCode
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

class EndgameData {
    val selectedClimb = mutableStateOf("None")
    val selectedSide = mutableStateOf("N/A")
    val numCycles = mutableIntStateOf(0)
    val isFailed = mutableStateOf(false)
    val climbTime = mutableIntStateOf(0)
}

class QuantitativeViewModel(private val submitRepository: SubmitRepository) :
    ViewModel() {
    val autoData = AutoData()
    val teleopData = TeleopData()
    val preMatchData = PreMatchData()
    val endgameData = EndgameData()
    val currentScreen = mutableIntStateOf(0)

    suspend fun submit(navController: NavController) {
        val allData =
            SubmitRepository.SubmitData(
                preMatchData, autoData, teleopData, endgameData)
        try {
            submitRepository.submitPost(allData)
        } catch (_: Exception) {
            val url = submitRepository.getSubmitLink(allData)
            val route = QRCode(url)
            navController.navigate(route, navOptions { popUpTo(Home) })
        }
    }
}
