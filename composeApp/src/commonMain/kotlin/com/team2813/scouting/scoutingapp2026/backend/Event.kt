package com.team2813.scouting.scoutingapp2026.backend

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Event(
    val name: String,
    @SerialName("event_code") val eventCode: String,
)