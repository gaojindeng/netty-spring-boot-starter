package io.github.gaojindeng.netty.demo.client.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

/**
 * @author gjd
 */
@ChannelHandler.Sharable
public class HeartbeatClientChannel extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            if ("pong".equals(((ByteBuf) msg).toString(CharsetUtil.UTF_8))) {
                return;
            }
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
                ByteBuf byteBuf = Unpooled.copiedBuffer("ping", CharsetUtil.UTF_8);
                // 发送心跳消息
                ctx.writeAndFlush(byteBuf);
            } else if (event.state() == IdleState.READER_IDLE) {

            } else if (event.state() == IdleState.WRITER_IDLE) {

            }
            super.userEventTriggered(ctx, evt);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
