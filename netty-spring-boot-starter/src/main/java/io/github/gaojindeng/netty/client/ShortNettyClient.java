package io.github.gaojindeng.netty.client;

import io.github.gaojindeng.netty.properties.NettyClientConfig;


/**
 * @author gjd
 */
public class ShortNettyClient extends BaseNettyClient {

    public ShortNettyClient(NettyClientConfig nettyClientConfig) {
        super(nettyClientConfig);
    }

    public ShortNettyClient(String name, NettyClientConfig nettyClientConfig) {
        this(nettyClientConfig);
        super.setName(name);
    }

    @Override
    public void execute(Object message) {
        ClientChannel channel = connect();
        try {
            execute(channel, message);
        } finally {
            channel.close();
        }
    }

    @Override
    public Object submit(Object message, long timeout) {
        ClientChannel channel = connect();
        try {
            return submit(channel, message, timeout);
        } finally {
            channel.close();
        }

    }
}
