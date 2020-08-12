package com.example.myproject.service;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class UpdateInfo implements Job {

	/**
	 * 间隔固定时间执行
	 * @param con
	 * @throws JobExecutionException
	 */
	public void execute(JobExecutionContext con) throws JobExecutionException {
		System.out.println("Update all info......");
		
		// TODO 更新时要做的内容
		
		System.out.println("Finished.");
	}
}
