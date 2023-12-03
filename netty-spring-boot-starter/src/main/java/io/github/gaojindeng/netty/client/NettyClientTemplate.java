package io.github.gaojindeng.netty.client;


/**
 * @author gjd
 */
public class NettyClientTemplate {

    private final NettyClient nettyClient;

    public NettyClientTemplate(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    public void send(Object message) {
        nettyClient.execute(message);
    }

    public <T> T sendAndReceive(Object message) {
        return sendAndReceive(message, 0);
    }

    public <T> T sendAndReceive(Object message, long timeout) {
        return (T) nettyClient.submit(message, timeout);
    }
}
