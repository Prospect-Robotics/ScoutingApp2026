package com.team2813.scouting.scoutingapp2026

import com.team2813.scouting.scoutingapp2026.backend.Logger

class LoggerImpl : Logger {
    override fun e(key: String, message: String, cause: Throwable?) {
        cause?.let {
            println(
                "[$key] ERROR - $message. CAUSED BY ${it.stackTraceToString()}")
        } ?: println("[$key] ERROR - $message")
    }

    override fun w(key: String, message: String, cause: Throwable?) {
        cause?.let {
            println(
                "[$key] WARNING - $message. CAUSED BY ${it.stackTraceToString()}")
        } ?: println("[$key] WARNING - $message")
    }

    override fun i(key: String, message: String, cause: Throwable?) {
        cause?.let {
            println(
                "[$key] INFO - $message. CAUSED BY ${it.stackTraceToString()}")
        } ?: println("[$key] INFO - $message")
    }

    override fun d(key: String, message: String, cause: Throwable?) {
        cause?.let {
            println(
                "[$key] DEBUG - $message. CAUSED BY ${it.stackTraceToString()}")
        } ?: println("[$key] DEBUG - $message")
    }
}
