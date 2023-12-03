package io.github.gaojindeng.netty.client;

import io.github.gaojindeng.netty.ConnectionManager;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author gjd
 */
public class ClientConnectionManger implements ConnectionManager {

    /**
     * 通道管理组
     */
    private final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 空闲通道
     */
    private final LinkedBlockingQueue<ClientChannel> idleChannelPool = new LinkedBlockingQueue<>();

    /**
     * 等待返回消息的通道
     */
    private final Map<String, ClientChannel> waitChannelMap = new ConcurrentHashMap<>();


    @Override

    public void addChannel(Channel channel) {
        channelGroup.add(channel);
    }

    public ClientChannel addWaitChannel(Channel channel) {
        ClientChannel clientChannel = new ClientChannel(channel);
        waitChannelMap.put(channel.id().asLongText(), clientChannel);
        return clientChannel;
    }

    public ClientChannel getWaitingChannel(Channel channel) {
        return waitChannelMap.get(channel.id().asLongText());
    }

    @Override

    public int getTotalConnection() {
        return channelGroup.size();
    }

    public ClientChannel acquireChannel() {
        return idleChannelPool.poll();
    }

    public void releaseChannel(ClientChannel channel) {
        idleChannelPool.offer(channel);
    }
}
