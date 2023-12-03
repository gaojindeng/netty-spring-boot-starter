package io.github.gaojindeng.netty;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author gjd
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({NettyServerConfiguration.class})
public @interface EnableNetty {
}
