package com.example.myproject.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class EditorDoc {
	private String userId;
	private String docId;
	private Date editorDate;
	private boolean isEditing;
	private boolean haveSubmit;
	
	public EditorDoc(String userId,String docId,Date editorDate,boolean isEditing,boolean haveSubmit) {
		this.userId = userId; this.docId = docId;
		this.editorDate = editorDate;this.setEditing(isEditing);this.haveSubmit = haveSubmit;
	}
	/**
	 * 找到某个人是否进入某个文档
	 * @param userId
	 * @param docId
	 * @return
	 */
	public static EditorDoc findEditorDocByUserIdDocId(String userId,String docId) {
		String sql = "SELECT * FROM EditorDoc WHERE UserId = '"+userId+"' AND DocId = '"+docId+"'";
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			if(rs.next()) {
				EditorDoc ed = new EditorDoc(rs.getString("UserId"),rs.getString("DocId"),rs.getDate("EditorDate"),
						rs.getBoolean("IsEditing"),rs.getBoolean("HaveSubmit"));
				rs.close();
				return ed;
			}
			else
				return null;
		} catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 某个用户是否需要更新
	 * @param userId
	 * @param docId
	 * @return
	 */
	public static boolean haveUpdate(String userId,String docId) {
		String sql = "SELECT HaveSubmit FROM EditorDoc WHERE DocId = '"+docId+"' AND UserId = '"+userId+"'";
		try {
			boolean hav = false;
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			if(rs.next()) {
				hav = rs.getBoolean("HaveSubmit");
			}
			rs.close();
			if(hav) {
				String sqll = "UPDATE EditorDoc SET HaveSubmit = 'false' WHERE DocId ='"+docId+"' AND UserId = '"+userId+"'";
				Repository.getInstance().doSqlUpdateStatement(sqll);
				return true;
			}
			else {
				return false;
			}
		} catch(Exception e) {
			return false;
		}
	}
	
	/**
	 * 找到正在编辑的人id
	 * @param docId
	 * @return
	 */
	public static String findEditingUserId(String docId) {
		String sql = "SELECT UserId FROM EditorDoc WHERE IsEditing = 'true' AND DocId = '"+docId+"'";
		try {
			String id = null;
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			if(rs.next()) {
				id = rs.getString("UserId");
			}
			rs.close();
			return id;
		} catch(Exception e) {
			return null;
		}
	}
	/**
	 * 申请编辑文档
	 * @param userId
	 * @param docId
	 * @return
	 */
	public static boolean editingDoc(String userId,String docId) {
		if(findEditingUserId(docId)==null) {
			String sql = "UPDATE EditorDoc SET IsEditing = 'true' WHERE UserId = '"+userId+"' AND "
					+ "DocId = '"+docId+"'";
			return Repository.getInstance().doSqlUpdateStatement(sql);
		}
		return false;
	}
	/**
	 * 用户结束编辑文档
	 * @param userId
	 * @param docId
	 * @return
	 */
	public static boolean finishedEditing(String userId,String docId) {
		String sql = "UPDATE EditorDoc SET IsEditing = 'false' WHERE UserId = '"+userId+"' AND "
				+ "DocId = '"+docId+"'";
		String sqll = "UPDATE EditorDoc SET HaveSubmit = 'true' WHERE DocId = '"+docId+"' AND UserId != '"+userId+"'";
		return Repository.getInstance().doSqlUpdateStatement(sql) && Repository.getInstance().doSqlUpdateStatement(sqll);
	}
	
	/**
	 * 找到进入文档的所有用户
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
	 * 添加新的进入记录
	 * @param e
	 * @return
	 */
	public static boolean addEditorDoc(EditorDoc e) {
		String sql = "INSERT INTO EditorDoc(UserId,DocId,EditorDate,IsEditing,HaveSubmit) "+
					"VALUES"+e.toTupleInString();
		return Repository.getInstance().doSqlUpdateStatement(sql);
	}
	/**
	 * 删除进入记录
	 * @param userId
	 * @param docId
	 * @return
	 */
	public static boolean deleteEditorDoc(String userId, String docId) {
		String sql = "DELETE FROM EditorDoc WHERE UserId = '"+userId+"' AND DocId = '"+docId+"'";
		return Repository.getInstance().doSqlUpdateStatement(sql);
	}
	
	public String toTupleInString() {
		return "('"+userId+"','"+docId+"','"+new Timestamp(editorDate.getTime())+"','"+isEditing+"','"+haveSubmit+"')";
	}
	public boolean isEditing() {
		return isEditing;
	}
	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public Date getEditorDate() {
		return editorDate;
	}
	public void setEditorDate(Date editorDate) {
		this.editorDate = editorDate;
	}
	public boolean isHaveSubmit() {
		return haveSubmit;
	}
	public void setHaveSubmit(boolean haveSubmit) {
		this.haveSubmit = haveSubmit;
	}
	
}
