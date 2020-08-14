package com.example.myproject.controller;

import java.io.File;
import java.io.FileWriter;
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
		System.out.println(res.size());
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
	

		
}
