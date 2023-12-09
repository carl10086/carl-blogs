package com.cb.examples.netty.echo

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup

open class EchoClientHandler(
    private val bufSize: Int
) : ChannelInboundHandlerAdapter() {

    private val firstMessage: ByteBuf = Unpooled.buffer(bufSize).apply {
        for (i in 0 until this.capacity()) {
            this.writeByte(i.toByte().toInt())
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        ctx.writeAndFlush(firstMessage)
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        ctx.write(msg)
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.flush()
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.exceptionCaught(ctx, cause)", "io.netty.channel.ChannelInboundHandlerAdapter")
    )
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        super.exceptionCaught(ctx, cause)
        ctx.close()
    }
}

fun main(args: Array<String>) {
    val b = Bootstrap()
    val group = NioEventLoopGroup()

    try {
        b.group(group)
            .channel(io.netty.channel.socket.nio.NioSocketChannel::class.java)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(object : io.netty.channel.ChannelInitializer<io.netty.channel.socket.SocketChannel>() {
                override fun initChannel(ch: io.netty.channel.socket.SocketChannel) {
                    val pipeline = ch.pipeline()
                    pipeline.addLast(EchoClientHandler(256))
                }
            })

        val f = b.connect("127.0.0.1", 8087)
        f.channel().closeFuture().sync()
    } finally {
        group.shutdownGracefully()
    }

}