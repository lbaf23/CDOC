package com.example.myproject.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.myproject.model.Team;
import com.example.myproject.model.Doc;
import com.example.myproject.model.DocUser;
import com.example.myproject.model.Message;
import com.example.myproject.model.ParticipateDoc;
import com.example.myproject.model.ParticipateTeam;
import com.example.myproject.service.DateOperate;
import com.example.myproject.service.FileOperate;
import com.example.myproject.service.JavaMail;
import com.example.myproject.service.TeamService;
import com.example.myproject.service.UserService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

@RestController
public class TeamController {
	
	// 展示的队伍
	class TeamForShow{
		String teamId;
		String teamLeaderName;
		String teamLeaderId;
		Date teamCreateDate;
		public TeamForShow(Team t) {
			this.teamId = t.getTeamId();
			DocUser du = DocUser.findUserById(t.getTeamLeader());
			this.teamLeaderId = du.getUserId();
			this.teamLeaderName = du.getUserName();
			this.teamCreateDate = t.getTeamCreateDate();
		}
		public String getTeamId() {
			return teamId;
		}
		public void setTeamId(String teamId) {
			this.teamId = teamId;
		}
		public String getTeamLeaderName() {
			return teamLeaderName;
		}
		public void setTeamLeaderName(String teamLeaderName) {
			this.teamLeaderName = teamLeaderName;
		}
		public String getTeamLeaderId() {
			return teamLeaderId;
		}
		public void setTeamLeaderId(String teamLeaderId) {
			this.teamLeaderId = teamLeaderId;
		}
		public Date getTeamCreateDate() {
			return teamCreateDate;
		}
		public void setTeamCreateDate(Date teamCreateDate) {
			this.teamCreateDate = teamCreateDate;
		}
		
	}
	// 展示的队员
	class MemberForShow{
		String userName;
		Date participateDate;
		String userId;
		String powerToTeam;
		String textPower;
		public MemberForShow(DocUser du,String teamId) {
			this.userName = du.getUserName();
			this.userId = du.getUserId();
			ParticipateTeam p = ParticipateTeam.findTeamByUserId(du.getUserId(), teamId);
			this.participateDate = p.getParticipateDate();
			if(p.getPowerToTeam().equals("owner")) {
				this.powerToTeam = "创建者";
				this.textPower = "可管理";
			}
			else if(p.getPowerToTeam().equals("admin")) {
				this.powerToTeam = "管理员";
				this.textPower = "可管理";
			}
			else {
				this.powerToTeam = "成员";
				this.textPower = "可编辑";
			}
			
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public Date getParticipateDate() {
			return participateDate;
		}
		public void setParticipateDate(Date participateDate) {
			this.participateDate = participateDate;
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
		public String getTextPower() {
			return textPower;
		}
		public void setTextPower(String textPower) {
			this.textPower = textPower;
		}
		
	}
	class DocForShow{
		String textName;
		String founder;
		Date updateDate;
		public DocForShow(Doc d) {
			this.textName = d.getDocName();
			DocUser u = DocUser.findUserById(d.getCreaterId());
			this.founder = u.getUserName();
			this.updateDate = d.getDocChangeDate();
		}
		public String getTextName() {
			return textName;
		}
		public void setTextName(String textName) {
			this.textName = textName;
		}
		public String getFounder() {
			return founder;
		}
		public void setFounder(String founder) {
			this.founder = founder;
		}
		public Date getUpdateDate() {
			return updateDate;
		}
		public void setUpdateDate(Date updateDate) {
			this.updateDate = updateDate;
		}
		
	}
	/*
	 * 返回的类
	 */
	class TeamInfo{
		TeamForShow teamData;
		ArrayList<MemberForShow> teamMember;
		ArrayList<DocForShow> teamFile;
		String userTeamPower;
		String userFilePower;
		public TeamForShow getTeamData() {
			return teamData;
		}
		public void setTeamData(TeamForShow teamData) {
			this.teamData = teamData;
		}
		public ArrayList<MemberForShow> getTeamMember() {
			return teamMember;
		}
		public void setTeamMember(ArrayList<MemberForShow> teamMember) {
			this.teamMember = teamMember;
		}
		public ArrayList<DocForShow> getTeamFile() {
			return teamFile;
		}
		public void setTeamFile(ArrayList<DocForShow> teamFile) {
			this.teamFile = teamFile;
		}
		public String getUserTeamPower() {
			return userTeamPower;
		}
		public void setUserTeamPower(String userTeamPower) {
			this.userTeamPower = userTeamPower;
		}
		public String getUserFilePower() {
			return userFilePower;
		}
		public void setUserFilePower(String userFilePower) {
			this.userFilePower = userFilePower;
		}
		
	}
	/**
	 * 团队页面加载的数据
	 * @param teamId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/showTeamInfo")
    @ResponseBody
    @CrossOrigin
    public Object showTeamInfo(@RequestParam("teamId") String teamId,
                             @RequestParam("userId") String userId){
		
		System.out.println("t:"+teamId+",u:"+userId);
		
		Ret r = new Ret();
		Team team = Team.findTeamByTeamId(teamId);
		TeamInfo t = new TeamInfo();
		t.setTeamData(new TeamForShow(team));
		ArrayList<Doc> fileList = TeamService.getTeamFiles(teamId);
		ArrayList<DocForShow> teamFile = new ArrayList<>();
		for(Doc dc:fileList) {
			teamFile.add(new DocForShow(dc));
		}
		System.out.println(teamFile);
		t.setTeamFile(teamFile);
		ParticipateTeam pt = ParticipateTeam.findTeamByUserId(userId, teamId);		
		t.setUserTeamPower(pt.getPowerToTeam());
		
		ArrayList<DocUser> members = TeamService.getTeamMembers(teamId);
		ArrayList<MemberForShow> mlist = new ArrayList<>();
				
		for(DocUser u:members) {
			mlist.add(new MemberForShow(u,teamId));
		}
				
		t.setTeamMember(mlist);
		
		t.setUserFilePower(pt.getPowerToFile());
		r.setResult(t);
		r.setSuccess(true);
		return r;
	}
	
    /**
     * 创建团队
     * @param teamName
     * @param userId
     * @return
     */
    @RequestMapping(value = "/CreateTeam")
    @ResponseBody
    @CrossOrigin
    public Object CreateTeam(@RequestParam("teamName") String teamName,
                             @RequestParam("userId") String userId){
        Ret r = new Ret();
        Random random = new Random();
        String teamId = "";
        do{
            teamId = null;
            for(int i = 0; i < 7; ++ i){
                teamId += random.nextInt(10);
            }
        }while(Team.findTeamByTeamId(teamId) != null);
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        if(Team.addTeam(new Team(teamId, teamName, userId, date)) &&
                ParticipateTeam.addParticipateTeam(new ParticipateTeam(teamId, userId, "owner", date, true)) ){
            r.setSuccess(true);
            r.setMessage("注册成功。");
            return r;
        }
        r.setSuccess(false);
        r.setMessage("注册失败。");
        return r;
    }
    
    

    /**
     * 邀请成员
     * @param userId
     * @param teamId
     * @return
     */
    @RequestMapping(value = "/teamInvite")
    @ResponseBody
    @CrossOrigin
    public Object Invite(@RequestParam("userId") String userId,
    				@RequestParam("objId") String objId,
                    @RequestParam("teamId") String teamId){
        Ret r = new Ret();
        DocUser docUser = DocUser.findUserById(objId);
        Team t = Team.findTeamByTeamId(teamId);
        if(docUser == null){
            r.setSuccess(false);
            r.setMessage("用户不存在");
            return r;
        }
        ParticipateTeam d = ParticipateTeam.findTeamByUserId(userId, teamId);
        if(d != null){
            r.setSuccess(false);
            r.setMessage("成员已在团队中。");
            return r;
        }
        if(Message.addMessage(new Message(Message.getNextId(),userId,objId,"用户"+userId+"邀请您加入团队："
        		+t.getTeamName()+ "。",new Date(),false,"2",teamId,"-1"))) {
            r.setSuccess(true);
            r.setMessage("发送成功。");
            return r;
        }
        r.setSuccess(false);
        r.setMessage("邀请失败。");
        return r;
    }

    /**
     * 团队成员
     * @param teamId
     * @return
     */
    @RequestMapping(value = "/GetParticipation")
    @ResponseBody
    @CrossOrigin
    public Object GetParticipation(@RequestParam("teamId") String teamId){
        ArrayList<ParticipateTeam> res = new ArrayList<>();
        res = ParticipateTeam.findTeamByTeamId(teamId);
        return res;
    }

    /**
     * 团队文档
     * @param belong 团队Id
     * @return
     */
    @RequestMapping(value = "/GetDoc")
    @ResponseBody
    @CrossOrigin
    public Object GetDoc(@RequestParam("belong") String belong){
        ArrayList<Doc> res = new ArrayList<Doc>();
        res = Doc.findAllDocByBelongTo(belong);
        return res;
    }

    /**
     * 修改团队权限
     * @param type
     * @return
     */
    @RequestMapping(value = "/changeTeamPower")
    @ResponseBody
    @CrossOrigin
    public Object changeTeamPower(@RequestParam("type") Integer type,
                                  @RequestParam("userId") String userId,
                                  @RequestParam("teamId") String teamId){
        Ret r = new Ret();
        
        if(type.equals(1)){
        	if(ParticipateTeam.changeParticipateInfo(userId, teamId, "PowerToTeam", "admin")) {
        		r.setMessage("修改成功");
        		r.setSuccess(true);
        	}
        	else {
        		r.setMessage("修改失败");
        		r.setSuccess(false);
        	}
        }
        else{
        	if(ParticipateTeam.changeParticipateInfo(userId, teamId, "PowerToTeam", "none")) {
        		r.setMessage("修改成功");
        		r.setSuccess(true);
        	}
        	else {
        		r.setMessage("修改失败");
        		r.setSuccess(false);
        	}
        }
        return r;
    }

    /**
     * 修改文档权限
     * @param type 1 admin 2 none
     * @param userId
     * @param teamId
     * @return
     */
    @RequestMapping(value = "/changeTextPower")
    @ResponseBody
    @CrossOrigin
    public Object changeTextPower(@RequestParam("type") String power,
                                  @RequestParam("userId") String userId,
                                  @RequestParam("teamId") String teamId){
        Ret r = new Ret();
        if(TeamService.setTeamMemberFilePower(teamId, userId, power)) {
        	r.setSuccess(true);
        	r.setMessage("修改成功");
        }
        else {
        	r.setSuccess(false);
        	r.setMessage("修改失败");
        }
        return r;
    }

    /**
     * 删除队伍
     * @param teamId
     * @return
     */
    @RequestMapping(value = "/delTeam")
    @ResponseBody
    @CrossOrigin
    public Object DelTeam(@RequestParam("teamId") String teamId) {
        Ret r = new Ret();
        if(TeamService.deleteTeam(teamId)) {
            r.setSuccess(true);
            r.setMessage("删除成功");
        }
        else {
            r.setSuccess(false);
            r.setMessage("删除失败");
        }
        return r;
    }

    /**
     * 删除成员
     * @param teamId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/delParticipateTeam")
    @ResponseBody
    @CrossOrigin
    public Ret DelParticipateTeam(@RequestParam("teamId") String teamId,
                                  @RequestParam(value="userId",required=false) String userId,
                                  @RequestParam("objId") String objId) {
        Ret r = new Ret();
        Team t = Team.findTeamByTeamId(teamId);
        if(userId==null) {
        	if(ParticipateTeam.delParticipationThorough(objId, teamId)) {
        		Message.addMessage(new Message(Message.getNextId(),objId,objId,
        				userId+"已退出团队："+t.getTeamName(),new Date(), false,"6",teamId,"-1") );
        		r.setSuccess(true);
        		r.setMessage("退出成功");
        	}
        	else {
        		r.setSuccess(false);
        		r.setMessage("退出失败");
        	}
        }
        else {
        	if(ParticipateTeam.delParticipationThorough(objId, teamId)) {
        		Message.addMessage(new Message(Message.getNextId(),userId,objId,
        				userId+"将您踢出团队："+t.getTeamName(),new Date(), false,"4",teamId,"-1") );
        		r.setSuccess(true);
        		r.setMessage("移除成功");
        	}
        	else {
        		r.setSuccess(false);
        		r.setMessage("移除失败");
        	}
        }
        return r;
    }
}
