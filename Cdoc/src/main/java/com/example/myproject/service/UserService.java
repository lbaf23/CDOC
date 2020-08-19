package com.example.myproject.service;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.example.myproject.model.Doc;
import com.example.myproject.model.DocUser;
import com.example.myproject.model.ParticipateDoc;
import com.example.myproject.model.Repository;
import com.example.myproject.model.Team;

public class UserService {
	/**
	 * 获取某个用户收藏的文件
	 * @param userId 用户id
	 * @param order 排序方式：1 按照最后修改时间排序 2 按照创建时间排序 3 按照文件名排序
	 * @param desc 是否按照时间近的到远的排序 true 从近到远 false 从远到近
	 * @return 文件列表
	 */
	public static ArrayList<Doc> getUserFavouriteDoc(DocUser user,int order,boolean desc) throws Exception {
		ArrayList<Doc> res = new ArrayList<>();
		ArrayList<String> userFavourite = user.getFavouriteDoc();
		for(String str:userFavourite) {
			String sql = "SELECT * FROM Doc WHERE Deleted = 'false' AND DocId = '"+str+"'";
			try {
				ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
				if(rs.next()) {
					res.add(new Doc(rs.getString("DocId"), rs.getString("CreaterId"), rs.getString("DocSrc"),rs.getDate("DocCreateDate"),
						rs.getDate("DocChangeDate"),rs.getString("DocLog"), rs.getBoolean("Deleted"), rs.getDate("DeleteDate"), rs.getString("BelongTo")) );    
				}
				rs.close();
			} catch (Exception e) {
			}	
		}
		
		if(order == 1) {
			Collections.sort(res, new Comparator<Doc>() {
				@Override
				public int compare(Doc d1,Doc d2) {
					if(desc)
						return d2.getDocChangeDate().compareTo(d1.getDocChangeDate());
					else
						return d1.getDocChangeDate().compareTo(d2.getDocChangeDate());
				}
			});
		}
		else if(order == 2) {
			Collections.sort(res, new Comparator<Doc>() {
				@Override
				public int compare(Doc d1,Doc d2) {
					if(desc)
						return d2.getDocCreateDate().compareTo(d1.getDocCreateDate());
					else
						return d1.getDocCreateDate().compareTo(d2.getDocCreateDate());
				}
			});
		}
		else {
			Collections.sort(res, new Comparator<Doc>() {
				@Override
				public int compare(Doc d1,Doc d2) {
					if(desc)
						return d2.getDocName().compareTo(d1.getDocName());
					else
						return d1.getDocName().compareTo(d2.getDocName());
				}
			});
		}
		return res;
	}
	
	/**
	 * 获取某个用户最近的文件
	 * @param user 用户类
	 * @param order 排序方式：1 按照最后修改时间排序 2 按照创建时间排序 3 按照文件名排序
	 * @param desc 是否按照时间近的到远的排序 true 从近到远 false 从远到近
	 * @return 文件列表
	 */
	public static ArrayList<Doc> getUserRecentlyDoc(DocUser user,int order,boolean desc){
		ArrayList<Doc> res = new ArrayList<>();
		try{
			ArrayList<String> userRecently = user.getRecentlyDoc();
			for(String str:userRecently) {
				String sql = "SELECT * FROM Doc WHERE Deleted = 'false' AND DocId = '"+str+"'";
				 
				try {
					ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
					if(rs.next()) {
						res.add(new Doc(rs.getString("DocId"), rs.getString("CreaterId"), rs.getString("DocSrc"),rs.getDate("DocCreateDate"),
							rs.getDate("DocChangeDate"),rs.getString("DocLog"), rs.getBoolean("Deleted"), rs.getDate("DeleteDate"), rs.getString("BelongTo")) );    
					}
					rs.close();
				} catch (Exception e) {
				}	
			}		
			if(order == 1) {
				Collections.sort(res, new Comparator<Doc>() {
					@Override
					public int compare(Doc d1,Doc d2) {
						if(desc)
							return d2.getDocChangeDate().compareTo(d1.getDocChangeDate());
						else
							return d1.getDocChangeDate().compareTo(d2.getDocChangeDate());
					}
				});
			}
			else if(order == 2) {
				Collections.sort(res, new Comparator<Doc>() {
					@Override
					public int compare(Doc d1,Doc d2) {
						if(desc)
							return d2.getDocCreateDate().compareTo(d1.getDocCreateDate());
						else
							return d1.getDocCreateDate().compareTo(d2.getDocCreateDate());
					}
				});
			}
			else {
				Collections.sort(res, new Comparator<Doc>() {
					@Override
					public int compare(Doc d1,Doc d2) {
						if(desc)
							return d2.getDocName().compareTo(d1.getDocName());
						else
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
	 * 查找用户参加的队伍
	 * @param userId 用户id
	 * @param order 排序方式：1根据参加队伍时间排序 2:根据队伍创建时间排序
	 * @return
	 */
	public static ArrayList<Team> getUserTeam(String userId,int order,boolean desc){
		ArrayList<Team> res = new ArrayList<>();
		String sql = "SELECT * FROM Team, ParticipateTeam WHERE Team.UserId = '"+userId+
					"' Team.TeamId = ParticipateTeam.TeamId";
		String type = desc==true ? "DESC":"";
		if(order == 1) {
			sql += " ORDER BY ParticipateTeam.ParticipateDate "+type;
		}
		else {
			sql += " ORDER BY Team.TeamCreateDate "+type;
		}
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			while(rs.next()) {
				Team t = new Team(rs.getString("TeamId"),rs.getString("TeamName"),rs.getString("TeamLeader"),rs.getDate("TeamCreateDate"));
				res.add(t);
			}
			rs.close();
		} catch(Exception e) {
		}
		return res;
	}
	
	/**
	 * 用户删除文件
	 * @param du
	 * @param doc
	 * @return
	 */
	public static boolean userDeleteDoc(DocUser du,Doc doc) {
		if(doc.getCreaterId().equals(du.getUserId())) { // 文件创建者 删除文件 移至回收站
			if(Doc.delDoc(doc.getDocId())) {
				DocUser.cancelFavourite(du, doc.getDocId());
				DocUser.cancelRecently(du, doc.getDocId());
				return true;
			}
			else {
				return false;
			}
		}
		else { // 文件参与者 删除文件 退出文件参与
			ParticipateDoc p = ParticipateDoc.findParticipateByDocIdUserId(du.getUserId(), doc.getDocId());			
			if(ParticipateDoc.delParticipateDoc(p)) {
				DocUser.cancelFavourite(du, doc.getDocId());
				DocUser.cancelRecently(du, doc.getDocId());
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	/**
	 * 根据字段搜索用户
	 * @param value
	 * @param model 1 根据id搜搜 2 根据name搜索
	 * @param userId 排除的人id
	 * @return
	 */
	public static ArrayList<DocUser> searchUser(String value,int model,String userId){
		String sql = "SELECT * FROM DocUser";
		if(model == 1) {
			sql += " WHERE UserId != '"+userId+"' AND UserId LIKE '%"+value+"%'";
		}
		else {
			sql += " WHERE UserId != '"+userId+"' AND UserName LIKE '%"+value+"%'";
		}
		ArrayList<DocUser> res = new ArrayList<>();
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			while(rs.next()) {
				res.add(new DocUser(rs.getString("UserId"), rs.getString("UserName"), rs.getString("UserPassword"), 
						rs.getString("UserImage"), rs.getString("recentlyDoc"), rs.getString("FavouriteDoc")) );
			}
			rs.close();
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return res;
		}
	}
	
	/**
	 * 设置文件模板
	 * @param id
	 * @param d
	 * @return
	 */
	public static boolean setDocTemplate(String id,Doc d) {
		if(id.equals("0")) {
			return true;
		}
		String tp = Doc.tempPath + "t"+id+".doc";
		try{
			File f = new File(d.getDocSrc());
			if( f.exists() )
				f.delete();
			String content = FileOperate.getFileContent(tp,false);
			return DocService.saveDocFile(content, d, null);
		} catch(Exception e) {
			return false;
		}
	}
}
