package com.KillSystem.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.KillSystem.Service.UserService;
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
		if(session.getAttribute("tel_num")==null||session.getAttribute("tel_num").equals("")) {
			User acc = userService.login(user);
			if(acc==null) {
				acc.setTel_num((String)session.getAttribute("tel_num"));
				acc.setPasswd((String)session.getAttribute("passwd"));
				return "fail";
			}
			session.setAttribute("username", acc.getUsername());
			session.setAttribute("tel_num", acc.getTel_num());
			session.setAttribute("passwd", acc.getPasswd());
			return "toLogin";
		}
		user.setTel_num((String)session.getAttribute("tel_num"));
		user.setPasswd((String)session.getAttribute("passwd"));
		if(userService.login(user)!=null) {
			return "toLogin";
		}
		return "fail";
	}
	
}
