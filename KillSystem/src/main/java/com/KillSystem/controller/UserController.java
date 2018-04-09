package com.KillSystem.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.KillSystem.Service.UserService;
import com.KillSystem.common.ResponseCode;
import com.KillSystem.common.ServerResponse;
import com.KillSystem.domain.ShippingAddress;
import com.KillSystem.domain.User;

/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 用户controller
 * 包括用户的登陆、登出、注册
 * 
 * 2018年4月5日
 *
 */
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
	
	@RequestMapping("/logout.do")
	public String logout(HttpSession session) {
		session.invalidate();
		return "toLogin";
	}
	
	@RequestMapping("/getSystemTime.do")
	@ResponseBody
	public ServerResponse getSystemTime(HttpSession session) {
		if (session.getAttribute("tel_num") == null || session.getAttribute("passwd") == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
		}

		return ServerResponse.createBySuccess(new Date());
		
	}
}
