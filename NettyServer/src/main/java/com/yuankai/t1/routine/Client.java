package com.yuankai.t1.routine;

import com.google.protobuf.ExtensionRegistry;
import com.yuankai.t1.net.codec.HeaderDecoder;
import com.yuankai.t1.net.codec.HeaderEncoder;
import com.yuankai.t1.net.codec.MyProtobufDecoder;
import com.yuankai.t1.net.codec.MyProtobufEncoder;
import com.yuankai.t1.proto.Protocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

	public static void main(String[] args) throws Exception {
		new Client().connect(10000, "127.0.0.1");
	}
	
	public void connect(int port, String host) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try  {
			Bootstrap b = new Bootstrap();
			final ExtensionRegistry registry = ExtensionRegistry.newInstance();
			b.group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					
					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						ch.pipeline().addLast(new HeaderDecoder());
						ch.pipeline().addLast(new MyProtobufDecoder(
								Protocol.Request.getDefaultInstance(), registry));
						ch.pipeline().addLast(new HeaderEncoder());
						ch.pipeline().addLast(new MyProtobufEncoder());
						ch.pipeline().addLast(new ClientHandler());
					}
					
				});
			// 发起异步连接请求
			ChannelFuture f = b.connect(host, port).sync();
			// 等待客户端链路关闭
			f.channel().closeFuture().sync();
		} finally {
			// 释放Nio线程组
			group.shutdownGracefully();
		}
	}

}
