package io.github.gaojindeng.netty;

import io.github.gaojindeng.netty.channel.BaseChannelHandlerManager;
import io.github.gaojindeng.netty.convert.RequestMessageConverter;
import io.github.gaojindeng.netty.convert.ResponseMessageConverter;
import io.github.gaojindeng.netty.properties.AbstractProperties;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @author gjd
 */
public abstract class AbstractNetty {
    private static final Logger log = LoggerFactory.getLogger(AbstractNetty.class);

    private String name = "default";

    protected int port;

    protected BaseChannelHandlerManager channelInitializer;

    protected AbstractProperties nettyProperties;

    private RequestMessageConverter requestMessageConverter;

    private ResponseMessageConverter responseMessageConverter;

    private ConnectionManager connectionManager;

    public AbstractNetty(AbstractProperties nettyProperties) {
        if (nettyProperties == null) {
            throw new IllegalArgumentException("nettyProperties is null");
        }
        this.nettyProperties = nettyProperties;
    }

    protected void init() {
        try {
            List<AbstractProperties.LoadClass> noSharableHandlers = nettyProperties.getNoSharableHandlers();
            for (AbstractProperties.LoadClass noSharableHandler : noSharableHandlers) {
                ConstructorObject constructor = buildConstructor(noSharableHandler);
                channelInitializer.add(() -> {
                    try {
                        return (ChannelHandler) constructor.newInstance();
                    } catch (Exception e) {
                        throw new IllegalArgumentException(e);
                    }
                });
            }

            List<AbstractProperties.LoadClass> sharableHandlers = nettyProperties.getSharableHandlers();
            for (AbstractProperties.LoadClass sharableHandler : sharableHandlers) {
                ConstructorObject constructor = buildConstructor(sharableHandler);
                channelInitializer.add((ChannelHandler) constructor.newInstance());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param loadClass
     * @return
     * @throws Exception
     */
    protected ConstructorObject buildConstructor(AbstractProperties.LoadClass loadClass) throws NoSuchMethodException, ClassNotFoundException {
        String className = loadClass.getClassName();
        Class<?> aClass = Class.forName(className);
        List<AbstractProperties.ParamClass> params = loadClass.getParams();
        if (CollectionUtils.isEmpty(params)) {
            return new ConstructorObject(aClass.getConstructor(), null);
        }

        Class<?>[] paramClasses = new Class[params.size()];
        Object[] paramValues = new Object[params.size()];
        if (!CollectionUtils.isEmpty(params)) {
            for (int i = 0; i < params.size(); i++) {
                AbstractProperties.ParamClass paramClass = params.get(i);
                paramClasses[i] = paramToClass(paramClass.getClassName());
                paramValues[i] = paramClass.getValue();
            }
        }
        return new ConstructorObject(aClass.getConstructor(paramClasses), paramValues);
    }

    public Class<?> paramToClass(String value) throws ClassNotFoundException {
        if ("int".equals(value)) {
            return int.class;
        } else if ("long".equals(value)) {
            return long.class;
        } else if ("double".equals(value)) {
            return double.class;
        }
        return Class.forName(value);
    }

    protected Object converterReq(Object request) {
        if (requestMessageConverter != null) {
            return requestMessageConverter.converter(request);
        }
        return request;
    }

    protected Object converterRes(Object response) {
        if (responseMessageConverter != null) {
            return responseMessageConverter.converter(response);
        }
        return response;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RequestMessageConverter getRequestMessageConverter() {
        return requestMessageConverter;
    }

    public void setRequestMessageConverter(RequestMessageConverter requestMessageConverter) {
        this.requestMessageConverter = requestMessageConverter;
    }

    public ResponseMessageConverter getResponseMessageConverter() {
        return responseMessageConverter;
    }

    public void setResponseMessageConverter(ResponseMessageConverter responseMessageConverter) {
        this.responseMessageConverter = responseMessageConverter;
    }

    public void setChannelInitializer(BaseChannelHandlerManager channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    protected static class ConstructorObject {
        private Constructor constructor;
        private Object[] paramValues;

        public ConstructorObject(Constructor constructor, Object[] paramValues) {
            this.constructor = constructor;
            this.paramValues = paramValues;
        }

        public Object newInstance() throws Exception {
            if (paramValues != null) {
                return constructor.newInstance(paramValues);
            } else {
                return constructor.newInstance();
            }
        }
    }

    public abstract void close();
}
