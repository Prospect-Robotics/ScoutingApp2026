package com.team2813.scouting.scoutingapp2026

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.team2813.scouting.scoutingapp2026.backend.DiskRepository
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DiskRepositoryImpl(val context: Context) : DiskRepository {
    override suspend fun readFile(path: String): String? {
        // if the file doesn't exist, don't bother to read it
        if (!File(context.filesDir, path).exists()) {
            return null
        }
        return withContext(Dispatchers.IO) {
            val fis = context.openFileInput(path)
            val data: StringBuilder = StringBuilder(fis.available())
            BufferedReader(InputStreamReader(fis)).use {
                for (line in it.readLines()) {
                    data.append(line).append('\n')
                }
            }
            data.toString()
        }
    }

    override suspend fun writeFile(path: String, data: String) {
        return withContext(Dispatchers.IO) {
            val fos = context.openFileOutput(path, MODE_PRIVATE)
            fos.bufferedWriter().use { it.write(data) }
        }
    }

    override suspend fun deleteFile(path: String) {
        val file = File(context.filesDir, path)
        if (file.exists()) {
            file.delete()
        }
    }

    override fun hasFile(path: String): Boolean {
        return File(context.filesDir, path).exists()
    }
}
