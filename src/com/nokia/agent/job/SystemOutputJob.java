package com.nokia.agent.job;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nokia.agent.onlinedata.Cache;


public class SystemOutputJob implements Runnable {
	private static final Logger logger = Logger.getLogger(SystemOutputJob.class);

	private static final String DB_PATH = "db/";
	private static final String TABLE_NAME = "sys_history_";
	private static final String SUFFIX = ".txt";
	private boolean isRunning = true;
	private Date lastDate = new Date();
	private Date recordDate;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void run() {
		RandomAccessFile rf = null;
		try {
			rf=new RandomAccessFile(getDBFileName(sdf.format(lastDate)),"rw");
			while(isRunning){
				Map<String,Object> map = Cache.historyQueue.take();
				if(map==null){
					continue;
				}
				recordDate = sdf1.parse(map.get("SYS_TIME").toString());
				if(!lastDate.equals(recordDate)){
					lastDate = recordDate;
					rf.close();
					rf = null;
					rf=new RandomAccessFile(getDBFileName(sdf.format(lastDate)),"rw");
				}
				rf.seek(rf.length());
				rf.writeBytes(map.toString()+"\r\n"); 
			}
			rf.close();
			rf = null;
			
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if(rf!=null){
				try {
					rf.close();
				} catch (IOException e) {}
			}
		}
	}
	
	private String getDBFileName(String date){
		StringBuffer dbFileName = new StringBuffer();
		dbFileName.append(DB_PATH).append(TABLE_NAME).append(date).append(SUFFIX);
		return dbFileName.toString();
	}
	
	private void isNewDay(Date oldDay,Date newDay){
		if(oldDay.equals(newDay)){
			
		}
	}

}
