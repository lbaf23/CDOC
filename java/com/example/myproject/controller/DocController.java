package com.example.myproject.controller;

import java.io.File;
import java.io.FileWriter;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.myproject.model.Doc;
import com.example.myproject.model.ParticipateDoc;
import com.example.myproject.service.DocService;
import com.example.myproject.service.FileOperate;
import com.example.myproject.service.JavaMail;
@RestController
public class DocController {
	/*
	 * 文件保存	
	 */
	@RequestMapping(value = "/docSave")
	@ResponseBody
	@CrossOrigin
	public Object docSave(@RequestParam("value") String value,
			@RequestParam("userId") String userId,
			@RequestParam("docId") String docId) {
		
		System.out.println("Doc Save. "+docId);
		Ret r = new Ret();
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
	 * 加载doc的内容
	 * @param userId
	 * @param docId
	 * @return
	 */
	@RequestMapping(value = "/getdocData")
	@ResponseBody
	@CrossOrigin
	public Object getdocData(@RequestParam("userId") String userId,
			@RequestParam("docId") String docId) {
		System.out.println("get data "+docId);
		Ret r = new Ret();
		Doc d = Doc.findDocByDocId(docId);
		try {
			r.setResult(FileOperate.getFileContent(d.getDocSrc()));
			r.setSuccess(true);
			return r;
		} catch (Exception e) {
			r.setSuccess(false);
			return r;
		}
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
		
}
