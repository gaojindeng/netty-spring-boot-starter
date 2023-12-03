package io.github.gaojindeng.netty.properties;

/**
 * @author gjd
 */
public class NettyServerConfig extends AbstractProperties {
    private int corePoolSize = 5;
    private int maxPoolSize = 100;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }
}
