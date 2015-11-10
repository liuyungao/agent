package com.nokia.agent.util;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.ProcStat;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

public class SystemInfo {

	private static int getCpuCount() throws SigarException {

		Sigar sigar = new Sigar();
		try {
			return sigar.getCpuInfoList().length;
		} finally {
			sigar.close();
		}
	}

	public static void main(String[] args) throws SigarException {
		System.out.println(getCpuCount());

		Sigar sigar = new Sigar();
		// -----------------CPU INFO
		CpuInfo infos[] = sigar.getCpuInfoList();
		for (int i = 0; i < infos.length; i++) {
			CpuInfo info = infos[i];
			System.out.print("CPU的总量MHz:" + info.getMhz() + " ,");
			System.out.print("获得CPU的厂商:" + info.getVendor() + " ,");
			System.out.print("获得CPU的类别:" + info.getModel() + " ,");
			System.out.print("缓冲存储器数量:" + info.getCacheSize() + " ,");
			
			//System.out.println(info.getTotalSockets());
			System.out.println();
		}

		// ------------------CPU USAGE
		CpuPerc cpuList = null;
		try {
			cpuList = sigar.getCpuPerc();
		} catch (SigarException e) {
			e.printStackTrace();
			return;
		}
		
		//printCpuPerc(cpuList);
//		for (int i = 0; i < cpuList.length; i++) {
//			printCpuPerc(cpuList[i]);
//		}
		
//		long[] pids = sigar.getProcList();
//		try {
//			for (long pid : pids) {
//				if(pid==2854){
//					ProcState prs = sigar.getProcState(pid);
//					ProcCpu pCpu = new ProcCpu();
//					pCpu.gather(sigar, pid);
//					System.out.print(prs.getName());
//					System.out.println(" pid: " + pid + " name:" + prs.getName());
//				}
//			}
//		} catch (Exception e) {
//			
//		}
		
//		printDiskInfo(sigar);
		printDiskInfo(sigar);
		sigar.close();
	}
	
	private static void printMemInfo(Sigar sigar) throws SigarException{
			// 物理内存信息  
			Mem mem = sigar.getMem();  
				// 内存总量  
				System.out.println("Total = " + mem.getTotal() / 1024L / 1024 + "M av");  
				// 当前内存使用量  
				System.out.println("Used = " + mem.getUsed() / 1024L / 1024 + "M used");  
				// 当前内存剩余量  
				System.out.println("Free = " + mem.getFree() / 1024L / 1024 + "M free");  
				
				// 系统页面文件交换区信息  
				Swap swap = sigar.getSwap();  
				// 交换区总量  
				System.out.println("Total = " + swap.getTotal() / 1024L + "K av");  
				// 当前交换区使用量  
				System.out.println("Used = " + swap.getUsed() / 1024L + "K used");  
				// 当前交换区剩余量  
				System.out.println("Free = " + swap.getFree() / 1024L + "K free");  
	}
	
	private static void printOperationSystemInfo(Sigar sigar){
		String hostname = "";  
		try {  
		    hostname = InetAddress.getLocalHost().getHostName();  
		} catch (Exception exc) {  
		    try {  
		        hostname = sigar.getNetInfo().getHostName();  
		    } catch (SigarException e) {  
		        hostname = "localhost.unknown";  
		    } finally {  
		        sigar.close();  
		    }  
		}  
		
		
		// 取当前操作系统的信息  
		OperatingSystem OS = OperatingSystem.getInstance();  
		// 操作系统内核类型如： 386、486、586等x86  
		System.out.println("OS.getArch() = " + OS.getArch());  
		System.out.println("OS.getCpuEndian() = " + OS.getCpuEndian());//  
		System.out.println("OS.getDataModel() = " + OS.getDataModel());//  
		// 系统描述  
		System.out.println("OS.getDescription() = " + OS.getDescription());  
		System.out.println("OS.getMachine() = " + OS.getMachine());//  
		// 操作系统类型  
		System.out.println("OS.getName() = " + OS.getName());  
		System.out.println("OS.getPatchLevel() = " + OS.getPatchLevel());//  
		// 操作系统的卖主  
		System.out.println("OS.getVendor() = " + OS.getVendor());  
		// 卖主名称  
		System.out  
		        .println("OS.getVendorCodeName() = " + OS.getVendorCodeName());  
		// 操作系统名称  
		System.out.println("OS.getVendorName() = " + OS.getVendorName());  
		// 操作系统卖主类型  
		System.out.println("OS.getVendorVersion() = " + OS.getVendorVersion());  
		// 操作系统的版本号  
		System.out.println("OS.getVersion() = " + OS.getVersion());  
		  
	}
	
	
	private static void printDiskInfo(Sigar sigar) throws SigarException{
		FileSystem fslist[] = sigar.getFileSystemList();  
		String dir = System.getProperty("user.home");// 当前用户文件夹路径  
		System.out.println(dir + "   " + fslist.length);  
		for (int i = 0; i < fslist.length; i++) {  
		    System.out.println("\n~~~~~~~~~~" + i + "~~~~~~~~~~");  
			FileSystem fs = fslist[i];  
			// 分区的盘符名称  
			System.out.println("fs.getDevName() = " + fs.getDevName());  
			// 分区的盘符名称  
			System.out.println("fs.getDirName() = " + fs.getDirName());  
			System.out.println("fs.getFlags() = " + fs.getFlags());//  
			// 文件系统类型，比如 FAT32、NTFS  
			System.out.println("fs.getSysTypeName() = " + fs.getSysTypeName());  
			// 文件系统类型名，比如本地硬盘、光驱、网络文件系统等  
			System.out.println("fs.getTypeName() = " + fs.getTypeName());  
			// 文件系统类型  
			System.out.println("fs.getType() = " + fs.getType());  
			FileSystemUsage usage = null;  
			try {  
			    usage = sigar.getFileSystemUsage(fs.getDirName());  
			} catch (SigarException e) {  
			    if (fs.getType() == 2)  {
			        throw e;  
			    }
			    continue;  
			}  
			switch (fs.getType()) {  
			case 0: // TYPE_UNKNOWN ：未知  
			    break;  
			case 1: // TYPE_NONE  
			    break;  
			case 2: // TYPE_LOCAL_DISK : 本地硬盘  
			    // 文件系统总大小  
			    System.out.println(" Total = " + usage.getTotal() + "KB");  
			    // 文件系统剩余大小  
			    System.out.println(" Free = " + usage.getFree() + "KB");  
			    // 文件系统可用大小  
			    System.out.println(" Avail = " + usage.getAvail() + "KB");  
			    // 文件系统已经使用量  
			    System.out.println(" Used = " + usage.getUsed() + "KB");  
			    double usePercent = usage.getUsePercent() * 100D;  
			    // 文件系统资源的利用率  
			    System.out.println(" Usage = " + usePercent + "%");  
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
			System.out.println(" DiskReads = " + usage.getDiskReads());  
			System.out.println(" DiskWrites = " + usage.getDiskWrites());  
		}  
	}
	
	

	private static void printCpuPerc(CpuPerc cpu) {
		System.out.print("用户使用率 :" + CpuPerc.format(cpu.getUser()));
		System.out.print("系统使用率:" + CpuPerc.format(cpu.getSys()));
		System.out.print("当前等待率 :" + CpuPerc.format(cpu.getWait()));
		System.out.print("Nice :" + CpuPerc.format(cpu.getNice()));
		System.out.print("当前空闲率 :" + CpuPerc.format(cpu.getIdle()));
		System.out.print("总的使用率 :" + CpuPerc.format(cpu.getCombined()));
		System.out.println();
	}
}
