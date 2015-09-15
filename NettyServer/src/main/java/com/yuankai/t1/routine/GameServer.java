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
		// ���÷���˵�NIO�߳���
		// ���ڷ������˽��ܿͻ�������
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		// ����SocketChannel�Ķ�д��
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			// ������������ 
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChildChannelHandler());
			// �󶨶˿ڣ�ͬ���ȴ��ɹ�
			
			ChannelFuture f = b.bind(port).sync();
			// �ȴ�����˼����˿ڹر�
			f.channel().closeFuture().sync();
		} finally {
			// �ͷ��̳߳���Դ
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	// SocketChannel ��netty���е���
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
