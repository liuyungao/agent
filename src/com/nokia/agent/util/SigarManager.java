package com.nokia.agent.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.ProcUtil;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import com.nokia.agent.message.BasicMessage;
import com.nokia.agent.message.ResponseMessage;

public class SigarManager {
	private static BasicMessage msg = new ResponseMessage();
	private static Sigar sigar = new Sigar();
	private static OperatingSystem os = OperatingSystem.getInstance();
	
	public Map<String,Object> getCpuStaticInfo() throws SigarException{
		Map<String,Object> map = new HashMap<String,Object>();
		CpuInfo[] cpuInfo = sigar.getCpuInfoList();
		map.put(msg.CPU_CORE_NUM, cpuInfo.length);
		map.put(msg.CPU_MHZ, cpuInfo[0].getMhz());
		map.put(msg.CPU_VENDOR, cpuInfo[0].getVendor());
		map.put(msg.CPU_CACHE_SIZE, cpuInfo[0].getCacheSize());
//		System.out.println("CPU核数:" + cpuInfo.length + "核");
//		System.out.println("CPU频率:" + cpuInfo[0].getMhz() + "Mhz");
//		System.out.println("CPU厂商:" + cpuInfo[0].getVendor());
//		System.out.println("CPU缓存大小:" + cpuInfo[0].getCacheSize() + "M");
		return map;
	}
	
	public Map<String,Object> getCpuDynamicInfo() throws SigarException{
		Map<String,Object> map = new HashMap<String, Object>();
		CpuPerc cpuPerc = sigar.getCpuPerc();
		map.put(msg.CPU_USER_USED, cpuPerc.format(cpuPerc.getUser()));
		map.put(msg.CPU_SYS_USED, cpuPerc.format(cpuPerc.getSys()));
		map.put(msg.CPU_TOTAL_IDLE, cpuPerc.format(cpuPerc.getIdle()));
		map.put(msg.CPU_TOTAL_USED, cpuPerc.format(cpuPerc.getCombined()));
		
//		System.out.println("CPU用户使用率:" + cpuPerc.format(cpuPerc.getUser()));
//		System.out.println("CPU系统使用率:" + cpuPerc.format(cpuPerc.getSys()));
		//System.out.println("CPU当前等待率:" + cpuPerc.format(cpuPerc.getWait()));
		//System.out.println("CPU当前错误率:" + cpuPerc.format(cpuPerc.getNice()));
//		System.out.println("CPU当前空闲率:" + cpuPerc.format(cpuPerc.getIdle()));
//		System.out.println("CPU总的使用率:" + cpuPerc.format(cpuPerc.getCombined()));
		return map;
	}
	
	public Map<String,Object> getMemStaticInfo() throws SigarException{
		Map<String,Object> map = new HashMap<String,Object>();
		Mem mem = sigar.getMem();
		map.put(msg.MEM_TOTAL, mem.getTotal());
//		System.out.println("内存总量:" + mem.getTotal()/1024L/1024L + "M");//GB
		
//		Swap swap = sigar.getSwap();
//		System.out.println("交换区总量:" + swap.getTotal()/1024/1024 + "M");//GB
		
		return map;
	}
	
	public Map<String,Object> getMemDynamicInfo() throws SigarException{
		Map<String,Object> map = new HashMap<String,Object>();
		Mem mem = sigar.getMem();
		map.put(msg.MEM_USED, mem.getUsed());
		map.put(msg.MEM_USED_PER, mem.getUsedPercent());
		map.put(msg.MEM_FREE, mem.getFree());
		map.put(msg.MEM_FREE_PER, mem.getFreePercent());
//		System.out.println("内存使用量:" + mem.getUsed()/1024L/1024L + "M used");
//		System.out.println("内存使用百分比:" + mem.getUsedPercent() + "%");
//		System.out.println("内存剩余量:" + mem.getFree()/1024L/1024L + "M free");
//		System.out.println("内存剩余百分比:" + mem.getFreePercent() + "%");
		
//		Swap swap = sigar.getSwap();
//		System.out.println("交换区使用量:" + swap.getUsed());
//		System.out.println("交换区剩余量:" + swap.getFree());
		
		return map;
	}
	
	public Map<String,Object> getDiskStaticInfo() throws SigarException{
		Map<String,Object> map = new HashMap<String, Object>();
		FileSystem fslist[] = sigar.getFileSystemList();
        for (int i = 0; i < fslist.length; i++) {
//            System.out.println("第" + (i+1) + "个分区:");
            FileSystem fs = fslist[i];
            // 分区的盘符名称
//            System.out.println("盘符名称:    " + fs.getDevName());
            // 分区的盘符名称
//            System.out.println("盘符路径:    " + fs.getDirName());
//            System.out.println("盘符标志:    " + fs.getFlags());//
            // 文件系统类型，比如 FAT32、NTFS
//            System.out.println("盘符类型:    " + fs.getSysTypeName());
            // 文件系统类型名，比如本地硬盘、光驱、网络文件系统等
//            System.out.println("盘符类型名:    " + fs.getTypeName());
            // 文件系统类型
//            System.out.println("盘符文件系统类型:    " + fs.getType());
            FileSystemUsage usage = null;
            usage = sigar.getFileSystemUsage(fs.getDirName());
            switch (fs.getType()) {
            case 0: // TYPE_UNKNOWN ：未知
                break;
            case 1: // TYPE_NONE
                break;
            case 2: // TYPE_LOCAL_DISK : 本地硬盘
                // 文件系统总大小
            	map.put(msg.DISK_TOTAL, usage.getTotal());
//                System.out.println(fs.getDevName() + "总大小:    " + usage.getTotal()/1024L + "M");
                // 文件系统剩余大小
                map.put(msg.DISK_FREE, usage.getFree());
//                System.out.println(fs.getDevName() + "剩余大小:    " + usage.getFree()/1024L + "M");
                // 文件系统可用大小
                map.put(msg.DISK_AVAIL, usage.getAvail());
//                System.out.println(fs.getDevName() + "可用大小:    " + usage.getAvail()/1024L + "M");
                // 文件系统已经使用量
                map.put(msg.DISK_USED, usage.getUsed());
//                System.out.println(fs.getDevName() + "已经使用量:    " + usage.getUsed()/1024L + "M");
                double usePercent = usage.getUsePercent() * 100D;
                // 文件系统资源的使用率
                map.put(msg.DISK_USED_PER, usePercent);
//                System.out.println(fs.getDevName() + "资源的使用率:    " + usePercent + "%");
                break;
            case 3:// TYPE_NETWORK ：网络
                break;
            case 4:// TYPE_RAM_DISK ：闪存
                break;
            case 5:// TYPE_CDROM ：光驱
                break;
            case 6:// TYPE_SWAP ：页面交换
                break;
            }
//            System.out.println(fs.getDevName() + "读出：    " + usage.getDiskReads());
//            System.out.println(fs.getDevName() + "写入：    " + usage.getDiskWrites());
        }
        return map;
	}
	
	public Map<String,Object> getOsStaticInfo(){
		Map<String,Object> map = new HashMap<String, Object>();
		// 操作系统内核类型如： 386、486、586等x86  
		map.put(msg.OS_ARCH, os.getArch());
//		System.out.println("操作系统内核:" + os.getArch());  
		//System.out.println("os.getCpuEndian() = " + os.getCpuEndian());//  
		//System.out.println("os.getDataModel() = " + os.getDataModel());//  
		// 系统描述  
		//System.out.println("os.getDescription() = " + os.getDescription());  
		//System.out.println("os.getMachine() = " + os.getMachine());//  
		// 操作系统类型  
		//System.out.println("os.getName() = " + os.getName());  
		//System.out.println("os.getPatchLevel() = " + os.getPatchLevel());//  
		// 操作系统的卖主  
		map.put(msg.OS_VENDOR, os.getVendor());
//		System.out.println("操作系统厂商:" + os.getVendor());  
		// 卖主名称  
		//System.out.println("os.getVendorCodeName() = " + os.getVendorCodeName());  
		// 操作系统名称  
		map.put(msg.OS_NAME, os.getVendorName());
//		System.out.println("操作系统名称:" + os.getVendorName());  
		// 操作系统卖主类型  
		//System.out.println("os.getVendorVersion() = " + os.getVendorVersion());  
		// 操作系统的版本号  
		map.put(msg.OS_VERSION, os.getVersion());
//		System.out.println("操作系统版本号:" + os.getVersion());  
		
		return map;
	}
	
	public static Object[] getProcess(String user) throws SigarException{
		List<Long> userPidList=new ArrayList<Long>();
		long[] pids = sigar.getProcList();
		for(long pid:pids){
			String procUser = sigar.getProcCredName(pid).getUser();
			if(user.equalsIgnoreCase(procUser)){
				userPidList.add(pid);
			}
		}
		return userPidList.toArray();
	}
	
	public static void getUserProcess(long pid) throws SigarException{
		String name = ProcUtil.getDescription(sigar, pid);
		System.out.println(name);
	}

	
	public static void main(String[] args) {
		SigarManager sigarMgr = new SigarManager();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
//			map.putAll(sigarMgr.getCpuStaticInfo());
//			System.out.println(map.toString());
//			System.out.println("===============");
//			SigarManager.getCpuDynamicInfo();
//			System.out.println("===============");
//			SigarManager.getMemStaticInfo();
//			System.out.println("===============");
//			SigarManager.getMemDynamicInfo();
//			System.out.println("===============");
//			sigarMgr.getDiskStaticInfo();
//			System.out.println("===============");
//			SigarManager.getOsStaticInfo();
//			Object[] pids = SigarManager.getProcess("liuyungao");
//			for(Object pid:pids){
//				SigarManager.getUserProcess(Long.parseLong(pid.toString()));
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
