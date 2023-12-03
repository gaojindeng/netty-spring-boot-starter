package io.github.gaojindeng.netty.server.executor;

import java.util.concurrent.*;

/**
 * @author gjd
 */
public class ThreadPoolExecutor extends java.util.concurrent.ThreadPoolExecutor {
    public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(Runnable command) {
        execute(command, 0, TimeUnit.MILLISECONDS);
    }

    public void execute(Runnable command, long timeout, TimeUnit unit) {
        try {
            super.execute(command);
        } catch (RejectedExecutionException rx) {
            if (super.getQueue() instanceof TaskQueue) {
                final TaskQueue queue = (TaskQueue) super.getQueue();
                try {
                    if (!queue.offer(command, timeout, unit)) {
                        throw new RejectedExecutionException("threadPoolExecutor.queueFull");
                    }
                } catch (InterruptedException x) {
                    throw new RejectedExecutionException(x);
                }
            } else {
                throw rx;
            }

        }
    }
}
