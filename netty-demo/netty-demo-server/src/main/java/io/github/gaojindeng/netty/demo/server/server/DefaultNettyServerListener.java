package io.github.gaojindeng.netty.demo.server.server;

import io.github.gaojindeng.netty.annotation.NettyMessageListener;
import io.github.gaojindeng.netty.demo.server.converter.DefaultRequestConverter;
import io.github.gaojindeng.netty.demo.server.converter.DefaultResponseConverter;
import io.github.gaojindeng.netty.listener.NettyServerReplyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author gjd
 */
@Component
@NettyMessageListener(reqConverter = DefaultRequestConverter.class, resConverter = DefaultResponseConverter.class)
public class DefaultNettyServerListener implements NettyServerReplyListener<String, String> {
    private static final Logger log = LoggerFactory.getLogger(DefaultNettyServerListener.class);

    @Override
    public String onMessage(String message) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("default-message: {}", message);
        return message;

    }
}
