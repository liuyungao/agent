package com.nokia.agent.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nokia.agent.onlinedata.Cache;
import com.nokia.agent.util.SigarManager;

/**
 * 系统信息收集任务类
 * agent启动后需要启动该任务类用于持续在后台收集服务器信息,并将信息写到本地磁盘
 * 
 * @author liuyungao
 * @date 2015-11-7
 *
 */
public class SystemCollectJob implements Job {
	private static final Logger logger = Logger.getLogger(SystemCollectJob.class);
	
	private SigarManager sigar = new SigarManager();
	private Map<String,Object> allSysInfoMap;
	private SimpleDateFormat sdf = null;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		try {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			allSysInfoMap = new HashMap<String, Object>();
			//记录系统时间
			allSysInfoMap.put("SYS_TIME", sdf.format(new Date()));
			
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
			
			Cache.historyQueue.put(allSysInfoMap);
		} catch (Exception e) {
			logger.error(e);
		}
	}

}
