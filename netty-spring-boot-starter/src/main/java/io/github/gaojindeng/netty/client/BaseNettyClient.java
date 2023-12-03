package io.github.gaojindeng.netty.client;

import io.github.gaojindeng.netty.channel.ClientChannelHandlerManager;
import io.github.gaojindeng.netty.AbstractNetty;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.github.gaojindeng.netty.properties.NettyClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gjd
 */
public abstract class BaseNettyClient extends AbstractNetty implements NettyClient {
    private static final Logger log = LoggerFactory.getLogger(BaseNettyClient.class);
    protected final String host;
    protected final EventLoopGroup group;
    protected final Bootstrap bootstrap;
    protected final long timeout;


    public BaseNettyClient(NettyClientConfig nettyClientConfig) {
        super(nettyClientConfig);
        super.setConnectionManager(new ClientConnectionManger());
        super.setChannelInitializer(new ClientChannelHandlerManager(nettyClientConfig, getConnectionManager()));
        super.init();
        this.port = nettyClientConfig.getPort();
        this.host = nettyClientConfig.getHost();
        this.timeout = nettyClientConfig.getTimeout();
        group = new NioEventLoopGroup(nettyClientConfig.getIoThreads());
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).handler(channelInitializer);
    }


    protected void execute(ClientChannel channel, Object message) {
        Object request = converterReq(message);
        channel.getChannel().writeAndFlush(request);
    }

    protected Object submit(ClientChannel channel, Object message, long timeout) {
        channel.cleanMessage();
        Object request = converterReq(message);
        channel.getChannel().writeAndFlush(request);
        Object result = channel.waitMessage(timeout < 1 ? this.timeout : timeout);
        return converterRes(result);
    }

    protected ClientChannel connect() {
        ChannelFuture future = null;
        try {
            future = bootstrap.connect(host, port).sync();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        Channel channel = future.channel();
        getConnectionManager().addChannel(channel);
        return getConnectionManager().addWaitChannel(channel);
    }

    @Override
    public ClientConnectionManger getConnectionManager() {
        return ((ClientConnectionManger) super.getConnectionManager());
    }

    @Override
    public void close() {
        group.shutdownGracefully();
    }
}
