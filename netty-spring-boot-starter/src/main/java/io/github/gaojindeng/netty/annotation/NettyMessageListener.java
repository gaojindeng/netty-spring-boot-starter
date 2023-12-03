package io.github.gaojindeng.netty.annotation;

import io.github.gaojindeng.netty.convert.MessageConverterAdapter;
import io.github.gaojindeng.netty.convert.RequestMessageConverter;
import io.github.gaojindeng.netty.convert.ResponseMessageConverter;

import java.lang.annotation.*;

/**
 * @author gjd
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NettyMessageListener {

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
