package com.team2813.scouting.scoutingapp2026.backend

import androidx.compose.runtime.MutableState
import com.team2813.scouting.scoutingapp2026.httpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.utils.io.core.Closeable

private val hexLookup =
    arrayOf(
        '0',
        '1',
        '2',
        '3',
        '4',
        '5',
        '6',
        '7',
        '8',
        '9',
        'A',
        'B',
        'C',
        'D',
        'E',
        'F',
    )

fun urlencode(str: String, plusSpace: Boolean = false): String {
    val bytes = str.encodeToByteArray()
    return bytes.joinToString(separator = "") { byte ->
        when (val value = byte.toInt()) {
            0x20 -> {
                if (plusSpace) {
                    "+"
                } else {
                    "%20"
                }
            }
            in 0x41..0x5A, // a-z
            in 0x61..0x7A, // A-Z
            in 0x30..0x39, // 0-9
            0x2D, // -
            0x5F, // _
            0x2E -> { // .
                Char(value).toString()
            }
            else ->
                "%${hexLookup[(value and 0xF0) shr 4]}${hexLookup[value and 0x0F]}"
        }
    }
}

infix fun <T, I, R> ((T) -> I).andThen(next: (I) -> R): (T) -> R {
    return { next(invoke(it)) }
}

class FormBuilder<T> private constructor(builder: Builder<T>) : Closeable {
    private val map: Map<String, (T) -> String> = builder.map
    private val formUrl = builder.formUrl
    private val httpClient by lazy {
        httpClient { this.followRedirects = true }
    }

    companion object {
        fun <T> build(
            formUrl: String,
            block: Builder<T>.() -> Unit
        ): FormBuilder<T> {
            return FormBuilder(Builder<T>(formUrl).apply(block))
        }
    }

    class BuilderWrapper<T, R>
    internal constructor(
        private val builder: Builder<T>,
        private val base: (T) -> R
    ) {
        fun <I> add(
            key: String,
            getter: (R) -> I,
            toString: (I) -> String = { it.toString() }
        ) {
            builder.add(key, base andThen getter, toString)
        }

        fun <I> addMutable(
            key: String,
            getter: (R) -> MutableState<I>,
            toString: (I) -> String = { it.toString() }
        ) {
            add(key, getter andThen MutableState<I>::value, toString)
        }

        fun <I> wrap(getter: (R) -> I, scope: BuilderWrapper<T, I>.() -> Unit) {
            val wrapper = BuilderWrapper(builder, this.base andThen getter)
            wrapper.scope()
        }
    }

    class Builder<T>(internal val formUrl: String) {
        internal val map: MutableMap<String, (T) -> String> = hashMapOf()

        fun <R> add(
            key: String,
            getter: (T) -> R,
            toString: (R) -> String = { it.toString() }
        ) {
            if (map.put(urlencode(key), getter andThen toString) != null) {
                throw IllegalArgumentException("$key was already put in!")
            }
        }

        fun <R> addMutable(
            key: String,
            getter: (T) -> MutableState<R>,
            toString: (R) -> String = { it.toString() }
        ) {
            add(key, getter andThen MutableState<R>::value, toString)
        }

        fun <R> wrap(getter: (T) -> R, scope: BuilderWrapper<T, R>.() -> Unit) {
            val wrapper = BuilderWrapper(this, getter)
            wrapper.scope()
        }

        fun addConstant(key: String, value: String) {
            if (map.put(urlencode(key), { _ -> value }) != null) {
                throw IllegalArgumentException("$key was already put in!")
            }
        }
    }

    fun getAutomaticSubmitLink(data: T): String {
        return map.entries.joinToString(
            prefix = "$formUrl/formResponse?", separator = "&") { (key, func) ->
                // use plus space to save on characters
                "$key=${urlencode(func(data), true)}"
            }
    }

    private fun generatePostData(data: T): String {
        return map.entries.joinToString(separator = "&") { (key, func) ->
            "$key=${urlencode(func(data))}"
        }
    }

    suspend fun postFormData(data: T) {
        val resp =
            httpClient.post("$formUrl/formResponse") {
                setBody(generatePostData(data))
                contentType(ContentType.Application.FormUrlEncoded)
            }
        if (!resp.status.isSuccess()) {
            throw RuntimeException("Non-success status code")
        }
    }

    override fun close() {
        httpClient.close()
    }
}
