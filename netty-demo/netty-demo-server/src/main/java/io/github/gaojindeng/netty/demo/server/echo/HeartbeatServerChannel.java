package io.github.gaojindeng.netty.demo.server.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gjd
 */
@ChannelHandler.Sharable
public class HeartbeatServerChannel extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(HeartbeatServerChannel.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            if ("ping".equals(((ByteBuf) msg).toString(CharsetUtil.UTF_8))) {
                ByteBuf byteBuf = Unpooled.copiedBuffer("pong", CharsetUtil.UTF_8);
                ctx.writeAndFlush(byteBuf);
                return;
            }
        }

        super.channelRead(ctx, msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.close();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
