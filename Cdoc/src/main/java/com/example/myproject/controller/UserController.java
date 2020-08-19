package com.example.myproject.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.example.myproject.model.Comment;
import com.example.myproject.model.Doc;
import com.example.myproject.model.DocUser;
import com.example.myproject.model.EditorDoc;
import com.example.myproject.model.Message;
import com.example.myproject.model.ParticipateDoc;
import com.example.myproject.model.Team;
import com.example.myproject.model.ParticipateTeam;
import com.example.myproject.service.DateOperate;
import com.example.myproject.service.DocService;
import com.example.myproject.service.FileOperate;
import com.example.myproject.service.JavaMail;
import com.example.myproject.service.TeamService;
import com.example.myproject.service.UserService;

@RestController
public class UserController {
	
	/**
	 * 用户登录
	 * @param userId
	 * @param userPassword
	 * @return
	 */
	@RequestMapping(value = "/userLogin")
	@ResponseBody
	@CrossOrigin
	public Object userLogin(@RequestParam("userId") String userId,
			@RequestParam("userPassword") String userPassword) {
		System.out.println("User Login.");
		Ret r = new Ret();
		DocUser du = DocUser.findUserById(userId);
		if(du == null) {
			r.setSuccess(false);
			r.setMessage("用户未注册！");
			return r;
		}
		if ( !du.getUserPassword().equals(userPassword) ) {
			r.setSuccess(false);
			r.setMessage("密码不正确！");
			return r;
		}
		else {
			r.setSuccess(true);
			r.setMessage("登录成功。");
			return r;
		}
	}
	
	/**
	 * 发送邮件 验证码
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/sendEmail")
	@ResponseBody
	@CrossOrigin
	public Object sendEmail(@RequestParam("userId") String userId) {
		String idcode = JavaMail.getCode(6);
		Ret r = new Ret();
		try{
			JavaMail.sendMail(userId, "C.DOC", "您在C.DOC文档注册的验证码是："+idcode+"，如非本人操作请忽略此条信息。");
			r.setSuccess(true);
			r.setMessage("发送成功");
			r.setResult(idcode);
		} catch(Exception e) {
			r.setSuccess(false);
			r.setMessage("发送失败");
		}
		return r;
	}
	
	/**
	 * 用户注册
	 * @param userId
	 * @param password1
	 * @param password2
	 * @param idcode
	 * @param subidcode
	 * @return
	 */
	@RequestMapping(value = "/userRegister")
	@ResponseBody
	@CrossOrigin
	public Object userRegister(@RequestParam("userId") String userId,
			@RequestParam("password1") String password1,
			@RequestParam("password2") String password2,
			@RequestParam("idcode") String idcode,
			@RequestParam("subidcode") String subidcode) {
		System.out.println("User Register.");
		Ret r = new Ret();
		DocUser du = DocUser.findUserById(userId);
		if(du != null) {
			r.setSuccess(false);
			r.setMessage("邮箱已注册！");
			return r;
		}
		if(!password1.equals(password2)) {
			r.setSuccess(false);
			r.setMessage("两次密码不相同！");
			return r;
		}
		if(password1.length() < 6) {
			r.setSuccess(false);
			r.setMessage("密码太短");
			return r;
		}
		if(!idcode.equals(subidcode)) {
			r.setSuccess(false);
			r.setMessage("验证码不正确！");
			return r;
		}
		if(DocUser.addUser(new DocUser(userId,userId,password1,DocUser.getRandomUserImage(),"","")) ) {
			r.setSuccess(true);
			r.setMessage("注册成功。");
			return r;
		}
		r.setSuccess(false);
		r.setMessage("注册失败。");
		return r;
		
	}
	
	/**
	 * 用户找回密码
	 * @param userId
	 * @param password1
	 * @param password2
	 * @param idcode
	 * @param subidcode
	 * @return
	 */
	@RequestMapping(value = "/userFindPassword")
	@ResponseBody
	@CrossOrigin
	public Object userFindPassword(@RequestParam("userId") String userId,
			@RequestParam("password1") String password1,
			@RequestParam("password2") String password2,
			@RequestParam("idcode") String idcode,
			@RequestParam("subidcode") String subidcode){
		System.out.println("User find pwd.");
		Ret r = new Ret();
		DocUser du = DocUser.findUserById(userId);
		if(du == null) {
			r.setSuccess(false);
			r.setMessage("邮箱未注册！");
			return r;
		}
		if(!password1.equals(password2)) {
			r.setSuccess(false);
			r.setMessage("两次密码不相同！");
			return r;
		}
		if(password1.length() < 6) {
			r.setSuccess(false);
			r.setMessage("密码太短");
			return r;
		}
		if(!idcode.equals(subidcode)) {
			r.setSuccess(false);
			r.setMessage("验证码不正确！");
			return r;
		}
		if(DocUser.changeUserInfo(userId,"UserPassword",password1) ) {
			r.setSuccess(true);
			r.setMessage("修改成功。");
			return r;
		}
		r.setSuccess(false);
		r.setMessage("修改失败。");
		return r;
	}
	
	@RequestMapping(value = "/showUserImage")
	@ResponseBody
	@CrossOrigin
	public Object showUserImage(@RequestParam("userId") String userId){
		try {
			return FileOperate.getBase64File(DocUser.findUserById(userId).getUserImage()) ;
		} catch (Exception e) {
			return "";
		}
	}
	class UserInfoRet extends Ret{
		String userImage;
		String userName;
		String userId;
		public UserInfoRet(DocUser d) {
			try {
				this.userImage = FileOperate.getBase64File(d.getUserImage());
			} catch (Exception e) {
				this.userImage = "";
			}
			this.userName = d.getUserName();
			this.userId = d.getUserId();
		}
		public UserInfoRet() {}
		public String getUserImage() {
			return userImage;
		}
		public void setUserImage(String userImage) {
			this.userImage = userImage;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		
	}
	/**
	 * 用户信息页面
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/showUserInfoPage")
	@ResponseBody
	@CrossOrigin
	public Object showUserInfoPage(@RequestParam("userId") String userId){
		DocUser u = DocUser.findUserById(userId);
		
		UserInfoRet r = new UserInfoRet();
		if(u == null) {
			r.setSuccess(false);
		}
		else {
			r.setSuccess(true);
			r.setUserName(u.getUserName());
			try {
				r.setUserImage(FileOperate.getBase64File(u.getUserImage()) );
			} catch (Exception e) {
				e.printStackTrace();
				r.setUserImage("");
			}
		}
		return r;
	}
	
	@RequestMapping(value = "/changeUserInfo")
	@ResponseBody
	@CrossOrigin
	public Object changeUserInfo(@RequestParam("userId") String userId,
			@RequestParam("newValue") String newValue,
			@RequestParam("type") Integer type,
			@RequestParam(value="password",required=false) String pwd){
		System.out.print("Change info ");
		Ret r = new Ret();
		if(type.equals(1)) { // 邮箱
			r.setMessage("");
			r.setSuccess(DocUser.changeUserInfo(userId, "UserId", newValue));
		}
		else if(type.equals(2)) { // 用户名
			r.setMessage("");
			r.setSuccess(DocUser.changeUserInfo(userId, "UserName", newValue));
		}
		else { // 密码
			DocUser du = DocUser.findUserById(userId);
			if(!du.getUserPassword().equals(pwd)) {
				r.setSuccess(false);
				r.setMessage("\n原密码不正确！");
			}
			else{
				r.setMessage("");
				r.setSuccess(DocUser.changeUserInfo(userId, "UserPassword", newValue));
			}
		}
		return r;
	}
	
	/**
	 * 展示用户文件
	 * @param userId
	 * @return
	 */
	
	class DocForShow{
		public String docname;
		public String docid;
		public String doc;
		public String owner;
		public String ownerid;
		public Date changedate;
		public Date createdate;
		public DocForShow(Doc d) {
			docid = d.getDocId();
			docname = d.getDocName();
			DocUser du = DocUser.findUserById(d.getCreaterId());
			owner = du.getUserName();
			ownerid = du.getUserId();
			doc = String.valueOf(d.getDocChangeDate()); // TODO 文件内容
			changedate = d.getDocChangeDate();
			createdate = d.getDocCreateDate();
		}
		public String getDocname() {
			return docname;
		}
		public void setDocname(String docname) {
			this.docname = docname;
		}
		public String getDocid() {
			return docid;
		}
		public void setDocid(String docid) {
			this.docid = docid;
		}
		public String getDoc() {
			return doc;
		}
		public void setDoc(String doc) {
			this.doc = doc;
		}
		public String getOwner() {
			return owner;
		}
		public void setOwner(String owner) {
			this.owner = owner;
		}
		public Date getChangedate() {
			return changedate;
		}
		public void setChangedate(Date changedate) {
			this.changedate = changedate;
		}
		public Date getCreatedate() {
			return createdate;
		}
		public void setCreatedate(Date createdate) {
			this.createdate = createdate;
		}
		public String getOwnerid() {
			return ownerid;
		}
		public void setOwnerid(String ownerid) {
			this.ownerid = ownerid;
		}
		
	}
	class Document{
		Date date;
		ArrayList<DesktopDocForShow> docList = new ArrayList<>();
		public Document(Date date,ArrayList<Doc> docList,String userId) {
			this.date = date;
			for(Doc d:docList) {
				DesktopDocForShow df=new DesktopDocForShow(d,userId);
				this.docList.add(df);
			}
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public ArrayList<DesktopDocForShow> getDocList() {
			return docList;
		}
		public void setDocList(ArrayList<DesktopDocForShow> docList) {
			this.docList = docList;
		}
	}
	
	/**
	 * 最近的文档
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getUserRecentlyDoc")
	@ResponseBody
	@CrossOrigin
	public Object getUserRecentlyDoc(@RequestParam("userId") String userId,
			@RequestParam("type") Integer type){
		ArrayList<Document> res = new ArrayList<>();
		ArrayList<Doc> ds = UserService.getUserRecentlyDoc(DocUser.findUserById(userId), type, true);
		if(ds.size()==0)
			return res;
		Date lastDate;
		if(type.equals(1))
			lastDate = ds.get(0).getDocChangeDate();
		else
			lastDate = ds.get(0).getDocCreateDate();
		ArrayList<Doc> dlist = new ArrayList<>();
		for(Doc d:ds) {
			Date l;
			if(type.equals(1))
				l = d.getDocChangeDate();
			else
				l = d.getDocCreateDate();
			if(l.compareTo(lastDate) < 0) {
				Collections.reverse(dlist);
				res.add(new Document(lastDate,dlist,userId) );
				dlist.clear();
				lastDate = l;
			}
			dlist.add(d);
		}
		res.add(new Document(lastDate,dlist,userId) );
		return res;
	}
	/**
	 * 我创建的文档
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getUserCreateDoc")
	@ResponseBody
	@CrossOrigin
	public Object getUserCreateDoc(@RequestParam("userId") String userId,
			@RequestParam("type") Integer type){ // 1 更改时间 2 创建时间
		ArrayList<Document> res = new ArrayList<>();
		ArrayList<Doc> ds = DocUser.showUserCreateDoc(userId, type, true);
		if(ds.size()==0)
			return res;
		Date lastDate;
		if(type.equals(1))
			lastDate = ds.get(0).getDocChangeDate();
		else
			lastDate = ds.get(0).getDocCreateDate();
		ArrayList<Doc> dlist = new ArrayList<>();
		for(Doc d:ds) {
			Date l;
			if(type.equals(1))
				l = d.getDocChangeDate();
			else
				l = d.getDocCreateDate();
			if(l.compareTo(lastDate) < 0) {
				res.add(new Document(lastDate,dlist,userId) );
				dlist.clear();
				lastDate = l;
			}
			dlist.add(d);
		}
		res.add(new Document(lastDate,dlist,userId) );
		return res;
	}
	/**
	 * 获取收藏的
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getUserFavouriteDoc")
	@ResponseBody
	@CrossOrigin
	public Object getUserFavouriteDoc(@RequestParam("userId") String userId,
			@RequestParam("type") Integer type){
		ArrayList<Document> res = new ArrayList<>();
		try {
			ArrayList<Doc> ds =  UserService.getUserFavouriteDoc(DocUser.findUserById(userId), type, true);		
			if(ds.size()==0)
				return res;
			Date lastDate;
			if(type.equals(1))
				lastDate = ds.get(0).getDocChangeDate();
			else
				lastDate = ds.get(0).getDocCreateDate();
			ArrayList<Doc> dlist = new ArrayList<>();
			for(Doc d:ds) {
				Date l;
				if(type.equals(1))
					l = d.getDocChangeDate();
				else
					l = d.getDocCreateDate();
				if(l.compareTo(lastDate) < 0) {
					res.add(new Document(lastDate,dlist,userId) );
					dlist.clear();
					lastDate = l;
				}
				dlist.add(d);
			}
			res.add(new Document(lastDate,dlist,userId) );
			return res;
		} catch(Exception e) {
			return res;
		}
	}
	/**
	 * 回收站文件
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getUserBinDoc")
	@ResponseBody
	@CrossOrigin
	public Object getUserBinDoc(@RequestParam("userId") String userId){
		ArrayList<Document> res = new ArrayList<>();
		ArrayList<Doc> ds =  DocUser.showUserDeleteDoc(userId, 1, true);
		if(ds.size()==0)
			return res;
		Date lastDate;
		lastDate = ds.get(0).getDocChangeDate();
		ArrayList<Doc> dlist = new ArrayList<>();
		for(Doc d:ds) {
			Date l;
			l = d.getDocChangeDate();
			if(l.compareTo(lastDate) < 0) {
				res.add(new Document(lastDate,dlist,userId) );
				dlist.clear();
				lastDate = l;
			}
			dlist.add(d);
		}
		res.add(new Document(lastDate,dlist,userId) );
		return res;
	}
	
	/**
	 * 展示用户桌面
	 * @param userId
	 * @param type
	 * @param table
	 * @return
	 */
	class DesktopDocForShow extends DocForShow{
		int power;
		public DesktopDocForShow(Doc d,String userId) {
			super(d);
			try {
				String p = ParticipateDoc.findPowerToDocById(userId, d.getDocId());
				if(p.equals("read"))
					this.power = 1;
				else if(p.equals("write"))
					this.power = 2;
				else
					this.power = 3;
			} catch (Exception e) {
				//e.printStackTrace();
				power = 1;
			}
		}
		public int getPower() {
			return power;
		}

		public void setPower(int power) {
			this.power = power;
		}
	}
	/**
	 * 返回用户桌面文件
	 * @param userId
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/getUserDesktopDoc")
	@ResponseBody
	@CrossOrigin
	public Object getUserDesktopDoc(@RequestParam("userId") String userId,
			@RequestParam("type") Integer type){
		
		ArrayList<Doc> ds =  DocUser.showUserDesktopDoc(userId, type, true);
		if(ds.size()==0)
			return new ArrayList<>();
		ArrayList<DesktopDocForShow> res = new ArrayList<>();
		for(Doc d:ds) {
			res.add(new DesktopDocForShow(d,userId));
		}
		return res;
	}
	
	/**
	 * 用户收藏文件
	 * @param docId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/userSetFavourite")
	@ResponseBody
	@CrossOrigin
	public Object userSetFavourite(@RequestParam("docId") String docId,
			@RequestParam("userId") String userId) {
		DocUser du = DocUser.findUserById(userId);
		Ret r = new Ret();
		if(du.getFavouriteDoc().contains(docId)) {
			r.setSuccess(true);
			r.setMessage("已收藏过此文件！");
		}
		else {
			if(du.setFavourite(docId)) {
				r.setSuccess(true);
				r.setMessage("收藏成功");
			}
			else {
				r.setSuccess(false);
				r.setMessage("收藏失败");
			}
		}
		return r;
	}
	/**
	 * 取消收藏
	 * @param docId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/userCancelFavourite")
	@ResponseBody
	@CrossOrigin
	public Object userCancelFavourite(@RequestParam("docId") String docId,
			@RequestParam("userId") String userId) {
		Ret r = new Ret();
		if(DocUser.cancelFavourite(DocUser.findUserById(userId), docId)) {
			r.setSuccess(true);
			r.setMessage("已取消收藏");
		}
		else {
			r.setSuccess(false);
			r.setMessage("操作失败");
		}
		return r;
	}
	
	/**
	 * 用户重命名文件
	 * @param docId
	 * @param userId
	 * @param newName
	 * @return
	 */
	@RequestMapping(value = "/userRenameFile")
	@ResponseBody
	@CrossOrigin
	public Object userRenameFile(@RequestParam("docId") String docId,
			@RequestParam("userId") String userId,
			@RequestParam("newName") String newName) {
				
		Doc d = Doc.findDocByDocId(docId);
		Ret r = new Ret();
		if(d.getCreaterId().equals(userId)) {
			if(Doc.renameDoc(d, newName)) {
				r.setSuccess(true);
				r.setMessage("修改成功");
			}
			else {
				r.setSuccess(false);
				r.setMessage("修改失败！");
			}
		}
		else {
			if(Doc.renameDoc(d, newName)) {
				r.setSuccess(true);
				r.setMessage("修改成功");
			}
			else {
				r.setSuccess(false);
				r.setMessage("修改失败！");
			}
		}
		return r;
	}
	/**
	 * 删除文件
	 * @param docId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/userDeleteFile")
	@ResponseBody
	@CrossOrigin
	public Object userDeleteFile(@RequestParam("docId") String docId,
			@RequestParam("userId") String userId) {
		Doc d = Doc.findDocByDocId(docId);
		Ret r = new Ret();
		boolean res = true;
		if(!d.getBelongTo().equals("-1") && !d.getCreaterId().equals(userId)) { // 团队删除
			res = TeamService.deleteFileFromTeam(d);
		}
		else {
			res = UserService.userDeleteDoc(DocUser.findUserById(userId), d);
		}
		if(res) {
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
	 * 彻底删除文件
	 * @param docId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/userDeleteFileThorough")
	@ResponseBody
	@CrossOrigin
	public Object userDeleteFileThorough(@RequestParam("docId") String docId,
			@RequestParam("userId") String userId) {
		Ret r = new Ret();
		if(Doc.delDocThorough(docId)) {
			// 删除数据库字段
			DocUser.cancelFavourite(DocUser.findUserById(userId), docId);
			DocUser.cancelRecently(DocUser.findUserById(userId), docId);
			r.setSuccess(true);
			r.setMessage("删除成功！");
		}
		else {
			r.setSuccess(false);
			r.setMessage("删除失败！");
		}
		return r;
	}
	
	/**
	 * 恢复文件
	 * @param docId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/userReverseFile")
	@ResponseBody
	@CrossOrigin
	public Object userReverseFile(@RequestParam("docId") String docId,
			@RequestParam("userId") String userId) {
		Ret r = new Ret();
		if(Doc.reverseDoc(docId)) {
			DocUser.setRecently(DocUser.findUserById(userId), docId);
			r.setSuccess(true);
			r.setMessage("恢复成功！");
		}
		else {
			r.setSuccess(false);
			r.setMessage("恢复失败！");
		}
		return r;
	}
	
	/**
	 * 搜索用户 一次显示5个
	 * @param value 搜索值
	 * @param model 搜索模式 1 id 2 name
	 * @return
	 */
	@RequestMapping(value = "/userSearchDocUser")
	@ResponseBody
	@CrossOrigin
	public Object userSearchDocUser(@RequestParam("value") String value,
			@RequestParam("model") int model,
			@RequestParam("userId") String userId) {
		ArrayList<UserInfoRet>res = new ArrayList<>();
		int i=0;
		for(DocUser du:UserService.searchUser(value, model, userId)) {
			res.add(new UserInfoRet(du));
			i++;
			if(i>=5)
				break;
		}
		return res;
	}
	
	/**
	 * 创建新文件
	 * @param userId
	 * @param docName
	 * @return
	 */
	@RequestMapping(value = "/userCreateNewFile")
	@ResponseBody
	@CrossOrigin
	public Object userCreateNewFile(@RequestParam("userId") String userId,
			@RequestParam("docName") String docName,
			@RequestParam("teamId") String teamId,
			@RequestParam("temp") String temp) {
				
		String docId = Doc.getNextId();
		Ret r = new Ret();
		Doc dc = new Doc(docId,userId,Doc.getDocSrcByDocName(docId, docName), new Date(),new Date(),Doc.getDocLogByDocName(docId, docName),
				false,new Date(),teamId);
				
		if(Doc.addDoc(dc) ) {
			DocUser.participateDoc(userId, docId, "admin");
			DocUser.setRecently(DocUser.findUserById(userId), docId);
			
			UserService.setDocTemplate(temp,dc);
			
			r.setSuccess(true);
			r.setMessage("创建成功");
			r.setResult(docId);
		}
		else {
			r.setSuccess(false);
			r.setMessage("创建失败");
		}
/*		if(!teamId.equals("-1")) {
			TeamService.addFileToTeamMembers(teamId, docId);
		}*/
		return r;
	}
	
	/**
	 * 用户分享文件
	 * @param userId
	 * @param objId
	 * @param power
	 * @return
	 */
	@RequestMapping(value = "/userShareFile")
	@ResponseBody
	@CrossOrigin
	public Object userShareFile(@RequestParam("userId") String userId,
			@RequestParam("objId") String objId,
			@RequestParam("docId") String docId,
			@RequestParam("power") Boolean power){
		Ret r = new Ret();
		String p = "只读";
		if(power)
			p = "可编辑";
		
		if(Message.addMessage(new Message(Message.getNextId(),userId,objId,
				userId+"分享给您一个文件，您的权限是："+p,new Date(), false,"1","-1",docId)) ) {
			r.setSuccess(true);
			r.setMessage("分享成功");
		}
		else {
			r.setSuccess(false);
			r.setMessage("分享失败");
		}
		
		return r;
	}
	/**
	 * 用户打开文件
	 * @param userId
	 * @param docId
	 * @return
	 */
	@RequestMapping(value = "/userOpenFile")
	@ResponseBody
	@CrossOrigin
	public Object userOpenFile(@RequestParam("userId") String userId,
			@RequestParam("docId") String docId) {
		Ret r = new Ret();
		if(EditorDoc.findEditorDocByUserIdDocId(userId, docId) == null) {
			r.setSuccess(EditorDoc.addEditorDoc(new EditorDoc(userId,docId,new Date(),false,false)) );
			DocUser.setRecently(DocUser.findUserById(userId), docId);
		}
		Doc.updateDocTime(docId);
		r.setSuccess(true);
		return r;
	}

	
	
	/**
	 * 用户关闭文件
	 * @param userId
	 * @param docId
	 * @return
	 */
	@RequestMapping(value = "/userCloseFile")
	@ResponseBody
	@CrossOrigin
	public Object userCloseFile(@RequestParam("userId") String userId,
			@RequestParam("docId") String docId) {
		Ret r = new Ret();
		try {
			EditorDoc.finishedEditing(userId, docId);
			r.setSuccess(EditorDoc.deleteEditorDoc(userId, docId));
		} catch(Exception e) {
			return r;
		}
		return r;
	}
	
	
	
	class DocData{
		Object value;
		Object users;
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		public Object getUsers() {
			return users;
		}
		public void setUsers(Object users) {
			this.users = users;
		}
	}
	class Users{
		String userImage;
		String userName;
		String userId;
		public Users(DocUser d) {
			try {
				this.userImage = FileOperate.getBase64File(d.getUserImage());
			} catch (Exception e) {
				this.userImage = "";
			}
			this.userName = d.getUserName();
			this.userId = d.getUserId();
		}
		public String getUserImage() {
			return userImage;
		}
		public void setUserImage(String userImage) {
			this.userImage = userImage;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		
	}
	/**
	 * 加载doc的内容等数据
	 * @param userId
	 * @param docId
	 * @return
	 */
	@RequestMapping(value = "/getdocData")
	@ResponseBody
	@CrossOrigin
	public Object getdocData(@RequestParam("userId") String userId,
			@RequestParam("docId") String docId) {
		Ret r = new Ret();
		DocData dd = new DocData();
		Doc d = Doc.findDocByDocId(docId);
		
		try {
			dd.setValue(FileOperate.getFileContent(d.getDocSrc(),false));
			ArrayList<Users> uu = new ArrayList<>();
			String id = EditorDoc.findEditingUserId(docId);
			int index = -1;
			ArrayList<DocUser> userlist = EditorDoc.findDocUserEditorDoc(docId);
			for(int i=0; i<userlist.size();i++) {
				DocUser du = userlist.get(i);
				uu.add(new Users(du));
				if(du.getUserId().equals(id)) {
					index = i;
				}
			}
			dd.setUsers(uu);
			r.setResult(dd);
			r.setSuccess(true);
			
			r.setMessage(String.valueOf(index));
						
			return r;
		} catch (Exception e) {
			r.setSuccess(false);
			return r;
		}
	}
	
	/**
	 * 检查是否有人提交
	 * @param userId
	 * @param docId
	 * @return
	 */
	@RequestMapping(value = "/checkDocData")
	@ResponseBody
	@CrossOrigin
	public Object checkDocData(@RequestParam("userId") String userId,
			@RequestParam("docId") String docId) {
		Ret r = new Ret();
		DocData dd = new DocData();
		Doc d = Doc.findDocByDocId(docId);
		
		try {
			if(EditorDoc.haveUpdate(userId, docId)) {
				dd.setValue(FileOperate.getFileContent(d.getDocSrc(),false));
				r.setResult(dd);
				r.setSuccess(true);
				return r;
			}
			else {
				r.setSuccess(false);
				return r;
			}
		} catch (Exception e) {
			r.setSuccess(false);
			return r;
		}
	}
	
	
	/**
	 * 加载使用的用户
	 * @param userId
	 * @param docId
	 * @return
	 */
	@RequestMapping(value = "/loadEditingUsers")
	@ResponseBody
	@CrossOrigin
	public Object loadEditingUsers(@RequestParam("userId") String userId,
			@RequestParam("docId") String docId) {
		Ret r = new Ret();
		DocData dd = new DocData();
		
		try {
			ArrayList<Users> uu = new ArrayList<>();
			String id = EditorDoc.findEditingUserId(docId);
			int index = -1;
			ArrayList<DocUser> userlist = EditorDoc.findDocUserEditorDoc(docId);
			for(int i=0; i<userlist.size();i++) {
				DocUser du = userlist.get(i);
				uu.add(new Users(du));
				if(du.getUserId().equals(id)) {
					index = i;
				}
			}
			dd.setUsers(uu);
			r.setResult(dd);
			r.setSuccess(true);
			
			r.setMessage(String.valueOf(index));
						
			return r;
		} catch (Exception e) {
			r.setSuccess(false);
			return r;
		}
	}
		
	/**
	 * 加载doc的内容等数据
	 * @param userId
	 * @param docId
	 * @return
	 */
	@RequestMapping(value = "/wantEditDoc")
	@ResponseBody
	@CrossOrigin
	public Object wantEditDoc(@RequestParam("userId") String userId,
			@RequestParam("docId") String docId) {
		Ret r = new Ret();
		if(EditorDoc.editingDoc(userId, docId)) {
			r.setSuccess(true);
			r.setMessage("申请成功");
		}
		else {
			r.setSuccess(false);
			r.setMessage("申请失败");
		}
		return r;
	}
	
	/**
	 * 用户关闭文件
	 * @param userId
	 * @param docId
	 * @return
	 */
	@RequestMapping(value = "/userSubmitChange")
	@ResponseBody
	@CrossOrigin
	public Object userSubmitChange(@RequestParam("userId") String userId,
			@RequestParam("docId") String docId) {
		Ret r = new Ret();
		if(EditorDoc.finishedEditing(userId, docId)) {
			r.setMessage("提交成功");
			r.setSuccess(true);
		}
		else {
			r.setMessage("提交失败");
			r.setSuccess(false);
		}
		return r;
	}
	/**
	 * 修改头像
	 * @param userId
	 * @param image
	 * @return
	 */
	@RequestMapping(value = "/uploadUserHeadImage")
	@ResponseBody
	@CrossOrigin
	public Object uploadUserHeadImage(@RequestParam("userId") String userId,
			@RequestParam("image") String image) {
		String p = DocUser.userImagePath+userId.substring(0,userId.indexOf("@"))+".jpg";
		Ret r = new Ret();
		if(FileOperate.writeBase64File(p, image.substring(image.indexOf(",")+1)) ) {
			DocUser.changeUserInfo(userId,"UserImage",p);
			r.setSuccess(true);
		}
		else {
			r.setSuccess(false);
		}
		return r;
	}
	
	
	// Doc部分
	/**
	 * 导出文件
	 * @param userId
	 * @param fileContent
	 * @param fileName
	 * @return
	 */
	@RequestMapping(value = "/userUploadFile")
	@ResponseBody
	@CrossOrigin
	public Object userUploadFile(@RequestParam("userId") String userId,
			@RequestParam("fileContent") String fileContent,
			@RequestParam("fileName") String fileName) {
			
		Ret r = new Ret();
		String docId = Doc.getNextId();		
		Doc dc = new Doc(docId,userId,Doc.getDocSrcByDocName(docId, fileName), new Date(),new Date(),Doc.getDocLogByDocName(docId, fileName),
				false,new Date(),"-1");
		if(Doc.addDoc(dc) ) {
			DocUser.participateDoc(userId, docId, "admin");
			DocUser.setRecently(DocUser.findUserById(userId), docId);
			
			String out = Doc.docPath+docId+"/"+fileName; // 文件输出的内容
			
			String docTmpDir = "DocSrc/temp/"+docId+"/"; // 创建临时路径
			File dir = new File(docTmpDir);
			if(!dir.exists())
				dir.mkdir();
			String htmlTmpDir = "DocSrc/temp/"+docId+"/html/";
			File hdir = new File(htmlTmpDir);
			if(!hdir.exists())
				hdir.mkdir();
			
			DocService.saveDocFile("",dc,userId);
			try {
				FileOperate.writeBase64File(docTmpDir+fileName, fileContent.substring(fileContent.indexOf(",")+1)); // word解码保存
				FileOperate.Word2003ToHtml(docTmpDir+fileName, htmlTmpDir+fileName);
				FileOperate.replaceAllPicSrcToBase64(htmlTmpDir,fileName, out);
			} catch(Exception e) {
				e.printStackTrace();
			}
			r.setSuccess(true);
			r.setMessage("创建成功");
			r.setResult(docId);
		}
		return r;
	}
	
	
	// Team部分
	/**
	 * 展示团队部分
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/showUserTeams")
	@ResponseBody
	@CrossOrigin
	public Object showUserTeams(@RequestParam("userId") String userId) {
		return ParticipateTeam.findUserTeams(userId, 1);
	}
	
	
	
}
