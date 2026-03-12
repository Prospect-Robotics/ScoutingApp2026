package com.team2813.scouting.scoutingapp2026.backend

import kotlinx.serialization.Serializable

@Serializable
data class AllData(
    val eventName: String? = null,
    val eventCode: String? = null,
    val allianceColor: AllianceColor = AllianceColor.Blue,
    val driverStationNum: Int = 1,
    val matchMap: Map<String, String> = mapOf()
)
