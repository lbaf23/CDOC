package com.example.myproject.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class DocUser {
	public static String DEV = "-";
	public static final String userImagePath = "UserImage/";
	
	private String userId;
	private String userName;
	private String userPassword;
	private String userImage;
	private ArrayList<String> recentlyDoc;
	private ArrayList<String> favouriteDoc;
	
	public DocUser(String userId,String userName, String userPassword, String userImage,String recentlyDoc,String favouriteDoc) {
		this.userId = userId; this.userName = userName; this.userPassword = userPassword; this.userImage = userImage;
		this.recentlyDoc = stringToList(recentlyDoc); this.favouriteDoc = stringToList(favouriteDoc);
	}
	
	/**
	 * 根据id查找用户
	 * @param id 用户id
	 * @return 用户 若null则不存在用户
	 */
	public static DocUser findUserById(String id) {
		try {
			String sql = "SELECT * FROM DocUser WHERE UserId = '"+id+"'";
			ArrayList<DocUser> res = new ArrayList<>();
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			while(rs.next()) {
				DocUser u = new DocUser(rs.getString("UserId"), rs.getString("UserName"), rs.getString("UserPassword"), 
						rs.getString("UserImage"), rs.getString("recentlyDoc"), rs.getString("FavouriteDoc"));
				res.add(u);
			}
			rs.close();
			if(res.size() == 0)
				return null;
			return res.get(0);
		} catch(Exception e) {
			return null;
		}
	}
	/**
	 * 添加新用户
	 * @param u 用户类
	 * @return 是否成功
	 */
	public static boolean addUser(DocUser u) {
		String sql = "INSERT INTO DocUser(UserId,UserName,UserPassword,UserImage,RecentlyDoc,FavouriteDoc) " 
				+ "VALUES"+u.toTupleInString();
		return Repository.getInstance().doSqlUpdateStatement(sql);
	}
	/**
	 * 用户修改信息
	 * @param userId 用户id
	 * @param column 要修改的列
	 * @param value 修改的信息
	 * @return 修改成功true 失败false
	 */
	public static boolean changeUserInfo(String userId, String column, String value) {
		String sql = "UPDATE DocUser SET "+column+" = '"+value+"' WHERE UserId = '"+userId+"'";
		return Repository.getInstance().doSqlUpdateStatement(sql);
	}
	/**
	 * 查找用户创建的文件
	 * @param id 用户id
	 * @param order 排序方式：1 按照最后修改时间排序 2 按照创建时间排序 3 按照文件名排序
	 * @param desc 是否按照时间近的到远的排序 true 从近到远 false 从远到近
	 * @return 创建的文件列表
	 */
	public static ArrayList<Doc> showUserCreateDoc(String id,int order,boolean desc){
		String sql = "SELECT * FROM Doc WHERE Deleted = 'false' AND CreaterId = '"+id+"'";
		String type = desc==true ? "DESC" : "";
		if(order==1) {
			sql += " ORDER BY DocChangeDate "+type;
		}
		else if(order==2) {
			sql += " ORDER BY DocCreateDate "+type;
		}
		else {
			sql += " ORDER BY DocChangeDate "+type;
		}
		ArrayList<Doc> res = new ArrayList<>();
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			while(rs.next()) {
				Doc d = new Doc(rs.getString("DocId"),rs.getString("CreaterId"),rs.getString("DocSrc"),rs.getDate("DocCreateDate"),rs.getDate("DocChangeDate"),
						rs.getString("DocLog"),rs.getBoolean("Deleted"),rs.getDate("DeleteDate"),rs.getString("BelongTo") );
				res.add(d);
			}
			rs.close();
			
			if(order==3) {
				Collections.sort(res, new Comparator<Doc>() {
					@Override
					public int compare(Doc d1,Doc d2) {
						return d1.getDocName().compareTo(d2.getDocName());
					}
				});
			}
			
			return res;
		} catch(Exception e) {
			return res;
		}
	}
	
	/**
	 * 查找用户删除的文件
	 * @param id 用户id
	 * @param order 排序方式：1 按照最后修改时间排序 2 按照创建时间排序 3 按照文件名排序
	 * @param desc 是否按照时间近的到远的排序 true 从近到远 false 从远到近
	 * @return 创建的文件列表
	 */
	public static ArrayList<Doc> showUserDeleteDoc(String id,int order,boolean desc){
		String sql = "SELECT * FROM Doc WHERE Deleted = 'true' AND CreaterId = '"+id+"'";
		String type = desc==true ? "DESC" : "";
		if(order==1) {
			sql += " ORDER BY DocChangeDate "+type;
		}
		else if(order==2) {
			sql += " ORDER BY DocCreateDate "+type;
		}
		else {
			sql += " ORDER BY DocChangeDate "+type;
		}
		ArrayList<Doc> res = new ArrayList<>();
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			while(rs.next()) {
				Doc d = new Doc(rs.getString("DocId"),rs.getString("CreaterId"),rs.getString("DocSrc"),rs.getDate("DocCreateDate"),rs.getDate("DocChangeDate"),
						rs.getString("DocLog"),rs.getBoolean("Deleted"),rs.getDate("DeleteDate"),rs.getString("BelongTo") );
				res.add(d);
			}
			rs.close();
			
			if(order==3) {
				Collections.sort(res, new Comparator<Doc>() {
					@Override
					public int compare(Doc d1,Doc d2) {
						return d1.getDocName().compareTo(d2.getDocName());
					}
				});
			}
			
			return res;
		} catch(Exception e) {
			return res;
		}
	}
	
	
	/**
	 * 查找用户参与的文件
	 * @param id 用户id
	 * @param order 排序方式：1 按照最后修改时间排序 2 按照创建时间排序 3 按照文件名排序
	 * @param desc 是否按照时间近的到远的排序 true 从近到远 false 从远到近
	 * @return 创建的文件列表
	 */
	public static ArrayList<Doc> showUserDesktopDoc(String id,int order,boolean desc){
		String sql = "SELECT * FROM Doc,ParticipateDoc WHERE Doc.Deleted = 'false' AND "
				+ "ParticipateDoc.UserId = '"+id+"' AND ParticipateDoc.DocId = Doc.DocId";
		String type = desc==true ? "DESC" : "";
		if(order==1) {
			sql += " ORDER BY DocChangeDate "+type;
		}
		else if(order==2) {
			sql += " ORDER BY DocCreateDate "+type;
		}
		else {
			sql += " ORDER BY DocChangeDate "+type;
		}		
		ArrayList<Doc> res = new ArrayList<>();
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			
			while(rs.next()) {
				Doc d = new Doc(rs.getString("DocId"),rs.getString("CreaterId"),rs.getString("DocSrc"),rs.getDate("DocCreateDate"),rs.getDate("DocChangeDate"),
						rs.getString("DocLog"),rs.getBoolean("Deleted"),rs.getDate("DeleteDate"),rs.getString("BelongTo") );
				res.add(d);
			}
			rs.close();
			
			if(order==3) {
				Collections.sort(res, new Comparator<Doc>() {
					@Override
					public int compare(Doc d1,Doc d2) {
						return d1.getDocName().compareTo(d2.getDocName());
					}
				});
			}
			
			return res;
		} catch(Exception e) {
			return res;
		}
	}
	
	/**
	 * 用户收藏文件
	 * @param userId 用户id
	 * @param docId 文档id
	 * @return 收藏结果
	 */
	public boolean setFavourite(String docId) {
		ArrayList<String> fav = DocUser.findUserById(userId).getFavouriteDoc();
		if(fav.contains(docId))
			return true;
		fav.add(docId);
		return DocUser.changeUserInfo(userId, "FavouriteDoc", DocUser.listToString(fav) );
	}
	/**
	 * 用户参与文件
	 * @param userId 用户id
	 * @param docId 文件id
	 * @return true/false 参与结果
	 */
	public static boolean participateDoc(String userId,String docId,String power) {
		return ParticipateDoc.addParticipateDoc(new ParticipateDoc(docId,userId,power));
	}
	
	/**
	 * 用户取消收藏
	 * @param user
	 * @param docId
	 * @return
	 */
	public static boolean cancelFavourite(DocUser user,String docId) {
		ArrayList<String> f = user.getFavouriteDoc();
		f.remove(docId);
		return DocUser.changeUserInfo(user.userId, "FavouriteDoc", listToString(f));
	}
	/**
	 * 用户最近浏览的
	 * @param user
	 * @param docId
	 * @return
	 */
	public static boolean setRecently(DocUser user,String docId) {
		ArrayList<String> fav = user.getRecentlyDoc();
		if(fav.contains(docId))
			fav.remove(docId);
		if(fav.size() > 20) {
			fav.remove(0);
		}
		fav.add(docId);
		return DocUser.changeUserInfo(user.userId, "RecentlyDoc", DocUser.listToString(fav) );
	}
	/**
	 * 从最近浏览的目录删除
	 * @param user
	 * @param docId
	 * @return
	 */
	public static boolean cancelRecently(DocUser user,String docId) {
		ArrayList<String> f = user.getRecentlyDoc();
		f.remove(docId);
		return DocUser.changeUserInfo(user.getUserId(), "RecentlyDoc", listToString(f));
	}
	
	
	/**
	 * 查找用户的消息记录并按时间排序
	 * @param userId
	 * @return
	 */
	public static ArrayList<Message> showUserMessage(String userId){
		return Message.findMessageByUserId(userId);
	}
	
	
	public static String listToString(ArrayList<String> al) {
		StringBuilder sd = new StringBuilder();
		for(String s:al) {
			sd.append(s);
			sd.append(DEV);
		}
		return sd.toString();
	}
	
	public static ArrayList<String> stringToList(String st){
		ArrayList<String> al = new ArrayList<String>();
		if(st==null) {
			return al;
		}
		String[] ss = st.split(DEV);
		for(String s:ss) {
			if(!s.equals("")) {
				al.add(s);
			}
		}
		return al;
	}
	
	public static String getRandomUserImage() {
		Random r = new Random();
		int i = r.nextInt(10);
		return userImagePath + "pic" + i + ".jpg";
	}
	
	// UserId,UserName,UserPassword,UserImage,RecentlyDoc,FavouriteDoc
	public String toTupleInString() {
		return "('" +userId+"','"+userName+"','"+userPassword+"','"+userImage+"','"+listToString(recentlyDoc)+"','"+listToString(favouriteDoc)+ "')";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public ArrayList<String> getRecentlyDoc() {
		return recentlyDoc;
	}

	public void setRecentlyDoc(ArrayList<String> recentlyDoc) {
		this.recentlyDoc = recentlyDoc;
	}

	public ArrayList<String> getFavouriteDoc() {
		return favouriteDoc;
	}

	public void setFavouriteDoc(ArrayList<String> favouriteDoc) {
		this.favouriteDoc = favouriteDoc;
	}
	
	
}
