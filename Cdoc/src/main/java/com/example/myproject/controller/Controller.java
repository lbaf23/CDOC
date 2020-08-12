package com.example.myproject.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import net.sf.json.JSONObject;

/*
 * 前端控制器
 * 
 */

@RestController
public class Controller {

	@RequestMapping(value = "/login")
	@ResponseBody
	@CrossOrigin
	public String login(@RequestParam("info") String info) {
		return "login";
	}
	
	
	
	/**
	 * 将json字符串转换为类
	 * @param jsonStr
	 * @param cla
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T jsonToObject(String jsonStr, Class<T> cla) {
		try{
			Object o;
			JSONObject jsonObject = net.sf.json.JSONObject.fromObject(jsonStr);
			o = JSONObject.toBean(jsonObject, cla);
			return (T)o;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

}
