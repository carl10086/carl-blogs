package com.cb.base.core

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hubspot.jackson.datatype.protobuf.ProtobufModule
import java.io.File
import java.nio.charset.Charset

/**
 *<pre>
 * json 工具的替代品. 激活了 kotlin module 导致其他的工具方法都不需要
 *</pre>
 *@author carl.yu
 *@since 2023/1/1
 **/
class KtJsonTools {

    companion object {
        /**
         * 为默认的 mapper 定义行为
         * 1. 强制定义策略是 下划线转驼峰
         * 2. 支持 protobuf 生成的类直接转 json
         */
        val mapper: ObjectMapper = jacksonObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .registerModule(ProtobufModule())
            .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)


        fun <T> readJsonEachRow(filePath: String, clazz: Class<T>): List<T> {
            return File(filePath).bufferedReader(Charset.defaultCharset()).use { reader ->
                reader.lineSequence().map { mapper.readValue(it, clazz) }.toList()
            }
        }
    }
}