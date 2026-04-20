package com.team2813.scouting.scoutingapp2026.backend

interface Logger {
    fun e(key: String, message: String, cause: Throwable? = null)

    fun w(key: String, message: String, cause: Throwable? = null)

    fun i(key: String, message: String, cause: Throwable? = null)

    fun d(key: String, message: String, cause: Throwable? = null)
}
