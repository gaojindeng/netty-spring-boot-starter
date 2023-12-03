package io.github.gaojindeng.netty.convert;

/**
 * @author gjd
 */
public interface MessageConverter<T, K> {
    K converter(T t);
}
