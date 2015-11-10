package com.nokia.agent.message;

import java.util.HashMap;
import java.util.Map;

/**
 * agent响应消息,agent向客户端返回的消息
 * 
 * @author liuyungao
 * @date 2015-11-7
 *
 */
public class ResponseMessage extends BasicMessage {

	private static final long serialVersionUID = 2044769077181016103L;
	
	/**
	 * 响应消息map,存放系统各个参数
	 */
	private Map<String, Object> sysInfoMap = new HashMap<String, Object>();
	
	public Map<String, Object> getSysInfoMap() {
		return sysInfoMap;
	}

}
