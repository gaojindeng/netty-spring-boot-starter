package io.github.gaojindeng.netty.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author gjd
 */
@Configuration
@ConfigurationProperties(prefix = "netty")
public class NettyConfigProperties {

    /**
     * 服务端配置
     */
    private NettyServerProperties server;

    /**
     * 客户端配置
     */
    private NettyClientProperties client;

    public static class NettyClientProperties extends NettyClientConfig {
        private Map<String, NettyClientConfig> configs;

        public Map<String, NettyClientConfig> getConfigs() {
            return configs;
        }

        public void setConfigs(Map<String, NettyClientConfig> configs) {
            this.configs = configs;
        }
    }

    public static class NettyServerProperties extends NettyServerConfig {
        private Map<String, NettyServerConfig> configs;

        public Map<String, NettyServerConfig> getConfigs() {
            return configs;
        }

        public void setConfigs(Map<String, NettyServerConfig> configs) {
            this.configs = configs;
        }
    }

    public NettyServerProperties getServer() {
        return server;
    }

    public void setServer(NettyServerProperties server) {
        this.server = server;
    }

    public NettyClientProperties getClient() {
        return client;
    }

    public void setClient(NettyClientProperties client) {
        this.client = client;
    }
}
