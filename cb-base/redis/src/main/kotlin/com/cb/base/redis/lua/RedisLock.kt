package com.cb.base.redis.lua

import com.cb.base.core.ClassResourceTools
import io.lettuce.core.ScriptOutputType
import io.lettuce.core.SetArgs
import io.lettuce.core.api.sync.RedisCommands
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*

interface RedisLock {
    /**
     * @param resource: 要锁的资源粒度
     * @param ttl: 锁的超时时间
     * @param
     */
    fun tryLock(resource: String, ttl: Duration): Boolean
    fun unlock(resource: String)
}

/**
 * 单实例的 安全分布式锁, 基于 redis 官方文档实现
 * https://redis.io/docs/latest/develop/use/patterns/distributed-locks/#implementations 中的 单实例算法
 */
class SimpleRedisLock(private val sync: RedisCommands<String, String>) : RedisLock {

    companion object {
        val id = UUID.randomUUID().toString()
        fun clientId(): String {
            return "$id:${Thread.currentThread().threadId()}"
        }

        private val logger = LoggerFactory.getLogger(SimpleRedisLock::class.java)

        private val unlockLua: RedisLuaScript =
            RedisLuaScript(ClassResourceTools.readAsString("lua/single_redis_lock_unlock.lua"))
    }

    override fun tryLock(resource: String, ttl: Duration): Boolean {
        val clientId = clientId()
        val result = sync.set(resource, clientId, SetArgs.Builder.nx().px(ttl))
        logger.info("lock finish, clientId:{}, resource:{}, result:{}", clientId, resource, result)
        return result == "OK"
    }

    override fun unlock(resource: String) {
        val clientId = clientId()
        val result: Int = LettuceEvalTools.evalLuaScript(
            unlockLua,
            sync,
            ScriptOutputType.INTEGER,
            arrayOf(resource),
            clientId
        )

        logger.info("unlock finish, clientId:{}, resource:{}, result:{}", clientId, resource, result)
    }


}

