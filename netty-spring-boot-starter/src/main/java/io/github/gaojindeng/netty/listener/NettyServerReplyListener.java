package io.github.gaojindeng.netty.listener;

/**
 * @author gjd
 */
public interface NettyServerReplyListener<T, R> {
    R onMessage(T message);
}
