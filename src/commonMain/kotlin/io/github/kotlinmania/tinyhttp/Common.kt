// port-lint: source common.rs
package io.github.kotlinmania.tinyhttp

/** Status code of a request or response. */
class StatusCode(
    val value: Int,
) : Comparable<StatusCode> {
    /**
     * Returns the default reason phrase for this status code.
     * For example the status code 404 corresponds to "Not Found".
     */
    fun defaultReasonPhrase(): String =
        when (value) {
            100 -> "Continue"
            101 -> "Switching Protocols"
            102 -> "Processing"
            103 -> "Early Hints"

            200 -> "OK"
            201 -> "Created"
            202 -> "Accepted"
            203 -> "Non-Authoritative Information"
            204 -> "No Content"
            205 -> "Reset Content"
            206 -> "Partial Content"
            207 -> "Multi-Status"
            208 -> "Already Reported"
            226 -> "IM Used"

            300 -> "Multiple Choices"
            301 -> "Moved Permanently"
            302 -> "Found"
            303 -> "See Other"
            304 -> "Not Modified"
            305 -> "Use Proxy"
            307 -> "Temporary Redirect"
            308 -> "Permanent Redirect"

            400 -> "Bad Request"
            401 -> "Unauthorized"
            402 -> "Payment Required"
            403 -> "Forbidden"
            404 -> "Not Found"
            405 -> "Method Not Allowed"
            406 -> "Not Acceptable"
            407 -> "Proxy Authentication Required"
            408 -> "Request Timeout"
            409 -> "Conflict"
            410 -> "Gone"
            411 -> "Length Required"
            412 -> "Precondition Failed"
            413 -> "Payload Too Large"
            414 -> "URI Too Long"
            415 -> "Unsupported Media Type"
            416 -> "Range Not Satisfiable"
            417 -> "Expectation Failed"
            421 -> "Misdirected Request"
            422 -> "Unprocessable Entity"
            423 -> "Locked"
            424 -> "Failed Dependency"
            426 -> "Upgrade Required"
            428 -> "Precondition Required"
            429 -> "Too Many Requests"
            431 -> "Request Header Fields Too Large"
            451 -> "Unavailable For Legal Reasons"

            500 -> "Internal Server Error"
            501 -> "Not Implemented"
            502 -> "Bad Gateway"
            503 -> "Service Unavailable"
            504 -> "Gateway Timeout"
            505 -> "HTTP Version Not Supported"
            506 -> "Variant Also Negotiates"
            507 -> "Insufficient Storage"
            508 -> "Loop Detected"
            510 -> "Not Extended"
            511 -> "Network Authentication Required"
            else -> "Unknown"
        }

    override fun compareTo(other: StatusCode): Int = value.compareTo(other.value)

    override fun equals(other: Any?): Boolean = other is StatusCode && value == other.value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = "StatusCode($value)"

    companion object {
        /** Builds a [StatusCode] from a signed 8-bit value (upstream `From<i8> for StatusCode`). */
        fun fromByte(value: Byte): StatusCode = StatusCode(value.toInt() and 0xFFFF)

        /** Builds a [StatusCode] from an unsigned 8-bit value (upstream `From<u8> for StatusCode`). */
        fun fromUByte(value: UByte): StatusCode = StatusCode(value.toInt())

        /** Builds a [StatusCode] from a signed 16-bit value (upstream `From<i16> for StatusCode`). */
        fun fromShort(value: Short): StatusCode = StatusCode(value.toInt() and 0xFFFF)

        /** Builds a [StatusCode] from an unsigned 16-bit value (upstream `From<u16> for StatusCode`). */
        fun fromUShort(value: UShort): StatusCode = StatusCode(value.toInt())

        /** Builds a [StatusCode] from a signed 32-bit value (upstream `From<i32> for StatusCode`). */
        fun fromInt(value: Int): StatusCode = StatusCode(value and 0xFFFF)

        /** Builds a [StatusCode] from an unsigned 32-bit value (upstream `From<u32> for StatusCode`). */
        fun fromUInt(value: UInt): StatusCode = StatusCode(value.toInt() and 0xFFFF)
    }
}

/** Represents a HTTP header. */
class Header(
    val field: HeaderField,
    val value: String,
) {
    companion object {
        /**
         * Builds a `Header` from two `ByteArray`s or two `String`s.
         *
         * Example:
         *
         * ```
         * val header = Header.fromBytes("Content-Type".encodeToByteArray(), "text/plain".encodeToByteArray()).getOrThrow()
         * ```
         */
        fun fromBytes(header: ByteArray, value: ByteArray): Result<Header> {
            val field = HeaderField.fromBytes(header).getOrElse { return Result.failure(it) }
            val v = asciiStringFromBytes(value).getOrElse { return Result.failure(it) }
            return Result.success(Header(field, v))
        }

        fun fromBytes(header: String, value: String): Result<Header> {
            val field =
                HeaderField
                    .fromBytes(header.encodeToByteArray())
                    .getOrElse { return Result.failure(it) }
            val v =
                asciiStringFromBytes(value.encodeToByteArray())
                    .getOrElse { return Result.failure(it) }
            return Result.success(Header(field, v))
        }

        fun parse(input: String): Result<Header> {
            val colon = input.indexOf(':')
            if (colon < 0) return Result.failure(IllegalArgumentException("missing ':' in header"))
            val fieldPart = input.substring(0, colon)
            val valuePart = input.substring(colon + 1)
            val field = HeaderField.parse(fieldPart).getOrElse { return Result.failure(it) }
            val v =
                asciiStringFromBytes(valuePart.trim().encodeToByteArray())
                    .getOrElse { return Result.failure(it) }
            return Result.success(Header(field, v))
        }

        fun fromStr(input: String): Result<Header> = parse(input)
    }

    override fun toString(): String = "$field: $value"
}

/**
 * Field of a header (eg. `Content-Type`, `Content-Length`, etc.)
 *
 * Comparison between two `HeaderField`s ignores case.
 */
class HeaderField internal constructor(
    private val inner: String,
) {
    companion object {
        fun fromBytes(bytes: ByteArray): Result<HeaderField> =
            asciiStringFromBytes(bytes).map { HeaderField(it) }

        fun parse(s: String): Result<HeaderField> {
            if (s.any { it.isWhitespace() }) {
                return Result.failure(IllegalArgumentException("header field contains whitespace"))
            }
            return asciiStringFromBytes(s.encodeToByteArray()).map { HeaderField(it) }
        }

        fun fromStr(s: String): Result<HeaderField> = parse(s)
    }

    fun asStr(): String = inner

    fun equiv(other: String): Boolean = other.equals(inner, ignoreCase = true)

    override fun toString(): String = inner

    override fun equals(other: Any?): Boolean {
        if (other !is HeaderField) return false
        return inner.equals(other.inner, ignoreCase = true)
    }

    override fun hashCode(): Int {
        var h = 0
        for (c in inner) {
            val lower = if (c in 'A'..'Z') (c.code + 32) else c.code
            h = 31 * h + lower
        }
        return h
    }
}

/**
 * HTTP request methods
 *
 * As per [RFC 7231](https://tools.ietf.org/html/rfc7231#section-4.1) and
 * [RFC 5789](https://tools.ietf.org/html/rfc5789)
 */
sealed class Method {
    /** `GET` */
    data object Get : Method()

    /** `HEAD` */
    data object Head : Method()

    /** `POST` */
    data object Post : Method()

    /** `PUT` */
    data object Put : Method()

    /** `DELETE` */
    data object Delete : Method()

    /** `CONNECT` */
    data object Connect : Method()

    /** `OPTIONS` */
    data object Options : Method()

    /** `TRACE` */
    data object Trace : Method()

    /** `PATCH` */
    data object Patch : Method()

    /** Request methods not standardized by the IETF */
    data class NonStandard(
        val name: String,
    ) : Method()

    fun asStr(): String =
        when (this) {
            Get -> "GET"
            Head -> "HEAD"
            Post -> "POST"
            Put -> "PUT"
            Delete -> "DELETE"
            Connect -> "CONNECT"
            Options -> "OPTIONS"
            Trace -> "TRACE"
            Patch -> "PATCH"
            is NonStandard -> name
        }

    override fun toString(): String = asStr()

    companion object {
        fun parse(s: String): Result<Method> =
            Result.success(
                when (s) {
                    "GET" -> Get
                    "HEAD" -> Head
                    "POST" -> Post
                    "PUT" -> Put
                    "DELETE" -> Delete
                    "CONNECT" -> Connect
                    "OPTIONS" -> Options
                    "TRACE" -> Trace
                    "PATCH" -> Patch
                    else -> {
                        val ascii =
                            asciiStringFromBytes(s.encodeToByteArray())
                                .getOrElse { return Result.failure(it) }
                        NonStandard(ascii)
                    }
                },
            )

        fun fromStr(s: String): Result<Method> = parse(s)
    }
}

/** HTTP version (usually 1.0 or 1.1). */
class HTTPVersion(
    val major: Int,
    val minor: Int,
) : Comparable<HTTPVersion> {
    override fun toString(): String = "$major.$minor"

    override fun compareTo(other: HTTPVersion): Int {
        if (major != other.major) return major.compareTo(other.major)
        return minor.compareTo(other.minor)
    }

    override fun equals(other: Any?): Boolean =
        other is HTTPVersion && major == other.major && minor == other.minor

    override fun hashCode(): Int = 31 * major + minor

    companion object {
        fun fromPair(majorMinor: Pair<Int, Int>): HTTPVersion =
            HTTPVersion(majorMinor.first, majorMinor.second)
    }
}

internal fun asciiStringFromBytes(bytes: ByteArray): Result<String> {
    for (b in bytes) {
        if (b.toInt() and 0x80 != 0) {
            return Result.failure(IllegalArgumentException("non-ASCII byte in string"))
        }
    }
    return Result.success(bytes.decodeToString())
}

/** Formats an epoch timestamp in seconds into IMF-fixdate format (RFC 7231 / RFC 5322). */
fun formatHttpDate(epochSeconds: Long): String {
    val days = (epochSeconds / 86400).toInt()
    val remSec = (epochSeconds % 86400).toInt().let { if (it < 0) it + 86400 else it }
    val hour = remSec / 3600
    val minute = (remSec % 3600) / 60
    val second = remSec % 60

    // Epoch 1970-01-01 was Thursday (day 4)
    val dayOfWeekNum = ((days + 4) % 7 + 7) % 7
    val dayNames = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val dayOfWeek = dayNames[dayOfWeekNum]

    // Civil day from epoch days
    val z = days + 719468
    val era = (if (z >= 0) z else z - 146096) / 146097
    val doe = z - era * 146097
    val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
    var y = yoe + era * 400
    val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
    val mp = (5 * doy + 2) / 153
    val d = doy - (153 * mp + 2) / 5 + 1
    val m = mp + (if (mp < 10) 3 else -9)
    if (m <= 2) y += 1

    val monthNames = arrayOf("", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val month = monthNames[m]

    val dayStr = d.toString().padStart(2, '0')
    val yearStr = y.toString().padStart(4, '0')
    val hourStr = hour.toString().padStart(2, '0')
    val minStr = minute.toString().padStart(2, '0')
    val secStr = second.toString().padStart(2, '0')

    return "$dayOfWeek, $dayStr $month $yearStr $hourStr:$minStr:$secStr GMT"
}

/** Builds a `Date` header with the current or specified timestamp. */
fun buildDateHeader(epochSeconds: Long = 0L): Header {
    val dateStr = formatHttpDate(epochSeconds)
    return Header.fromBytes("Date", dateStr).getOrThrow()
}
