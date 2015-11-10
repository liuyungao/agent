package com.nokia.agent.boot;

import java.util.Map;

import org.apache.log4j.Logger;

import com.nokia.agent.job.QuartzManager;
import com.nokia.agent.job.SystemCollectJob;
import com.nokia.agent.job.SystemOutputJob;
import com.nokia.agent.server.Server;
import com.nokia.agent.server.ServerConfig;
import com.nokia.agent.server.ServerHandler;
import com.nokia.agent.util.PropertiesUtil;

/**
 * agent启动类
 * 
 * @author liuyungao
 * @date 2015-11-7
 *
 */
public final class Startup {
	private static final Logger logger = Logger.getLogger(Startup.class);
	
	/**
	 * 启动主函数
	 * @param args
	 */
	public static void main(String[] args) {
		Startup startup = new Startup();
		startup.init();
		startup.start();
	}
	
	/**
	 * agent初始化
	 */
	private void init(){
		try {
			Map<String,String> config = PropertiesUtil.getProperties(ServerConfig.CONFIG_FILE);
			ServerConfig.PORT_VALUE = Integer.parseInt(config.get(ServerConfig.PORT_KEY));
			ServerConfig.RECORD_HISTORY = Boolean.valueOf(config.get(ServerConfig.RECORD_HISTORY_KEY));
			
		} catch (Exception e) {
			logger.error(e);
			System.exit(1);
		}
	}
	
	/**
	 * 启动agent
	 */
	private void start(){
		try {
			//启动socket服务
			Server server = new Server(ServerConfig.PORT_VALUE, new ServerHandler());
			server.start();
			
			//启动系统历史数据输出线程
			if(ServerConfig.RECORD_HISTORY){
				Thread sysOutputThread = new Thread(new SystemOutputJob());
				sysOutputThread.start();
			}
			
			//启动系统历史数据收集任务
			QuartzManager.addJob("systemCollectJob", "systemCollectJob-group", "systemCollectTrigger", "systemCollectTrigger-group", new SystemCollectJob().getClass(), "0/1 * * * * ?", null, null);
			
			logger.info("agent is started!");
			
		} catch (Exception e) {
			logger.error(e);
			System.exit(1);
		}
	}

}
