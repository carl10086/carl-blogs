package com.cb.example.web

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class ExampleApp

fun main(args: Array<String>) {
    SpringApplication.run(ExampleApp::class.java, *args)
}