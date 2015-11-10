package com.nokia.agent.test;

import com.nokia.agent.client.Client;
import com.nokia.agent.message.RequestMessage;

public class ClientTest {

	public static void main(String[] args) throws InterruptedException {
		String agentIp = "127.0.0.1";
		int agentPort = 2345;
		Client client = new Client(agentIp, agentPort, new HandlerTest());
		client.connect();
		
		RequestMessage req = new RequestMessage();
		req.setCommand(RequestMessage.REQ_REALTIME_DATA_START_COMMAND);
		req.setRequestContent(new String[]{req.CPU_TOTAL_USED,req.CPU_TOTAL_IDLE,req.MEM_FREE,req.MEM_FREE_PER,req.MEM_TOTAL,req.MEM_USED,req.MEM_USED_PER});
		req.setInteval(5);
		client.send(req);
		
		Thread.sleep(20*1000);
		req.setCommand(RequestMessage.REQ_REALTIME_DATE_MODIFY_COMMAND);
		req.setRequestContent(new String[]{req.CPU_TOTAL_USED,req.CPU_TOTAL_IDLE,req.MEM_FREE,req.MEM_FREE_PER,req.MEM_TOTAL,req.MEM_USED,req.MEM_USED_PER});
		req.setInteval(1);
		client.send(req);
		
		Thread.sleep(20*1000);
		req.setCommand(RequestMessage.REQ_REALTIME_DATA_STOP_COMMAND);
		client.send(req);

	}

}
