package com.nokia.agent.job;

import java.text.ParseException;

import org.apache.mina.core.session.IoSession;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.nokia.agent.message.RequestMessage;

/**
 * Quartz管理类
 * 
 * @author liuyungao
 * @date 2015-11-7
 *
 */
public class QuartzManager {

	private static StdSchedulerFactory factory = new StdSchedulerFactory();
	private static String JOB_GROUP_NAME = "job_group1";
	private static String TRIGGER_GROUP_NAME = "trigger_group1";
	private static String SESSION_NAME = "session";
	private static String REQUSET_MESSAGE = "request_content";

	/**
	 * 添加调度任务
	 * @param jobName	任务名称
	 * @param jobClass	任务执行类
	 * @param time		任务调度时间
	 * @param session	消息会话
	 * @param reqMsg	请求消息
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void addJob(String jobName, Class<?> jobClass, String time, IoSession session, RequestMessage reqMsg)
			throws SchedulerException, ParseException {
		Scheduler sched = factory.getScheduler();
		JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, jobClass);
		jobDetail.getJobDataMap().put(SESSION_NAME, session);
		jobDetail.getJobDataMap().put(REQUSET_MESSAGE, reqMsg);
		CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);
		trigger.setCronExpression(time);
		sched.scheduleJob(jobDetail, trigger);
		if (!sched.isShutdown()) {
			sched.start();
		}
	}

	/**
	 * 添加调度任务
	 * @param jobName			任务名称
	 * @param jobGroupName		任务组名称
	 * @param triggerName		触发器名称
	 * @param triggerGroupName	触发器组名称
	 * @param jobClass			任务执行类
	 * @param time				任务调度时间
	 * @param session			消息会话
	 * @param reqMsg			请求消息
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void addJob(String jobName, String jobGroupName,
			String triggerName, String triggerGroupName, Class<?> jobClass, String time, IoSession session, RequestMessage reqMsg)
			throws SchedulerException, ParseException {
		Scheduler sched = factory.getScheduler();
		JobDetail jobDetail = new JobDetail(jobName, jobGroupName, jobClass);// 任务名，任务组，任务执行类
		jobDetail.getJobDataMap().put(SESSION_NAME, session);
		jobDetail.getJobDataMap().put(REQUSET_MESSAGE, reqMsg);
		// 触发器
		CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);// 触发器名,触发器组
		trigger.setCronExpression(time);// 触发器时间设定
		sched.scheduleJob(jobDetail, trigger);
		if (!sched.isShutdown())
			sched.start();
	}

	/**
	 * 编辑调度任务
	 * @param jobName	任务名称
	 * @param time		任务调度时间
	 * @param session	消息会话
	 * @param reqMsg	请求消息
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void modifyJobTime(String jobName, String time,IoSession session, RequestMessage reqMsg) throws SchedulerException, ParseException {
		Scheduler sched = factory.getScheduler();
		Trigger trigger = sched.getTrigger(jobName, TRIGGER_GROUP_NAME);
		if (trigger != null) {
			CronTrigger ct = (CronTrigger) trigger;
			String oldTime = ct.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				JobDetail jobDetail = sched.getJobDetail(jobName,JOB_GROUP_NAME);
				Class<?> jobClass = jobDetail.getJobClass();
				removeJob(jobName);
				addJob(jobName, jobClass, time, session, reqMsg);
			}
		}
	}

	/**
	 * 编辑调度任务
	 * @param triggerName		触发器名称
	 * @param triggerGroupName	触发器组名称
	 * @param time				任务调度时间
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void modifyJobTime(String triggerName,
			String triggerGroupName, String time) throws SchedulerException,
			ParseException {
		Scheduler sched = factory.getScheduler();
		Trigger trigger = sched.getTrigger(triggerName, triggerGroupName);
		if (trigger != null) {
			CronTrigger ct = (CronTrigger) trigger;
			// 修改时间
			ct.setCronExpression(time);
			// 重启触发器
			sched.resumeTrigger(triggerName, triggerGroupName);
		}
	}

	/**
	 * 删除调度任务
	 * @param jobName	任务名称
	 * @throws SchedulerException
	 */
	public static void removeJob(String jobName) throws SchedulerException {
		Scheduler sched = factory.getScheduler();
		sched.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// 停止触发器
		sched.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// 移除触发器
		sched.deleteJob(jobName, JOB_GROUP_NAME);// 删除任务
	}

	/**
	 * 删除调度任务
	 * @param jobName			任务名称
	 * @param jobGroupName		任务组名称
	 * @param triggerName		触发器名称
	 * @param triggerGroupName	触发器组名称
	 * @throws SchedulerException
	 */
	public static void removeJob(String jobName, String jobGroupName,
			String triggerName, String triggerGroupName)
			throws SchedulerException {
		Scheduler sched = factory.getScheduler();
		sched.pauseTrigger(triggerName, triggerGroupName);// 停止触发器
		sched.unscheduleJob(triggerName, triggerGroupName);// 移除触发器
		sched.deleteJob(jobName, jobGroupName);// 删除任务
	}
	
	/**
	 * 启动所有调度任务
	 * @throws SchedulerException
	 */
	public static void startJobs() throws SchedulerException{
		Scheduler sched = factory.getScheduler();
		sched.start();
	}
	
	/**
	 * 停止所有调度任务
	 * @throws SchedulerException
	 */
	public static void shutdownJobs() throws SchedulerException {
		Scheduler sched = factory.getScheduler();
		if (!sched.isShutdown()) {
			sched.shutdown();
		}
	}

}
