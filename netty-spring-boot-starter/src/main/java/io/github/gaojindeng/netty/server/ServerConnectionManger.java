package io.github.gaojindeng.netty.server;

import io.github.gaojindeng.netty.ConnectionManager;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author gjd
 * @since 2023/11/30
 */
public class ServerConnectionManger implements ConnectionManager {

    private final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void addChannel(Channel channel) {
        channelGroup.add(channel);
    }

    @Override
    public int getTotalConnection() {
        return channelGroup.size();
    }
}
