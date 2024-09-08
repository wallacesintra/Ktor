import com.example.module
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun tasksCanBeFoundByPriority() = testApplication {
        application {
            module()
        }

        val response = client.get("/tasks/byPriority/medium")
        val body = response.bodyAsText()

        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(body, "Mow the lawn")
        assertContains(body, "Paint the fence")
    }

    @Test
    fun invalidPriorityProduce400Error() = testApplication {
        application {
            module()
        }

        val response = client.get("/tasks/byPriority/somethingWrong")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun unusedPriorityProduces404() = testApplication {
        application {
            module()
        }

        val response = client.get("/tasks/byPriority/Vital")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun newTaskCanBeAdded() = testApplication {
        application {
            module()
        }

        val response = client.post("/tasks") {
            header(
                HttpHeaders.ContentType,
                ContentType.Application.FormUrlEncoded.toString()
            )
            setBody(
                listOf(
                    "name" to "swimming",
                    "description" to "Go to the Sandy Beach",
                    "priority" to "Vital",
                ).formUrlEncode()
            )
        }

        assertEquals(HttpStatusCode.NoContent, response.status)
        val response2 = client.get("/tasks")
        assertEquals(HttpStatusCode.OK, response2.status)
        val body = response2.bodyAsText()

        assertContains(body, "swimming")
        assertContains(body, "Go to the beach")
    }

}
