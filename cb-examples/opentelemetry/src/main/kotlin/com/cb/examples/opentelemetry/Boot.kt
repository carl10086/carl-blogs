package com.cb.examples.opentelemetry

import org.slf4j.LoggerFactory
import org.springframework.boot.Banner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.ThreadLocalRandom


@SpringBootApplication
open class Boot


@RestController
open class RollController {

    @GetMapping("/rolldice")
    fun index(@RequestParam("player") player: Optional<String>): String {
        val result = this.getRandomNumber(1, 6)
        if (player.isPresent) {
            logger.info("{} is rolling the dice: {}", player.get(), result)
        } else {
            logger.info("Anonymous player is rolling the dice: {}", result)
        }
        return result.toString()
    }

    private fun getRandomNumber(min: Int, max: Int): Int {
        return ThreadLocalRandom.current().nextInt(min, max + 1)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RollController::class.java)
    }

}

fun main(args: Array<String>) {
    val app = SpringApplication(Boot::class.java)
    app.setBannerMode(Banner.Mode.OFF)
    app.run(*args)
}