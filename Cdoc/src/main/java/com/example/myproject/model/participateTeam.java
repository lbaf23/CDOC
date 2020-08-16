package com.example.myproject.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class participateTeam {
    private String teamId;
    private String userId;
    private String powerToTeam;
    private Date participateDate;
    private boolean isLeader;

    public participateTeam(String teamId, String userId, String powerToTeam, Date participateDate, boolean isLeader){
        this.isLeader = isLeader;
        this.participateDate = participateDate;
        this.powerToTeam = powerToTeam;
        this.teamId = teamId;
        this.userId = userId;
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

    public static boolean addParticipateTeam(participateTeam p) {
        String sql = "INSERT INTO Team(TeamId,UserId,PowerToTeam,ParticipateDate,IsLeader) "
                + "VALUES"+p.toTupleInString();
        return Repository.getInstance().doSqlUpdateStatement(sql);
    }
    

    public static participateTeam findTeamByUserId(String uid, String tid) {
        String sql = "SELECT * FROM Team WHERE UserId = '" + uid + "'" + "AND TeamId = '" + tid + "'";
        ArrayList<participateTeam> res = new ArrayList<>();
        try {
            ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
            while (rs.next()) {
                participateTeam p = new participateTeam(rs.getString("TeamId"),rs.getString("UserId"), rs.getString("PowerToTeam"), rs.getDate("ParticipateTime"), rs.getBoolean("IsLeader"));
                res.add(p);
            }
            rs.close();
        } catch (Exception e) {
            return null;
        }
        if (res.size() == 0)
            return null;
        return res.get(0);
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
            rs.close();
        } catch (Exception e) {
            return res;
        }
        return res;
    }
    
    public static ArrayList<participateTeam> findTeamByTeamId(String tid) {
        String sql = "SELECT * FROM Team WHERE TeamId = '" + tid + "'";
        ArrayList<participateTeam> res = new ArrayList<>();
        try {
            ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
            while (rs.next()) {
                participateTeam p = new participateTeam(rs.getString("TeamId"),rs.getString("UserId"), rs.getString("PowerToTeam"), rs.getDate("ParticipateTime"), rs.getBoolean("IsLeader"));
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

    public static boolean delParticipationThorough(String d, String e) {
        String sql = "DELETE FROM ParticipateTeam WHERE UserId ='"
                + d+"'" + "AND TeamId = '" + e + "'";
        return Repository.getInstance().doSqlUpdateStatement(sql);
    }

    public static boolean delParticipateThorough(String d) {
        String sql = "DELETE FROM ParticipateTeam WHERE TeamId ='"
                + d+"'";
        return Repository.getInstance().doSqlUpdateStatement(sql);
    }

    public String toTupleInString() {
        return "('" +teamId+"','"+userId+"','"+powerToTeam+"','"+participateDate+"','"+isLeader+"')";
    }
}
