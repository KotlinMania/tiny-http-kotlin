// port-lint: source util/messages_queue.rs
package io.github.kotlinmania.tinyhttp.util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.milliseconds

class MessagesQueueTest {
    @Test
    fun testPushAndPop() {
        val q = MessagesQueue.withCapacity<String>(4)
        q.push("first")
        q.push("second")
        assertEquals(2, q.size())
        assertEquals("first", q.pop())
        assertEquals("second", q.pop())
        assertNull(q.pop())
    }

    @Test
    fun testUnblockSignal() {
        val q = MessagesQueue.withCapacity<Int>(4)
        q.push(42)
        q.unblock()
        assertEquals(42, q.pop())
        assertNull(q.pop())
    }

    @Test
    fun testPopTimeout() {
        val q = MessagesQueue.withCapacity<String>(4)
        assertNull(q.popTimeout(100.milliseconds))
        q.push("delayed")
        assertEquals("delayed", q.popTimeout(100.milliseconds))
        assertNull(q.popTimeout(10.milliseconds))
    }
}
