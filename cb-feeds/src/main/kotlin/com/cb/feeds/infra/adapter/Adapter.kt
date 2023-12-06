package com.cb.feeds.infra.adapter

import com.cb.base.core.KtJsonTools
import org.springframework.stereotype.Component


data class RelationDto(
    val fromId: String,
    val toId: String,
    val relation: String,
    val createAt: Long,
)

/**
 * 业务比较简单，把所有对三方的 adapter 先 mock 到一起
 */
interface GrpcAdapter {
    fun queryFollowerCnt(authorIds: List<Int>): Map<Int, Int>

    fun filterUserNeedToPush(userIds: List<Int>): List<Int> {
        return emptyList()
    }

    fun queryFriendsHasPublished(userId: Long, timeWindow: LongRange): List<Int>
}

@Component
open class GrpcAdapterImpl(
) : GrpcAdapter {

    private var relations: List<RelationDto>

    init {
        val filePath = Thread.currentThread().contextClassLoader.getResource("jsonl/relation.jsonl")?.file!!
        relations = KtJsonTools.readJsonEachRow(filePath, RelationDto::class.java)

    }

    override fun queryFollowerCnt(authorIds: List<Int>): Map<Int, Int> {
        /*1. 根据原始事件统计出 followCnt*/
        val cntMap = relations
            .asSequence()
            .filter { "follow" == it.relation }
            .groupBy { it.toId }
            .mapKeys {
                it.key.split(":")[1]
            }
            .map {
                Pair(
                    it.key.toInt(),
                    it.value.size
                )
            }.associate { it }

        /*2. 直接返回粉丝数*/
        return authorIds.associateWith { cntMap.getOrDefault(it, 0) }

    }

    override fun queryFriendsHasPublished(userId: Long, timeWindow: LongRange): List<Int> {
        TODO("Not yet implemented")
    }

}