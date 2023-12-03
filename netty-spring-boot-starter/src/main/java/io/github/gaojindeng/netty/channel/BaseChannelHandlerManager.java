package io.github.gaojindeng.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author gjd
 */
public abstract class BaseChannelHandlerManager extends ChannelInitializer<Channel> {

    private final List<ChannelHandler> sharableHandlers = new ArrayList<>();

    private final List<Supplier<ChannelHandler>> noSharableHandlers = new ArrayList<>();

    public void add(ChannelHandler channelHandler) {
        sharableHandlers.add(channelHandler);
    }

    public void add(Supplier<ChannelHandler> supplier) {
        noSharableHandlers.add(supplier);
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        beforeHandlers().forEach(pipeline::addLast);

        // 添加自定义不可共享处理器
        noSharableHandlers.forEach(s -> pipeline.addLast(s.get()));

        // 添加自定义可共享处理器
        sharableHandlers.forEach(pipeline::addLast);

        afterHandlers().forEach(pipeline::addLast);
    }

    abstract List<ChannelHandler> beforeHandlers();

    abstract List<ChannelHandler> afterHandlers();
}