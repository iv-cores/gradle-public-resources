package org.ivcode.gradle.www.tasklet

import org.gradle.api.Task

/**
 * A tasklet represents only the logic of a task.
 * This is to make an isolable unit of work that can be tested.
 */
@FunctionalInterface
interface Tasklet<T: Task> {

    /**
     * Configures the task.
     */
    fun configuration(task: T)
}