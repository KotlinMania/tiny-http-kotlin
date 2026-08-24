// port-lint: source util/refined_tcp_stream.rs
package io.github.kotlinmania.tinyhttp.util

/**
 * Underlying stream representation for HTTP connections.
 */
sealed class Stream {
    data class Http(val connection: Any) : Stream()
    data class Https(val secureStream: Any) : Stream()

    fun isSecure(): Boolean = this is Https
}

/**
 * Refined stream wrapper controlling read/write halves and shutdown lifecycle.
 */
class RefinedTcpStream(
    val stream: Stream,
    val closeRead: Boolean,
    val closeWrite: Boolean,
) {
    /**
     * Returns true if this struct wraps a secure connection.
     */
    fun isSecure(): Boolean = stream.isSecure()

    companion object {
        fun new(stream: Stream): Pair<RefinedTcpStream, RefinedTcpStream> {
            val read = RefinedTcpStream(stream = stream, closeRead = true, closeWrite = false)
            val write = RefinedTcpStream(stream = stream, closeRead = false, closeWrite = true)
            return Pair(read, write)
        }
    }
}
