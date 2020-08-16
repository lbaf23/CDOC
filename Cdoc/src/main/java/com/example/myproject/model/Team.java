package com.example.myproject.model;

import java.sql.ResultSet;
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
        ArrayList<Team> res = new ArrayList<>();
        try {
            ResultSet rs = Repository.getInstance().doSqlSelectStatement(sql);
            while (rs.next()) {
                Team t = new Team(rs.getString("TeamId"), rs.getString("TeamName"), rs.getString("TeamLeader"), rs.getDate("TeamCreateDate"));
                res.add(t);
            }
            rs.close();
        } catch (Exception e) {
            return null;
        }
        if (res.size() == 0)
            return null;
        return res.get(0);
    }

    public static boolean changeTeamInfo(String teamId, String column, String value) {
        String sql = "UPDATE TeamId SET "+column+" = "+value+" WHERE TeamId = '"+teamId+"'";
        return Repository.getInstance().doSqlUpdateStatement(sql);
    }

    public static boolean delTeamThorough(String d) {
        String sql = "DELETE FROM Team WHERE TeamId ='"
                + d+"'";
        return Repository.getInstance().doSqlUpdateStatement(sql);
    }

    public String toTupleInString() {
        return "('" +teamId+"','"+teamName+"','"+teamLeader+"','"+teamCreateDate+"')";
    }
}
