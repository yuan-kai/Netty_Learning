package com.yuankai.t1.action;

import com.yuankai.t1.proto.Protocol.Request;

public class ActionRequest {
	
	private Request request;

	public ActionRequest(Request request) {
		this.request = request;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public int getId() {
		return request.getId();
	}

}
