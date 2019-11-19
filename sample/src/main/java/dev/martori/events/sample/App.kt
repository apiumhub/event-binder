package dev.martori.events.sample

import android.app.Application
import dev.martori.events.core.GlobalBind
import dev.martori.events.sample.binding.binds.bindDetailsErrors
import dev.martori.events.sample.data.network.api.DetailsDto
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.android.ext.android.get

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        embeddedServer(Netty, 8080) {
            install(ContentNegotiation) { gson { } }
            routing {
                get("/details/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: 0
                    call.respond(DetailsDto(id, "Named: $id"))
                }
            }
        }.start()
        initKoin()
        GlobalBind.bindDetailsErrors(get(), get())
    }
}