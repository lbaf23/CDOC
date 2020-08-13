package com.example.myproject.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.myproject.service.Exceptions.DateIllegalException;

/**
 * 一些日期的操作
 * @author 李柯凡
 *
 */
public class DateOperate {
	private static String dev = "/";
	
	/**
	 * 检查日期是否合法，若不合法或日期大于当前日期，则返回-1，若合法则返回日期距离今天的年数，相当于根据出生日期求年龄
	 * @param d
	 * @return
	 */
	public static int CheckDate(String d) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy"+dev+"MM"+dev+"dd");
		Date date = new Date();
		
		try {
			f.setLenient(false);
			date = f.parse(d);
		}catch(Exception e) {
			return -1;
		}
		
		Calendar now = Calendar.getInstance();
		Calendar birth = Calendar.getInstance();
		birth.setTime(date);
		
		if(birth.after(now)) {
			return -1;
		}
		else {
			int age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
			if( now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR) ) {
				age += 1;
			}
			return age-1;
		}
	}
	/**
	 * 日期相加，d1为日期，d2为加的日期数
	 * 月份和日自动进位
	 * @param d1 如2019/1/1格式的日期，年月日不能为0
	 * @param d2 如1/2/1格式的日期数
	 * @return 数据合法返回相加结果
	 * @throws Exception 数据不合法抛出异常
	 */
	public static String addDate(String d1,String d2) throws Exception {
		String[] s = d2.split(dev);
		int i1 = Integer.parseInt(s[0]);
		int i2 = Integer.parseInt(s[1]);
		int i3 = Integer.parseInt(s[2]);
		
		SimpleDateFormat f = new SimpleDateFormat("yyyy" + dev + "MM" + dev + "dd");
		f.setLenient(false);
		Calendar c1 = Calendar.getInstance();
		
		Date date1 = new Date();
		if(d1!=null) {
			date1 = f.parse(d1);	
		}
		
		c1.setTime(date1);
		
		c1.add(Calendar.YEAR, i1);
		c1.add(Calendar.MONTH, i2);
		c1.add(Calendar.DATE, i3);
		return f.format(c1.getTime());
	}
	/**
	 * 日期相减，d1为日期，d2为减去的日期数
	 * @param d1 如2019/1/1格式的日期，年月日不能为0
	 * @param d2 如1/0/1格式的日期数
	 * @return 合法则返回相减结果
	 * @throws Exception 数据不合法抛出异常
	 */
	public static String subDate(String d1,String d2) throws Exception {
		String[] s = d2.split(dev);
		int i1 = Integer.parseInt(s[0]);
		int i2 = Integer.parseInt(s[1]);
		int i3 = Integer.parseInt(s[2]);
		
		SimpleDateFormat f = new SimpleDateFormat("yyyy" + dev + "MM" + dev + "dd");
		f.setLenient(false);
		Calendar c1 = Calendar.getInstance();
		
		Date date1 = new Date();
		if(d1!=null) {
			date1 = f.parse(d1);	
		}
		
		c1.setTime(date1);
		
		c1.add(Calendar.YEAR, -i1);
		c1.add(Calendar.MONTH, -i2);
		c1.add(Calendar.DATE, -i3);
		return f.format(c1.getTime());
	}
	
	/**
	 * 比较两个日期的先后，若d1早于d2，返回1，若相等返回0，若d1晚于d2则返回-1
	 * 年月日均不能为0
	 * @param d1 日期1
	 * @param d2 日期2
	 * @return 比较结果
	 * @throws Exception 数据不合法抛出异常
	 */
	public static int compare(String d1,String d2) throws Exception {
		SimpleDateFormat f = new SimpleDateFormat("yyyy" + dev + "MM" + dev + "dd");
		f.setLenient(false);
		Date date1 = new Date();
		if(d1 != null) {
			date1 = f.parse(d1);
		}
		else {
			date1 = f.parse(now());
		}
		
		Date date2 = f.parse(d2);
		return date1.equals(date2) ? 0 : (date1.after(date2) ? -1:1);
	}
	
	/**
	 * 获取当前日期
	 * @return 当前日期
	 */
	public static String now() {
		Date d = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy" + dev + "MM" + dev + "dd");
		return f.format(d);
	}
	
	public static Date nowDate() {
		return new Date();
	}
	/**
	 * 格式化日期为年月日的格式
	 * @param date 字符串类型的日期
	 * @return 格式化的日期
	 * @throws Exception 数据不合法抛出异常
	 */
	public static String formatDate(String date) throws Exception {
		int y = Integer.parseInt(date.substring(0, date.indexOf(dev)) );
		String s = date.substring(date.indexOf(dev) + 1);
		int m = Integer.parseInt(s.substring(0, s.indexOf(dev)) );
		int d = Integer.parseInt(s.substring(s.indexOf(dev) + 1) );
		return y + "年" + m + "月" + d + "日";
	}
	/**
	 * 两个格式化的日期数相加，月份和日不能自动进位
	 * @param d1 如1/0/1格式的日期数
	 * @param d2 如1/0/1格式的日期数
	 * @return 格式化的结果
	 * @throws Exception 数据不合法抛出异常
	 */
	public static String addDateFormat(String d1,String d2) throws Exception {
		String[] s1 = d1.split(dev);
		String[] s2 = d2.split(dev);
		if(s1.length!=3||s2.length!=3) {
			throw new DateIllegalException();
		}
		s1[0] = String.valueOf(Integer.parseInt(s1[0]) + Integer.parseInt(s2[0]));
		s1[1] = String.valueOf(Integer.parseInt(s1[1]) + Integer.parseInt(s2[1]));
		s1[2] = String.valueOf(Integer.parseInt(s1[2]) + Integer.parseInt(s2[2]));
		return s1[0] + dev + s1[1] + dev + s1[2];
	}
	/**
	 * 获取日期分隔符
	 * @return 分隔符
	 */
	public static String getDev() {
		return dev;
	}
	/**
	 * 设置日期的分隔符，默认为/
	 * @param dev 分隔符
	 */
	public static void setDev(String dev) {
		DateOperate.dev = dev;
	}
	
	
}
