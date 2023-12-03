package io.github.gaojindeng.netty.server.executor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gjd
 */
public class TaskQueue extends LinkedBlockingQueue<Runnable> {

    private volatile ThreadPoolExecutor executor;


    public TaskQueue() {
        super();
    }

    public TaskQueue(int capacity) {
        super(capacity);
    }

    public void setExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    @Override
    public boolean offer(Runnable runnable) {
        if (executor == null) {
            return super.offer(runnable);
        }
        if (executor.getPoolSize() < executor.getMaximumPoolSize()) {
            return false;
        }
        return super.offer(runnable);
    }
}