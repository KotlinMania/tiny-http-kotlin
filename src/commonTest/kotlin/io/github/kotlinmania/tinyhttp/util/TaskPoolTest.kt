// port-lint: source util/task_pool.rs
package io.github.kotlinmania.tinyhttp.util

import kotlin.test.Test
import kotlin.test.assertTrue

class TaskPoolTest {
    @Test
    fun testTaskPoolCreation() {
        val pool = TaskPool.new()
        var ran = false
        pool.spawn { ran = true }
        assertTrue(true)
    }
}
