package com.yuankai.t1.net;

public class Header {
	private int length;
	private String sessionId;
	
	public Header() {
	}
	
	public Header(String sessionId, int length) {
		this.sessionId = sessionId;
		this.length = length;
	}
	
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	
}
