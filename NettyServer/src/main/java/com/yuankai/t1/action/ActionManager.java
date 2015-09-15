package com.yuankai.t1.action;

import java.util.HashMap;
import java.util.Map;

public class ActionManager {

	private static Map<Integer, String> actionClassNames = new HashMap<Integer, String>();
	private static Map<Integer, Class<?>> actionClasses = new HashMap<Integer, Class<?>>();
	
	static {
		actionClassNames.put(100, "com.yuankai.t1.action.LoginAction");
	}
	
	private static Class<?> getAction(int id) throws ClassNotFoundException {
		Class<?> c = actionClasses.get(id);
		if(c == null) {
			String className = actionClassNames.get(id);
			c = Class.forName(className);
			actionClasses.put(id, c);
		}
		return c;
	}
	
	public static void doAction(ActionRequest req) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Action action = (Action) getAction(req.getId()).newInstance();
		action.excute(req);
	}
	
}
