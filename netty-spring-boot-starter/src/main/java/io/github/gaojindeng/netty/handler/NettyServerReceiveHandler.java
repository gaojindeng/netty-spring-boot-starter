package io.github.gaojindeng.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.github.gaojindeng.netty.server.AbstractNettyServer;

/**
 * @author gjd
 */
public class NettyServerReceiveHandler extends ChannelInboundHandlerAdapter {

    private final AbstractNettyServer nettyServer;

    public NettyServerReceiveHandler(AbstractNettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        nettyServer.handleMessage(ctx.channel(), msg);
    }
}
