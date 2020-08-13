package com.example.myproject.model;

import com.example.myproject.service.DateOperate;
import com.example.myproject.service.FileOperate;
import com.example.myproject.service.UserService;


public class ModelTest {
	public static void main(String[] args) {
		
		try {
			FileOperate.renameFile("D:/工作/小学期项目/1.doc", "D:/工作/小学期项目/2.doc");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
