package io.github.gaojindeng.netty.demo.server.controller;

import io.github.gaojindeng.netty.ConnectionManager;
import io.github.gaojindeng.netty.DefaultNettyContainer;
import io.github.gaojindeng.netty.server.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @author gjd
 */
@Component
public class ConnectionPrint {

    private static final Logger log = LoggerFactory.getLogger(ConnectionPrint.class);


    @Resource
    private DefaultNettyContainer defaultNettyContainer;

    @PostConstruct
    public void init() {
        new Thread(() -> {
            Map<String, NettyServer> nettyServerMap = defaultNettyContainer.getNettyServerMap();
            while (true) {
                nettyServerMap.forEach((key, value) -> {
                    ConnectionManager connectionManager = value.getConnectionManager();
                    log.info("{}-totalConnection: {}", key, connectionManager.getTotalConnection());
                });
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
