package com.cb.sch.recommend

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Undertow
import org.http4k.server.asServer


fun helloWorld(): HttpHandler {
    return routes(
        "/hello" bind Method.GET to { Response(OK).body("Hello World!") }
    )
}

fun main(args: Array<String>) {
    helloWorld().asServer(Undertow(8080)).start()
}