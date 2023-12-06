package com.cb.base.core

import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

/**
 * @author carl
 * @since 2023-01-29 10:09 PM
 **/
class JucTools {

    companion object {

        private val log = LoggerFactory.getLogger(JucTools::class.java)

        /**
         * a concurrent tools for common supplier run at same time
         * @param timeout totally timeout
         * @param exec executor . may not thread Pool executor is better
         */
        fun <T> supplyAsync(
            sps: List<Supplier<List<T>>>,
            exec: ThreadPoolExecutor,
            timeout: Duration = Duration.ofMillis(300L)
        ): List<T> {
            if (sps.isNotEmpty()) {
                val futures = sps.map { CompletableFuture.supplyAsync(it, exec) }.toTypedArray()

                try {
                    CompletableFuture.allOf(*futures).get(timeout.toMillis(), TimeUnit.MILLISECONDS)
                } catch (e: Exception) {
                    /*if has exception ,we still try to return success futures*/
                    log.error("JucTools supplyAsync failed", e)
                    /*maybe useless, how to do it?*/
                    futures.filter { !it.isDone }.forEach { it.cancel(true) }
                }

                return buildList {
                    futures.filter { it.isDone && !it.isCompletedExceptionally }.forEach {
                        /*is done , has */
                        addAll(it.get())
                    }
                }

            }

            return emptyList()
        }
    }
}