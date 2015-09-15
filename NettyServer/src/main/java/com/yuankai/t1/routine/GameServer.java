package com.yuankai.t1.routine;

import com.google.protobuf.ExtensionRegistry;
import com.yuankai.t1.net.codec.HeaderDecoder;
import com.yuankai.t1.net.codec.HeaderEncoder;
import com.yuankai.t1.net.codec.MyProtobufDecoder;
import com.yuankai.t1.net.codec.MyProtobufEncoder;
import com.yuankai.t1.proto.Protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class GameServer {

	public static void main(String[] args) throws Exception {
		new GameServer().bind(10000);
	}
	
	public GameServer() {
	}
	
	public void bind(int port) throws InterruptedException {
		// 配置服务端的NIO线程组
		// 用于服务器端接受客户端连接
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		// 进行SocketChannel的读写。
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			// 服务器端配置 
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChildChannelHandler());
			// 绑定端口，同步等待成功
			
			ChannelFuture f = b.bind(port).sync();
			// 等待服务端监听端口关闭
			f.channel().closeFuture().sync();
		} finally {
			// 释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	// SocketChannel 是netty包中的类
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			final ExtensionRegistry registry = ExtensionRegistry.newInstance();
			ch.pipeline().addLast(new HeaderDecoder());
			ch.pipeline().addLast(new MyProtobufDecoder(
					Protocol.Request.getDefaultInstance(), registry));
			ch.pipeline().addLast(new HeaderEncoder());
			ch.pipeline().addLast(new MyProtobufEncoder());
			ch.pipeline().addLast(new ServerHandler());
		}
		
	}

}
