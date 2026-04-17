package com.team2813.scouting.scoutingapp2026

import androidx.compose.ui.platform.ClipEntry
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.java.Java
import java.awt.datatransfer.StringSelection
import java.net.http.HttpClient.Version.HTTP_2
import org.koin.core.KoinApplication
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

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

val platformModule: Module = module {
    // TODO: implement!
}
actual val extraConfiguration: KoinConfiguration? = koinConfiguration {
    modules(platformModule)
    printLogger(Level.DEBUG)
}
