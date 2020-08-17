package com.example.myproject.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Team {
    private String teamId;
    private String teamName;
    private String teamLeader;
    private Date teamCreateDate;

    public Team(String teamId, String teamName, String teamLeader, Date teamCreateDate) {
        this.teamId = teamId;
        this.teamLeader = teamLeader;
        this.teamName = teamName;
        this.teamCreateDate = teamCreateDate;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(String teamLeader) {
        this.teamLeader = teamLeader;
    }

    public Date getTeamCreateDate() {
        return teamCreateDate;
    }

    public void setTeamCreateDate(Date teamCreateDate) {
        this.teamCreateDate = teamCreateDate;
    }

    public static boolean addTeam(Team team) {
        String sql = "INSERT INTO Team(TeamId,TeamName,TeamLeader,TeamCreateDate) "
                + "VALUES"+team.toTupleInString();
        return Repository.getInstance().doSqlUpdateStatement(sql);
    }

    public static Team findTeamByTeamId(String id) {
        String sql = "SELECT * FROM Team WHERE TeamId = '" + id + "'";
        try {
            ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
            if (rs.next()) {
                Team t = new Team(rs.getString("TeamId"), rs.getString("TeamName"), rs.getString("TeamLeader"), rs.getDate("TeamCreateDate"));
                rs.close();
                return t;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
    /**
	 * 获取下一条id
	 * @return
	 */
	public static String getNextId() {
		try {
			ResultSet rs = Repository.getInstance().doSqlSelectStatement("SELECT TOP 1 TeamId FROM Team " + 
					"ORDER BY convert(int,TeamId) DESC;");
			if(rs.next()) {
				int index = Integer.parseInt(rs.getString("TeamId"));
				rs.close();
				return String.valueOf(index + 1);
			}
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
    
    public static boolean changeTeamInfo(String teamId, String column, String value) {
        String sql = "UPDATE TeamId SET "+column+" = "+value+" WHERE TeamId = '"+teamId+"'";
        return Repository.getInstance().doSqlUpdateStatement(sql);
    }

    /**
     * 删除队伍
     * @param d
     * @return
     */
    public static boolean delTeamThorough(String d) {
        String sql = "DELETE FROM Team WHERE TeamId ='"
                + d+"'";
        return Repository.getInstance().doSqlUpdateStatement(sql);
    }

    public String toTupleInString() {
        return "('" +teamId+"','"+teamName+"','"+teamLeader+"','"+new Timestamp(teamCreateDate.getTime())+"')";
    }
}
