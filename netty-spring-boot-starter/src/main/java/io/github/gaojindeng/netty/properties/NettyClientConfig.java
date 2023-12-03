package io.github.gaojindeng.netty.properties;

/**
 * @author gjd
 */
public class NettyClientConfig extends AbstractProperties {
    private String host;
    /**
     * 是否保持连接
     */
    private boolean keepConn;
    private long timeout = 60000;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isKeepConn() {
        return keepConn;
    }

    public void setKeepConn(boolean keepConn) {
        this.keepConn = keepConn;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
