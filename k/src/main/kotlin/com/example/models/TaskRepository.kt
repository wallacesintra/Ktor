package com.example.models

import jdk.internal.org.jline.utils.ShutdownHooks

object TaskRepository {
    private val tasks = mutableListOf(
        Tasks("cleaning", "Clean the house", Priority.LOW),
        Tasks("gardening", "Mow the lawn", Priority.MEDIUM),
        Tasks("shopping", "Buy the groceries", Priority.HIGH),
        Tasks("painting", "Paint the fence", Priority.HIGH),
    )

    fun allTasks(): List<Tasks> = tasks

    fun tasksByPriority(priority: Priority): List<Tasks> = tasks.filter { it.priority == priority }

    fun taskByName(name: String): Tasks? = tasks.find { it.name == name }

    fun addTask(task: Tasks) {
        if (taskByName(task.name) == null) {
            throw IllegalStateException("Task with name ${task.name} already exists")
        }
        tasks.add(task)
    }
}


