// port-lint: source request.rs
package io.github.kotlinmania.tinyhttp

import io.github.kotlinmania.sha1.Sha1
import io.github.kotlinmania.tinyhttp.util.CustomStream

/**
 * Wrapper that executes an action when dropped or closed.
 */
class NotifyOnDrop<R>(
    private val sender: (Unit) -> Unit,
    val inner: R,
) : AutoCloseable {
    fun read(buf: ByteArray): Int = (inner as? CustomStream<*, *>)?.read(buf) ?: 0

    fun write(buf: ByteArray): Int = (inner as? CustomStream<*, *>)?.write(buf) ?: buf.size

    fun flush() {
        (inner as? CustomStream<*, *>)?.flush()
    }

    override fun close() {
        drop()
    }

    fun drop() {
        sender(Unit)
    }
}

/**
 * Error that can happen when building a `Request` object.
 */
sealed class RequestCreationError : Exception() {
    data object ExpectationFailed : RequestCreationError()

    data class CreationIoError(
        override val message: String,
    ) : RequestCreationError()

    companion object {
        fun from(message: String): RequestCreationError = CreationIoError(message)
    }
}

/**
 * Dummy interface that regroups the read and write operations.
 */
interface ReadWrite {
    fun read(buf: ByteArray): Int

    fun write(buf: ByteArray): Int

    fun flush()
}

/**
 * Represents an HTTP request made by a client.
 */
class Request(
    val method: Method,
    val path: String,
    val httpVersion: HTTPVersion,
    val headers: List<Header>,
    val body: ByteArray = ByteArray(0),
    val remoteAddr: String? = null,
    val secure: Boolean = false,
    val bodyLength: Long? = null,
) : AutoCloseable {
    private var dataReader: ByteArray? = body
    private var responseWriter: ByteArray? = ByteArray(0)
    private var mustSendContinue: Boolean = false
    private var notifyWhenResponded: ((Unit) -> Unit)? = null

    /**
     * Returns the resource requested by the client.
     */
    fun url(): String = path

    /**
     * Sends a response with a `Connection: upgrade` header, then turns the `Request` into a `Stream`.
     */
    fun upgrade(
        protocol: String,
        response: Response<*>,
    ): CustomStream<ByteArray, ByteArray> {
        val writer = responseWriter ?: ByteArray(0)
        response.rawPrint(
            writer,
            httpVersion,
            headers,
            false,
            protocol,
        )
        return CustomStream(extractReaderImpl(), extractWriterImpl())
    }

    /**
     * Allows to read the body of the request.
     */
    fun asReader(): ByteArray {
        if (mustSendContinue) {
            val msg = Response.newEmpty(StatusCode(100))
            msg.rawPrint(
                responseWriter ?: ByteArray(0),
                httpVersion,
                headers,
                true,
                null,
            )
            mustSendContinue = false
        }
        return dataReader ?: body
    }

    /**
     * Returns the body decoded as UTF-8 string.
     */
    fun asString(): String = body.decodeToString()

    /**
     * Turns the `Request` into a writer.
     */
    fun intoWriter(): ByteArray = extractWriterImpl()

    /**
     * Extract the response writer object from the Request.
     */
    fun extractWriterImpl(): ByteArray {
        val writer = responseWriter ?: ByteArray(0)
        responseWriter = null
        return writer
    }

    /**
     * Extract the body reader object from the Request.
     */
    fun extractReaderImpl(): ByteArray {
        val reader = dataReader ?: body
        dataReader = null
        return reader
    }

    /**
     * Sends a response to this request.
     */
    fun <R> respond(response: Response<R>): Result<Unit> {
        val res = respondImpl(response)
        notifyWhenResponded?.invoke(Unit)
        return res
    }

    fun <R> respondImpl(response: Response<R>): Result<Unit> {
        val writer = extractWriterImpl()
        val doNotSendBody = method == Method.Head
        val printRes =
            response.rawPrint(
                writer,
                httpVersion,
                headers,
                doNotSendBody,
                null,
            )
        return ignoreClientClosingErrors(printRes)
    }

    fun ignoreClientClosingErrors(result: Result<Unit>): Result<Unit> = result

    fun withNotifySender(sender: (Unit) -> Unit): Request {
        notifyWhenResponded = sender
        return this
    }

    fun fmt(): String = "Request($method $path from $remoteAddr)"

    override fun toString(): String = fmt()

    override fun close() {
        drop()
    }

    fun drop() {
        if (responseWriter != null) {
            val response = Response.newEmpty(StatusCode(500))
            respondImpl(response)
            notifyWhenResponded?.invoke(Unit)
        }
    }

    companion object {
        const val WEBSOCKET_GUID: String = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11"

        /**
         * Turns a Sec-WebSocket-Key into a Sec-WebSocket-Accept key per RFC 6455.
         */
        @OptIn(kotlin.io.encoding.ExperimentalEncodingApi::class)
        fun convertWebSocketKey(input: String): String {
            val combined = (input + WEBSOCKET_GUID).encodeToByteArray()
            val digest = Sha1.digest(combined)
            return kotlin.io.encoding.Base64
                .encode(digest)
        }

        /**
         * Builds a new request from parsed components and body.
         */
        fun newRequest(
            secure: Boolean,
            method: Method,
            path: String,
            version: HTTPVersion,
            headers: List<Header>,
            remoteAddr: String? = null,
            body: ByteArray = ByteArray(0),
        ): Result<Request> {
            val transferEncoding = headers.find { it.field.equiv("Transfer-Encoding") }?.value
            val contentLength =
                if (transferEncoding != null) {
                    null
                } else {
                    headers.find { it.field.equiv("Content-Length") }?.value?.toLongOrNull()
                }

            val expectHeader = headers.find { it.field.equiv("Expect") }?.value
            if (expectHeader != null && !expectHeader.equals("100-continue", ignoreCase = true)) {
                return Result.failure(RequestCreationError.ExpectationFailed)
            }

            val req =
                Request(
                    method = method,
                    path = path,
                    httpVersion = version,
                    headers = headers,
                    body = body,
                    remoteAddr = remoteAddr,
                    secure = secure,
                    bodyLength = contentLength ?: body.size.toLong(),
                )
            req.mustSendContinue = expectHeader?.equals("100-continue", ignoreCase = true) == true
            return Result.success(req)
        }
    }
}
