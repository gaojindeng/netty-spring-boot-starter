package io.github.gaojindeng.netty.channel;

import io.github.gaojindeng.netty.ConnectionManager;
import io.github.gaojindeng.netty.handler.NettyServerConnectionHandler;
import io.github.gaojindeng.netty.handler.NettyServerReceiveHandler;
import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.github.gaojindeng.netty.properties.NettyServerConfig;
import io.github.gaojindeng.netty.server.AbstractNettyServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author gjd
 */
public class ServerChannelHandlerManager extends BaseChannelHandlerManager {

    private final NettyServerConfig nettyServerConfig;

    private final AbstractNettyServer nettyServer;

    private final ConnectionManager connectionManager;

    public ServerChannelHandlerManager(NettyServerConfig nettyProperties, AbstractNettyServer nettyServer, ConnectionManager connectionManager) {
        this.nettyServerConfig = nettyProperties;
        this.nettyServer = nettyServer;
        this.connectionManager = connectionManager;

    }

    @Override
    List<ChannelHandler> beforeHandlers() {
        List<ChannelHandler> channelHandlers = new ArrayList<>();
        channelHandlers.add(new IdleStateHandler(nettyServerConfig.getReaderIdleSeconds(), nettyServerConfig.getWriterIdleSeconds(), nettyServerConfig.getAllIdleSeconds(), TimeUnit.SECONDS));
        channelHandlers.add(new NettyServerConnectionHandler(nettyServerConfig.getMaxConn(), connectionManager));
        return channelHandlers;
    }

    @Override
    List<ChannelHandler> afterHandlers() {
        return Collections.singletonList(new NettyServerReceiveHandler(nettyServer));
    }
}
