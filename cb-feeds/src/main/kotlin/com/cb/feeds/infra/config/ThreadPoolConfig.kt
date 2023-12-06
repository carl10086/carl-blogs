package com.cb.feeds.infra.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.CustomizableThreadFactory
import java.util.concurrent.*

@Configuration
open class ThreadPoolConfig {

    private fun buildPool(
        threadNamePrefix: String,
        poolSize: Int,
        keepAliveTimeAsSec: Long,
        workQueue: BlockingQueue<Runnable>,
    ): ThreadPoolExecutor {
        return ThreadPoolExecutor(
            poolSize,
            poolSize,
            keepAliveTimeAsSec,
            TimeUnit.SECONDS,
            workQueue,
            CustomizableThreadFactory(threadNamePrefix)
        ) { r, executor ->
            val msg = """
            Thread pool is EXHAUSTED! Thread Name: $threadNamePrefix,
            detail: $executor
        """.trimIndent()
            logger.error(msg)
            throw RejectedExecutionException(msg)
        }.apply {
            allowCoreThreadTimeOut(true)
        }
    }

    @Bean
    open fun feedQueryPool(): ThreadPoolExecutor {
        return buildPool(
            "userFixAuditPool",  /*临时写死、TODO 后续拖到配置中心*/
            16,
            0,
            ArrayBlockingQueue<Runnable>(128),
        )
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ThreadPoolConfig::class.java)
    }
}