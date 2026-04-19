package com.team2813.scouting.scoutingapp2026.backend

import androidx.compose.ui.text.toLowerCase
import com.team2813.scouting.scoutingapp2026.httpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlin.collections.setOf
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.core.Koin
import org.koin.core.logger.Logger

private const val filename = "matches.json"
private const val stateFilename = "state.json"
private const val cachedFilename = "cached.json"

@Serializable
private data class CurrentState(
    val eventCode: String,
    val color: AllianceColor,
    val driverStationNum: Int,
) {
    override fun toString(): String {
        return "$eventCode-$color-$driverStationNum"
    }
}

private fun AllData.asCurrentState(): CurrentState? {
    return eventCode?.let { CurrentState(it, allianceColor, driverStationNum) }
}

class BlueAllianceRepositoryActual(
    private val diskRepo: DiskRepository,
    private val tbaKey: String
) : BlueAllianceRepository {
    private val _allData: MutableStateFlow<AllData> =
        MutableStateFlow(AllData())
    override val allData: StateFlow<AllData> = _allData.asStateFlow()
    private val httpClient: HttpClient = httpClient {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        defaultRequest {
            url("https://www.thebluealliance.com/api/v3/")
            header("X-TBA-Auth-Key", tbaKey)
        }
    }

    override suspend fun setCompetition(
        compId: String,
        color: AllianceColor,
        driverStationNum: Int
    ) {
        readFromCache(CurrentState(compId, color, driverStationNum))?.let {
            _allData.value = it
            return
        }
        readCompetition(compId, color, driverStationNum)?.let {
            _allData.value = it
            writeToDisk(it)
        }
    }

    override suspend fun fetchCompetition(
        compId: String,
        color: AllianceColor,
        driverStationNum: Int
    ) {
        if (readFromCache(CurrentState(compId, color, driverStationNum)) !=
            null) {
            return // already know about it; nothing to fetch
        }
        readCompetition(compId, color, driverStationNum)?.let {
            writeToDisk(it)
        }
    }

    private suspend inline fun <reified T> HttpResponse.okBody(): T {
        if (status.isSuccess()) {
            return body<T>()
        }
        throw RuntimeException("Non-success response! $status")
    }

    private suspend fun readCompetition(
        compId: String,
        color: AllianceColor,
        driverStationNum: Int
    ): AllData? {
        try {
            // All TBA keys are all lowercase
            val compId = compId.lowercase()
            val requestedEvent =
                httpClient.get("event/2026${compId}/simple").okBody<Event>()

            val matches =
                httpClient
                    .get("event/2026${compId}/matches/simple")
                    .okBody<List<Match>>()
            val matchMap =
                matches
                    .filter { it.compLevel == CompLevel.Qualifications }
                    .associate {
                        it.matchNumber.toString() to
                            it.alliances.let { alliances ->
                                when (color) {
                                    AllianceColor.Blue ->
                                        alliances.blue.teams[driverStationNum - 1]
                                            .substring(3)
                                    AllianceColor.Red ->
                                        alliances.red.teams[driverStationNum - 1]
                                            .substring(3)
                                }
                            }
                    }
            return AllData(
                requestedEvent.name,
                requestedEvent.eventCode,
                color,
                driverStationNum,
                matchMap)
        } catch (e: Exception) {
            println(
                "An error occurred while loading the competition $compId on $color $driverStationNum: ${e.stackTraceToString()}")
            return null
        }
    }

    override suspend fun clearCompetition() {
        val data =
            _allData.updateAndGet {
                it.copy(eventName = null, eventCode = null, matchMap = mapOf())
            }
        writeToDisk(data)
    }

    override suspend fun clearLocalCache() {
        diskRepo.readFile(cachedFilename)?.let {
            val data: Set<String> = Json.decodeFromString(it)
            for (filename in data) {
                diskRepo.deleteFile(filename)
            }
        }
        val emptySet: Set<String> = setOf()
        diskRepo.writeFile(cachedFilename, Json.encodeToString(emptySet))
    }

    private suspend fun writeToDisk(data: AllData) {
        val filename =
            data.asCurrentState()?.let {
                diskRepo.writeFile(stateFilename, Json.encodeToString(it))
                addFileToCache("$it.json")
                "$it.json"
            } ?: filename
        val data = Json.encodeToString(data)
        diskRepo.writeFile(filename, data)
    }

    private suspend fun addFileToCache(filename: String) {
        val data: MutableSet<String> = diskRepo.readFile(cachedFilename)?.let {
            Json.decodeFromString(it)
        } ?: mutableSetOf()
        if (data.add(filename)) {
            diskRepo.writeFile(cachedFilename, Json.encodeToString(data))
        }
    }

    private suspend fun readFromCache(state: CurrentState): AllData? {
        return diskRepo.readFile(cachedFilename)?.let {
            val cache: Set<String> = Json.decodeFromString(it)
            if (cache.contains("$state.json")) {
                diskRepo.readFile("$state.json")?.let { stateContents ->
                    Json.decodeFromString<AllData>(stateContents)
                }
            } else {
                null
            }
        }
    }

    // may be useful later for selective deletion
    private suspend fun removeFileFromCache(filename: String) {
        diskRepo.readFile(cachedFilename)?.let {
            val data: MutableSet<String> = Json.decodeFromString(it)
            if (data.remove(filename)) {
                diskRepo.writeFile(cachedFilename, Json.encodeToString(data))
                diskRepo.deleteFile(filename)
            }
        }
    }

    private suspend fun readFromDisk(): AllData? {
        diskRepo.readFile(stateFilename)?.let { stateStr ->
            val state: CurrentState? = Json.decodeFromString(stateStr)
            if (state != null) {
                    diskRepo.readFile("$state.json")
                } else {
                    diskRepo.readFile(filename)
                }
                ?.let { Json.decodeFromString<AllData>(it) }
        }
        return diskRepo.readFile(filename)?.let {
            Json.decodeFromString<AllData>(it)
        }
    }

    @OptIn(ExperimentalAtomicApi::class)
    private val loaded = AtomicBoolean(false)

    @OptIn(ExperimentalAtomicApi::class)
    override suspend fun load() {
        if (loaded.compareAndSet(expectedValue = false, newValue = true)) {
            readFromDisk()?.let { _allData.value = it }
        }
    }
}

class BlueAllianceRepositorySkeleton : BlueAllianceRepository {
    private val _allData: MutableStateFlow<AllData> =
        MutableStateFlow(AllData())
    override val allData: StateFlow<AllData> = _allData.asStateFlow()

    override suspend fun setCompetition(
        compId: String,
        color: AllianceColor,
        driverStationNum: Int
    ) {
        _allData.update {
            it.copy(
                eventName = compId,
                eventCode = compId,
                allianceColor = color,
                driverStationNum = driverStationNum)
        }
    }

    override suspend fun fetchCompetition(
        compId: String,
        color: AllianceColor,
        driverStationNum: Int
    ) {}

    override suspend fun clearCompetition() {
        _allData.update {
            it.copy(eventCode = null, eventName = null, matchMap = mapOf())
        }
    }

    override suspend fun clearLocalCache() {}

    override suspend fun load() {}
}

interface BlueAllianceRepository {
    val allData: StateFlow<AllData>

    suspend fun setCompetition(
        compId: String,
        color: AllianceColor,
        driverStationNum: Int
    )

    suspend fun fetchCompetition(
        compId: String,
        color: AllianceColor,
        driverStationNum: Int
    )

    suspend fun clearLocalCache()

    suspend fun clearCompetition()

    suspend fun load()
}
