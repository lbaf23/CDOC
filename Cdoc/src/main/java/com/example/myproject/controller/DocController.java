package com.example.myproject.controller;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
import com.example.myproject.service.DateOperate;
import com.example.myproject.service.DocService;
import com.example.myproject.service.FileOperate;
import com.example.myproject.service.JavaMail;
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
		
		System.out.println("save value :: "+value);
		
		if(value.equals("<p></p>")) {
			System.out.println("空格");
			return r;
		}
		
		System.out.println(userId+" Save "+docId+" :: "+value);
		Doc d = Doc.findDocByDocId(docId);
		Doc.updateDocTime(docId);
		if(DocService.saveDocFile(value,d,userId)) {
			r.setSuccess(true);
		}
		else {
			r.setSuccess(false);
		}
		return r;
	}
	

	/**
	 * 
	 * 
	 */

	
	/**
	 * 记录log
	 * @param userId
	 * @param docId
	 * @return
	 */
	@RequestMapping(value = "/setdocLog")
	@ResponseBody
	@CrossOrigin
	public Object setdocLog(@RequestParam("userId") String userId,
			@RequestParam("docId") String docId) {
		System.out.println(userId + " savelog "+docId);
		return DocService.saveLog(Doc.findDocByDocId(docId), userId);
	}
	
	
	/**
	 * 用户评论
	 * @param userId
	 * @param userPassword
	 * @return
	 */
	@RequestMapping(value = "/submitComment")
	@ResponseBody
	@CrossOrigin
	public Object submitComment(@RequestParam("userId") String userId,
			@RequestParam("docId") String docId,
			@RequestParam("content") String content) {
		System.out.println("Comment");
		Ret r = new Ret();
		Comment c=new Comment(Comment.getNextId(),docId,userId,content,DateOperate.nowDate());
		Comment.addComment(c);
		System.out.println(c.toTupleInString());
		r.setSuccess(true);
		r.setMessage("评论成功！");
		return r;
	}
	/**
	 * 删除评论
	 * @param userId
	 * @param userPassword
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/deleteComment")
	@ResponseBody
	@CrossOrigin
	public Object deleteComment(@RequestParam("commentId") String commentId) throws Exception {
		System.out.println("delete Comment");
		Ret r = new Ret();
		System.out.println(commentId);
		Comment c=Comment.findCommentByCommentId(commentId);
		Comment.deleteComment(c);
		System.out.println(c.toTupleInString());
		r.setSuccess(true);
		r.setMessage("删除成功！");
		return r;
	}
	
	/**
	 * 加载评论
	 * @param userId
	 * @param userPassword
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/initialComment")
	@ResponseBody
	@CrossOrigin
	public Object initialComment(@RequestParam("docId") String docId) throws Exception {
		System.out.println("initial Comment");
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
	 * @param userPassword
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/initialMessage")
	@ResponseBody
	@CrossOrigin
	public Object initialMessage(@RequestParam("userId") String userId) throws Exception {
		System.out.println("initial Message");
		ArrayList<Message> m1=Message.findReadMessageByObjId(userId);
		ArrayList<Message> m2=Message.findUnReadMessageByObjId(userId);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<MessageForShow> r1=new ArrayList<>();
		ArrayList<MessageForShow> r2=new ArrayList<>();
		MessageRet r = new MessageRet();
		for(int i=0;i<m1.size();i++) {
			r1.add(new MessageForShow(m1.get(i).getMessageType(),m1.get(i).getMessageId(),m1.get(i).getMessageContent(),formatter.format(m1.get(i).getMessageDate()),m1.get(i).getUserId()));
		}
		for(int i=0;i<m2.size();i++) {
			r2.add(new MessageForShow(m2.get(i).getMessageType(),m2.get(i).getMessageId(),m2.get(i).getMessageContent(),formatter.format(m2.get(i).getMessageDate()),m2.get(i).getUserId()));
		}
		System.out.println(r1.size());
		System.out.println(r2.size());
		r.setResult(r1);
		r.setResult2(r2);
		return r;
		
	}
	class MessageForShow{
		String type,id,message,date,people;
		MessageForShow(String a,String b,String c,String d,String e){
			this.type=a;
			this.id=b;
			this.message=c;
			this.date=d;
			this.people=e;
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
	 * @param userId
	 * @param userPassword
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/manageMessage")
	@ResponseBody
	@CrossOrigin
	public Object manageMessage(@RequestParam("messageId") String messageId) throws Exception {
		System.out.println("manage Message");
		Ret r = new Ret();
		Message m=Message.findMessageById(messageId);
		if(m!=null) {
			m.setMessageRead(true);
			Message.deleteMessage(messageId);
			Message.addMessage(m);
			r.setSuccess(true);
			r.setMessage("");
		}
		else
			r.setSuccess(false);
		return r;
	}
	/**
	 * 删除消息
	 * @param userId
	 * @param userPassword
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/deleteMessage")
	@ResponseBody
	@CrossOrigin
	public Object deleteMessage(@RequestParam("messageId") String messageId) throws Exception {
		System.out.println("delete Message");
		Message.deleteMessage(messageId);
		Ret r = new Ret();
		r.setSuccess(true);
		return r;
	}
}
