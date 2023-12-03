package io.github.gaojindeng.netty.client;

import io.netty.channel.Channel;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author gjd
 */
public class ClientChannel {
    private final Channel channel;
    private final SynchronousQueue<Object> synchronousQueue = new SynchronousQueue<>();
    private volatile Thread thread;

    public ClientChannel(Channel channel) {
        this.channel = channel;
    }

    public void writeMessage(Object message) {
        synchronousQueue.offer(message);
    }

    public Object waitMessage(long timeout) {
        try {
            return synchronousQueue.poll(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            thread = null;
            throw new IllegalStateException("Connection closed, request interrupted", e);
        }
    }

    public void cleanMessage() {
        synchronousQueue.clear();
        thread = Thread.currentThread();
    }

    public Channel getChannel() {
        return channel;
    }

    public Thread getThread() {
        return thread;
    }

    public void close() {
        channel.close();
        synchronousQueue.clear();
    }
}
