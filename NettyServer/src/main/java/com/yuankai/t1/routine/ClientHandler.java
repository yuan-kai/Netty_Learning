package com.yuankai.t1.routine;

import com.yuankai.t1.net.Header;
import com.yuankai.t1.net.Message;
import com.yuankai.t1.proto.Protocol;
import com.yuankai.t1.proto.Protocol.Login;
import com.yuankai.t1.proto.Protocol.Request;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ClientHandler extends ChannelHandlerAdapter {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		System.out.println(cause);
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("start send msg");
		Login login = Login.newBuilder().setUser("ksfzhaohui")
				.setPswd("11111111").build();
		Request.Builder builder = Request.newBuilder();
		builder.setId(100);
		builder.setExtension(Protocol.login, login);
		Request request = builder.build();
		Header header = new Header();
		header.setSessionId("11111111111111111111111111111111");
		Message message = new Message(header, request);
		ctx.writeAndFlush(message);
		System.out.println("sent msg");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		System.out.println("¶ÁÈ¡Êý¾Ý");
	}
	
	
	public ClientHandler() {
	}
	
}
