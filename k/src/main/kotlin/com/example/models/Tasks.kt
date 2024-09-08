package com.example.models

enum class Priority {
    LOW, MEDIUM, HIGH, VITAL
}

data class Tasks(
    val name: String,
    val description: String,
    val priority: Priority,
)

fun Tasks.taskAsRow() = """
    <tr>
    <td>$name</td> <td>$description</td> <td>$priority</td>
    </tr>
""".trimIndent()


fun List<Tasks>.tasksAsTable() = this.joinToString(
    prefix = "<table rules = \"all\">",
    postfix = "</table>",
    separator = "\n",
    transform = Tasks::taskAsRow
)
