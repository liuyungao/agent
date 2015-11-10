package com.nokia.agent.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * agent服务主类,当agent被部署到服务器上后,启动该类agent才能提供服务
 * 
 * @author liuyungao
 * @date 2015-11-07
 *
 */
public class Server {
	
	private SocketAcceptor _acceptor;
	private int _port;
	private IoHandler _handler;
	
	/**
	 * 构造函数
	 * @param port		监听的端口
	 * @param handler	处理客户端请求的服务类
	 * @throws IOException
	 */
	public Server(int port, IoHandler handler) throws IOException{
		this._port = port;
		this._handler = handler;
	}
	
	/**
	 * 启动agent
	 * @throws IOException
	 */
	public void start() throws IOException{
		_acceptor = new NioSocketAcceptor();
		DefaultIoFilterChainBuilder chain = _acceptor.getFilterChain();
		//自定义传输协议,支持序列化对象的传输
		chain.addLast("myChain", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		//设置监听类
		_acceptor.setHandler(this._handler);
		//绑定监听端口
		_acceptor.bind(new InetSocketAddress(this._port));
	}
	
	/**
	 * 停止agent
	 */
	public void stop(){
		_acceptor.unbind(new InetSocketAddress(this._port));
		_acceptor.dispose();
	}

}
