package io.github.gaojindeng.netty.client;

import io.github.gaojindeng.netty.properties.NettyClientConfig;

/**
 * @author gjd
 */
public class LongNettyClient extends BaseNettyClient {

    private final int maxConn;

    public LongNettyClient(NettyClientConfig nettyClientConfig) {
        super(nettyClientConfig);
        this.maxConn = nettyClientConfig.getMaxConn();
    }

    public LongNettyClient(String name, NettyClientConfig nettyClientConfig) {
        this(nettyClientConfig);
        super.setName(name);
    }

    @Override
    public void execute(Object message) {
        ClientChannel channel = acquireChannel();
        try {
            execute(channel, message);
        } finally {
            releaseChannel(channel);
        }
    }

    @Override
    public Object submit(Object message, long timeout) {
        ClientChannel channel = acquireChannel();
        try {
            return submit(channel, message, timeout);
        } finally {
            releaseChannel(channel);
        }

    }

    /**
     * 获取连接
     *
     * @return
     */
    public ClientChannel acquireChannel() {
        ClientChannel channel = getConnectionManager().acquireChannel();

        if (channel == null) {
            channel = connect();
        } else if (!channel.getChannel().isActive()) {
            channel.close();
            channel = connect();
        }
        return channel;
    }

    /**
     * 释放连接
     *
     * @param channel
     */
    public void releaseChannel(ClientChannel channel) {
        if (getConnectionManager().getTotalConnection() < maxConn) {
            getConnectionManager().releaseChannel(channel);
        } else {
            channel.close();
        }
    }


}
