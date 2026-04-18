package com.team2813.scouting.scoutingapp2026

import com.team2813.scouting.scoutingapp2026.backend.DiskRepository
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.lang3.SystemUtils

private const val folderName = "com.team2813.scouting2026"

class DiskRepositoryImpl : DiskRepository {
    private fun getFile(path: String): File {
        return if (SystemUtils.IS_OS_UNIX) {
            File(System.getenv("HOME"), "/.local/share/$folderName/$path")
        } else {
            File(System.getenv("APPDATA"), "\\Roaming\\$folderName\\$path")
        }
    }

    override suspend fun readFile(path: String): String? =
        withContext(Dispatchers.IO) {
            val file = getFile(path)
            if (!file.exists()) {
                // if file doesn't exist, don't bother to try and read it
                null
            } else {
                // read full contents of the file
                FileInputStream(file).use { it.readAllBytes().decodeToString() }
            }
        }

    override suspend fun writeFile(path: String, data: String): Unit =
        withContext(Dispatchers.IO) {
            val file = getFile(path)

            Files.createDirectories(file.parentFile.toPath())
            FileOutputStream(file).use { it.write(data.encodeToByteArray()) }
        }

    override suspend fun deleteFile(path: String): Unit =
        withContext(Dispatchers.IO) {
            val file = getFile(path)

            file.delete()
        }

    override fun hasFile(path: String): Boolean {
        val file = getFile(path)
        return file.exists()
    }
}
