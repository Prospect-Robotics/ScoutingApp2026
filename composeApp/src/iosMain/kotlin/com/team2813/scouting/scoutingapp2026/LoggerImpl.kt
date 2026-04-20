package com.team2813.scouting.scoutingapp2026

import com.team2813.scouting.scoutingapp2026.backend.Logger
import platform.Foundation.NSLog

class LoggerImpl : Logger {
    override fun e(key: String, message: String, cause: Throwable?) {
        cause?.let {
            NSLog(
                "[$key] ERROR - $message. CAUSED BY ${it.stackTraceToString()}")
        } ?: NSLog("[$key] ERROR - $message")
    }

    override fun w(key: String, message: String, cause: Throwable?) {
        cause?.let {
            NSLog(
                "[$key] WARNING - $message. CAUSED BY ${it.stackTraceToString()}")
        } ?: NSLog("[$key] WARNING - $message")
    }

    override fun i(key: String, message: String, cause: Throwable?) {
        cause?.let {
            NSLog(
                "[$key] INFO - $message. CAUSED BY ${it.stackTraceToString()}")
        } ?: NSLog("[$key] INFO - $message")
    }

    override fun d(key: String, message: String, cause: Throwable?) {
        cause?.let {
            NSLog(
                "[$key] DEBUG - $message. CAUSED BY ${it.stackTraceToString()}")
        } ?: NSLog("[$key] DEBUG - $message")
    }
}
