package com.nokia.agent.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

import com.nokia.agent.job.QuartzManager;
import com.nokia.agent.job.RealTimeJob;
import com.nokia.agent.message.RequestMessage;

/**
 * agent处理客户端请求的监听类
 * 
 * @author liuyungao
 * @date 2015-11-07
 *
 */
public class ServerHandler extends IoHandlerAdapter{
	private static final Logger logger = Logger.getLogger(ServerHandler.class);
	
	private StdSchedulerFactory schedulerFactory;
	private Scheduler scheduler;
	
	/**
	 * 构造函数
	 */
	public ServerHandler() {
		try {
			//初始化Quartz并启动调度,当有客户端请求时动态添加调度任务
			schedulerFactory = new StdSchedulerFactory();
			schedulerFactory.initialize();
			scheduler = schedulerFactory.getScheduler();
			scheduler.start();
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		super.exceptionCaught(session, cause);
		logger.error("连接异常关闭！", cause);
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		super.inputClosed(session);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		super.messageReceived(session, message);
		RequestMessage req = (RequestMessage)message;
		
		// 根据指令动态添加调度任务
		int COMMAND = req.getCommand();
		switch (COMMAND) {
		case RequestMessage.REQ_REALTIME_DATA_START_COMMAND://start realtime
			logger.info("收到获取实时数据请求,开始返回实时数据");
			QuartzManager.addJob(String.valueOf(session.getId()), new RealTimeJob().getClass(), req.getInteval(),session,req);
			break;
		case RequestMessage.REQ_REALTIME_DATE_MODIFY_COMMAND://modify realtime time
			logger.info("收到修改调度任务时间请求,开始按修改后时间返回数据");
			QuartzManager.modifyJobTime(String.valueOf(session.getId()), req.getInteval(), session,req);
			break;
		case RequestMessage.REQ_REALTIME_DATA_STOP_COMMAND://stop realtime
			logger.info("收到停止实时数据请求,开始停止返回实时数据");
			QuartzManager.removeJob(String.valueOf(session.getId()));
			break;
		case 3://get history begin
			
			break;
		case 4://get history end
			
			break;

		default:
			break;
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		logger.info(session.getRemoteAddress()+"关闭了连接！");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		super.sessionIdle(session, status);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
	}
	
}
