package com.team2813.scouting.scoutingapp2026

import androidx.compose.ui.platform.ClipEntry
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.java.Java
import java.awt.datatransfer.StringSelection
import java.net.http.HttpClient.Version.HTTP_2

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient =
    HttpClient(Java) {
        config(this)
        engine {
            pipelining = true
            protocolVersion = HTTP_2
        }
    }

actual fun clipEntryOf(string: String): ClipEntry =
    ClipEntry(StringSelection(string))
