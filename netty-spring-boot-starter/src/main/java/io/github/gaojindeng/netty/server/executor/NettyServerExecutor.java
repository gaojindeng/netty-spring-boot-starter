package io.github.gaojindeng.netty.server.executor;


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gjd
 */
public class NettyServerExecutor {

    private final ThreadPoolExecutor threadPoolExecutor;

    public NettyServerExecutor(String name, int corePoolSize,
                               int maximumPoolSize) {
        TaskQueue taskQueue = new TaskQueue();
        threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize + 1, maximumPoolSize + 1, 60, TimeUnit.SECONDS,
                taskQueue, new CustomThreadFactory(name + "-listener"), new ThreadPoolExecutor.AbortPolicy());
        taskQueue.setExecutor(threadPoolExecutor);
    }

    public ThreadPoolExecutor getExecutor() {
        return threadPoolExecutor;
    }

    public void setCorePoolSize(int corePoolSize) {
        threadPoolExecutor.setCorePoolSize(corePoolSize);
    }

    public void setMaxPoolSize(int maxPoolSize) {
        threadPoolExecutor.setMaximumPoolSize(maxPoolSize);
    }

    public static class CustomThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        public CustomThreadFactory(String poolName) {
            namePrefix = poolName + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, namePrefix + threadNumber.getAndIncrement());
        }
    }
}
