package com.KillSystem.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.KillSystem.Service.UserService;
import com.KillSystem.common.ServerResponse;
import com.KillSystem.domain.User;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/login.do")
	public String login(User user, HttpSession session) {
		if(user==null&&(session.getAttribute("tel_num")==null||session.getAttribute("tel_num").equals(""))) {
			System.out.print("输入为空");
			return "fail";
		}
		User acc = userService.login(user);
		if(acc==null){
			System.out.print("验证失败");
			return "fail";
		}
		session.setAttribute("username", acc.getUsername());
		session.setAttribute("tel_num", acc.getTel_num());
		session.setAttribute("passwd", acc.getPasswd());
		System.out.print("验证成功");
		return "sort";
	}
	
	@RequestMapping("/register.do")
	public String register(User user) {
		if(("").equals(user.getUsername())||("").equals(user.getTel_num())||("").equals(user.getPasswd())||
				user.getUsername()==null||user.getTel_num()==null||user.getPasswd()==null) {
			System.out.println(user.getTel_num()+user.getUsername()+user.getPasswd());
			return "toRegister";
		}
		if(userService.register(user)==1) {
			return "toLogin";
		}
		return "toRegister";
	}
	
	@RequestMapping("/loginOut.do")
	public String loginOut(HttpSession session) {
		session.invalidate();
		return "toLogin";
	}
}
