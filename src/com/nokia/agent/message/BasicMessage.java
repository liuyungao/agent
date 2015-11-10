package com.nokia.agent.message;

import java.io.Serializable;

/**
 * agent消息基类,所有消息都必须继承该类
 * 
 * @author liuyungao
 * @date 2015-11-07
 *
 */
public abstract class BasicMessage implements Serializable {
	private static final long serialVersionUID = -5288464028403173066L;
	
	//command
	public static final int REQ_REALTIME_DATA_START_COMMAND = 0;
	public static final int REQ_REALTIME_DATE_MODIFY_COMMAND = 1;
	public static final int REQ_REALTIME_DATA_STOP_COMMAND = 2;
			
	public static final int REQ_HISTORY_DATA_COMMAND = 3;
	
	
	/**
	 * 系统时间
	 */
	public final String SYS_TIME = "SYS_TIME";
	//sysInfoMap keys
	/**
	 * 操作系统内核
	 */
	public final String OS_ARCH = "OS_ARCH";
	/**
	 * 操作系统厂商
	 */
	public final String OS_VENDOR = "OS_VENDOR";
	/**
	 * 操作系统名称
	 */
	public final String OS_NAME = "OS_NAME";
	/**
	 * 操作系统版本号
	 */
	public final String OS_VERSION = "OS_VERSION";
	
	/**
	 * CPU核数
	 */
	public final String CPU_CORE_NUM = "CPU_CORE_NUM";
	/**
	 * CPU频率
	 */
	public final String CPU_MHZ = "CPU_MHZ";
	/**
	 * CPU厂商
	 */
	public final String CPU_VENDOR = "CPU_VENDOR";
	/**
	 * CPU缓存大小
	 */
	public final String CPU_CACHE_SIZE = "CPU_CACHE_SIZE";
	/**
	 * CPU用户使用率
	 */
	public final String CPU_USER_USED= "CPU_USER_USED";
	/**
	 * CPU系统使用率
	 */
	public final String CPU_SYS_USED= "CPU_SYS_USED";
	/**
	 * CPU总空闲率
	 */
	public final String CPU_TOTAL_IDLE = "CPU_TOTAL_IDLE";
	/**
	 * CPU总使用率
	 */
	public final String CPU_TOTAL_USED = "CPU_TOTAL_USED";
	
	/**
	 * 内存总量
	 */
	public final String MEM_TOTAL = "MEM_TOTAL";
	/**
	 * 内存使用量
	 */
	public final String MEM_USED = "MEM_USED";
	/**
	 * 内存使用百分比
	 */
	public final String MEM_USED_PER = "MEM_USED_PER";
	/**
	 * 内存剩余量
	 */
	public final String MEM_FREE = "MEM_FREE";
	/**
	 * 内存剩余百分比
	 */
	public final String MEM_FREE_PER = "MEM_FREE_PER";
	
	/**
	 * 磁盘总大小
	 */
	public final String DISK_TOTAL = "DISK_TOTAL";
	/**
	 * 磁盘剩余大小
	 */
	public final String DISK_FREE = "DISK_FREE";
	/**
	 * 磁盘可用大小
	 */
	public final String DISK_AVAIL = "DISK_AVAIL";
	/**
	 * 磁盘已用大小
	 */
	public final String DISK_USED = "DISK_USED";
	/**
	 * 磁盘已用百分比
	 */
	public final String DISK_USED_PER = "DISK_USED_PER";
	
	
	private int command;
	private int inteval;
	
	public int getCommand() {
		return command;
	}
	public void setCommand(int command) {
		this.command = command;
	}
	public String getInteval() {
		//设置执行Quartz的有效时间范围
		if(this.inteval<=0 || this.inteval >= 30){
			return "0/1 * * * * ?";
		}
		return "0/"+this.inteval+" * * * * ?";
	}
	public void setInteval(int inteval) {
		this.inteval = inteval;
	}
	
	
	//util method
	public String toMb(String key, Object value){
		if(value==null){
			return null;
		}
		if(this.MEM_TOTAL.equalsIgnoreCase(key)){
			long memTotal = Long.parseLong(String.valueOf(value));
			memTotal = memTotal/1024/1024;
			return String.valueOf(memTotal);
		}
		if(this.MEM_FREE.equalsIgnoreCase(key)){
			long memFree = Long.parseLong(String.valueOf(value));
			memFree = memFree/1024/1024;
			return String.valueOf(memFree);
		}
		if(this.MEM_USED.equalsIgnoreCase(key)){
			long memUsed = Long.parseLong(String.valueOf(value));
			memUsed = memUsed/1024/1024;
			return String.valueOf(memUsed);
		}
		return null;
	}
	
	public String toGb(String key, Object value){
		if(value==null){
			return null;
		}
		if(this.MEM_TOTAL.equalsIgnoreCase(key)){
			long memTotal = Long.parseLong(String.valueOf(value));
			memTotal = memTotal/1024/1024/1024;
			return String.valueOf(memTotal);
		}
		if(this.MEM_FREE.equalsIgnoreCase(key)){
			long memFree = Long.parseLong(String.valueOf(value));
			memFree = memFree/1024/1024/1024;
			return String.valueOf(memFree);
		}
		if(this.MEM_USED.equalsIgnoreCase(key)){
			long memUsed = Long.parseLong(String.valueOf(value));
			memUsed = memUsed/1024/1024/1024;
			return String.valueOf(memUsed);
		}
		return null;
	}

}
