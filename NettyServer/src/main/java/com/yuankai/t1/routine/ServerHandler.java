package com.yuankai.t1.routine;

import com.yuankai.t1.action.ActionManager;
import com.yuankai.t1.action.ActionRequest;
import com.yuankai.t1.net.Message;
import com.yuankai.t1.proto.Protocol.Request;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandler extends ChannelHandlerAdapter {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		System.out.println("a connection");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof Message) {
			System.out.println("message object");
		}
		Message message = (Message) msg;
		Object obj = message.getData();
		if(obj instanceof Request) {
			System.out.println("request object");
		}
		Request req = (Request) obj;
		ActionRequest actionRequest = new ActionRequest(req);
		ActionManager.doAction(actionRequest);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelReadComplete(ctx);
	}

}
