package io.github.gaojindeng.netty.client;

/**
 * @author gjd
 */
public interface NettyClient {

    void execute(Object message);

    Object submit(Object message, long timeout);
}
