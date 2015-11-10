package com.nokia.agent.server;

/**
 * agent服务启动所需的配置
 * 
 * @author liuyungao
 * @date 2015-11-7
 *
 */
public class ServerConfig {
	/**
	 * agent配置文件
	 */
	public final static String CONFIG_FILE = "conf.properties";
	public final static String PORT_KEY = "port";
	public final static String RECORD_HISTORY_KEY = "record_history";
	
	
	
	/**
	 * agent服务端口
	 */
	public static int PORT_VALUE=2345;
	/**
	 * 是否记录历史数据
	 */
	public static boolean RECORD_HISTORY = true;

	
}
