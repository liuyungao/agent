package com.nokia.agent.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.nokia.agent.message.ResponseMessage;

public class ClientHandler extends IoHandlerAdapter {
	
	
	@Override
	public void sessionOpened(IoSession session) throws Exception{
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
	}
	
	@Override
	public void exceptionCaught(IoSession arg0, Throwable cause)
		throws Exception {
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		this.handler((ResponseMessage)message);
	}
	
	public void handler(ResponseMessage respMessage){
		
	}
	
}
