package com.cb.examples.netty.echo

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler

@Sharable
class EchoServer : ChannelInboundHandlerAdapter() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        ctx.write(msg)
    }
}

fun main(args: Array<String>) {
    val boss = NioEventLoopGroup(1)
    val worker = NioEventLoopGroup()

    try {
        val serverBoot = ServerBootstrap()

        serverBoot
            .group(boss, worker)
            .channel(NioServerSocketChannel::class.java) /*nio 模式*/
            .option(ChannelOption.SO_BACKLOG, 100)
            .handler(LoggingHandler(LogLevel.INFO))
            .childHandler(object : ChannelInitializer<SocketChannel>() {
                override fun initChannel(ch: SocketChannel) {
                }
            })

    } finally {
        boss.shutdownGracefully()
        worker.shutdownGracefully()
    }
}