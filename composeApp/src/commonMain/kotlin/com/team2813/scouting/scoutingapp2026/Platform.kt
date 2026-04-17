package com.team2813.scouting.scoutingapp2026

import androidx.compose.ui.platform.ClipEntry
import com.team2813.scouting.scoutingapp2026.backend.BlueAllianceRepository
import com.team2813.scouting.scoutingapp2026.backend.BlueAllianceRepositoryActual
import com.team2813.scouting.scoutingapp2026.backend.BlueAllianceRepositorySkeleton
import com.team2813.scouting.scoutingapp2026.backend.DiskRepository
import com.team2813.scouting.scoutingapp2026.backend.DiskRepositorySkeletal
import com.team2813.scouting.scoutingapp2026.backend.EventViewModel
import com.team2813.scouting.scoutingapp2026.backend.QuantitativeViewModel
import com.team2813.scouting.scoutingapp2026.backend.SubmitRepository
import com.team2813.scouting.scoutingapp2026.backend.SubmitRepositoryActual
import com.team2813.scouting.scoutingapp2026.backend.SubmitRepositorySkeleton
import io.ktor.client.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.MissingResourceException
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.bind
import org.koin.dsl.module
import scoutingapp2026.composeapp.generated.resources.Res

expect fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient

expect fun clipEntryOf(string: String): ClipEntry

val commonModule: Module = module {
    single { SubmitRepositoryActual() } bind SubmitRepository::class
    single { DiskRepositorySkeletal() } bind DiskRepository::class
    single<BlueAllianceRepository> {
        try {
            val apiKey = runBlocking {
                Res.readBytes("tba-key.txt").decodeToString()
            }
            BlueAllianceRepositoryActual(diskRepo = get(), apiKey)
        } catch (_: MissingResourceException) {
            // Even Res.getUri fails if the resource doesn't exist with
            // MissingResourceException. Thus, the best we can do to handle the
            // resource not existing is to try and read it, and catch the
            // exception.
            BlueAllianceRepositorySkeleton()
        }
    }
    viewModel<QuantitativeViewModel> {
        QuantitativeViewModel(submitRepository = get(), tbaRepository = get())
    }
    viewModel<EventViewModel> { EventViewModel(tbaRepo = get()) }
}

// extra koin config for logging/modules implementing backend stuff
expect val platformConfig: KoinConfiguration?
val previewModule: Module = module {
    single { SubmitRepositorySkeleton() } bind SubmitRepository::class
    single { BlueAllianceRepositorySkeleton() } bind
        BlueAllianceRepository::class
    viewModel<QuantitativeViewModel> {
        QuantitativeViewModel(submitRepository = get(), tbaRepository = get())
    }
}
