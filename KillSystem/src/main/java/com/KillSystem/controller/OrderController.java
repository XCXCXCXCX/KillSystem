package com.KillSystem.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.KillSystem.Service.OrderService;
import com.KillSystem.common.ServerResponse;

@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/createOrder.do")
	@ResponseBody
	public ServerResponse createOrder(HttpServletRequest request,HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file) {
		//1.在redis中库存减一，若失败，返回“创建订单失败”
		//2.在redis中创建订单，若失败，撤回库存减一操作并返回“创建订单失败”
		//3.在mysql队列尾加入创建订单请求，若失败，打印日志“redis中创建订单成功，但创建订单请求未能成功加入mysql队列”
		
		return null;
		
	}
	
	@RequestMapping("/pay.do")
	@ResponseBody
	public ServerResponse pay(HttpServletRequest request,HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file) {
		//todo
		//1.在redis中创建支付订单，若失败，返回“订单已存在”
		//2.收到支付宝回调后，在redis中更新支付订单，若失败，返回“订单支付失败：1.重新发起支付请求2.取消支付并删除订单和删除订单支付”
		
		return null;
	
	}
	
	@RequestMapping("/alipayCallback.do")
	@ResponseBody
	public ServerResponse alipayCallback(HttpServletRequest request,HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file) {
		//todo
		
		return null;
		
	}
	
	@RequestMapping("/payIsSuccess.do")
	@ResponseBody
	public ServerResponse payIsSuccess(HttpServletRequest request,HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file) {
		//查询该订单是否支付成功：在redis中查询key{订单号+pay}的value值是否为1，若为1，返回支付成功，若为0，返回未支付。
		
		return null;
		
	}
	
}
