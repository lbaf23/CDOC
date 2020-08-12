package com.example.myproject.service;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;


public class JavaMail {

	/**
	 * 向指定邮箱发送邮件
	 * @param email 收件人邮箱
	 * @param title 邮件标题
	 * @param content 邮件内容
	 * @throws Exception
	 */
	public static void sendMail(String email,String title,String content) throws Exception {
		// 收件人电子邮箱
		String from = "******@qq.com";

		// 指定发送邮件的主机为 smtp.qq.com
		String host = "smtp.qq.com";  //QQ 邮件服务器

		// 获取系统属性
		Properties properties = System.getProperties();
		
		// 设置邮件服务器
		properties.setProperty("mail.smtp.host", host);

		properties.put("mail.smtp.auth", "true");
		MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.ssl.socketFactory", sf);
		// 获取默认session对象
		Session session = Session.getDefaultInstance(properties,new Authenticator(){
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("*******@qq.com", "*******"); //发件人邮件用户名、密码
			}
		});

		MimeMessage message = new MimeMessage(session);
		// Set From: 头部头字段
		message.setFrom(new InternetAddress(from));
		// Set To: 头部头字段
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
		// 设置消息体

		message.setSubject(title);
		message.setText(content);

		// 发送消息
		Transport.send(message);
		
		System.out.println("Sent email successfully.");
	}

	/**
	 * 获取随机数字验证码，返回字符串类型
	 * @param n 验证码位数
	 * @return 验证码
	 */
	public static String getCode(int n) {
		Random r = new Random();
		StringBuilder s = new StringBuilder();
		for(int i=0; i<n; i++) {
			s.append(String.valueOf(r.nextInt(10)));
		}
		return s.toString();
	}
}
