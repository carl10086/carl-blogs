package com.cb.feeds.infra.persist

import com.cb.feeds.biz.FeedItem


data class FeedOutboxDO(
    /*此时 userId 就是 authorId*/
    val userId: Int,
    val feedId: String,
    val feedCreateAt: Long
)


data class FeedInboxDO(
    val userId: Int,
    val authorId: Int,
    val feedId: String,
    val feedCreateAt: Long
)


interface FeedOutboxDao {
    fun batchSave(items: List<FeedOutboxDO>)
    fun query(userIds: List<Int>, timeWindow: LongRange, limit: Int): List<FeedItem>
}

interface FeedInboxDao {
    fun query(userId: Long, timeWindow: LongRange, limit: Int): List<FeedItem>
}