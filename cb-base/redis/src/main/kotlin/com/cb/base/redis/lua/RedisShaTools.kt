package com.cb.base.redis.lua

import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * <pre>
 *     代码来自 spring-data-redis 的  {@see org.springframework.data.redis.core.script.DigestUtils}
 * </pre>
 *
 * @author carl
 * @create 2022-12-08 12:05 PM
 **/
class RedisShaTools {
    companion object {
        private val HEX_CHARS = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f'
        )

        private val UTF8_CHARSET = Charset.forName("UTF8")

        /**
         * Returns the SHA1 of the provided data
         *
         * @param data The data to calculate, such as the contents of a file
         * @return The human-readable SHA1
         */
        fun sha1DigestAsHex(data: String): String {
            val dataBytes = getDigest("SHA").digest(data.toByteArray(UTF8_CHARSET))
            return String(encodeHex(dataBytes))
        }

        private fun encodeHex(data: ByteArray): CharArray {
            val l = data.size
            val out = CharArray(l shl 1)
            var i = 0
            var j = 0
            while (i < l) {
                out[j++] = HEX_CHARS[0xF0 and data[i].toInt() ushr 4]
                out[j++] = HEX_CHARS[0x0F and data[i].toInt()]
                i++
            }
            return out
        }

        /**
         * Creates a new [MessageDigest] with the given algorithm. Necessary because `MessageDigest` is not
         * thread-safe.
         */
        private fun getDigest(algorithm: String): MessageDigest {
            return try {
                MessageDigest.getInstance(algorithm)
            } catch (ex: NoSuchAlgorithmException) {
                throw IllegalStateException("Could not find MessageDigest with algorithm \"$algorithm\"", ex)
            }
        }

    }
}