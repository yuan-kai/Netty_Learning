package com.yuankai.t1.net;

public class Message {
	private Header header;
	private Object data;
	
	public Message(Header header, Object data) {
		this.header = header;
		this.data = data;
	}
	
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
