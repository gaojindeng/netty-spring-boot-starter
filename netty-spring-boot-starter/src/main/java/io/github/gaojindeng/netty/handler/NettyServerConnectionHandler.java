package io.github.gaojindeng.netty.handler;

import io.github.gaojindeng.netty.ConnectionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.github.gaojindeng.netty.AbstractNetty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gjd
 */
@ChannelHandler.Sharable
public class NettyServerConnectionHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(AbstractNetty.class);
    private final int maxConn;
    private final ConnectionManager connectionManager;


    public NettyServerConnectionHandler(int maxConn, ConnectionManager connectionManager) {
        this.maxConn = maxConn;
        this.connectionManager = connectionManager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int totalConnection = connectionManager.getTotalConnection();
        if (totalConnection >= maxConn) {
            // 如果连接数已达到最大值，拒绝新的连接请求
            log.error("netty connection is full ! size:{}", totalConnection);
            ctx.close();
            return;
        }
        // 如果连接数未达到最大值，接受新的连接，并将 Channel 添加到 ChannelGroup 中
        connectionManager.addChannel(ctx.channel());
        ctx.fireChannelActive();
    }
}