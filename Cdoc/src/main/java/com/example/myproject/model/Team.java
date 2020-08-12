package com.example.myproject.model;

import java.util.Date;

public class Team {
	private String teamId;
	private String teamName;
	private String teamLeader;
	private Date teamCreateDate;
	
	public Team(String teamId,String teamName,String teamLeader,Date teamCreateDate) {
		this.teamId = teamId;
		this.teamName = teamName;
		this.teamLeader = teamLeader;
		this.teamCreateDate = teamCreateDate;
	}
}
