package com.oldnoop.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

	//跳转到登录页面
	@RequestMapping("/toLogin")
	public String toLogin(){
		return "user/login";
	}
	
	//登陆表单提交请求处理
	@RequestMapping("/login")
	@ResponseBody
	public String login(HttpSession session,String username,String password){
		String userId = login(username, password);
		session.setAttribute("userId", userId);
		return "1";
	}
	
	//模拟登陆,获得用户的userId
	private String login(String username,String password){
		//模拟三个用户
		String[] array = new String[]{"123456","123457","123458"};
		return array[new Random().nextInt(3)];
	}
}
