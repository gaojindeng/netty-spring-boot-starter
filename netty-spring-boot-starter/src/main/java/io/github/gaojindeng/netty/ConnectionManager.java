package io.github.gaojindeng.netty;

import io.netty.channel.Channel;

/**
 * @author gjd
 */
public interface ConnectionManager {

    void addChannel(Channel channel);

    int getTotalConnection();
}
