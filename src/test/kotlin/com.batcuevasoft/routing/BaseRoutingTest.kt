package com.batcuevasoft.routing

import com.google.gson.Gson
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.ktor.plugin.Koin

abstract class BaseRoutingTest {

    protected var koinModules: Module? = null
    protected var moduleList: Application.() -> Unit = { }

    init {
        stopKoin()
    }

    fun withBaseTestApplication(test: suspend ApplicationTestBuilder.() -> Unit) {
        testApplication {
            koinModules?.let {
                install(ContentNegotiation) { gson { } }
                install(Koin) { modules(it) }
            }
            application {
                moduleList()
            }

            test()
        }
    }

    fun toJsonBody(obj: Any): String = gson.toJson(obj)

    fun <R> TestApplicationResponse.parseBody(clazz: Class<R>): R {
        return gson.fromJson(content, clazz)
    }

    companion object {
        private val gson = Gson()
    }
}
