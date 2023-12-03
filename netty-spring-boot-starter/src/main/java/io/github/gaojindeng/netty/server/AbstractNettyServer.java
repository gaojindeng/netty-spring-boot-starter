package io.github.gaojindeng.netty.server;

import io.github.gaojindeng.netty.channel.ServerChannelHandlerManager;
import io.github.gaojindeng.netty.AbstractNetty;
import io.github.gaojindeng.netty.properties.NettyServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gjd
 */
public abstract class AbstractNettyServer extends AbstractNetty {
    private static final Logger log = LoggerFactory.getLogger(AbstractNettyServer.class);

    protected EventLoopGroup bossGroup;
    protected EventLoopGroup workerGroup;


    public AbstractNettyServer(NettyServerConfig nettyServerConfig) {
        super(nettyServerConfig);
        super.setConnectionManager(new ServerConnectionManger());
        super.setChannelInitializer(new ServerChannelHandlerManager(nettyServerConfig, this, getConnectionManager()));
        super.init();
        this.port = nettyServerConfig.getPort();
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup(nettyServerConfig.getIoThreads());
    }

    public void start() {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = serverBootstrap.childHandler(channelInitializer).bind(port).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("Netty started successfully on port {}", port);
                } else {
                    log.error("Failed to start server on port {}", port, future.cause());
                    close();
                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("InterruptedException to start server on port {}", port, e);
        } finally {
            close();
        }
    }

    public abstract void handleMessage(Channel channel, Object object);


    @Override
    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
