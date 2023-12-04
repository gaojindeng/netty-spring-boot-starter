# netty与springBoot整合开箱即用框架

当我们面对特殊的数据传输需求，比如定长报文、银联的ISO8583报文，或者是公司内部设计的定制化协议，通常会选择使用netty来进行开发。
### 简介
netty-spring-boot-starter作为一个整合框架，充分利用了springBoot的自动装配特性，使得整合netty变得更为便捷。它实现了完全的配置化，大大降低了使用netty开发特定协议的难度。这样一来，开发人员可以轻松上手，无需深入了解netty内部复杂的组件和原理，直接达到开箱即用的目的。
这个框架的出现填补了使用netty开发特殊协议时的技术鸿沟，让开发者更专注于业务逻辑的实现，而无需过多关注底层网络通信的细节。它的配置化特性为项目的快速启动和部署提供了便利，让开发团队能够更专注于业务需求的实现，提高了整体开发效率。
开发人员只需要关注拆包粘包，报文转换即可。
### 使用说明
##### 添加依赖
```xml
<dependency>
  <groupId>io.github.gaojindeng</groupId>
  <artifactId>netty-spring-boot-starter</artifactId>
  <version>0.0.2-RELEASE</version>
</dependency>
```
##### 配置说明
```properties
############ server端配置 ##############
netty.server.port=8001            #端口-必填
netty.server.corePoolSize=5       #业务处理核心线程数-默认5
netty.server.maxPoolSize=10       #业务处理核心线程数-默认100
netty.server.maxConn=19           #最大连接数-默认1000
netty.server.ioThreads=5          #对应netty的worker线程数-默认0取netty默认值
#如果需要心跳，可以设置以下参数，对应netty的IdleStateHandler处理器的三个参数
netty.server.readerIdleSeconds=0
netty.server.writerIdleSeconds=0
netty.server.allIdleSeconds=0
#通道处理器，sharableHandlers和noSharableHandlers分开填，构造方法只能是无参、int、long、double
netty.server.sharableHandlers[0].className=io.netty.handler.codec.string.StringEncoder
netty.server.sharableHandlers[1].className=io.netty.handler.codec.string.StringDecoder
netty.server.noSharableHandlers[0].className=io.netty.handler.codec.FixedLengthFrameDecoder
netty.server.noSharableHandlers[0].params[0].className=int
netty.server.noSharableHandlers[0].params[0].value=4

############ client端配置 ##############
netty.client.port=8001
netty.client.host=127.0.0.1
netty.client.ioThreads=5
netty.client.keepConn=true        #是否保持连接-默认false，每次发送都建立新连接
netty.client.maxConn=10
netty.client.timeout=60000        #等待服务端返回消息超时时间
netty.client.readerIdleSeconds=0
netty.client.writerIdleSeconds=0
netty.client.allIdleSeconds=5
netty.server.sharableHandlers[0].className=io.netty.handler.codec.string.StringEncoder
netty.server.sharableHandlers[1].className=io.netty.handler.codec.string.StringDecoder

```
支持多个客户端服务端配置：
```properties
##server_1为该配置自定义的名称
netty.server.configs.server_1.port=8002
netty.server.configs.server_1.corePoolSize=5
#netty.server.configs.server_1....

##client_1为该配置自定义的名称
netty.client.configs.client_1.port=8001
netty.client.configs.client_1.host=127.0.0.1
#netty.server.configs.client_1....
```
### 代码示例
**示例1：发送接收定长字符串**
> **字符串+定长报文拆拆包粘包通道处理器**

服务端：
```yaml
netty:
  server:
    port: 8001
    sharableHandlers:
      - className: io.netty.handler.codec.string.StringEncoder
      - className: io.netty.handler.codec.string.StringDecoder
    noSharableHandlers:
      - className: io.netty.handler.codec.FixedLengthFrameDecoder
        params:
          - className: int
            value: 4
```
```java
/**
 * 带返回值实现NettyServerReplyListener接口
 * 不带返回值实现NettyServerListener接口
 * reqConverter：收到报文进行报文转换-可以为空
 * resConverter：返回报文转换后再发送出去-可以为空
 */
@Component
@NettyMessageListener(reqConverter = DefaultRequestConverter.class, resConverter = DefaultResponseConverter.class)
public class DefaultNettyServerListener implements NettyServerReplyListener<String, String> {

    @Override
    public String onMessage(String message) {
        return "success";
    }
}
```
客户端：
```yaml
netty:
  client:
    port: 8001
    host: 127.0.0.1
    sharableHandlers:
      - className: io.netty.handler.codec.string.StringEncoder
      - className: io.netty.handler.codec.string.StringDecoder
```
```java
//注入template对象
@NettyClient(reqConverter = DefaultRequestConverter.class, resConverter = DefaultResponseConverter.class)
private NettyClientTemplate nettyClientTemplate;

String request = "test";
String response = nettyClientTemplate.sendAndReceive(request);
```

**示例2：发送接收java对象**
> **java对象拆包粘包通道处理器**

**服**务端：
```yaml
netty:
  server:
    configs:
      server_1:
        port: 8002
        sharableHandlers:
          #该解码器有特殊类型构造参数，需要转成无参构造方法
          - className: io.github.gaojindeng.netty.demo.server.channel.MyObjectDecoder
          - className: io.netty.handler.codec.serialization.ObjectEncoder
```
```java
/**
 * 配置文件指定sharableHandlers和noSharableHandlers，构造方法只能时无参、int、long、double
 * 所以特殊参数的通道处理器必须重写后再添加到配置文件中
 * {@link AbstractNetty#paramToClass(String)}
 */
public class MyObjectDecoder extends ObjectDecoder {
    public MyObjectDecoder() {
        super(ClassResolvers.cacheDisabled(null));
    }
}


@NettyMessageListener("server_1")
@Component
public class NettyServerReplay1Listener implements NettyServerReplyListener<MessageDemoRequestDTO, MessageDemoResponseDTO> {
    private static final Logger log = LoggerFactory.getLogger(NettyServerReplay1Listener.class);

    @Override
    public MessageDemoResponseDTO onMessage(MessageDemoRequestDTO message) {
        log.info("server_1-message: {}", message);
        return new MessageDemoResponseDTO(message.getValue() + "replay");
    }
}
```
**客户端：**
```yaml
netty:
  client:
    configs:
      client1:
        port: 8002
        host: 127.0.0.1
        sharableHandlers:
          - className: io.netty.handler.codec.serialization.ObjectEncoder
        noSharableHandlers:
          - className: io.github.gaojindeng.netty.demo.client.channel.MyObjectDecoder
```
```java
//注入template对象
@NettyClient("client1")
private NettyClientTemplate nettyClientTemplate1;

MessageDemoResponseDTO response = nettyClientTemplate1.sendAndReceive(new MessageDemoRequestDTO(value));
```


**示例3：http请求**
> **http通道处理器**

**服**务端：
```yaml
netty:
  server:
    configs:
      server_2:
        port: 8003
        noSharableHandlers:
          - className: io.netty.handler.codec.http.HttpServerCodec
          - className: io.netty.handler.codec.http.HttpObjectAggregator
            params:
              - className: int
                value: 65536
```
```java
@NettyMessageListener("server_2")
@Component
public class NettyServerReplay2Listener implements NettyServerReplyListener<FullHttpRequest, DefaultFullHttpResponse> {
    private static final Logger log = LoggerFactory.getLogger(NettyServerReplay2Listener.class);

    @Override
    public DefaultFullHttpResponse onMessage(FullHttpRequest message) {
        log.info("server_2-message: {}", message.content().toString(CharsetUtil.UTF_8));
        // 构造 HTTP 响应
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.content().writeBytes("Hello, this is the server!".getBytes());

        // 设置响应头信息
        response.headers().set("Content-Type", "text/plain");
        response.headers().set("Content-Length", response.content().readableBytes());

        return response;
    }
```
**客户端：**
```yaml
netty:
  client:
    configs:
      client2:
        port: 8003
        host: 127.0.0.1
        noSharableHandlers:
          - className: io.netty.handler.codec.http.HttpClientCodec
          - className: io.netty.handler.codec.http.HttpObjectAggregator
            params:
              - className: int
                value: 65536
```
```java
//注入template对象
@NettyClient("client2")
private NettyClientTemplate nettyClientTemplate2;


// 构建HTTP请求
        String requestBody = "message";
        HttpRequest request = new DefaultFullHttpRequest(
        HttpVersion.HTTP_1_1, HttpMethod.POST, "/",
        Unpooled.copiedBuffer(requestBody, CharsetUtil.UTF_8));

        request.headers().set(HttpHeaderNames.HOST, "localhost");
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, ((DefaultFullHttpRequest) request).content().readableBytes());


        FullHttpResponse o = nettyClientTemplate2.sendAndReceive(request);
        return o.content().toString(CharsetUtil.UTF_8);
```

> 如果需要添加心跳功能，则需要添加自定义的心跳处理器，重写userEventTriggered方法，然后添加到配置文件中。

### 其他
重写了线程池的execute方法，当核心线程数小于最大线程时，直接使用拿最大线程来执行，当最大线程数都满了才会放到阻塞队列。

