package com.example.myproject.service;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class JobScheduler {
	public static Scheduler getScheduler() throws SchedulerException{
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		return schedulerFactory.getScheduler();
	}

	/**
	 * 调用此函数设置触发器
	 * 每隔固定时间执行updateInfo.execute
	 * @param hour 间隔的小时数
	 * @param minute 间隔的分钟数
	 * @param second 间隔的秒数
	 * @throws SchedulerException
	 */
	public static void schedulerJob(int hour, int minute, int second) throws SchedulerException{
		JobDetail jobDetail = JobBuilder.newJob(UpdateInfo.class).withIdentity("updateInfo", "group1").build();
		Trigger trigger;
		if(second != 0) {
			int time = ( (hour * 60) + minute) * 60 + second;
			trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group3")
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(time).repeatForever())
					.build();
		}
		else if(minute != 0) {
			int time = (hour * 60) + minute;
			trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group3")
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(time).repeatForever())
					.build();
		}
		else {
			trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group3")
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(hour).repeatForever())
					.build();
		}
		Scheduler scheduler = getScheduler();
		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.start();	
	}
}
