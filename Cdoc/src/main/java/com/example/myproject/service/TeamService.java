package com.example.myproject.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import com.example.myproject.model.Doc;
import com.example.myproject.model.DocUser;
import com.example.myproject.model.ParticipateDoc;
import com.example.myproject.model.ParticipateTeam;
import com.example.myproject.model.Repository;
import com.example.myproject.model.Team;

public class TeamService {
	
	/**
	 * 修改团队中某人对于团队中文件的权限
	 * @param teamId
	 * @param userId
	 * @param power 1 read 2 write
	 * @return
	 */
	public static boolean setTeamMemberFilePower(String teamId,String userId,String power) {
		String p = "read";
		if(power.equals("2")) {
			p = "write";
		}
		//String sqll = "UPDATE ParticipateDoc SET PowerToDoc ='"+p+"' WHERE UserId = '"+userId+"'";
		String sql = "UPDATE ParticipateTeam SET PowerToFile = '"+p+"' WHERE UserId = '"+userId+"'";
		return Repository.getInstance().doSqlUpdateStatement(sql);
	}
	
	/**
	 * 给团队加入一个文件
	 * @param teamId
	 * @param docId
	 * @return
	 */
	/*
	public static boolean addFileToTeamMembers(String teamId,String docId) {
		ArrayList<DocUser> users = TeamService.getTeamMembers(teamId);
		for(DocUser du:users) {
			ParticipateTeam p = ParticipateTeam.findTeamByUserId(du.getUserId(), teamId);
			ParticipateDoc.addParticipateDoc(new ParticipateDoc(docId,du.getUserId(),p.getPowerToFile()));
		}
		return true;
	}
	*/
	/**
	 * 找到团队的所有文件
	 * @param teamId
	 * @return
	 */
	public static ArrayList<Doc> getTeamFiles(String teamId){
		String sql = "SELECT * FROM Doc WHERE Deleted = 'false' AND Doc.BelongTo = '"+teamId+"' ORDER BY DocChangeDate DESC";
		ArrayList<Doc> res =  new ArrayList<>();
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			while(rs.next()) {
				res.add(new Doc(rs.getString("DocId"), rs.getString("CreaterId"), rs.getString("DocSrc"),rs.getDate("DocCreateDate"),
						rs.getDate("DocChangeDate"),rs.getString("DocLog"), rs.getBoolean("Deleted"), rs.getDate("DeleteDate"), rs.getString("BelongTo")) );
			}
			rs.close();
			return res;
		} catch(Exception e) {
			return res;
		}
	}
	
	/**
	 * 参加队伍的所有用户
	 * @param teamId
	 * @return
	 */
	public static ArrayList<DocUser> getTeamMembers(String teamId){
		String sql = "SELECT * FROM DocUser,ParticipateTeam WHERE"
				+ " ParticipateTeam.TeamId = '"+teamId+"' AND ParticipateTeam.UserId = DocUser.UserId";
		ArrayList<DocUser> res =  new ArrayList<>();
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
			while(rs.next()) {
				res.add(new DocUser(rs.getString("UserId"), rs.getString("UserName"), rs.getString("UserPassword"), 
						rs.getString("UserImage"), rs.getString("recentlyDoc"), rs.getString("FavouriteDoc")) );
			}
			rs.close();
			return res;
		} catch(Exception e) {
			return res;
		}
	}
	/**
	 * 删除队伍
	 * @param teamId
	 * @return
	 */
	public static boolean deleteTeam(String teamId) {
		String sql = "UPDATE Doc SET BelongTo = null WHERE BelongTo = '"+teamId+"'";
		return Repository.getInstance().doSqlUpdateStatement(sql) && Team.delTeamThorough(teamId);
	}
	/**
	 * 将成员加入团队
	 * @param userId
	 * @param teamId
	 * @return
	 */
	public static boolean addMemberToTeam(String userId,String teamId) {
		boolean res = ParticipateTeam.addParticipateTeam( new ParticipateTeam(teamId,userId,"none", new Date(), false,"read"));
	    res  = res && TeamService.setTeamMemberFilePower(teamId, userId, "1");
	    return res;
	}
	/**
	 * 删除团队中的文件
	 * @param d
	 * @return
	 */
	public static boolean deleteFileFromTeam(Doc d) {
		return Doc.updateDoc(d.getDocId(), "BelongTo","-1");
	}
}
