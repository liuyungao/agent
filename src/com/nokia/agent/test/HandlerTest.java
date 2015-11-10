package com.nokia.agent.test;

import java.util.Map;

import com.nokia.agent.client.ClientHandler;
import com.nokia.agent.message.ResponseMessage;

public class HandlerTest extends ClientHandler {

	@Override
	public void handler(ResponseMessage respMessage) {
		super.handler(respMessage);
		Map<String, Object> map = respMessage.getSysInfoMap();
//		System.out.println(map.toString());
		System.out.println(respMessage.MEM_TOTAL+":"+respMessage.toMb(respMessage.MEM_TOTAL, map.get(respMessage.MEM_TOTAL))+"MB");
		System.out.println(respMessage.MEM_FREE+":"+respMessage.toMb(respMessage.MEM_FREE, map.get(respMessage.MEM_FREE))+"MB");
		System.out.println(respMessage.MEM_USED+":"+respMessage.toMb(respMessage.MEM_USED, map.get(respMessage.MEM_USED))+"MB");
	}
	
}
