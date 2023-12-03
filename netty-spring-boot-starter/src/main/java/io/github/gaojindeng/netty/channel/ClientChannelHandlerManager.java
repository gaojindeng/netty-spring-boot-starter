package io.github.gaojindeng.netty.channel;

import io.github.gaojindeng.netty.client.ClientChannel;
import io.github.gaojindeng.netty.client.ClientConnectionManger;
import io.github.gaojindeng.netty.handler.NettyClientReceiveHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateHandler;
import io.github.gaojindeng.netty.properties.NettyClientConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author gjd
 */
public class ClientChannelHandlerManager extends BaseChannelHandlerManager {

    private final ClientConnectionManger clientConnectionManger;

    private final NettyClientConfig nettyClientConfig;

    public ClientChannelHandlerManager(NettyClientConfig nettyClientConfig, ClientConnectionManger clientConnectionManger) {
        this.clientConnectionManger = clientConnectionManger;
        this.nettyClientConfig = nettyClientConfig;
    }

    @Override
    List<ChannelHandler> beforeHandlers() {
        List<ChannelHandler> channelHandlers = new ArrayList<>();
        channelHandlers.add(new IdleStateHandler(nettyClientConfig.getReaderIdleSeconds(), nettyClientConfig.getWriterIdleSeconds(), nettyClientConfig.getAllIdleSeconds(), TimeUnit.SECONDS));
        //该处理器用来处理等待返回消息时，连接突然中断
        channelHandlers.add(new ChannelInboundHandlerAdapter() {
            @Override
            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                ClientChannel waitingChannel = clientConnectionManger.getWaitingChannel(ctx.channel());
                if (waitingChannel != null) {
                    Thread thread = waitingChannel.getThread();
                    if (thread == null) {
                        Thread.yield();
                    }
                    thread = waitingChannel.getThread();
                    if (thread != null) {
                        thread.interrupt();
                    }
                }
                super.channelInactive(ctx);
            }
        });
        return channelHandlers;
    }

    @Override
    List<ChannelHandler> afterHandlers() {
        List<ChannelHandler> channelHandlers = new ArrayList<>();
        channelHandlers.add(new NettyClientReceiveHandler(clientConnectionManger));
        return channelHandlers;
    }
}
