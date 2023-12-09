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
open class EchoServerHandler : ChannelInboundHandlerAdapter() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        ctx.write(msg)
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.flush()
    }

    @Deprecated("Deprecated in Java")
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}

fun main() {
    val boss = NioEventLoopGroup(1)
    val worker = NioEventLoopGroup()

    val serverHandler = EchoServerHandler()

    try {
        val b = ServerBootstrap()

        b.group(boss, worker)
            .channel(NioServerSocketChannel::class.java) /*nio 模式*/
            .option(ChannelOption.SO_BACKLOG, 100)
            .handler(LoggingHandler(LogLevel.INFO))
            .childHandler(object : ChannelInitializer<SocketChannel>() {
                override fun initChannel(ch: SocketChannel) {
                    val pipeline = ch.pipeline()
                    pipeline.addLast(serverHandler)
                }
            })

        val f = b.bind(8087).sync()
        f.channel().closeFuture().sync()
    } finally {
        boss.shutdownGracefully()
        worker.shutdownGracefully()
    }
}