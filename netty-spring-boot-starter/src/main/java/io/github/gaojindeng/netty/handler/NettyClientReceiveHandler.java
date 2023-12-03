package io.github.gaojindeng.netty.handler;

import io.github.gaojindeng.netty.client.ClientChannel;
import io.github.gaojindeng.netty.client.ClientConnectionManger;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author gjd
 */
public class NettyClientReceiveHandler extends ChannelInboundHandlerAdapter {

    private final ClientConnectionManger clientConnectionManger;

    public NettyClientReceiveHandler(ClientConnectionManger clientConnectionManger) {
        this.clientConnectionManger = clientConnectionManger;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ClientChannel waitingChannel = clientConnectionManger.getWaitingChannel(ctx.channel());
        waitingChannel.writeMessage(msg);
    }
}
