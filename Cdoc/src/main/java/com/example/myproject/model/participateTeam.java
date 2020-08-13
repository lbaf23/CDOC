package com.example.myproject.model;

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

    public String toTupleInString() {
        return "('" +teamId+"','"+userId+"','"+powerToTeam+"','"+participateDate+"','"+isLeader+"')";
    }
}
