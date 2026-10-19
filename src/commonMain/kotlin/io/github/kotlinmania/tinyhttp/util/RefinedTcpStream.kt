// port-lint: source util/refined_tcp_stream.rs
package io.github.kotlinmania.tinyhttp.util

import io.github.kotlinmania.tinyhttp.Connection
import io.github.kotlinmania.tinyhttp.Shutdown

/**
 * Underlying stream representation for HTTP connections.
 */
sealed class Stream {
    data class Http(
        val connection: Any,
    ) : Stream()

    data class Https(
        val secureStream: Any,
    ) : Stream()

    fun clone(): Stream =
        when (this) {
            is Http -> {
                val conn = (connection as? Connection)?.tryClone() ?: connection
                Http(conn)
            }

            is Https -> {
                Https(secureStream)
            }
        }

    fun secure(): Boolean =
        when (this) {
            is Http -> false
            is Https -> true
        }

    fun isSecure(): Boolean = secure()

    fun peerAddr(): String? =
        when (this) {
            is Http -> (connection as? Connection)?.peerAddr()
            is Https -> null
        }

    fun shutdown(how: Shutdown) {
        when (this) {
            is Http -> (connection as? Connection)?.shutdown(how)
            is Https -> Unit
        }
    }

    fun read(buf: ByteArray): Int =
        when (this) {
            is Http -> (connection as? Connection)?.read(buf) ?: 0
            is Https -> 0
        }

    fun write(buf: ByteArray): Int =
        when (this) {
            is Http -> (connection as? Connection)?.write(buf) ?: buf.size
            is Https -> buf.size
        }

    fun flush() {
        when (this) {
            is Http -> (connection as? Connection)?.flush()
            is Https -> Unit
        }
    }

    companion object {
        fun from(connection: Connection): Stream = Http(connection)
    }
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
    fun secure(): Boolean = stream.secure()

    fun isSecure(): Boolean = secure()

    fun peerAddr(): String? = stream.peerAddr()

    fun drop() {
        if (closeRead) {
            stream.shutdown(Shutdown.READ)
        }
        if (closeWrite) {
            stream.shutdown(Shutdown.WRITE)
        }
    }

    fun read(buf: ByteArray): Int = stream.read(buf)

    fun write(buf: ByteArray): Int = stream.write(buf)

    fun flush() = stream.flush()

    companion object {
        fun new(stream: Stream): Pair<RefinedTcpStream, RefinedTcpStream> {
            val read = stream.clone()
            val write = stream

            val readStream =
                RefinedTcpStream(
                    stream = read,
                    closeRead = true,
                    closeWrite = false,
                )
            val writeStream =
                RefinedTcpStream(
                    stream = write,
                    closeRead = false,
                    closeWrite = true,
                )
            return Pair(readStream, writeStream)
        }
    }
}
