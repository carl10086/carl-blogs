package com.cb.base.core

import org.springframework.core.io.ClassPathResource
import java.io.BufferedReader

object ClassResourceTools {

    fun readAsString(path: String): String {
        return ClassPathResource(path).inputStream.use {
            it.bufferedReader().use(BufferedReader::readText)
        }
    }

}