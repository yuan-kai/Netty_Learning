package com.yuankai.t1.net.codec;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.MessageLite;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.CorruptedFrameException;

public class MyProtobufDecoder extends Decoder {
	
	private static final boolean HAS_PARSER;

    static {
        boolean hasParser = false;
        try {
            // MessageLite.getParsetForType() is not available until protobuf 2.5.0.
            MessageLite.class.getDeclaredMethod("getParserForType");
            hasParser = true;
        } catch (Throwable t) {
            // Ignore
        }

        HAS_PARSER = hasParser;
    }

    private final MessageLite prototype;
    private final ExtensionRegistry extensionRegistry;

    public MyProtobufDecoder(MessageLite prototype, ExtensionRegistry extensionRegistry) {
        if (prototype == null) {
            throw new NullPointerException("prototype");
        }
        this.prototype = prototype.getDefaultInstanceForType();
        this.extensionRegistry = extensionRegistry;
    }

	@Override
	public Object transFromData(Object msg) throws Exception {
		checkMessage(msg);
//		ByteBuf in = transToByteBuf(msg);
//		return decode(in);
		Object obj = decode((ByteBuf) msg);
		return obj;
	}
	
	private Object decode(ByteBuf msg) throws Exception {
		final byte[] array;
        final int offset;
        final int length = msg.readableBytes();
        if (msg.hasArray()) {
            array = msg.array();
            offset = msg.arrayOffset() + msg.readerIndex();
        } else {
            array = new byte[length];
            msg.getBytes(msg.readerIndex(), array, 0, length);
            offset = 0;
        }

        if (extensionRegistry == null) {
            if (HAS_PARSER) {
                return prototype.getParserForType().parseFrom(array, offset, length);
            } else {
            	return prototype.newBuilderForType().mergeFrom(array, offset, length).build();
            }
        } else {
            if (HAS_PARSER) {
                return prototype.getParserForType().parseFrom(array, offset, length, extensionRegistry);
            } else {
                return prototype.newBuilderForType().mergeFrom(array, offset, length, extensionRegistry).build();
            }
        }
	}
	
	private void checkMessage(Object msg) throws Exception {
		if (!(msg instanceof ByteBuf)) {
			throw new Exception("½âÎö´íÎó");
		}
	}
	
	private ByteBuf transToByteBuf(Object msg) throws Exception {
		ByteBuf in = (ByteBuf) msg;
        final byte[] buf = new byte[5];
        for (int i = 0; i < buf.length; i ++) {
            buf[i] = in.readByte();
            if (buf[i] >= 0) {
                int length = CodedInputStream.newInstance(buf, 0, i + 1).readRawVarint32();
                if (length < 0) {
                    throw new CorruptedFrameException("negative length: " + length);
                }
                return in.readBytes(length);
            }
        }
        return null;
	}

}
