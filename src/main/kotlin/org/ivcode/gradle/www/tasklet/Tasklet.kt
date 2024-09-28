package org.ivcode.gradle.www.tasklet

import org.gradle.api.Task

/**
 * A tasklet represents only the logic of a task. This is to isolate the logic
 * so that it can be tested independently of the task.
 */
@FunctionalInterface
interface Tasklet<T: Task> {
    fun execute(task: T)
}