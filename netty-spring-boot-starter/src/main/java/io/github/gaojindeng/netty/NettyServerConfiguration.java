package io.github.gaojindeng.netty;

import io.github.gaojindeng.netty.annotation.NettyClient;
import io.github.gaojindeng.netty.annotation.NettyMessageListener;
import io.github.gaojindeng.netty.client.NettyClientTemplate;
import io.github.gaojindeng.netty.client.NettyClientTemplateImpl;
import io.github.gaojindeng.netty.convert.RequestMessageConverter;
import io.github.gaojindeng.netty.convert.ResponseMessageConverter;
import io.github.gaojindeng.netty.listener.NettyServerListener;
import io.github.gaojindeng.netty.listener.NettyServerReplyListener;
import io.github.gaojindeng.netty.server.NettyServer;
import io.github.gaojindeng.netty.convert.MessageConverterAdapter;
import io.github.gaojindeng.netty.properties.NettyConfigProperties;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gjd
 */
@Configuration
@EnableConfigurationProperties(NettyConfigProperties.class)
public class NettyServerConfiguration implements ApplicationContextAware, SmartInitializingSingleton, BeanPostProcessor {
    private ConfigurableApplicationContext applicationContext;


    @Bean
    public DefaultNettyContainer defaultNettyContainer(NettyConfigProperties nettyConfigProperties) {
        return new DefaultNettyContainer(nettyConfigProperties);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(NettyClient.class) && field.getType().equals(NettyClientTemplate.class)) {
                field.setAccessible(true);
                GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;
                DefaultNettyContainer defaultNettyContainer = genericApplicationContext.getBean(DefaultNettyContainer.class);
                NettyClient annotation = field.getAnnotation(NettyClient.class);
                try {
                    io.github.gaojindeng.netty.client.NettyClient nettyClient = defaultNettyContainer.getNettyClient(annotation.value());
                    Class<? extends RequestMessageConverter<?, ?>> reqConverterClass = annotation.reqConverter();
                    if (reqConverterClass != MessageConverterAdapter.class) {
                        RequestMessageConverter<?, ?> reqConverter = genericApplicationContext.getBean(reqConverterClass);
                        ((AbstractNetty) nettyClient).setRequestMessageConverter(reqConverter);
                    }

                    Class<? extends ResponseMessageConverter<?, ?>> resConverterClass = annotation.resConverter();
                    if (resConverterClass != MessageConverterAdapter.class) {
                        ResponseMessageConverter<?, ?> resConverter = genericApplicationContext.getBean(resConverterClass);
                        ((AbstractNetty) nettyClient).setResponseMessageConverter(resConverter);
                    }

                    field.set(bean, new NettyClientTemplateImpl(nettyClient));
                } catch (IllegalAccessException ignore) {
                }
            }
        }

        return bean;
    }

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(NettyMessageListener.class)
                .entrySet().stream().filter(entry -> !ScopedProxyUtils.isScopedTarget(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        beans.forEach(this::registerContainer);
    }

    private void registerContainer(String beanName, Object bean) {
        Class<?> clazz = AopProxyUtils.ultimateTargetClass(bean);

        if (NettyServerListener.class.isAssignableFrom(bean.getClass()) && NettyServerReplyListener.class.isAssignableFrom(bean.getClass())) {
            throw new IllegalStateException(clazz + " cannot be both instance of " + NettyServerListener.class.getName() + " and " + NettyServerReplyListener.class.getName());
        }

        if (!NettyServerListener.class.isAssignableFrom(bean.getClass()) && !NettyServerReplyListener.class.isAssignableFrom(bean.getClass())) {
            throw new IllegalStateException(clazz + " is not instance of " + NettyServerListener.class.getName() + " or " + NettyServerReplyListener.class.getName());
        }

        NettyMessageListener annotation = clazz.getAnnotation(NettyMessageListener.class);

        String value = annotation.value();

        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;
        DefaultNettyContainer defaultNettyContainer = genericApplicationContext.getBean(DefaultNettyContainer.class);
        NettyServer nettyServer = defaultNettyContainer.getNettyServer(value);
        if (nettyServer.isStarted()) {
            throw new IllegalStateException("{" + value + "} netty server repeat");
        }
        Class<? extends RequestMessageConverter<?, ?>> reqConverterClass = annotation.reqConverter();
        if (reqConverterClass != MessageConverterAdapter.class) {
            RequestMessageConverter<?, ?> reqConverter = genericApplicationContext.getBean(reqConverterClass);
            nettyServer.setRequestMessageConverter(reqConverter);
        }

        Class<? extends ResponseMessageConverter<?, ?>> resConverterClass = annotation.resConverter();
        if (resConverterClass != MessageConverterAdapter.class) {
            ResponseMessageConverter<?, ?> resConverter = genericApplicationContext.getBean(resConverterClass);
            nettyServer.setResponseMessageConverter(resConverter);
        }
        if (bean instanceof NettyServerListener) {
            nettyServer.setNettyServerListener((NettyServerListener) bean);
        } else if (bean instanceof NettyServerReplyListener) {
            nettyServer.setNettyServerReplyListener((NettyServerReplyListener) bean);
        }
        nettyServer.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }
}
