// port-lint: source response.rs
package io.github.kotlinmania.tinyhttp

/**
 * Transfer encoding to use when sending the message.
 * Note that only supported encodings are listed here.
 */
enum class TransferEncoding {
    Identity,
    Chunked;

    companion object {
        fun parse(input: String): Result<TransferEncoding> =
            when {
                input.equals("identity", ignoreCase = true) -> Result.success(Identity)
                input.equals("chunked", ignoreCase = true) -> Result.success(Chunked)
                else -> Result.failure(IllegalArgumentException("unknown transfer encoding: $input"))
            }
    }
}

/**
 * Object representing an HTTP response whose purpose is to be given to a `Request`.
 */
class Response<R>(
    val reader: R,
    var statusCode: StatusCode = StatusCode(200),
    headers: List<Header> = emptyList(),
    var dataLength: Long? = null,
    var chunkedThreshold: Long? = null,
) {
    private val mutableHeaders: MutableList<Header> = ArrayList(16)
    val headers: List<Header> get() = mutableHeaders

    init {
        for (h in headers) {
            addHeader(h)
        }
    }

    /**
     * Set a threshold for `Content-Length` where we choose chunked transfer.
     */
    fun withChunkedThreshold(length: Long): Response<R> {
        this.chunkedThreshold = length
        return this
    }

    /**
     * Convert the response into the underlying reader type.
     */
    fun intoReader(): R = reader

    /**
     * The current `Content-Length` threshold for switching over to chunked transfer.
     * The default is 32768 bytes.
     */
    fun getChunkedThresholdOrDefault(): Long = chunkedThreshold ?: 32768L

    /**
     * Adds a header to the list, performing validation checks.
     */
    fun addHeader(header: Header) {
        // ignoring forbidden headers
        if (header.field.equiv("Connection") ||
            header.field.equiv("Trailer") ||
            header.field.equiv("Transfer-Encoding") ||
            header.field.equiv("Upgrade")
        ) {
            return
        }

        if (header.field.equiv("Content-Length")) {
            header.value.toLongOrNull()?.let {
                dataLength = it
            }
            return
        } else if (header.field.equiv("Content-Type")) {
            val idx = mutableHeaders.indexOfFirst { it.field.equiv("Content-Type") }
            if (idx >= 0) {
                mutableHeaders[idx] = header
                return
            }
        }

        mutableHeaders.add(header)
    }

    fun withHeader(header: Header): Response<R> {
        addHeader(header)
        return this
    }

    fun withStatusCode(code: StatusCode): Response<R> {
        this.statusCode = code
        return this
    }

    fun withStatusCode(code: Int): Response<R> {
        this.statusCode = StatusCode(code)
        return this
    }

    fun <S> withData(newReader: S, newDataLength: Long?): Response<S> =
        Response(
            reader = newReader,
            statusCode = this.statusCode,
            headers = this.mutableHeaders,
            dataLength = newDataLength,
            chunkedThreshold = this.chunkedThreshold,
        )

    companion object {
        /**
         * Builds a response from byte data.
         */
        fun fromData(data: ByteArray): Response<ByteArray> =
            Response(
                reader = data,
                statusCode = StatusCode(200),
                headers = emptyList(),
                dataLength = data.size.toLong(),
            )

        /**
         * Builds a response from a string with default UTF-8 text/plain header.
         */
        fun fromString(data: String): Response<ByteArray> {
            val bytes = data.encodeToByteArray()
            val contentType = Header.fromBytes("Content-Type", "text/plain; charset=UTF-8").getOrThrow()
            return Response(
                reader = bytes,
                statusCode = StatusCode(200),
                headers = listOf(contentType),
                dataLength = bytes.size.toLong(),
            )
        }

        /**
         * Builds an empty response with the given status code.
         */
        fun empty(statusCode: StatusCode): Response<ByteArray> =
            Response(
                reader = ByteArray(0),
                statusCode = statusCode,
                headers = emptyList(),
                dataLength = 0L,
            )

        fun empty(statusCode: Int): Response<ByteArray> =
            empty(StatusCode(statusCode))
    }
}

/**
 * Chooses transfer encoding based on request and response parameters.
 */
fun chooseTransferEncoding(
    statusCode: StatusCode,
    requestHeaders: List<Header>,
    httpVersion: HTTPVersion,
    entityLength: Long?,
    hasAdditionalHeaders: Boolean = false,
    chunkedThreshold: Long = 32768L,
): TransferEncoding {
    // HTTP 1.0 doesn't support other encoding
    if (httpVersion <= HTTPVersion(1, 0)) {
        return TransferEncoding.Identity
    }

    // Per RFC 7230 §3.3.1: status code 1xx or 204 MUST NOT send Transfer-Encoding
    if (statusCode.value < 200 || statusCode.value == 204) {
        return TransferEncoding.Identity
    }

    val teHeader = requestHeaders.find { it.field.equiv("TE") }
    if (teHeader != null) {
        val parsed = parseHeaderValue(teHeader.value).toMutableList()
        parsed.sortByDescending { it.second }
        for ((enc, q) in parsed) {
            if (q <= 0.0f) continue
            val te = TransferEncoding.parse(enc).getOrNull()
            if (te != null) return te
        }
    }

    if (hasAdditionalHeaders) {
        return TransferEncoding.Chunked
    }

    if (entityLength == null || entityLength >= chunkedThreshold) {
        return TransferEncoding.Chunked
    }

    return TransferEncoding.Identity
}
