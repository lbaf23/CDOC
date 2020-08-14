package com.example.myproject.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Message {
	private String messageId;
	private String userId; //  发送的人id
	private String objId;  //  接收的人id
	private String messageContent;
	private Date messageDate;
	private boolean read;
	
	public Message(String messageId, String userId, String objId, String messageContent, Date messageDate, boolean read) {
		this.messageId = messageId; this.userId = userId; this.messageContent= messageContent;
		this.messageDate = messageDate;this.objId = objId; this.read = read;
	}
	
	/**
	 * 根据id查找消息
	 * @param id 消息id
	 * @return
	 */
	public static Message findMessageById(String id) {
		String sql = "SELECT * FROM Message WHERE MessageId = '"+id+"'";
		try {
			ArrayList<Message> m = new ArrayList<>();
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			while(rs.next()) {
				Message mg = new Message(rs.getString("MessageId"),rs.getString("UserId"),rs.getString("ObjId"),
						rs.getString("MessageContent"),rs.getDate("MessageDate"),rs.getBoolean("Read"));
				m.add(mg);
			}
			if(m.size() == 0) {
				return null;
			}
			return m.get(0);
		} catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 查找某个用户的消息，分为三种状态，时间排序
	 * @param userId 用户id
	 * @param state 0全部 1已读 2未读 
	 * @return
	 */
	public static ArrayList<Message> findNotReadMessageByUserId(String userId,int state) {
		String sql = "SELECT * FROM Message WHERE UserId = '"+userId+"'";
		if(state == 1) {
			sql += " AND Read = 'true'";
		}
		else if(state == 2) {
			sql += " AND Read = 'false'";
		}
		sql += " ORDER BY MessageDate DESC";
		ArrayList<Message> res = new ArrayList<>();
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			while(rs.next()) {
				res.add(new Message(rs.getString("MessageId"),rs.getString("UserId"),rs.getString("ObjId"),
						rs.getString("MessageContent"),rs.getDate("MessageDate"),rs.getBoolean("Read")) );
			}
			rs.close();
			return res;
		} catch(Exception e) {
			return res;
		}
	}
	
	/**
	 * 根据用户id查找消息并按时间从近到远排序
	 * @param id
	 * @return
	 */
	public static ArrayList<Message> findMessageByUserId(String id) {
		String sql = "SELECT * FROM Message WHERE UserId = '"+id+"' ORDER BY MessageDate DESC";
		ArrayList<Message> m = new ArrayList<>();
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			while(rs.next()) {
				Message mg = new Message(rs.getString("MessageId"),rs.getString("UserId"),rs.getString("ObjId"),
						rs.getString("MessageContent"),rs.getDate("MessageDate"),rs.getBoolean("Read"));
				m.add(mg);
			}
			return m;
		} catch(Exception e) {
			return m;
		}
	}
	/**
	 * 添加一条消息
	 * @param m
	 * @return
	 */
	public static boolean addMessage(Message m) {
		String sql = "INSERT INTO Message(MessageId,UserId,ObjId,MessageContent,MessageDate,Read) VALUES "
				+ m.toTupleInString();
		return Repository.getInstance().doSqlUpdateStatement(sql);
	}
	/**
	 * 获取下一条id
	 * @return
	 */
	public static String getNextId() {
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement("SELECT TOP 1 MessageId FROM Message " + 
					"ORDER BY convert(int,MessageId) DESC;");
			if(rs.next()) {
				return String.valueOf(Integer.parseInt(rs.getString("MessageId")) + 1);
			}
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
	public String toTupleInString() {
		return "('"+messageId+"','"+userId+"','"+objId+"','"+messageContent+"','"+new Timestamp(messageDate.getTime())+"','"+read+"')";
	}
}
