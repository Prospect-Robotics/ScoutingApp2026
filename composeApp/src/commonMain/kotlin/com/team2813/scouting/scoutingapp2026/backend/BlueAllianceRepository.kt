package com.team2813.scouting.scoutingapp2026.backend

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.collections.setOf
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

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
    return eventCode?.let {
        CurrentState(it, allianceColor, driverStationNum)
    }
}
class BlueAllianceRepositoryActual(private val diskRepo: DiskRepository, private val tbaKey: String): BlueAllianceRepository {
    private val _allData: MutableStateFlow<AllData> = MutableStateFlow(AllData())
    override val allData: StateFlow<AllData> = _allData.asStateFlow()
    override suspend fun setCompetition(compId: String, color: AllianceColor, driverStationNum: Int) {
        readFromCache(CurrentState(compId, color, driverStationNum))?.let {
            _allData.value = it
            return
        }
        readCompetition(compId, color, driverStationNum)?.let {
            _allData.value = it
        }
    }

    override suspend fun fetchCompetition(compId: String, color: AllianceColor, driverStationNum: Int) {
        if (readFromCache(CurrentState(compId, color, driverStationNum)) != null) {
            return // already know about it; nothing to fetch
        }
        readCompetition(compId, color, driverStationNum)
    }

    private suspend fun readCompetition(compId: String, color: AllianceColor, driverStationNum: Int): AllData? {
        TODO("Read from TBA")
    }

    override suspend fun clearCompetition() {
        val data = _allData.updateAndGet {
            it.copy(
                eventName = null,
                eventCode = null,
                matchMap = mapOf()
            )
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
        val filename = data.asCurrentState()?.let {
            diskRepo.writeFile(stateFilename, Json.encodeToString(it))
            addFileToCache("$it.json")
            it.toString()
        } ?: filename
        val data = Json.encodeToString(data)
        diskRepo.writeFile(filename, data)
    }

    private suspend fun addFileToCache(filename: String) {
        diskRepo.readFile(cachedFilename)?.let {
            val data: MutableSet<String> = Json.decodeFromString(it)
            if (data.add(filename)) {
                diskRepo.writeFile(cachedFilename, Json.encodeToString(data))
            }
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
            }?.let {
                Json.decodeFromString<AllData>(it)
            }
        }
        return diskRepo.readFile(filename)?.let { Json.decodeFromString<AllData>(it)}
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

class BlueAllianceRepositorySkeleton: BlueAllianceRepository {
    private val _allData: MutableStateFlow<AllData> = MutableStateFlow(AllData())
    override val allData: StateFlow<AllData> = _allData.asStateFlow()
    override suspend fun setCompetition(compId: String, color: AllianceColor, driverStationNum: Int) {
        _allData.update {
            it.copy(
                eventName = compId,
                eventCode = compId,
                allianceColor = color,
                driverStationNum = driverStationNum
            )
        }
    }

    override suspend fun fetchCompetition(compId: String, color: AllianceColor, driverStationNum: Int) {}
    override suspend fun clearCompetition() {
        _allData.update {
            it.copy(
                eventCode = null,
                eventName = null,
                matchMap = mapOf()
            )
        }
    }

    override suspend fun clearLocalCache() {}

    override suspend fun load() {}
}

interface BlueAllianceRepository {
    val allData: StateFlow<AllData>
    suspend fun setCompetition(compId: String, color: AllianceColor, driverStationNum: Int)
    suspend fun fetchCompetition(compId: String, color: AllianceColor, driverStationNum: Int)
    suspend fun clearLocalCache()
    suspend fun clearCompetition()
    suspend fun load()
}