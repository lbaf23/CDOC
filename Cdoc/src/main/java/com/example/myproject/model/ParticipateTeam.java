package com.example.myproject.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/*
 * PowerToTeam
 *  owner 拥有者(创建者) admin+解散团队
 *  admin管理员 邀请，踢出成员
 *  none普通成员 无权限
 */
public class ParticipateTeam {
    private String teamId;
    private String userId;
    private String powerToTeam;
    private Date participateDate;
    private boolean isLeader;
    private String powerToFile;

    public ParticipateTeam(String teamId, String userId, String powerToTeam, Date participateDate, boolean isLeader){
        this.isLeader = isLeader;
        this.participateDate = participateDate;
        this.powerToTeam = powerToTeam;
        this.teamId = teamId;
        this.userId = userId;
        this.powerToFile = "";
    }
    public ParticipateTeam(String teamId, String userId, String powerToTeam, Date participateDate, boolean isLeader, String powerToFile){
        this.isLeader = isLeader;
        this.participateDate = participateDate;
        this.powerToTeam = powerToTeam;
        this.teamId = teamId;
        this.userId = userId;
        this.powerToFile = powerToFile;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPowerToTeam() {
        return powerToTeam;
    }

    public void setPowerToTeam(String powerToTeam) {
        this.powerToTeam = powerToTeam;
    }

    public Date getParticipateDate() {
        return participateDate;
    }

    public void setParticipateDate(Date participateDate) {
        this.participateDate = participateDate;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeader(boolean leader) {
        isLeader = leader;
    }
    
    

    public static boolean addParticipateTeam(ParticipateTeam p) {
        String sql = "INSERT INTO ParticipateTeam(TeamId,UserId,PowerToTeam,ParticipateDate,IsLeader,PowerToFile) "
                + "VALUES"+p.toTupleInString();
        return Repository.getInstance().doSqlUpdateStatement(sql);
    }
    

    /**
     * 根据用户id和队伍id查找参加队伍的记录
     * @param uid
     * @param tid
     * @return
     */
    public static ParticipateTeam findTeamByUserId(String uid, String tid) {
        String sql = "SELECT * FROM ParticipateTeam WHERE UserId = '" + uid + "'" + "AND TeamId = '" + tid + "'";
        try {
            ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
            if (rs.next()) {
            	ParticipateTeam res = new ParticipateTeam(rs.getString("TeamId"),rs.getString("UserId"), rs.getString("PowerToTeam"), rs.getDate("ParticipateDate"), rs.getBoolean("IsLeader"));
                rs.close();
                return res;
            }
            else {
            	return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    
    public static boolean changeParticipateInfo(String userId, String teamId, String column, String value) {
        String sql = "UPDATE ParticipateTeam SET "+column+" = '"+value+"' WHERE UserId = '"+userId+"'" + " AND TeamId = '" + teamId + "'";
        return Repository.getInstance().doSqlUpdateStatement(sql);
    }
    

    /**
     * 找到用户参与的所有队伍
     * @param uid 用户Id
     * @param order 1参加队伍时间排序 2队伍创建时间排序
     * @return
     */
    public static ArrayList<Team> findUserTeams(String uid,int order) {
        String sql = "SELECT * FROM Team,ParticipateTeam WHERE UserId = '" + uid + "' AND Team.TeamId = ParticipateTeam.TeamId";
        if(order==1) {
        	sql += " ORDER BY ParticipateTeam.ParticipateDate DESC";
        }
        else {
        	sql += " ORDER BY Team.TeamCreateDate DESC";
        }
        ArrayList<Team> res = new ArrayList<>();
        try {
            ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
            while (rs.next()) {
                Team t = new Team(rs.getString("TeamId"),rs.getString("TeamName"), rs.getString("TeamLeader"), rs.getDate("TeamCreateDate"));
                res.add(t);
            }
            System.out.println("res"+res.size());
            rs.close();
        } catch (Exception e) {
            return res;
        }
        return res;
    }
    
    /**
     * 根据队伍id查找队伍
     * @param tid
     * @return
     */
    public static ArrayList<ParticipateTeam> findTeamByTeamId(String tid) {
        String sql = "SELECT * FROM Team WHERE TeamId = '" + tid + "'";
        ArrayList<ParticipateTeam> res = new ArrayList<>();
        try {
            ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
            while (rs.next()) {
                ParticipateTeam p = new ParticipateTeam(rs.getString("TeamId"),rs.getString("UserId"), 
                		rs.getString("PowerToTeam"), rs.getDate("ParticipateTime"), rs.getBoolean("IsLeader"));
                res.add(p);
            }
            rs.close();
        } catch (Exception e) {
            return null;
        }
        if (res.size() == 0)
            return null;
        return res;
    }

    public static boolean changeParticipateInfo(String userId, String column, String value) {
        String sql = "UPDATE UserId SET "+column+" = "+value+" WHERE UserId = '"+userId+"'";
        return Repository.getInstance().doSqlUpdateStatement(sql);
    }

    /**
     * 删除队伍中某个人
     * @param userId
     * @param teamId
     * @return
     */
    public static boolean delParticipationThorough(String userId, String teamId) {
        String sql = "DELETE FROM ParticipateTeam WHERE UserId = '"
                + userId+"'" + " AND TeamId = '" + teamId + "'";
        return Repository.getInstance().doSqlUpdateStatement(sql);
    }

    public String toTupleInString() {
        return "('" +teamId+"','"+userId+"','"+powerToTeam+"','"+new Timestamp(participateDate.getTime())+"','"+isLeader+"','"+powerToFile+"')";
    }
	public String getPowerToFile() {
		return powerToFile;
	}
	public void setPowerToFile(String powerToFile) {
		this.powerToFile = powerToFile;
	}
}
