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
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.charsets.Charsets
import kotlinx.coroutines.runBlocking
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.module
import scoutingapp2026.composeapp.generated.resources.Res

expect fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient

expect fun clipEntryOf(string: String): ClipEntry

val commonModule: Module = module {
    single { SubmitRepositoryActual() } bind SubmitRepository::class
    single { DiskRepositorySkeletal() } bind DiskRepository::class
    single<BlueAllianceRepository> {
        val uri = Res.getUri("tba-key.txt")
        if (SystemFileSystem.exists(Path(uri))) {
            val apiKey = runBlocking {
                Res.readBytes("tba-key.txt").decodeToString()
            }
            BlueAllianceRepositoryActual(diskRepo = get(), apiKey)
        } else {
            BlueAllianceRepositorySkeleton()
        }
    }
    viewModel<QuantitativeViewModel> {
        QuantitativeViewModel(submitRepository = get(), tbaRepository = get())
    }
    viewModel<EventViewModel> {
        EventViewModel(tbaRepo = get())
    }
}
val previewModule: Module = module {
    single { SubmitRepositorySkeleton() } bind SubmitRepository::class
    single { BlueAllianceRepositorySkeleton() } bind BlueAllianceRepository::class
    viewModel<QuantitativeViewModel> {
        QuantitativeViewModel(submitRepository = get(), tbaRepository = get())
    }
}
