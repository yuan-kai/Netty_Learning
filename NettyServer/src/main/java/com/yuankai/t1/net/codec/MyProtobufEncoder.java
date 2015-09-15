package com.yuankai.t1.net.codec;

import static io.netty.buffer.Unpooled.wrappedBuffer;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.MessageLite;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;

public class MyProtobufEncoder extends Encoder {

	@Override
	public Object transFormData(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf out = transToByteBuf(msg);
		System.out.println("msg 解析后的长度：" + out.readableBytes());
//		return transToByte(ctx, out);
		return out;
	}
	
	private ByteBuf transToByteBuf(Object msg) {
		if (msg instanceof MessageLite) {
            return wrappedBuffer(((MessageLite) msg).toByteArray());
        }
        if (msg instanceof MessageLite.Builder) {
            return wrappedBuffer(((MessageLite.Builder) msg).build().toByteArray());
        }
        return null;
	}
	
	private Object transToByte(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		System.out.println("msg 解析前的长度：" + msg.readableBytes());
		ByteBuf out = ctx.alloc().heapBuffer();
		int bodyLen = msg.readableBytes();
		int headerLen = CodedOutputStream.computeRawVarint32Size(bodyLen);
		out.ensureWritable(headerLen + bodyLen);
		
		CodedOutputStream headerOut =
		        CodedOutputStream.newInstance(new ByteBufOutputStream(out));
		headerOut.writeRawVarint32(bodyLen);
		headerOut.flush();
		
		out.writeBytes(msg, msg.readerIndex(), bodyLen);
		
		System.out.println("msg 解析后的长度：" + out.readableBytes());
		return out;
	}

}
