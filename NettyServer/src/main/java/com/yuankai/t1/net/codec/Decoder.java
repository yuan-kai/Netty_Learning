package com.yuankai.t1.net.codec;

import java.util.List;

import com.yuankai.t1.net.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public abstract class Decoder extends MessageToMessageDecoder<Message> {

	@Override
	protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
		Object object = transFromData(msg.getData());
		msg.setData(object);
		out.add(msg);
	}
	
	public abstract Object transFromData(Object msg) throws Exception;

}
