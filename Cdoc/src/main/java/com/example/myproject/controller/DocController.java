package com.example.myproject.controller;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.myproject.model.Comment;
import com.example.myproject.model.Doc;
import com.example.myproject.model.DocUser;
import com.example.myproject.model.Message;
import com.example.myproject.model.ParticipateDoc;
import com.example.myproject.model.ParticipateTeam;
import com.example.myproject.model.Repository;
import com.example.myproject.model.Team;
import com.example.myproject.service.DateOperate;
import com.example.myproject.service.DocService;
import com.example.myproject.service.FileOperate;
import com.example.myproject.service.JavaMail;
import com.example.myproject.service.TeamService;
@RestController
public class DocController {
	/**
	 * 用户保存 
	 * @param value
	 * @param userId
	 * @param docId
	 * @return
	 */
	@RequestMapping(value = "/docSave")
	@ResponseBody
	@CrossOrigin
	public Object docSave(@RequestParam("value") String value,
			@RequestParam("userId") String userId,
			@RequestParam("docId") String docId) {
		
		Ret r = new Ret();
		if(value.equals("<p></p>")) {
			return r;
		}
		try {
			Doc d = Doc.findDocByDocId(docId);
			Doc.updateDocTime(docId);
			if(DocService.saveDocFile(value,d,userId)) {
				r.setSuccess(true);
				setdocLog(userId,docId);
			}
			else {
				r.setSuccess(false);
			}
		} catch(Exception e) {
			r.setSuccess(false);
		}
		return r;
	}
	/**
	 * 记录log
	 * @param userId
	 * @param docId
	 * @return
	 */
	public static boolean setdocLog(String userId, String docId) {
		DocUser.setRecently(DocUser.findUserById(userId), docId);
		return DocService.saveLog(Doc.findDocByDocId(docId), userId);
	}
	
	class DocInfo{
		String docId;
		String docName;
		String authorName;
		String authorId;
		ArrayList<String> cooperatorId;
		public String getDocId() {
			return docId;
		}
		public void setDocId(String docId) {
			this.docId = docId;
		}
		public String getDocName() {
			return docName;
		}
		public void setDocName(String docName) {
			this.docName = docName;
		}
		public String getAuthorName() {
			return authorName;
		}
		public void setAuthorName(String authorName) {
			this.authorName = authorName;
		}
		public String getAuthorId() {
			return authorId;
		}
		public void setAuthorId(String authorId) {
			this.authorId = authorId;
		}
		public ArrayList<String> getCooperatorId() {
			return cooperatorId;
		}
		public void setCooperatorId(ArrayList<String> cooperatorId) {
			this.cooperatorId = cooperatorId;
		}
		
	}
	/**
	 * 获取文件信息
	 * @param docId
	 * @return
	 */
	@RequestMapping(value = "/getdocInfo")
	@ResponseBody
	@CrossOrigin
	public Object getdocInfo(@RequestParam("docId") String docId) {
		Ret r = new Ret();
		if(docId==null)
			return r;
		DocInfo di = new DocInfo();
		
		Doc d = Doc.findDocByDocId(docId);
		DocUser du = DocUser.findUserById(d.getCreaterId());
		di.setDocId(docId);
		di.setDocName(d.getDocName());
		di.setAuthorId(du.getUserId());
		di.setAuthorName(du.getUserName());
		try {
			di.setCooperatorId(ParticipateDoc.findUserIdByDocId(docId));
		} catch (Exception e) {
		}
		r.setResult(di);
		r.setSuccess(true);
		return r;
	}
	/**
	 * 获取文件log
	 * @param docId
	 * @return
	 */
	@RequestMapping(value = "/getdocLogInfo")
	@ResponseBody
	@CrossOrigin
	public Object getdocLogInfo(@RequestParam("docId") String docId) {
		Doc d = Doc.findDocByDocId(docId);
		Ret r = new Ret();
		try {
			String res = FileOperate.getFileContent(d.getDocLog(),true);
			r.setSuccess(true);
			r.setResult(res.substring(res.indexOf(":")+1));
			r.setMessage(res.substring(0,res.indexOf(":")));
		} catch (Exception e) {
			r.setSuccess(false);
		}
		return r;
	}
	
	
	/**
	 * 用户评论
	 * @param userId
	 * @param docId
	 * @param content
	 * @return
	 */
	@RequestMapping(value = "/submitComment")
	@ResponseBody
	@CrossOrigin
	public Object submitComment(@RequestParam("userId") String userId,
			@RequestParam("docId") String docId,
			@RequestParam("content") String content) {
		Ret r = new Ret();
		Comment c=new Comment(Comment.getNextId(),docId,userId,content,DateOperate.nowDate());
		Comment.addComment(c);
		Doc d=Doc.findDocByDocId(docId);
		String messageContent=userId+"给你的文档"+docId+"评论了";
		Message m=new Message(Message.getNextId(),userId,d.getCreaterId(),messageContent,DateOperate.nowDate(),false,"3","-1",docId);
		Message.addMessage(m);
		r.setSuccess(true);
		r.setMessage("评论成功！");
		return r;
	}
	/**
	 * 删除评论
	 * @param commentId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/deleteComment")
	@ResponseBody
	@CrossOrigin
	public Object deleteComment(@RequestParam("commentId") String commentId) throws Exception {
		Ret r = new Ret();
		Comment c=Comment.findCommentByCommentId(commentId);
		Comment.deleteComment(c);
		r.setSuccess(true);
		r.setMessage("删除成功！");
		return r;
	}
	/**
	 * 加载作者
	 * @param docId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/loadauthor")
	@ResponseBody
	@CrossOrigin
	public Object loadauthor(@RequestParam("docId") String docId) throws Exception {
		Ret r = new Ret();
		Doc d=Doc.findDocByDocId(docId);
		r.setResult(d.getCreaterId());
		r.setSuccess(true);
		r.setMessage("");
		return r;
	}
	/**
	 * 加载评论
	 * @param docId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/initialComment")
	@ResponseBody
	@CrossOrigin
	public Object initialComment(@RequestParam("docId") String docId) throws Exception {
		DocUser d;
		ArrayList<Comment> c=Comment.findCommentByDocId(docId);
		ArrayList<CommentForShow> res=new ArrayList<>();
		for(int i=0;i<c.size();i++) {
			d=DocUser.findUserById(c.get(i).getUserId());
			res.add(new CommentForShow(FileOperate.getBase64File(d.getUserImage()),d.getUserName(),c.get(i).getContent(),d.getUserId(),c.get(i).getCommentId()));
		}
		return res;
		
	}
	class CommentForShow{
		String userhead,username,comments,useremail,commentid;
		CommentForShow(String a,String b,String c,String d,String e){
			this.userhead=a;
			this.username=b;
			this.comments=c;
			this.useremail=d;
			this.commentid=e;
		}
		public String getUserhead() {
			return userhead;
		}
		public void setUserhead(String userhead) {
			this.userhead = userhead;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getComments() {
			return comments;
		}
		public void setComments(String comments) {
			this.comments = comments;
		}
		public String getUseremail() {
			return useremail;
		}
		public void setUseremail(String useremail) {
			this.useremail = useremail;
		}
		public String getCommentid() {
			return commentid;
		}
		public void setCommentid(String commentid) {
			this.commentid = commentid;
		}
	}
	/**
	 * 加载消息
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/initialMessage")
	@ResponseBody
	@CrossOrigin
	public Object initialMessage(@RequestParam("userId") String userId) throws Exception {
		ArrayList<Message> m1=Message.findReadMessageByObjId(userId);
		ArrayList<Message> m2=Message.findUnReadMessageByObjId(userId);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<MessageForShow> r1=new ArrayList<>();
		ArrayList<MessageForShow> r2=new ArrayList<>();
		MessageRet r = new MessageRet();
		String p="";
		for(int i=0;i<m1.size();i++) {
			if(m1.get(i).getMessageType().equals("1")) {
				char ch[]=m1.get(i).getMessageContent().toCharArray();
				for(int j=0;j<ch.length;j++) {
					if(ch[j]=='：') {
						if(ch[j+1]=='只') {
							p="read";
						}
						else if(ch[j+1]=='可') {
							p="write";
						}
					}
				}
			}
			r1.add(new MessageForShow(m1.get(i).getMessageType(),m1.get(i).getMessageId(),m1.get(i).getMessageContent(),formatter.format(m1.get(i).getMessageDate()),m1.get(i).getUserId(),m1.get(i).getTeamId(),m1.get(i).getDocId(),p));
		}
		for(int i=0;i<m2.size();i++) {
			if(m2.get(i).getMessageType().equals("1")) {
				char ch[]=m2.get(i).getMessageContent().toCharArray();
				for(int j=0;j<ch.length;j++) {
					if(ch[j]=='：') {
						if(ch[j+1]=='只') {
							p="read";
						}
						else if(ch[j+1]=='可') {
							p="write";
						}
					}
				}
			}
			
			r2.add(new MessageForShow(m2.get(i).getMessageType(),m2.get(i).getMessageId(),m2.get(i).getMessageContent(),formatter.format(m2.get(i).getMessageDate()),m2.get(i).getUserId(),m2.get(i).getTeamId(),m2.get(i).getDocId(),p));
		}
		
		r.setResult(r1);
		r.setResult2(r2);
		return r;
		
	}
	class MessageForShow{
		String type,id,message,date,people,teamId,docId,power;
		MessageForShow(String a,String b,String c,String d,String e,String f,String g,String h){
			this.type=a;
			this.id=b;
			this.message=c;
			this.date=d;
			this.people=e;
			this.teamId=f;
			this.docId=g;
			this.power=h;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getPeople() {
			return people;
		}
		public void setPeople(String people) {
			this.people = people;
		}
		public String getTeamId() {
			return teamId;
		}
		public void setTeamId(String teamId) {
			this.teamId = teamId;
		}
		public String getDocId() {
			return docId;
		}
		public void setDocId(String docId) {
			this.docId = docId;
		}
		public String getPower() {
			return power;
		}
		public void setPower(String power) {
			this.power = power;
		}
		
		
	}
	class MessageRet extends Ret{
		Object result2;

		public Object getResult2() {
			return result2;
		}

		public void setResult2(Object result2) {
			this.result2 = result2;
		}
		
	}
	/**
	 * 处理消息
	 * @param messageId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/manageMessage")
	@ResponseBody
	@CrossOrigin
	public Object manageMessage(@RequestParam("messageId") String messageId) throws Exception {
		Ret r = new Ret();
		Message m=Message.findMessageById(messageId);
		String p="";
		if(m!=null) {
			Message.updateColumn(messageId, "MessageRead", "true");
			r.setSuccess(true);
			r.setMessage("");
			if(m.getMessageType().equals("1")) {
				char ch[]=m.getMessageContent().toCharArray();
				for(int i=0;i<ch.length;i++) {
					if(ch[i]=='：') {
						if(ch[i+1]=='只') {
							p="read";
						}
						else if(ch[i+1]=='可') {
							p="write";
						}
					}
				}
			ParticipateDoc pt=new ParticipateDoc(m.getDocId(),m.getObjId(),p);
			String ss=ParticipateDoc.findPowerToDocById(m.getObjId(),m.getDocId());
			if(ss==null) {
				ParticipateDoc.addParticipateDoc(pt);
			}
			}	
		}
		else
			r.setSuccess(false);
		return r;
	}
	/**
	 * 同意邀请
	 * @param messageId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/acceptInvite")
	@ResponseBody
	@CrossOrigin
	public Object acceptInvite(@RequestParam("messageId") String messageId) throws Exception {
		Message m=Message.findMessageById(messageId);
		String content=m.getObjId()+"同意加入"+m.getTeamId()+"团队";
		Message m1=new Message(Message.getNextId(),m.getObjId(),m.getUserId(),content,DateOperate.nowDate(),false,"3",m.getTeamId(),m.getDocId());
		Message.addMessage(m1);
		String con=m.getMessageContent()+"(已同意)";
		Message.updateColumn(messageId, "MessageContent",con );
		TeamService.addMemberToTeam(m.getObjId(),m.getTeamId());
		Ret r = new Ret();
		r.setSuccess(true);
		return r;
	}
	/**
	 * 拒绝邀请
	 * @param messageId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/rejectInvite")
	@ResponseBody
	@CrossOrigin
	public Object rejectInvite(@RequestParam("messageId") String messageId) throws Exception {
		Message m=Message.findMessageById(messageId);
		String content=m.getObjId()+"拒绝加入"+m.getTeamId()+"团队";
		Message m1=new Message(Message.getNextId(),m.getObjId(),m.getUserId(),content,DateOperate.nowDate(),false,"3",m.getTeamId(),m.getDocId());
		Message.addMessage(m1);
		String con=m.getMessageContent()+"(已拒绝)";
		Message.updateColumn(messageId, "MessageContent",con);
		Ret r = new Ret();
		r.setSuccess(true);
		return r;
	}
	/**
	 * 删除消息
	 * @param messageId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/deleteMessage")
	@ResponseBody
	@CrossOrigin
	public Object deleteMessage(@RequestParam("messageId") String messageId) throws Exception {
		Message.deleteMessage(messageId);
		Ret r = new Ret();
		r.setSuccess(true);
		return r;
	}
	/**
	 * 创建团队
	 * @param teamName
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/createTeam")
	@ResponseBody
	@CrossOrigin
	public Object createTeam(@RequestParam("teamName") String teamName,
			@RequestParam("userId") String userId) {
		String id=Team.getNextId();
		Team t=new Team(id,teamName,userId,DateOperate.nowDate());
		Team.addTeam(t);
		ParticipateTeam p=new ParticipateTeam(id,userId,"owner",DateOperate.nowDate(),true,"admin");
		ParticipateTeam.addParticipateTeam(p);
		Ret r = new Ret();
		r.setResult(id);
		r.setSuccess(true);
		return r;
	}
	/**
	 * 清空回收站
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/userClearRecycleBin")
	@ResponseBody
	@CrossOrigin
	public Object userClearRecycleBin(@RequestParam("userId") String userId) {
		Ret r = new Ret();
		try {
			String sql = "DELETE FROM Doc WHERE Deleted = 'true' AND CreaterId = '"+userId+"'";
			if(Repository.getInstance().doSqlUpdateStatement(sql)) {
				r.setSuccess(true);
				r.setMessage("删除成功");
			}
			else {
				r.setSuccess(false);
				r.setMessage("删除失败");
			}
			return r;
		} catch(Exception e) {
			return r;
		}
	}
	
}
