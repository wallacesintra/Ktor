package com.example.plugins

import com.example.models.Priority
import com.example.models.TaskRepository
import com.example.models.Tasks
import com.example.models.tasksAsTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureRouting() {
    routing {

        staticResources("/task-ui", "task-ui")

        get("/") {
            call.respondText("Hello World!")
        }

        val tasks = TaskRepository.allTasks()

        get("/tasks"){
            call.respondText(
                contentType = ContentType.parse("text/html"),
//                text = """
//                    <h3>Tasks to do</h3>
//                    <ol>
//                    <li>Task 1</li>
//                    <li>Task 2</li>
//                    <li>Task 3</li>
//                    </ol>
//                """.trimIndent()
                text = tasks.tasksAsTable()
            )
        }

        get("/tasks/byPriority/{priority}"){
            val priorityTxt = call.parameters["priority"]

            if(priorityTxt.isNullOrEmpty()){
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            try {
                val priority = Priority.valueOf(priorityTxt.uppercase(Locale.getDefault()))
                val tasksList = TaskRepository.tasksByPriority(priority)


                if (tasksList.isEmpty()){
                    call.respond(HttpStatusCode.NotFound)
                    return@get

                }

                call.respondText(
                    contentType = ContentType.parse("text/html"),
                    text = tasksList.tasksAsTable()
                )
            }catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post("/tasks"){
            val formContent = call.receiveParameters()

            val params = Triple(
                formContent["name"] ?: "",
                formContent["description"] ?: "",
                formContent["priority"] ?: ""
            )

            if(params.toList().any{it.isNotEmpty()}){
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            try {
                val priority = Priority.valueOf(params.third.uppercase(Locale.getDefault()))
                TaskRepository.addTask(
                    Tasks(
                        name = params.first,
                        description = params.second,
                        priority = priority,
                    )
                )

                call.respond(HttpStatusCode.NoContent)

            }catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest)
            }
        }


    }
}
