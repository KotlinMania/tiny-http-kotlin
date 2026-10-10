// port-lint: source connection.rs
package io.github.kotlinmania.tinyhttp

/**
 * Socket address representation for network listeners.
 */
sealed class ListenAddr {
    data class Ip(
        val address: String,
        val port: Int,
    ) : ListenAddr() {
        override fun toString(): String = "$address:$port"
    }

    data class Unix(
        val path: String,
    ) : ListenAddr() {
        override fun toString(): String = path
    }

    fun toIp(): Ip? = this as? Ip

    fun toUnix(): Unix? = this as? Unix
}

/**
 * Configuration address specification for listening servers.
 */
sealed class ConfigListenAddr {
    data class Ip(
        val addresses: List<String>,
        val port: Int,
    ) : ConfigListenAddr()

    data class Unix(
        val path: String,
    ) : ConfigListenAddr()

    companion object {
        fun fromSocketAddr(address: String, port: Int): ConfigListenAddr =
            Ip(listOf(address), port)

        fun unixFromPath(path: String): ConfigListenAddr =
            Unix(path)
    }
}

/**
 * Socket shutdown direction.
 */
enum class Shutdown {
    READ,
    WRITE,
    BOTH,
}

/**
 * Unified listener abstraction.
 */
sealed class Listener {
    data class Tcp(
        val address: String,
        val port: Int,
    ) : Listener()

    data class Unix(
        val path: String,
    ) : Listener()

    fun localAddr(): ListenAddr =
        when (this) {
            is Tcp -> ListenAddr.Ip(address, port)
            is Unix -> ListenAddr.Unix(path)
        }

    fun accept(): Pair<Connection, String?> =
        when (this) {
            is Tcp -> Pair(Connection.Tcp(address, port), "$address:$port")
            is Unix -> Pair(Connection.Unix(path), null)
        }
}

/**
 * Unified connection abstraction.
 */
sealed class Connection {
    data class Tcp(
        val peerAddress: String?,
        val peerPort: Int?,
    ) : Connection()

    data class Unix(
        val path: String,
    ) : Connection()

    fun peerAddr(): String? =
        when (this) {
            is Tcp -> peerAddress?.let { "$it:$peerPort" }
            is Unix -> null
        }

    fun tryClone(): Connection =
        when (this) {
            is Tcp -> Tcp(peerAddress, peerPort)
            is Unix -> Unix(path)
        }

    fun shutdown(how: Shutdown = Shutdown.BOTH) {
        // Platform stream shutdown
    }
}
