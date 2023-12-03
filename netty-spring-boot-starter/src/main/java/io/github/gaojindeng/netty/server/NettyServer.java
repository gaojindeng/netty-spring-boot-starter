package io.github.gaojindeng.netty.server;

import io.github.gaojindeng.netty.listener.NettyServerListener;
import io.github.gaojindeng.netty.listener.NettyServerReplyListener;
import io.netty.channel.Channel;
import io.github.gaojindeng.netty.properties.NettyServerConfig;
import io.github.gaojindeng.netty.server.executor.NettyServerExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author gjd
 */
public class NettyServer extends AbstractNettyServer {
    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);

    private NettyServerListener nettyServerListener;

    private NettyServerReplyListener nettyServerReplyListener;

    private final NettyServerExecutor nettyServerExecutor;

    private boolean started;

    public NettyServer(NettyServerConfig nettyServerConfig) {
        super(nettyServerConfig);
        this.nettyServerExecutor = new NettyServerExecutor(nettyServerConfig.getCorePoolSize(), nettyServerConfig.getMaxPoolSize());
    }

    public NettyServer(String name, NettyServerConfig nettyServerConfig) {
        this(nettyServerConfig);
        super.setName(name);
    }


    @Override
    public void start() {
        Thread thread = new Thread(super::start);
        thread.setName("NettyServerThread-" + getName());
        nettyServerExecutor.getExecutor().execute(thread);
        started = true;
    }

    @Override
    public void handleMessage(Channel channel, Object object) {
        nettyServerExecutor.getExecutor().execute(() -> {
            Object req = converterReq(object);
            if (nettyServerListener != null) {
                nettyServerListener.onMessage(req);
            } else if (nettyServerReplyListener != null) {
                Object response = converterRes(nettyServerReplyListener.onMessage(req));
                channel.writeAndFlush(response);
            } else {

            }
        });
    }

    public boolean isStarted() {
        return started;
    }

    public void setNettyServerListener(NettyServerListener nettyServerListener) {
        this.nettyServerListener = nettyServerListener;
    }

    public void setNettyServerReplyListener(NettyServerReplyListener nettyServerReplyListener) {
        this.nettyServerReplyListener = nettyServerReplyListener;
    }
}
