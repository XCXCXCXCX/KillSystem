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
import com.KillSystem.common.ResponseCode;
import com.KillSystem.common.ServerResponse;
import com.KillSystem.domain.Goods;
import com.github.pagehelper.PageInfo;


/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 商品controller
 * 包括商品的搜索，查看商品详情，最新商品数和最新商品条目的查询
 * 
 * 2018年4月5日
 *
 */
@Controller
public class GoodsController {

	@Autowired
	private GoodsService goodsService;

	@RequestMapping("/sort.do")
	public ModelAndView sort(Goods goods, String pageNo, HttpSession session, HttpServletRequest request) {
		if (session.getAttribute("tel_num") == null || session.getAttribute("passwd") == null) {
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
		PageInfo<Map<String, Goods>> p = goodsService.select(goods, pageno, 3);
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
		if (session.getAttribute("tel_num") == null || session.getAttribute("passwd") == null) {
			return new ModelAndView("fail", null);
		}
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
}
