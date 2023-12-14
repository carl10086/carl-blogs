package com.cb.base.redis.lua

/**
 * <pre>
 *     1. 类似 spring 封装了 redis 的 lua script .
 *     2. 但是没有提供 spring 的监控机制, 在 内容变化之后自动 重算 sha hash 码;
 *     3. 仅仅 提供了 1个字段 不用通过 redis server , 直接计算 sha 码
 *
 * </pre>
 *
 *
 * @author carl
 * @create 2022-12-08 12:25 PM
 **/
data class RedisLuaScript(
    /*lua 脚本源码*/
    val luaSrcCode: String
) {
    val sha: String = RedisShaTools.sha1DigestAsHex(luaSrcCode)

}