package com.KillSystem.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.KillSystem.Service.GoodsService;
import com.KillSystem.common.ServerResponse;
import com.KillSystem.domain.Goods;
import com.github.pagehelper.PageInfo;

@Controller
public class GoodsController {

	@Autowired
	private GoodsService goodsService;

	@RequestMapping("/sort.do")
	public ModelAndView sort(Goods goods, String pageNo, HttpSession session, HttpServletRequest request) {
		if (session.getAttribute("username") == null) {
			return new ModelAndView("fail", null);
		}
		int pageno = 1;
		if (pageNo == null) {
			pageno = 1;
		} else {
			pageno = Integer.parseInt(pageNo);
		}
		session.setAttribute("goods_name", goods.getGoods_name());
		session.setAttribute("goods_price", goods.getGoods_price());
		session.setAttribute("begin_time", goods.getBegin_time());
		PageInfo<Map<String, Goods>> p = goodsService.select(goods, pageno, 4);
		int k = 0;
		while (k < p.getSize()) {
			DateTime begin = new DateTime(p.getList().get(k).get("begin_time"));
			DateTime end = new DateTime(p.getList().get(k).get("end_time"));
			if (end.isAfterNow() && begin.isBeforeNow()) {
				request.setAttribute("msg" + k, "可抢购");
			} else if (begin.isAfterNow()) {
				request.setAttribute("msg" + k, "未上线");
			} else if (end.isBeforeNow()) {
				request.setAttribute("msg" + k, "已下线");
			}
			k++;
		}
		request.setAttribute("totalPageCount", p.getPages());
		request.setAttribute("pageNo", pageno);
		return new ModelAndView("sort", "page", p);
	}

	@RequestMapping("/getGoodsInfo.do")
	public ModelAndView getGoodsInfo(Goods goods, String pageNo, HttpSession session, HttpServletRequest request) {
		goods = goodsService.getGoodsById(goods);
		return new ModelAndView("goodsInfo","thisGoods",goods);
	}

	// todo
	@RequestMapping("/sortNewCount.do")
	@ResponseBody
	public ServerResponse sortNewCount(Goods goods, String pageNo, HttpSession session, HttpServletRequest request) {
		// goods.setCreate_time(new DateTime().now());
		// PageInfo<Map<String,Goods>> p = goodsService.select(goods, 1, 10);
		// int count = p.getList().size();
		// return ServerResponse.createBySuccess("查询最新商品数成功！", count);
		return null;
	}

	// todo
	@RequestMapping("/sortNew.do")
	@ResponseBody
	public ServerResponse sortNew(Goods goods, String pageNo, HttpSession session, HttpServletRequest request) {
		// goods.setCreate_time(new DateTime().now());
		// PageInfo<Map<String,Goods>> p = goodsService.select(goods, 1, 10);
		// return ServerResponse.createBySuccess("查询最新商品成功！", p);
		return null;
	}
}
