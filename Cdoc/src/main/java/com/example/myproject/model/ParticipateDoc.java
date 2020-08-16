package com.example.myproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ParticipateDoc {
	private String docId;
	private String userId;
	private String powerToDoc;
	
	public ParticipateDoc(String docId,String userId,String powerToDoc){
		this.docId=docId;
		this.userId=userId;
		this.powerToDoc=powerToDoc;
	}

	/**
	 * 根据docId查找参与者
	 * @param id
	 * @return 参与者
	 * @throws Exception
	 */
	public static ArrayList<String> findUserIdByDocId(String id) throws Exception {
		String sql = "SELECT UserId FROM ParticipateDoc WHERE DocId = '"+id+"'";
		ArrayList<String> res = new ArrayList<>();
		ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
		while(rs.next()) {
			res.add(rs.getString("UserId"));
		}
		rs.close();
		if(res.size() == 0)
			return null;
		return res;
	}
	
	/**
	 * 根据userId查找文档
	 * @param id
	 * @return 文档
	 * @throws Exception
	 */
	public static ArrayList<String> findDocIdByUserId(String id) throws Exception {
		String sql = "SELECT DocId FROM ParticipateDoc WHERE UserId = '"+id+"'";
		ArrayList<String> res = new ArrayList<>();
		ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
		while(rs.next()) {
			res.add(rs.getString("DocId"));
		}
		rs.close();
		if(res.size() == 0)
			return null;
		return res;
	}
	
	/**
	 * 根据userId和docId查找权限
	 * @param userId,docId
	 * @return 文档
	 * @throws Exception
	 */
	public static String findPowerToDocById(String userId,String docId) throws Exception {
		String sql = "SELECT PowerToDoc FROM ParticipateDoc WHERE UserId = '"+userId+"'AND DocId='"+docId+"'";
		ArrayList<String> res = new ArrayList<>();
		ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
		while(rs.next()) {
			res.add(rs.getString("PowerToDoc"));
		}
		rs.close();
		if(res.size() == 0)
			return null;
		return res.get(0);
	}
	
	/**
	 * 根据用户id和文档id查找参与关系
	 * @param userId
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	public static ParticipateDoc findParticipateByDocIdUserId(String userId,String docId) {
		String sql = "SELECT * FROM ParticipateDoc WHERE UserId = '"+userId+"'AND DocId='"+docId+"'";
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			if(rs.next()) {
				return new ParticipateDoc(rs.getString("DocId"), rs.getString("UserId"),
						rs.getString("PowerToDoc"));
			}
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * 添加新参与文档
	 * @param p
	 * @return 是否成功
	 */
	public static boolean addParticipateDoc(ParticipateDoc p) {
		String sql = "INSERT INTO ParticipateDoc(DocId,UserId,PowerToDoc) " 
				+ "VALUES"+p.toTupleInString();
		return Repository.getInstance().doSqlUpdateStatement(sql);
	}
	
	/**
	 * 退出参与文档
	 * @param p
	 * @return 是否成功
	 */
	public static boolean delParticipateDoc(ParticipateDoc p) {
		String sql = "DELETE FROM ParticipateDoc WHERE DocId ='"
				+ p.getDocId()+"' AND UserId = '"+p.getUserId()+"'";
		return Repository.getInstance().doSqlUpdateStatement(sql);
	}
	
	
	public String toTupleInString() {
		return "('" +docId+"','"+userId+"','"+powerToDoc+ "')";
	}
	
	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPowerToDoc() {
		return powerToDoc;
	}

	public void setPowerToDoc(String powerToDoc) {
		this.powerToDoc = powerToDoc;
	}
}
