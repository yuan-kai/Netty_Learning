package com.yuankai.t1.net.codec;

import java.util.List;

import com.yuankai.t1.net.Header;
import com.yuankai.t1.net.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class HeaderDecoder extends ByteToMessageDecoder {
	
	/**
	 * sessionID, length
	 */

	private static final int headerLength = 36;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(in.readableBytes() < headerLength) {
			return;
		}
		in.markReaderIndex();
		byte[] sessionIdByte = new byte[32];
		in.readBytes(sessionIdByte);
		int length = in.readInt();
		System.out.println("收到的长度：" + length);
		if(in.readableBytes() < length) {
			in.resetReaderIndex();
			return;
		}
		String sessionId = new String(sessionIdByte);
		Header header = new Header(sessionId, length);
		Message msg = new Message(header, in.readBytes(length));
		out.add(msg);
	}

}
