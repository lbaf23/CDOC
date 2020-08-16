package com.example.myproject.model;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.example.myproject.service.FileOperate;

public class Doc {
	public static final String docPath = "DocSrc/";
	public static final String logPath = "DocSrc/DocLog/";
	
	private String docId;
	private String createrId;
	private String docSrc;
	private Date docCreateDate;
	private Date docChangeDate;
	private String docLog;
	private boolean deleted;    //true or false
	private Date deleteDate;
	private String belongTo;
	
	
	public Doc(String docId,String createrId,String docSrc,Date docCreateDate,Date docChangeDate,String docLog,boolean deleted,Date deleteDate,String belongTo) {
		this.docId=docId;
		this.createrId=createrId;
		this.docSrc=docSrc;
		this.docCreateDate=docCreateDate;
		this.docChangeDate=docChangeDate;
		this.docLog=docLog;
		this.deleted=deleted;
		this.deleteDate=deleteDate;
		this.belongTo=belongTo;
	}
	
	/**
	 * 根据docId查找文档
	 * @param id
	 * @return 文档
	 * @throws Exception
	 */
	public static Doc findDocByDocId(String id) {
		String sql = "SELECT * FROM Doc WHERE Deleted = 'false' AND DocId = '"+id+"'";
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			if(rs.next()) {
				Doc d = new Doc(rs.getString("DocId"), rs.getString("CreaterId"), rs.getString("DocSrc"),rs.getDate("DocCreateDate"),
						rs.getDate("DocChangeDate"),rs.getString("DocLog"), rs.getBoolean("Deleted"), rs.getDate("DeleteDate"), rs.getString("BelongTo"));
				rs.close();
				return d;
			}
			else {
				return null;
			}
		}catch(Exception e) {
			return null;
		}
	}
	/**
	 * 根据createrId查找全部文档
	 * @param id
	 * @return 文档列表
	 * @throws Exception
	 */
	public static ArrayList<Doc> findAllDocByCreaterId(String id){
		String sql = "SELECT * FROM Doc WHERE Deleted = 'false' AND CreaterId = '"+id+"'";
		ArrayList<Doc> res = new ArrayList<>();
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			while(rs.next()) {
				Doc d = new Doc(rs.getString("DocId"), rs.getString("CreaterId"), rs.getString("DocSrc"),rs.getDate("DocCreateDate"),
						rs.getDate("DocChangeDate"),rs.getString("DocLog"), rs.getBoolean("Deleted"), rs.getDate("DeleteDate"), rs.getString("BelongTo"));
				res.add(d);
			}
			rs.close();
		}catch(Exception e) {
			return res;
		}
		if(res.size() == 0)
			return null;
		return res;
	}
	/**
	 * 根据createrId查找已删除文档
	 * @param id
	 * @return 文档列表
	 * @throws Exception
	 */
	public static ArrayList<Doc> findDeleterdDocByCreaterId(String id) {
		ArrayList<Doc> res = findAllDocByCreaterId(id);
		ArrayList<Doc> res2= new ArrayList<>();;
		for(int i=0;i<res.size();i++) {
			if(res.get(i).isDeleted()) {
				res2.add(res.get(i));
			}
		}
		if(res2.size() == 0)
			return null;
		return res2;
	}
	/**
	 * 根据createrId查找未删除文档
	 * @param id
	 * @return 文档列表
	 * @throws Exception
	 */
	public static ArrayList<Doc> findNoDeleterdDocByCreaterId(String id) {
		ArrayList<Doc> res = findAllDocByCreaterId(id);
		ArrayList<Doc> res2= new ArrayList<>();;
		for(int i=0;i<res.size();i++) {
			if(res.get(i).isDeleted() == false) {
				res2.add(res.get(i));
			}
		}
		if(res2.size() == 0)
			return null;
		return res2;
	}
	
	/**
	 * 根据belongTo查找全部文档 查找属于某个团队的文档
	 * @param id
	 * @return 文档列表
	 * @throws Exception
	 */
	public static ArrayList<Doc> findAllDocByBelongTo(String id) {
		String sql = "SELECT * FROM Doc WHERE Deleted = 'false' AND BelongTo = '"+id+"'";
		ArrayList<Doc> res = new ArrayList<>();
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			while(rs.next()) {
				Doc d = new Doc(rs.getString("DocId"), rs.getString("CreaterId"), rs.getString("DocSrc"),rs.getDate("DocCreateDate"),
						rs.getDate("DocChangeDate"),rs.getString("DocLog"), rs.getBoolean("Deleted"), rs.getDate("DeleteDate"), rs.getString("BelongTo"));
				res.add(d);
			}
			rs.close();
		}catch(Exception e) {
			return res;
		}
		if(res.size() == 0)
			return null;
		return res;
	}
	
	/**
	 * 添加新文档
	 * @param d
	 * @return 是否成功
	 */
	public static boolean addDoc(Doc d) {
		String sql = "INSERT INTO Doc(DocId,CreaterId,DocSrc,DocCreateDate,DocChangeDate,DocLog,Deleted,DeleteDate,BelongTo) " 
				+ "VALUES"+d.toTupleInString();
		return Repository.getInstance().doSqlUpdateStatement(sql);
	}
	
	/**
	 * 更新文档信息
	 * @param docId
	 * @param column
	 * @param value
	 * @return
	 */
	public static boolean updateDoc(String docId,String column,String value) {
		String sql = "UPDATE Doc SET "+column+" = '"+value+"' WHERE DocId = '"+docId+"'";
		Doc.updateDocTime(docId);
		return Repository.getInstance().doSqlUpdateStatement(sql);
	}
	
	/**
	 * 删除文档
	 * @param d 文档id
	 * @return 是否成功
	 */
	public static boolean delDoc(String d) {
		String sql = "UPDATE Doc SET Deleted = 'true' WHERE DocId ='" 
				+ d+"'";
		updateDocTime(d);
		return Repository.getInstance().doSqlUpdateStatement(sql);
	}
	/**
	 * 彻底删除文件
	 * @param d
	 * @return
	 */
	public static boolean delDocThorough(String d) {
		String sql = "DELETE FROM Doc WHERE DocId ='" 
				+ d+"'";
		return Repository.getInstance().doSqlUpdateStatement(sql);
	}
	/**
	 * 恢复文档
	 * @param docId
	 * @return
	 */
	public static boolean reverseDoc(String docId) {
		String sql = "UPDATE Doc SET Deleted = 'false' WHERE DocId = '"+docId+"'";
		return Repository.getInstance().doSqlUpdateStatement(sql);
	}
	
	/**
	 * 修改文档名
	 * @param d 文档
	 * @param newName 新文档名
	 * @return
	 */
	public static boolean renameDoc(Doc d,String newName) {
		String newSrc = d.getDocPath() +"/"+ newName;
		String sql = "UPDATE Doc SET DocSrc = '" + newSrc + "' WHERE DocId = '"+
						d.getDocId() + "'";
		boolean res = true;
		FileOperate.renameFile(d.getDocSrc(), newSrc);
		res = res && Repository.getInstance().doSqlUpdateStatement(sql);
		updateDocTime(d.getDocId());
		return res;
	}
	
	/**
	 * 更新文档最后修改时间
	 * @param docId
	 * @return
	 */
	public static boolean updateDocTime(String docId) {
		String sql = "UPDATE Doc SET DocChangeDate = '"+new Timestamp(new Date().getTime())+"' WHERE DocId = '"+docId+"'";
		return Repository.getInstance().doSqlUpdateStatement(sql);
	}
	
	public String toTupleInString() {
		return "('" +docId+"','"+createrId+"','"+docSrc+"','"+new Timestamp(docCreateDate.getTime())+"','"+new Timestamp(docChangeDate.getTime())+"','"+docLog+"','"+deleted+"','"+new Timestamp(deleteDate.getTime())+"','"+belongTo+ "')";
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getCreaterId() {
		return createrId;
	}

	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}

	public String getDocSrc() {
		return docSrc;
	}

	public void setDocSrc(String docSrc) {
		this.docSrc = docSrc;
	}


	public String getDocLog() {
		return docLog;
	}

	public void setDocLog(String docLog) {
		this.docLog = docLog;
	}
	

	public String getBelongTo() {
		return belongTo;
	}

	public void setBelongTo(String belongTo) {
		this.belongTo = belongTo;
	}
	
	public Date getDocChangeDate() {
		return docChangeDate;
	}

	public void setDocChangeDate(Date docChangeDate) {
		this.docChangeDate = docChangeDate;
	}

	public static String getNextId() {
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement("SELECT TOP 1 DocId FROM Doc " + 
					"ORDER BY convert(int,DocId) DESC;");
			if(rs.next()) {
				int index = Integer.parseInt(rs.getString("DocId")) + 1;
				rs.close();
				return String.valueOf(index);
			}
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public Date getDocCreateDate() {
		return docCreateDate;
	}

	public void setDocCreateDate(Date docCreateDate) {
		this.docCreateDate = docCreateDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	public Date getDeleteDate() {
		return deleteDate;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	/**
	 * 计算文档src
	 * @param docId
	 * @param docName
	 * @return
	 */
	public static String getDocSrcByDocName(String docId,String docName) {
		return docPath+docId+"/"+docName;
	}
	/**
	 * 计算文档log地址
	 * @param docId
	 * @param docName
	 * @return
	 */
	public static String getDocLogByDocName(String docId,String docName) {
		return logPath+docId+".log";
	}
	
	/**
	 * 获取文件名
	 * @return
	 */
	public String getDocName() {
		return this.docSrc.substring(this.docSrc.lastIndexOf("/")+1);
	}
	/**
	 * 获取文件路径
	 * @return
	 */
	public String getDocPath() {
		return this.docSrc.substring(0,this.docSrc.lastIndexOf("/"));
	}
	/**
	 * 获取文档大小
	 * @return
	 */
	public String getDocSize() {
		//TODO 此处获取实际文档的大小，暂时不必须做
		File f = new File(this.docSrc);
		return "0 kb";
	}
}
