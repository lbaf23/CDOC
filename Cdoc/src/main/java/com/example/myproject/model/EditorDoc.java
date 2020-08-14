package com.example.myproject.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class EditorDoc {
	private String userId;
	private String docId;
	private Date editorDate;
	
	public EditorDoc(String userId,String docId,Date editorDate) {
		this.userId = userId; this.docId = docId;
		this.editorDate = editorDate;
	}
	/**
	 * 找到某个人是否在编辑某个文档
	 * @param userId
	 * @param docId
	 * @return
	 */
	public static EditorDoc findEditorDocByUserIdDocId(String userId,String docId) {
		String sql = "SELECT * FROM EditorDoc WHERE UserId = '"+userId+"' AND DocId = '"+docId+"'";
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			if(rs.next()) {
				return new EditorDoc(rs.getString("UserId"),rs.getString("DocId"),rs.getDate("EditorDate"));
			}
			else
				return null;
		} catch(Exception e) {
			return null;
		}
	}
	/**
	 * 找到编辑文档的所有用户
	 * @param docId
	 * @return
	 */
	public static ArrayList<DocUser> findDocUserEditorDoc(String docId){
		String sql = "SELECT UserId FROM EditorDoc WHERE DocId = '"+docId+"'";
		ArrayList<DocUser> res = new ArrayList<>();
		try {
			ArrayList<String> userId = new ArrayList<>();
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			while(rs.next()) {
				userId.add(rs.getString("UserId"));
			}
			rs.close();
			for(String s:userId) {
				res.add(DocUser.findUserById(s));
			}
			return res;
		} catch(Exception e) {
			return res;
		}
	}
	
	/**
	 * 添加新的编辑记录
	 * @param e
	 * @return
	 */
	public static boolean addEditorDoc(EditorDoc e) {
		String sql = "INSERT INTO EditorDoc(UserId,DocId,EditorDate) "+
					"VALUES"+e.toTupleInString();
		return Repository.getInstance().doSqlUpdateStatement(sql);
	}
	/**
	 * 删除编辑记录
	 * @param userId
	 * @param docId
	 * @return
	 */
	public static boolean deleteEditorDoc(String userId, String docId) {
		String sql = "DELETE FROM EditorDoc WHERE UserId = '"+userId+"' AND DocId = '"+docId+"'";
		return Repository.getInstance().doSqlUpdateStatement(sql);
	}
	
	public String toTupleInString() {
		return "('"+userId+"','"+docId+"','"+new Timestamp(editorDate.getTime())+"')";
	}
}
