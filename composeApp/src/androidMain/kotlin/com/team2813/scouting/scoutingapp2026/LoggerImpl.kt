package com.team2813.scouting.scoutingapp2026

import android.util.Log
import com.team2813.scouting.scoutingapp2026.backend.Logger

class LoggerImpl : Logger {
    override fun e(key: String, message: String, cause: Throwable?) {
        cause?.let { Log.e(key, message, it) } ?: Log.e(key, message)
    }

    override fun w(key: String, message: String, cause: Throwable?) {
        cause?.let { Log.w(key, message, it) } ?: Log.w(key, message)
    }

    override fun i(key: String, message: String, cause: Throwable?) {
        cause?.let { Log.i(key, message, it) } ?: Log.i(key, message)
    }

    override fun d(key: String, message: String, cause: Throwable?) {
        cause?.let { Log.d(key, message, it) } ?: Log.d(key, message)
    }
}
