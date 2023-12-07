package com.cb.feeds

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class Bootstrap

fun main(args: Array<String>) {
    SpringApplication.run(Bootstrap::class.java, *args)
}