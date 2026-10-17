// port-lint: source util/task_pool.rs
package io.github.kotlinmania.tinyhttp.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class Sharing(
    var activeTasks: Int = 0,
    var waitingTasks: Int = 0,
)

internal class Registration(
    private val sharing: Sharing,
    private val active: Boolean,
) {
    init {
        if (active) sharing.activeTasks++ else sharing.waitingTasks++
    }

    fun drop() {
        if (active) sharing.activeTasks-- else sharing.waitingTasks--
    }

    companion object {
        fun new(sharing: Sharing, active: Boolean = true): Registration =
            Registration(sharing, active)
    }
}

/**
 * Manages a collection of threads.
 *
 * A new thread is created every time all the existing threads are full.
 * Any idle thread will automatically die after a few seconds.
 */
class TaskPool(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
) {
    private val sharing = Sharing()

    /**
     * Executes a task on the pool's coroutine scope.
     * If no thread is available, spawns a new one.
     */
    fun spawn(task: () -> Unit) {
        scope.launch {
            task()
        }
    }

    fun addThread(initialFn: (() -> Unit)? = null) {
        if (initialFn != null) {
            spawn(initialFn)
        }
    }

    fun drop() {
        sharing.activeTasks = 0
    }

    companion object {
        fun new(): TaskPool = TaskPool()
    }
}
