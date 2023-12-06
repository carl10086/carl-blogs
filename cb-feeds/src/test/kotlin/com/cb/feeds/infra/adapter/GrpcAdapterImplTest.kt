package com.cb.feeds.infra.adapter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GrpcAdapterImplTest {
    private val grpcAdapter = GrpcAdapterImpl()

    @Test
    fun `test queryFollowerCnt`() {
        grpcAdapter.queryFollowerCnt(listOf(1, 2, 3)).forEach { println(it) }
    }
}