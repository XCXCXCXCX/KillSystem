package com.KillSystem.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.KillSystem.Service.GoodsService;
import com.KillSystem.Service.IFileService;
import com.KillSystem.Service.OrderService;
import com.KillSystem.Service.UserService;
import com.KillSystem.common.ResponseCode;
import com.KillSystem.common.ServerResponse;
import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.KillSystem.util.PropertiesUtil;

/**
 * @author xcxcxcxcx
 * 
 * @Comments 管理员controller 包括管理员登陆，商品的增删改查，订单的删改查
 * 
 *           2018年4月5日
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private IFileService iFileService;

	// 分页查询商品表
	// 返回ModelAndView对象，包含jsp视图和pageInfo对象
	@RequestMapping("/toAdminHome.do")
	public ModelAndView toAdminHome(Goods goods, String pageNo, HttpSession session, HttpServletRequest request) {
		// 是否登陆管理员
		if (session.getAttribute("username") == null && session.getAttribute("passwd") == null) {
			return new ModelAndView("fail", null);
		}
		// 默认返回第一页的内容
		int pageno = 1;
		if (pageNo == null) {
			pageno = 1;
		} else {
			pageno = Integer.parseInt(pageNo);
		}
		// 从mysql查询到第pageno页的商品信息
		PageInfo<Map<String, Goods>> p = goodsService.selectById(goods, pageno, 10);
		// 处理每个商品的开始结束时间信息，判断商品上线的状态并封装到request请求中返回
		int k = 0;
		while (k < p.getSize()) {
			DateTime begin = new DateTime(p.getList().get(k).get("begin_time"));
			DateTime end = new DateTime(p.getList().get(k).get("end_time"));
			if (end.isAfterNow() && begin.isBeforeNow()) {
				request.setAttribute("msg" + k, "上线中");
			} else if (begin.isAfterNow()) {
				request.setAttribute("msg" + k, "未上线");
			} else if (end.isBeforeNow()) {
				request.setAttribute("msg" + k, "已下线");
			}
			k++;
		}
		// 查询到的商品的总页面数
		request.setAttribute("totalPageCount", p.getPages());
		// 查询到的当前页面的页数
		request.setAttribute("pageNo", pageno);
		return new ModelAndView("adminHome", "page", p);

	}

	@RequestMapping("/toAdminOrder.do")
	public ModelAndView toAdminOrder(Order order, String pageNo, HttpSession session, HttpServletRequest request) {
		if (session.getAttribute("username") == null && session.getAttribute("passwd") == null) {
			return new ModelAndView("fail", null);
		} else {
			// todo
			int pageno = 1;
			if (pageNo == null) {
				pageno = 1;
			} else {
				pageno = Integer.parseInt(pageNo);
			}
			PageInfo<Map<String, Order>> p = orderService.select(order, pageno, 10);
			request.setAttribute("totalPageCount", p.getPages());
			request.setAttribute("pageNo", pageno);
			return new ModelAndView("adminOrder", "page", p);
		}

	}

	@RequestMapping("/adminLogin.do")
	@ResponseBody
	public ServerResponse adminLogin(String username, String passwd, HttpServletRequest request, HttpSession session) {
		if (username == null && passwd == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");
		}
		System.out.println(username + "," + passwd);
		if (userService.checkAdminRole(username, passwd) != null) {
			session.setAttribute("username", username);
			session.setAttribute("passwd", passwd);
			return ServerResponse.createBySuccessMessage("管理员登陆成功！");
		}
		return ServerResponse.createByErrorMessage("管理员登陆失败！");
	}

	// 文件上传
	// 指定请求参数中的value="upload_file"，与前台开发中指定fileKey参数对应
	@RequestMapping("/upload.do")
	@ResponseBody
	public ServerResponse upload(@RequestParam(value="goods_id",required=false)int goods_id,HttpServletRequest request, HttpSession session,
			@RequestParam(value = "upload_file", required = false) MultipartFile file) {
		String username = (String) session.getAttribute("username");
		String passwd = (String) session.getAttribute("passwd");
		// 是否登陆管理员
		if (username == null || passwd == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");
		}
		//获取上传到web服务器的path路径
		String path = request.getSession().getServletContext().getRealPath("upload");
		//把该web服务器下的需要上传到FTP服务器的文件上传到FTP服务器
		String targetFileName = iFileService.upload(file, path,goods_id);
		//把文件的uri和url封装后返回前台
		String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
		System.out.println(url);
		Map fileMap = Maps.newHashMap();
		fileMap.put("uri", targetFileName);
		fileMap.put("url", url);
		return ServerResponse.createBySuccess(fileMap);

	}

	@RequestMapping("/adminDelete.do")
	@ResponseBody
	public ServerResponse adminDelete(Goods goods, HttpSession session) {
		if (session.getAttribute("username") == null || session.getAttribute("passwd") == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");
		}
		if (goodsService.delete(goods) == 1) {
			return ServerResponse.createBySuccessMessage("删除商品成功！");
		}
		return ServerResponse.createByErrorMessage("删除商品失败！");

	}

	@RequestMapping("/addGoodsInfo.do")
	@ResponseBody
	public ServerResponse addGoodsInfo(Goods goods, HttpSession session) {
		if (session.getAttribute("username") == null || session.getAttribute("passwd") == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");
		}
		if (goodsService.update(goods) == 1) {
			return ServerResponse.createBySuccessMessage("更新商品详情成功！");
		}
		return ServerResponse.createByErrorMessage("更新商品详情失败！");

	}

	@RequestMapping("/adminUpdateOrInsert.do")
	@ResponseBody
	public ServerResponse adminUpdateOrInsert(Goods goods, HttpSession session) {
		if (session.getAttribute("username") == null || session.getAttribute("passwd") == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");
		}
		if (goods == null) {
			return ServerResponse.createByErrorMessage("商品信息为空！");
		}
		if (goods.getGoods_id() == 0) {
			if (goodsService.insert(goods) == 1) {
				return ServerResponse.createBySuccessMessage("添加商品成功！");
			}
			return ServerResponse.createByErrorMessage("添加商品失败！");
		} else {
			if (goodsService.update(goods) == 1) {
				return ServerResponse.createBySuccessMessage("更新商品信息成功！");
			}
			return ServerResponse.createByErrorMessage("更新商品信息失败！");
		}

	}

	@RequestMapping("/orderDelete.do")
	@ResponseBody
	public ServerResponse orderDelete(Order order, HttpSession session) {
		if (session.getAttribute("username") == null || session.getAttribute("passwd") == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");
		}
		if (orderService.delete(order) == 1) {
			return ServerResponse.createBySuccessMessage("删除订单成功！");
		}
		return ServerResponse.createByErrorMessage("删除订单失败！");
	}

	@RequestMapping("/orderUpdate.do")
	@ResponseBody
	public ServerResponse orderUpdate(Order order, HttpSession session) {
		if (session.getAttribute("username") == null || session.getAttribute("passwd") == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");
		}
		if (orderService.update(order) == 1) {
			return ServerResponse.createBySuccessMessage("更新订单信息成功！");
		}
		return ServerResponse.createByErrorMessage("更新订单信息失败！");
	}

	@RequestMapping("/orderUpdateOrderState.do")
	@ResponseBody
	public ServerResponse orderUpdateOrderState(Order order, HttpSession session) {
		if (session.getAttribute("username") == null || session.getAttribute("passwd") == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");
		}
		if (orderService.updateOrderState(order) == 1) {
			return ServerResponse.createBySuccessMessage("更新订单状态成功！");
		}
		return ServerResponse.createByErrorMessage("更新订单状态失败！");

	}
}
