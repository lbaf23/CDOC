package com.example.myproject.model;

import java.util.Date;

import com.example.myproject.service.DateOperate;
import com.example.myproject.service.DocService;
import com.example.myproject.service.FileOperate;
import com.example.myproject.service.UserService;


public class ModelTest {
	public static void main(String[] args) {
		
		try {
			FileOperate.replaceAllPicSrcToBase64("DocSrc/temp/","输出.doc","DocSrc/temp/out.doc");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
