// port-lint: source client.rs
package io.github.kotlinmania.tinyhttp

import io.github.kotlinmania.tinyhttp.util.RefinedTcpStream

/**
 * Error that can happen when reading a request from a client stream.
 */
sealed class ReadError : Exception() {
    data object WrongRequestLine : ReadError()
    data class WrongHeader(val httpVersion: HTTPVersion) : ReadError()
    data class ExpectationFailed(val httpVersion: HTTPVersion) : ReadError()
    data class ReadIoError(override val message: String) : ReadError()
}

/**
 * Manages the connection lifecycle to a connected client.
 */
class ClientConnection(
    val writeSocket: RefinedTcpStream,
    val readSocket: RefinedTcpStream,
    val remoteAddr: String? = null,
    val secure: Boolean = false,
) {
    var noMoreRequests: Boolean = false
        private set

    companion object {
        fun new(
            writeSocket: RefinedTcpStream,
            readSocket: RefinedTcpStream,
            remoteAddr: String? = null,
            secure: Boolean = false,
        ): ClientConnection =
            ClientConnection(
                writeSocket = writeSocket,
                readSocket = readSocket,
                remoteAddr = remoteAddr,
                secure = secure,
            )
    }
}
