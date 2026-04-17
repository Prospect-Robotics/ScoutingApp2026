package com.team2813.scouting.scoutingapp2026

import com.team2813.scouting.scoutingapp2026.backend.DiskRepository
import kotlinx.cinterop.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Foundation.*

class DiskRepositoryImpl : DiskRepository {
    @OptIn(ExperimentalForeignApi::class)
    override suspend fun readFile(path: String): String? {
        return withContext(Dispatchers.IO) {
            val documentDirectory =
                (NSSearchPathForDirectoriesInDomains(
                    NSDocumentDirectory, NSUserDomainMask, true)[0])
                    as NSString

            val filePath =
                documentDirectory.stringByAppendingPathComponent(path)

            NSString.stringWithContentsOfFile(
                filePath, NSUTF8StringEncoding, null)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun writeFile(path: String, data: String) {
        withContext(Dispatchers.IO) {
            val documentDirectory =
                (NSSearchPathForDirectoriesInDomains(
                    NSDocumentDirectory, NSUserDomainMask, true)[0])
                    as NSString

            val filePath =
                documentDirectory.stringByAppendingPathComponent(path)

            // Compiler bug; cast succeeds always
            // see
            // https://stackoverflow.com/questions/75167606/kmm-cast-string-to-nsstring-get-warning-this-case-can-never-succeed
            @Suppress("CAST_NEVER_SUCCEEDS")
            (data as NSString).writeToFile(
                filePath, true, NSUTF8StringEncoding, null)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun deleteFile(path: String) {
        val documentDirectory =
            (NSSearchPathForDirectoriesInDomains(
                NSDocumentDirectory, NSUserDomainMask, true)[0])
                as NSString

        val filePath = documentDirectory.stringByAppendingPathComponent(path)
        memScoped {
            val error = alloc<ObjCObjectVar<NSError?>>()
            NSFileManager.defaultManager.removeItemAtPath(filePath, error.ptr)
        }
        // eat the error :)
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun hasFile(path: String): Boolean {
        val documentDirectory =
            (NSSearchPathForDirectoriesInDomains(
                NSDocumentDirectory, NSUserDomainMask, true)[0])
                as NSString

        val filePath = documentDirectory.stringByAppendingPathComponent(path)
        return NSFileManager.defaultManager.fileExistsAtPath(filePath)
    }
}
