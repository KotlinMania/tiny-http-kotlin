// port-lint: source lib.rs
package io.github.kotlinmania.tinyhttp

import io.github.kotlinmania.tinyhttp.util.MessagesQueue

/**
 * Configuration of the server for SSL/TLS.
 */
data class SslConfig(
    val certificate: ByteArray,
    val privateKey: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SslConfig) return false
        return certificate.contentEquals(other.certificate) && privateKey.contentEquals(other.privateKey)
    }

    override fun hashCode(): Int {
        var result = certificate.contentHashCode()
        result = 31 * result + privateKey.contentHashCode()
        return result
    }
}

/**
 * Represents the parameters required to create a server.
 */
data class ServerConfig(
    val addr: ConfigListenAddr,
    val ssl: SslConfig? = null,
)

/**
 * The main server class.
 */
class Server(
    val config: ServerConfig,
    val listeningAddr: ListenAddr,
) {
    private val messages = MessagesQueue.withCapacity<Request>(16)
    var isClosed: Boolean = false
        private set

    /**
     * Blocks or retrieves the next incoming request if available.
     */
    fun recv(): Result<Request> {
        if (isClosed) {
            return Result.failure(IllegalStateException("Server is closed"))
        }
        val req = messages.pop() ?: return Result.failure(IllegalStateException("No requests available"))
        return Result.success(req)
    }

    /**
     * Tries to pop an incoming request without blocking.
     */
    fun tryRecv(): Request? {
        if (isClosed) return null
        return messages.tryPop()
    }

    /**
     * Closes the server and releases listeners.
     */
    fun unblock() {
        isClosed = true
        messages.unblock()
    }

    /**
     * Enqueues an incoming request for testing or processing.
     */
    fun enqueueRequest(request: Request) {
        if (!isClosed) {
            messages.push(request)
        }
    }

    companion object {
        /**
         * Shortcut for creating a simple HTTP server on a specific address and port.
         */
        fun http(address: String, port: Int): Result<Server> {
            val config = ServerConfig(addr = ConfigListenAddr.fromSocketAddr(address, port), ssl = null)
            val listenAddr = ListenAddr.Ip(address, port)
            return Result.success(Server(config, listenAddr))
        }

        /**
         * Shortcut for creating an HTTPS server with SSL configuration.
         */
        fun https(address: String, port: Int, sslConfig: SslConfig): Result<Server> {
            val config = ServerConfig(addr = ConfigListenAddr.fromSocketAddr(address, port), ssl = sslConfig)
            val listenAddr = ListenAddr.Ip(address, port)
            return Result.success(Server(config, listenAddr))
        }
    }
}
