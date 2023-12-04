package io.github.gaojindeng.netty.demo.client.channel;

import io.github.gaojindeng.netty.AbstractNetty;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;

/**
 * 配置文件指定sharableHandlers和noSharableHandlers，构造方法只能时无参、int、long、double
 * 所以特殊参数的通道处理器必须重写后再添加到配置文件中
 * {@link AbstractNetty#paramToClass(String)}
 *
 * @author gjd
 */
public class MyObjectDecoder extends ObjectDecoder {
    public MyObjectDecoder() {
        super(ClassResolvers.cacheDisabled(null));
    }
}
