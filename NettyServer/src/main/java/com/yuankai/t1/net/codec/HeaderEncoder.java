package com.yuankai.t1.net.codec;

import com.yuankai.t1.net.Header;
import com.yuankai.t1.net.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class HeaderEncoder extends MessageToByteEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		// д��ͷ��Ϣ
		Header header = msg.getHeader();
		out.writeBytes(header.getSessionId().getBytes());
		int len = ((ByteBuf)msg.getData()).readableBytes();
		System.out.println("���͵����ݳ��ȣ�" + len);
		out.writeInt(len);
		out.writeBytes((ByteBuf)msg.getData());
		System.out.println("header�������");
	}

}
