package io.github.gaojindeng.netty.listener;

/**
 * @author gjd
 */
public interface NettyServerListener<T> {
    void onMessage(T message);
}
