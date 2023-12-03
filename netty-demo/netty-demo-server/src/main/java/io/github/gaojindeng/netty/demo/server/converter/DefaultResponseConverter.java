package io.github.gaojindeng.netty.demo.server.converter;

import io.github.gaojindeng.netty.convert.ResponseMessageConverter;
import org.springframework.context.annotation.Configuration;

/**
 * @author gjd
 */
@Configuration
public class DefaultResponseConverter implements ResponseMessageConverter<String, String> {
    @Override
    public String converter(String o) {
        return o + "server_response";
    }
}
