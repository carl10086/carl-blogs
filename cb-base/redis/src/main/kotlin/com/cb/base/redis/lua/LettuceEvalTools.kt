package com.cb.base.redis.lua

import io.lettuce.core.RedisNoScriptException
import io.lettuce.core.ScriptOutputType
import io.lettuce.core.api.sync.RedisCommands
import org.slf4j.LoggerFactory

/**
 * <pre>
 *     简单工具类, 执行 lua 脚本 ,不存在,自动加载
 * </pre>
 * @author carl
 * @create 2022-12-08 3:03 PM
 **/
class LettuceEvalTools {

    companion object {

        private val log = LoggerFactory.getLogger(LettuceEvalTools::class.java)

        /**
         * <pre>
         *      执行 eval 脚本 ;
         *      1. 自动计算 sha, 不走 server load
         *      2. server 端 不存在的时候 走 eval, 会自动缓存在 server 端
         * </pre>
         *
         *
         * @param script lua 脚本, 必须包含 sha
         */
        fun <K, V, T> evalLuaScript(
            script: RedisLuaScript,
            sync: RedisCommands<K, V>,
            type: ScriptOutputType,
            keys: Array<K>,
            vararg values: V
        ): T {
            if (log.isDebugEnabled) {
                log.debug("script found cached with sha in server , sha:{}", script.sha)
            }
            return try {
                sync.evalsha(
                    script.sha,
                    type,
                    keys,
                    *values
                )

            } catch (e: RedisNoScriptException) {
                log.warn("script not cached in server, use eval, script: $script")
                /*脚本不存在 ,走 eval, 下一次会自动 缓存在 server 端*/
                sync.eval(
                    script.luaSrcCode,
                    type,
                    keys,
                    *values
                )
            }
        }
    }

}