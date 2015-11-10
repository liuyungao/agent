package com.nokia.agent.message;

/**
 * agent请求消息,客户端向agent发送的消息
 * 
 * @author liuyungao
 * @date 2015-11-7
 *
 */
public class RequestMessage extends BasicMessage {
	
	private static final long serialVersionUID = -560473429291554645L;
	
	/**
	 * 请求内容
	 */
	private String[] requestContent;

	public String[] getRequestContent() {
		return requestContent;
	}

	public void setRequestContent(String[] requestContent) {
		this.requestContent = requestContent;
	}
	
}
