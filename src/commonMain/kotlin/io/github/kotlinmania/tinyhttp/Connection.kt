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

    fun toIp(): Ip? =
        when (this) {
            is Ip -> this
            is Unix -> null
        }

    fun toUnix(): Unix? =
        when (this) {
            is Ip -> null
            is Unix -> this
        }

    fun fmt(): String =
        when (this) {
            is Ip -> "$address:$port"
            is Unix -> path
        }

    override fun toString(): String = fmt()

    companion object {
        fun from(ip: Ip): ListenAddr = ip

        fun from(unix: Unix): ListenAddr = unix
    }
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

    fun bind(): Listener =
        when (this) {
            is Ip -> Listener.Tcp(addresses.firstOrNull() ?: "0.0.0.0", port)
            is Unix -> Listener.Unix(path)
        }

    companion object {
        fun fromSocketAddrs(addresses: List<String>, port: Int): ConfigListenAddr =
            Ip(addresses, port)

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

    companion object {
        fun from(tcp: Tcp): Listener = tcp

        fun from(unix: Unix): Listener = unix
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

    fun read(buf: ByteArray): Int =
        when (this) {
            is Tcp -> 0
            is Unix -> 0
        }

    fun write(buf: ByteArray): Int =
        when (this) {
            is Tcp -> buf.size
            is Unix -> buf.size
        }

    fun flush() {
        when (this) {
            is Tcp -> Unit
            is Unix -> Unit
        }
    }

    fun peerAddr(): String? =
        when (this) {
            is Tcp -> peerAddress?.let { "$it:$peerPort" }
            is Unix -> null
        }

    fun shutdown(how: Shutdown = Shutdown.BOTH) {
        when (this) {
            is Tcp -> Unit
            is Unix -> Unit
        }
    }

    fun tryClone(): Connection =
        when (this) {
            is Tcp -> Tcp(peerAddress, peerPort)
            is Unix -> Unix(path)
        }

    companion object {
        fun from(tcp: Tcp): Connection = tcp

        fun from(unix: Unix): Connection = unix
    }
}
