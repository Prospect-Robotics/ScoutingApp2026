package com.team2813.scouting.scoutingapp2026.backend

interface DiskRepository {
    suspend fun readFile(path: String): String?

    suspend fun writeFile(path: String, data: String)

    suspend fun deleteFile(path: String)

    fun hasFile(path: String): Boolean
}

class DiskRepositorySkeletal : DiskRepository {
    override suspend fun readFile(path: String): String? = null

    override suspend fun writeFile(path: String, data: String) {}

    override suspend fun deleteFile(path: String) {}

    override fun hasFile(path: String): Boolean = false
}
