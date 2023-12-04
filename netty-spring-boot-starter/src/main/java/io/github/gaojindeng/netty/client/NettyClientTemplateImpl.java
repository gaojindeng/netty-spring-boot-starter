package io.github.gaojindeng.netty.client;

/**
 * @author gjd
 */
public class NettyClientTemplateImpl implements NettyClientTemplate {

    private final NettyClient nettyClient;

    public NettyClientTemplateImpl(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    public void send(Object message) {
        nettyClient.execute(message);
    }

    @Override
    public <T> T sendAndReceive(Object message) {
        return sendAndReceive(message, 0);
    }

    @Override
    public <T> T sendAndReceive(Object message, long timeout) {
        return (T) nettyClient.submit(message, timeout);
    }
}
