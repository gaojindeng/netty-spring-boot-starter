package io.github.gaojindeng.netty.demo.client.controller;

import io.github.gaojindeng.netty.annotation.NettyClient;
import io.github.gaojindeng.netty.client.NettyClientTemplate;
import io.github.gaojindeng.netty.demo.client.converter.DefaultRequestConverter;
import io.github.gaojindeng.netty.demo.client.converter.DefaultResponseConverter;
import io.github.gaojindeng.netty.demo.client.dto.MessageDemoRequestDTO;
import io.github.gaojindeng.netty.demo.client.dto.MessageDemoResponseDTO;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @NettyClient(reqConverter = DefaultRequestConverter.class, resConverter = DefaultResponseConverter.class)
    private NettyClientTemplate nettyClientTemplate;

    @NettyClient("client1")
    private NettyClientTemplate nettyClientTemplate1;

    @NettyClient("client2")
    private NettyClientTemplate nettyClientTemplate2;

    public TestController() {
    }

    @GetMapping("/test")
    public String send() {
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            new Thread(() -> {
                String format = String.format("%04d", finalI);
                String o = nettyClientTemplate.sendAndReceive(format);
                log.info("{} {}", format, o);
            }).start();
        }
        return "success";
    }

    @GetMapping("/test1")
    public Object test2(@RequestParam("value") String value) {
        MessageDemoResponseDTO o = nettyClientTemplate1.sendAndReceive(new MessageDemoRequestDTO(value));
        return o.toString();
    }

    @GetMapping("/test2")
    public Object test3(@RequestParam("value") String value) {

        // 构建HTTP请求
        String requestBody = value;
        HttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.POST, "/",
                Unpooled.copiedBuffer(requestBody, CharsetUtil.UTF_8));

        request.headers().set(HttpHeaderNames.HOST, "localhost");
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, ((DefaultFullHttpRequest) request).content().readableBytes());


        FullHttpResponse o = nettyClientTemplate2.sendAndReceive(request);
        return o.content().toString(CharsetUtil.UTF_8);
    }
}
