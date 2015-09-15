package com.yuankai.t1.net.codec;

import java.util.List;

import com.yuankai.t1.net.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public abstract class Encoder extends MessageToMessageEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
		Object data = transFormData(ctx, msg.getData());
		msg.setData(data);
		out.add(msg);
	}
	
	public abstract Object transFormData(ChannelHandlerContext ctx, Object msg) throws Exception;

}
