package com.nokia.agent.client;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.nokia.agent.message.RequestMessage;

public class Client {

	private NioSocketConnector connector;
	private IoSession session;
	private String agentIp;
	private int agentPort;
	private IoHandler handler;

	public String getAgentIp() {
		return agentIp;
	}

	public void setAgentIp(String agentIp) {
		this.agentIp = agentIp;
	}

	public int getAgentPort() {
		return agentPort;
	}

	public void setAgentPort(int agentPort) {
		this.agentPort = agentPort;
	}

	public Client(String agentIp, int agentPort, IoHandler handler){
		this.agentIp = agentIp;
		this.agentPort = agentPort;
		this.handler = handler;
	}
	
	public void connect() {
		connector = new NioSocketConnector();
		
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		
		chain.addLast("myChain", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

		connector.setHandler(this.handler);

		connector.setConnectTimeoutMillis(30);

		ConnectFuture cf = connector.connect(new InetSocketAddress(this.agentIp, this.agentPort));
		cf.awaitUninterruptibly(3000L);

		session = cf.getSession();

		if (session != null) {
			this.session = session;
		} 
	}

	public void close() {
		if (session != null) {
			session.close(true);
			session = null;
		}

		if (connector != null) {
			connector.dispose();
		}
	}
	
	public void send(RequestMessage requestMessage) {
		if (session != null) {
			session.write(requestMessage);
		}
	}

}
