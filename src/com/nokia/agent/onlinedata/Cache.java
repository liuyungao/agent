package com.nokia.agent.onlinedata;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class Cache {
	
	public static LinkedBlockingQueue<Map<String,Object>> historyQueue = new LinkedBlockingQueue<Map<String,Object>>();

}
