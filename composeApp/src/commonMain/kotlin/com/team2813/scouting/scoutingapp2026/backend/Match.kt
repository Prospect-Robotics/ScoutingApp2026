package com.team2813.scouting.scoutingapp2026.backend

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Match(
    @SerialName("comp_level") val compLevel: CompLevel,
    @SerialName("match_number") val matchNumber: Int,
    val alliances: Alliances,
)

@Serializable
internal enum class CompLevel {
    @SerialName("qm") Qualifications,
    @SerialName("ef") Playoff,
    @SerialName("qf") Quarterfinals,
    @SerialName("sf") Semifinals,
    @SerialName("f") Finals,
}

@Serializable
internal data class Alliance(@SerialName("team_keys") val teams: List<String>)

@Serializable
internal data class Alliances(val red: Alliance, val blue: Alliance)
