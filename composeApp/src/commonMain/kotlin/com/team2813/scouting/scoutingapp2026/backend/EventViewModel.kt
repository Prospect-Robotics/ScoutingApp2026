package com.team2813.scouting.scoutingapp2026.backend

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class EventViewModel(private val tbaRepo: BlueAllianceRepository) :
    ViewModel() {
    suspend fun setEvent(
        eventCode: String,
        allianceColor: AllianceColor,
        driveStationId: Int
    ) {
        tbaRepo.setCompetition(eventCode, allianceColor, driveStationId)
    }

    val allData: StateFlow<AllData> = tbaRepo.allData
}
