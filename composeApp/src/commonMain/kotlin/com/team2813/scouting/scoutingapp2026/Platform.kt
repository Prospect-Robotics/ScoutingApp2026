package com.team2813.scouting.scoutingapp2026

import androidx.compose.ui.platform.ClipEntry
import com.team2813.scouting.scoutingapp2026.backend.QuantitativeViewModel
import com.team2813.scouting.scoutingapp2026.backend.SubmitRepository
import com.team2813.scouting.scoutingapp2026.backend.SubmitRepositoryActual
import com.team2813.scouting.scoutingapp2026.backend.SubmitRepositorySkeleton
import io.ktor.client.*
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient

expect fun clipEntryOf(string: String): ClipEntry

val commonModule: Module = module {
    single { SubmitRepositoryActual() } bind SubmitRepository::class
    viewModel<QuantitativeViewModel> {
        QuantitativeViewModel(submitRepository = get())
    }
}
val previewModule: Module = module {
    single { SubmitRepositorySkeleton() } bind SubmitRepository::class
    viewModel<QuantitativeViewModel> {
        QuantitativeViewModel(submitRepository = get())
    }
}
