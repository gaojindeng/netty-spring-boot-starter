package io.github.gaojindeng.netty.demo.client;

import io.github.gaojindeng.netty.EnableNetty;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableNetty
public class NettyDemoClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NettyDemoClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
