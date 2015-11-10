package com.nokia.agent.job;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.hyperic.sigar.SigarException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nokia.agent.message.RequestMessage;
import com.nokia.agent.message.ResponseMessage;
import com.nokia.agent.util.SigarManager;

/**
 * 实时信息任务调度类
 * 
 * @author liuyungao
 * @date 2015-11-7
 *
 */
public class RealTimeJob implements Job {
	private static final Logger logger = Logger.getLogger(RealTimeJob.class);
	
	private SigarManager sigar = new SigarManager();
	private ResponseMessage resp;
	private Map<String,Object> allSysInfoMap;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		IoSession session = (IoSession)context.getJobDetail().getJobDataMap().get("session");
		RequestMessage reqMsg = (RequestMessage)context.getJobDetail().getJobDataMap().get("request_content");
		
		resp = new ResponseMessage();
		allSysInfoMap = new HashMap<String, Object>();
		Map<String,Object> map = resp.getSysInfoMap();
		
		String[] reqContents = reqMsg.getRequestContent();
		if(reqContents==null || reqContents.length==0){
			return;
		}
		try {
			//获取操作系统静态参数
			allSysInfoMap.putAll(sigar.getOsStaticInfo());
			//获取CPU静态参数
			allSysInfoMap.putAll(sigar.getCpuStaticInfo());
			//获取CPU动态参数
			allSysInfoMap.putAll(sigar.getCpuDynamicInfo());
			//获取内存静态参数
			allSysInfoMap.putAll(sigar.getMemStaticInfo());
			//获取内存动态参数
			allSysInfoMap.putAll(sigar.getMemDynamicInfo());
			//获取磁盘信息
			allSysInfoMap.putAll(sigar.getDiskStaticInfo());
			
			//从获取的所有系统信息中过滤出客户端想要的
			for(String reqContent : reqContents){
				map.put(reqContent, allSysInfoMap.get(reqContent));
			}
			
		} catch (SigarException e) {
			logger.error(e);
		}
		
		//将响应消息返回给客户端
		session.write(resp);
	}

}
