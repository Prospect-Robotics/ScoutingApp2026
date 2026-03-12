package com.team2813.scouting.scoutingapp2026.backend

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringArrayResource
import scoutingapp2026.composeapp.generated.resources.Res
import scoutingapp2026.composeapp.generated.resources.alliance_color

enum class AllianceColor {
    Red,
    Blue;
    //TODO: Implement the alliance color string array resource
    val value: String
        @Composable
        get() = stringArrayResource(Res.array.alliance_color)[ordinal]
}