package io.github.gaojindeng.netty.demo.server.converter;

import io.github.gaojindeng.netty.convert.RequestMessageConverter;
import org.springframework.context.annotation.Configuration;

/**
 * @author gjd
 */
@Configuration
public class DefaultRequestConverter implements RequestMessageConverter<String, String> {
    @Override
    public String converter(String o) {
        return o + "server_request";
    }
}
