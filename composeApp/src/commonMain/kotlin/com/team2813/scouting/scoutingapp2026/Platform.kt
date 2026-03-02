package com.team2813.scouting.scoutingapp2026

import androidx.compose.ui.platform.ClipEntry
import com.team2813.scouting.scoutingapp2026.backend.QuantitativeViewModel
import com.team2813.scouting.scoutingapp2026.backend.SubmitRepository
import io.ktor.client.*
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient

expect fun clipEntryOf(string: String): ClipEntry

val commonModule: Module = module {
    single { SubmitRepository() }
    viewModel<QuantitativeViewModel> {
        QuantitativeViewModel(submitRepository = get())
    }
}
