package com.batcuevasoft.routing.registration

import com.batcuevasoft.model.ResponseUser
import com.batcuevasoft.modules.registration.RegistrationController
import com.batcuevasoft.modules.registration.registrationModule
import com.batcuevasoft.routing.BaseRoutingTest
import com.batcuevasoft.routing.instrumentation.RegistrationControllerInstrumentation.givenAResponseUser
import com.batcuevasoft.routing.instrumentation.RegistrationControllerInstrumentation.givenPostUserBody
import com.batcuevasoft.statuspages.InvalidUserException
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.koin.dsl.module

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrationRoutingTest : BaseRoutingTest() {

    private val registrationController: RegistrationController = mockk()

    @BeforeAll
    fun setup() {
        koinModules = module {
            single { registrationController }
        }

        moduleList = {
            install(Routing) {
                registrationModule()
            }
        }
    }

    @BeforeEach
    fun clearMocks() {
        clearMocks(registrationController)
    }

    @Test
    fun `when creating user with successful insertion, we return response user body`() = withBaseTestApplication {
        val userId = 11
        val responseUser = givenAResponseUser(userId)
        coEvery { registrationController.createUser(any()) } returns responseUser

        val body = toJsonBody(givenPostUserBody())
        val response = client.post("/user") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(body)
        }
        assertThat(HttpStatusCode.OK).isEqualTo(response.status)
        val responseBody = response.body<ResponseUser>()
        assertThat(responseUser).isEqualTo(responseBody)
    }

    @Test
    fun `when creating user already created, we return 400 error`() = withBaseTestApplication {
        coEvery { registrationController.createUser(any()) } throws InvalidUserException("User is already taken")

        val body = toJsonBody(givenPostUserBody())
        val exception = assertThrows<InvalidUserException> {
            client.post("/user") {
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(body)
            }
        }
        assertThat(exception.message).isEqualTo("User is already taken")
    }
}
