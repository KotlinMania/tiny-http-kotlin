// port-lint: source util/task_pool.rs
package io.github.kotlinmania.tinyhttp.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Manages task dispatch and background execution.
 */
class TaskPool(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
) {
    /**
     * Executes a task on the pool's coroutine scope.
     */
    fun spawn(task: () -> Unit) {
        scope.launch {
            task()
        }
    }

    companion object {
        fun new(): TaskPool = TaskPool()
    }
}
