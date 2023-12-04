package io.github.gaojindeng.netty.demo.server.server;

import io.github.gaojindeng.netty.annotation.NettyMessageListener;
import io.github.gaojindeng.netty.listener.NettyServerReplyListener;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author gjd
 */
@NettyMessageListener("server_2")
@Component
public class NettyServerReplay2Listener implements NettyServerReplyListener<FullHttpRequest, DefaultFullHttpResponse> {
    private static final Logger log = LoggerFactory.getLogger(NettyServerReplay2Listener.class);

    @Override
    public DefaultFullHttpResponse onMessage(FullHttpRequest message) {
        log.info("server_2-message: {}", message.content().toString(CharsetUtil.UTF_8));
        // 构造 HTTP 响应
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.content().writeBytes("Hello, this is the server!".getBytes());

        // 设置响应头信息
        response.headers().set("Content-Type", "text/plain");
        response.headers().set("Content-Length", response.content().readableBytes());

        return response;
    }
}
