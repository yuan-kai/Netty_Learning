package com.yuankai.t1.action;

import java.util.Map;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.yuankai.t1.proto.Protocol.Request;

public class LoginAction implements Action {

	public void excute(ActionRequest req) {
		Request request = req.getRequest();
		Map<FieldDescriptor, Object>  fields = request.getAllFields();
		System.out.println("fields size:" + fields.size());
		for(Object value : fields.values()) {
			System.out.println(value);
//			if (value instanceof Login) {
//				Login info = (Login) value;
//				System.out.println(info.getPswd());
//				System.out.println(info.getUser());
//			}
		}
		System.out.println(request);
	}
	
}
