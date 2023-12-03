package io.github.gaojindeng.netty.demo.server.server;

import io.github.gaojindeng.netty.annotation.NettyMessageListener;
import io.github.gaojindeng.netty.demo.client.dto.MessageDemoRequestDTO;
import io.github.gaojindeng.netty.demo.client.dto.MessageDemoResponseDTO;
import io.github.gaojindeng.netty.listener.NettyServerReplyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author gjd
 */
@NettyMessageListener("server_1")
@Component
public class NettyServerReplay1Listener implements NettyServerReplyListener<MessageDemoRequestDTO, MessageDemoResponseDTO> {
    private static final Logger log = LoggerFactory.getLogger(NettyServerReplay1Listener.class);

    @Override
    public MessageDemoResponseDTO onMessage(MessageDemoRequestDTO message) {
        log.info("server_1-message: {}", message);

        return new MessageDemoResponseDTO(message.getValue() + "replay");
    }
}
