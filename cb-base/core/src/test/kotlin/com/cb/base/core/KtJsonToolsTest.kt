package com.cb.base.core

import com.cb.base.core.KtJsonTools.Companion.readJsonEachRow
import com.cb.protobuf.events.FeedPublishedDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class KtJsonToolsTest {

    @Test
    fun `test readJsonEachRow`() {
        val filePath = Thread.currentThread().contextClassLoader.getResource("jsonl/feed_publish.jsonl")?.file!!
        // Call your method to get actual data

        // Call your method to get actual data
        val actualData: List<FeedPublishedDto> = readJsonEachRow(filePath, FeedPublishedDto::class.java)

        // Define what you expect to be in the file
        val expectedData = listOf(
            FeedPublishedDto.newBuilder()
                .setUserId(1)
                .setFeedId("feed1")
                .setFeedCreateAt(1638740612000)
                .build(),
            FeedPublishedDto.newBuilder()
                .setUserId(2)
                .setFeedId("feed2")
                .setFeedCreateAt(1638740612100)
                .build(),
            FeedPublishedDto.newBuilder()
                .setUserId(3)
                .setFeedId("feed3")
                .setFeedCreateAt(1638740612200)
                .build(),
            FeedPublishedDto.newBuilder()
                .setUserId(4)
                .setFeedId("feed4")
                .setFeedCreateAt(1638740612300)
                .build(),
            FeedPublishedDto.newBuilder()
                .setUserId(5)
                .setFeedId("feed5")
                .setFeedCreateAt(1638740612400)
                .build()
        )

        // Now use assertEquals to verify the file contents were parsed correctly
        assertEquals(expectedData, actualData)
    }
}