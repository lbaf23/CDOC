package com.example.myproject.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;

/*
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
*/
import org.w3c.dom.Document;

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
