package com.team2813.scouting.scoutingapp2026

import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry
import io.ktor.client.*
import io.ktor.client.engine.android.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

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

val platformModule: Module = module {
    // required android context added in MainActivity, as we don't statically
    // know the context (and shouldn't)
    single<DiskRepositoryImpl> { DiskRepositoryImpl(context = get()) }
}
actual val platformConfig: KoinConfiguration? = koinConfiguration {
    modules(platformModule)
    androidLogger(Level.DEBUG)
}
