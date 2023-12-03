package io.github.gaojindeng.netty.annotation;

import io.github.gaojindeng.netty.convert.RequestMessageConverter;
import io.github.gaojindeng.netty.convert.ResponseMessageConverter;
import io.github.gaojindeng.netty.convert.MessageConverterAdapter;

import java.lang.annotation.*;

/**
 * @author gjd
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NettyClient {

    String value() default "default";

    /**
     * 请求消息转换器
     *
     * @return
     */
    Class<? extends RequestMessageConverter<?, ?>> reqConverter() default MessageConverterAdapter.class;

    /**
     * 响应消息转换器
     *
     * @return
     */
    Class<? extends ResponseMessageConverter<?, ?>> resConverter() default MessageConverterAdapter.class;
}
