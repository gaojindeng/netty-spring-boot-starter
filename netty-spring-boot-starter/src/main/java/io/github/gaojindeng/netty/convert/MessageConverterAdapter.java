package io.github.gaojindeng.netty.convert;

/**
 * @author gjd
 */
public class MessageConverterAdapter implements RequestMessageConverter<Object, Object>, ResponseMessageConverter<Object, Object> {
    @Override
    public Object converter(Object o) {
        return o;
    }
}
