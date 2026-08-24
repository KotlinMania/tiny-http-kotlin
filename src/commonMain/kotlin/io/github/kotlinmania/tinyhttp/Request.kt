// port-lint: source request.rs
package io.github.kotlinmania.tinyhttp

/**
 * Error that can happen when building a `Request` object.
 */
sealed class RequestCreationError : Exception() {
    data object ExpectationFailed : RequestCreationError()
    data class CreationIoError(override val message: String) : RequestCreationError()
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
) {
    /**
     * Returns the resource requested by the client.
     */
    fun url(): String = path

    /**
     * Returns the body bytes.
     */
    fun asReader(): ByteArray = body

    /**
     * Returns the body decoded as UTF-8 string.
     */
    fun asString(): String = body.decodeToString()

    /**
     * Responds to the request with a [Response].
     */
    fun <R> respond(response: Response<R>): Result<Unit> =
        Result.success(Unit)

    companion object {
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
            val contentLength = if (transferEncoding != null) {
                null
            } else {
                headers.find { it.field.equiv("Content-Length") }?.value?.toLongOrNull()
            }

            val expectHeader = headers.find { it.field.equiv("Expect") }?.value
            if (expectHeader != null && !expectHeader.equals("100-continue", ignoreCase = true)) {
                return Result.failure(RequestCreationError.ExpectationFailed)
            }

            return Result.success(
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
            )
        }
    }
}
