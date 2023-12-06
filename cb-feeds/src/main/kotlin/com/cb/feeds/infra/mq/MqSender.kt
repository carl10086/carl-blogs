package com.cb.feeds.infra.mq

import com.cb.protobuf.events.FeedPublishedDto

interface MqSender {
    fun batchSend(pushedEvents: List<FeedPublishedDto>, topic: String)
}