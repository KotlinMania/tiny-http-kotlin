// port-lint: source connection.rs
package io.github.kotlinmania.tinyhttp

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ConnectionTest {
    @Test
    fun testListenAddr() {
        val ip = ListenAddr.Ip("127.0.0.1", 8080)
        assertEquals("127.0.0.1:8080", ip.toString())
        assertEquals("127.0.0.1", ip.toIp()?.address)
        assertNull(ip.toUnix())

        val unix = ListenAddr.Unix("/tmp/socket.sock")
        assertEquals("/tmp/socket.sock", unix.toString())
        assertEquals("/tmp/socket.sock", unix.toUnix()?.path)
        assertNull(unix.toIp())
    }

    @Test
    fun testConfigListenAddr() {
        val configIp = ConfigListenAddr.fromSocketAddr("0.0.0.0", 80)
        assertEquals(ConfigListenAddr.Ip(listOf("0.0.0.0"), 80), configIp)

        val configUnix = ConfigListenAddr.unixFromPath("/tmp/app.sock")
        assertEquals(ConfigListenAddr.Unix("/tmp/app.sock"), configUnix)
    }
}
