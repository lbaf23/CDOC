package com.example.myproject.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import com.example.myproject.model.Doc;

public class DocService {
	/**
	 * 保存文件
	 * @param value
	 * @param doc
	 * @param userId
	 * @return
	 */
	public static boolean saveDocFile(String value,Doc doc,String userId) {
		// 更新相关文件
		File dir = new File(doc.getDocPath());
		if(!dir.exists()) {
			dir.mkdir();
		}
		File f = new File(doc.getDocSrc());
		if(f.exists())
			f.delete();
		return FileOperate.writeFile(doc.getDocSrc(), value);
	}
	/**
	 * 记录log
	 * @param doc
	 * @param userId
	 * @return
	 */
	public static boolean saveLog(Doc doc,String userId) {
		// 存储更新记录
		try {
			String log = String.valueOf(new Timestamp(new Date().getTime()) ) + "  用户 "+userId+" 更新文档数据。\n";
			return FileOperate.writeFile(doc.getDocLog(), log);
		}catch(Exception e) {
			return false;
		}
	}
}
