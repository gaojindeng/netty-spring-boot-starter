package io.github.gaojindeng.netty.demo.server;

import io.github.gaojindeng.netty.EnableNetty;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableNetty
public class NettyDemoServerApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(NettyDemoServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
