package com.cb.feeds.biz

import com.cb.feeds.infra.adapter.GrpcAdapter
import com.cb.feeds.infra.mq.MqSender
import com.cb.feeds.infra.persist.FeedInboxDao
import com.cb.feeds.infra.persist.FeedOutboxDO
import com.cb.feeds.infra.persist.FeedOutboxDao
import com.cb.protobuf.events.FeedPublishedDto
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.ArrayList
import kotlin.concurrent.withLock


@Service
open class FeedBizService(
    private val grpcAdapter: GrpcAdapter,
    private val feedOutboxDao: FeedOutboxDao,
    private val mqSender: MqSender,
    private val feedInboxDao: FeedInboxDao,
    private val feedQueryPool: ThreadPoolExecutor,
) {

    /**
     * 假设:
     * 1. 按照粉丝数来作为 push & pull 的条件
     */
    fun onFeedPublish(
        events: List<FeedPublishedDto>
    ) {
        val authorIds = events.map { it.userId }

        /*1. 这些用户使用 push 策略, 基于 粉丝数或者当前上线状态计算*/
        val pushedUsers = grpcAdapter.filterUserNeedToPush(authorIds).toSet()

        /*2. 不管是推还是拉提前写入到 outBox, 并用这个作为最终一致性的兜底, 批处理*/
        val outboxList = events.map { FeedOutboxDO(it.userId, it.feedId, it.feedCreateAt) }
        feedOutboxDao.batchSave(outboxList)

        /*3. optional, 对于粉丝数 < 多少阈值的用户, 使用 push 来减少 pull 的压力*/
        val pushedEvents = events.filter { pushedUsers.contains(it.userId) }
        mqSender.batchSend(pushedEvents, "push")
    }

    fun followingActivity(userId: Long, timeWindow: LongRange): List<FeedItem> {
        val limit = 48
        val heap = FeedMergeHeap(limit)

        // 1. 先异步开启查询 index
        val inboxTask = CompletableFuture.supplyAsync({
            val inboxList = feedInboxDao.query(userId, timeWindow, limit)
            heap.push(inboxList)
        }, feedQueryPool)


        // 2. 然后看下关注的高粉丝用户中，有多少这段时间内发布了内容
        // 返回的是最好是不在 inBox 中，inBox 可以用来优化, 给 inBox 的用户打个 tag
        val friends = grpcAdapter.queryFriendsHasPublished(userId, timeWindow)
        val pushedUsers = grpcAdapter.filterUserNeedToPush(friends).toSet()

        // 3. 我们以拉为主，所以这里底层查询优化很关键
        // 为什么要分窗口 异步化，因为 分布式数据库会命中多个分区，小分区量的性能是最好的，这个规则也适合 mysql 分库分表
        val outBoxTasks = friends.asSequence().filter { !pushedUsers.contains(it) }.windowed(10).map { userIds ->
            CompletableFuture.supplyAsync({
                val outBoxList = feedOutboxDao.query(userIds, timeWindow, limit)
                heap.push(outBoxList)
            }, feedQueryPool)
        }.toList()

        val allTasks = ArrayList(outBoxTasks)
        allTasks.add(inboxTask)

        // 4. 对异步任务统一超时
        try {
            CompletableFuture.allOf(*allTasks.toTypedArray()).get(500, TimeUnit.MILLISECONDS)
        } catch (e: Exception) {
            allTasks.filter { !it.isDone }.forEach { it.cancel(true) }
            // fixme log
        }

        // TOdo ,here need to filter by exposed feed or blacklist
        return heap.getAll()
    }
}


data class FeedItem(
    val feedId: Long,
    val feedCreateAt: Long /*这里暂定用时间*/
)

/**
 * 用来同步的数据结构
 */
data class FeedMergeHeap(private val size: Int) {

    private val queue = PriorityQueue<FeedItem>(size) { o1, o2 ->
        if (o1.feedCreateAt > o2.feedCreateAt) 1 else -1
    }

    private val lock = ReentrantLock()

    fun push(items: List<FeedItem>) {
        lock.withLock {
            items.forEach {
                if (queue.size < size) {
                    queue.offer(it)
                } else if (it.feedCreateAt > queue.peek().feedCreateAt) {
                    queue.poll()
                    queue.offer(it)
                }
            }
        }
    }

    fun getAll(): List<FeedItem> {
        lock.withLock {
            return ArrayList(queue)
        }
    }
}
