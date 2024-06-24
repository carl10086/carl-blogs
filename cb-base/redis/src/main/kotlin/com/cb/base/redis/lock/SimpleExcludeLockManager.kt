package com.cb.base.redis.lock

import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class LockResult(
    private val resource: String,
    private val clientId: String,
    private val lockManager: SimpleExcludeLockManager
) : AutoCloseable {
    override fun close() {
        lockManager.unlock(resource, clientId)
    }

}

/**
 * 基于 内存的排他锁: 简单实现
 *
 * 1. 基于 ConcurrentHashMap 原子性实现，putIfAbsent 则成功
 */
class SimpleExcludeLockManager(
    size: Int = 256
) {
    private val lockMap = ConcurrentHashMap<String, String>(size)

    private fun clientId(): String {
        val uuid = UUID.randomUUID().toString()
        val threadId = Thread.currentThread().threadId().toString()
        return "$uuid:$threadId"
    }

    fun tryLock(resource: String): LockResult? {
        val clientId = clientId()
        val existingClientId = lockMap.putIfAbsent(resource, clientId)
        return if (existingClientId == null) {
            // Successfully acquired lock
            logger.info("client :{} get lock:{} success", clientId, resource)
            LockResult(resource, clientId, this)
        } else {
            // Failed to acquire lock
            logger.info("client :{} get lock:{} failed", clientId, resource)
            null
        }
    }

    fun unlock(resource: String, clientId: String) {
        lockMap.computeIfPresent(resource) { _, existingClientId ->
            val threadId = Thread.currentThread().threadId().toString()
            val existingThreadId = existingClientId.split(":")[1]
            if (existingClientId == clientId && threadId == existingThreadId) {
                logger.info("client {} unlock {} success", clientId, resource)
                null // Remove the resource from the map
            } else {
                logger.info("client {} unlock {} failed", clientId, resource)
                existingClientId
            }
        }
    }


    companion object {
        private val logger = LoggerFactory.getLogger(SimpleExcludeLockManager::class.java)
    }
}