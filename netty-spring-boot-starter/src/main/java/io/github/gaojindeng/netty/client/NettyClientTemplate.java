package io.github.gaojindeng.netty.client;


/**
 * @author gjd
 */
public interface NettyClientTemplate {

    void send(Object message);

    <T> T sendAndReceive(Object message);

    <T> T sendAndReceive(Object message, long timeout);
}
