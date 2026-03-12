package com.team2813.scouting.scoutingapp2026

import android.content.ClipData
import android.os.Build
import androidx.compose.ui.platform.ClipEntry
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.android.Android

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient =
    HttpClient(Android) {
        config(this)
        engine {
            connectTimeout = 100_000
            socketTimeout = 100_000
        }
    }

actual fun clipEntryOf(string: String): ClipEntry =
    ClipEntry(ClipData.newPlainText("plain text", string))
