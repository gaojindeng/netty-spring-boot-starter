package io.github.gaojindeng.netty;

import io.github.gaojindeng.netty.client.BaseNettyClient;
import io.github.gaojindeng.netty.client.LongNettyClient;
import io.github.gaojindeng.netty.client.NettyClient;
import io.github.gaojindeng.netty.client.ShortNettyClient;
import io.github.gaojindeng.netty.properties.NettyClientConfig;
import io.github.gaojindeng.netty.properties.NettyConfigProperties;
import io.github.gaojindeng.netty.properties.NettyServerConfig;
import io.github.gaojindeng.netty.server.AbstractNettyServer;
import io.github.gaojindeng.netty.server.NettyServer;
import org.springframework.beans.factory.DisposableBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gjd
 */
public class DefaultNettyContainer implements DisposableBean {
    private NettyConfigProperties nettyConfigProperties;

    private NettyServer defaultNettyServer;
    private NettyClient defaultNettyClient;
    private final Map<String, NettyServer> nettyServerMap = new HashMap<>();
    private final Map<String, NettyClient> nettyClientMap = new HashMap<>();

    public DefaultNettyContainer(NettyConfigProperties nettyConfigProperties) {
        this.nettyConfigProperties = nettyConfigProperties;
        initNettyServer(nettyConfigProperties.getServer());
        initNettyClient(nettyConfigProperties.getClient());
    }

    private void initNettyClient(NettyConfigProperties.NettyClientProperties client) {
        if (client == null) {
            return;
        }
        defaultNettyClient = getDefaultNettyClient(client);
        if (defaultNettyClient != null) {
            nettyClientMap.put(((AbstractNetty) defaultNettyClient).getName(), defaultNettyClient);
        }
        Map<String, NettyClientConfig> configs = client.getConfigs();
        if (configs != null) {
            configs.forEach((name, value) -> {
                NettyClient nettyClient;
                if (value.isKeepConn()) {
                    nettyClient = new LongNettyClient(name, value);
                } else {
                    nettyClient = new ShortNettyClient(name, value);
                }
                nettyClientMap.put(((AbstractNetty) nettyClient).getName(), nettyClient);
            });
        }
    }

    private void initNettyServer(NettyConfigProperties.NettyServerProperties server) {
        if (server == null) {
            return;
        }
        defaultNettyServer = getDefaultNettyServer(server);
        if (defaultNettyServer != null) {
            nettyServerMap.put(defaultNettyServer.getName(), defaultNettyServer);
        }
        Map<String, NettyServerConfig> configs = server.getConfigs();
        if (configs != null) {
            configs.forEach((name, value) -> {
                NettyServer nettyServer = new NettyServer(name, value);
                nettyServerMap.put(nettyServer.getName(), nettyServer);
            });
        }
    }

    private NettyServer getDefaultNettyServer(NettyServerConfig nettyServerConfig) {
        Integer port = nettyServerConfig.getPort();
        if (port == null) {
            return null;
        }
        return new NettyServer(nettyServerConfig);
    }

    private NettyClient getDefaultNettyClient(NettyClientConfig nettyClientConfig) {
        String host = nettyClientConfig.getHost();
        Integer port = nettyClientConfig.getPort();
        if (host == null || port == null) {
            return null;
        }

        if (nettyClientConfig.isKeepConn()) {
            return new LongNettyClient(nettyClientConfig);
        } else {
            return new ShortNettyClient(nettyClientConfig);
        }
    }

    public NettyServer getNettyServer(String name) {
        return nettyServerMap.get(name);
    }

    public NettyClient getNettyClient(String name) {
        return nettyClientMap.get(name);
    }

    public Map<String, NettyServer> getNettyServerMap() {
        return nettyServerMap;
    }

    public Map<String, NettyClient> getNettyClientMap() {
        return nettyClientMap;
    }

    @Override
    public void destroy() {
        nettyServerMap.values().forEach(AbstractNettyServer::close);
        nettyClientMap.values().forEach(nettyClient -> ((BaseNettyClient) nettyClient).close());
    }
}
