package com.ktc.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import com.ktc.actor.DocumentSystem;

public class WebSocketServer {
    private final int port;
    private final DocumentSystem documentSystem;
    
    public WebSocketServer(int port, DocumentSystem documentSystem) {
        this.port = port;
        this.documentSystem = documentSystem;
    }
    
    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            
                            // HTTP codec for WebSocket handshake
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            
                            // WebSocket compression (optional)
                            pipeline.addLast(new WebSocketServerCompressionHandler());
                            
                            // WebSocket protocol handler
                            pipeline.addLast(new WebSocketServerProtocolHandler("/websocket", null, true));
                            
                            // Your custom handler
                            pipeline.addLast(new TripleSocket(documentSystem));
                        }
                    });
            
            Channel channel = bootstrap.bind(port).sync().channel();
            System.out.println("WebSocket server started on ws://localhost:" + port + "/websocket");
            
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
} 