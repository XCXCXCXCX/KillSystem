package com.KillSystem.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.KillSystem.Service.ShippingAddressService;
import com.KillSystem.common.ResponseCode;
import com.KillSystem.common.ServerResponse;
import com.KillSystem.domain.Goods;
import com.KillSystem.domain.ShippingAddress;
import com.KillSystem.domain.User;
import com.github.pagehelper.PageInfo;

/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 收货地址controller
 * 包括收货地址的增删查
 * 
 * 2018年4月5日
 *
 */
@Controller
public class ShippingAddressController {
	
	@Autowired
	private ShippingAddressService shippingAddressService;

	@RequestMapping("/getShippingAddress.do")
	public ModelAndView getShippingAddress(Goods goods,HttpServletRequest request,HttpSession session) {
		if (session.getAttribute("tel_num") == null || session.getAttribute("passwd") == null) {
			return new ModelAndView("fail", null);
		}
		User user = new User();
		user.setTel_num((String)session.getAttribute("tel_num"));
		request.setAttribute("goods_name", goods.getGoods_name());
		request.setAttribute("goods_stock", goods.getGoods_stock());
		request.setAttribute("goods_price", goods.getGoods_price());
		PageInfo<Map<String,ShippingAddress>> p = shippingAddressService.selectByuserid(user,1, 6);
		return new ModelAndView("shippingAddress","page",p);
		
	}
	
	@RequestMapping("/insertShippingAddress.do")
	@ResponseBody
	public ServerResponse insertShippingAddress(ShippingAddress shippingAddress,HttpServletRequest request,HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file) {
		if (session.getAttribute("tel_num") == null || session.getAttribute("passwd") == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户登录已过期");
		}
		
		if(shippingAddressService.insert(shippingAddress) == 1) {
			return ServerResponse.createBySuccessMessage("新增收货地址成功!");
		}
		return ServerResponse.createBySuccessMessage("新增收货地址失败!");
	
	}
	
	@RequestMapping("/deleteShippingAddress.do")
	@ResponseBody
	public ServerResponse deleteShippingAddress(ShippingAddress shippingAddress,HttpServletRequest request,HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file) {
		if (session.getAttribute("tel_num") == null || session.getAttribute("passwd") == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户登录已过期");
		}
		
		if(shippingAddressService.delete(shippingAddress) == 1) {
			return ServerResponse.createBySuccessMessage("删除收货地址成功!");
		}
		return ServerResponse.createBySuccessMessage("删除收货地址失败!");
		
	}
	
}
