package com.team2813.scouting.scoutingapp2026

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
