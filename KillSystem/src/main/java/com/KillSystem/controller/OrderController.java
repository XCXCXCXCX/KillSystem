package com.KillSystem.controller;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.KillSystem.Service.GoodsService;
import com.KillSystem.Service.OrderService;
import com.KillSystem.common.ResponseCode;
import com.KillSystem.common.ServerResponse;
import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;
import com.KillSystem.util.DateTimeUtil;
import com.KillSystem.util.MD5Util;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;



/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 订单controller
 * 包括订单的创建、订单的支付、支付宝回调的接口、支付订单状态的轮询
 * 
 * 2018年4月4日
 *
 */
@Controller
public class OrderController {
	
	private static final Logger log = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private GoodsService goodsService;
	
	//private boolean hasPay = false;
	
	/**
	 * 计时器:创建订单、减少库存后，需要在6分钟之内完成==>预支付==>支付成功，
	 * 		若无法完成后续支付流程，删除订单、撤销库存减少
	 * 目的:防止过多的无效的、长期不支付订单占据秒杀库存量
	 * 测试后发现开启了过多线程所以放弃该方法
	 * 										
	 */		
	/*class MyTask extends TimerTask{

		private Order order;
		
		MyTask(Order order){
			this.order = order;
		}
		
	    @Override
	    public void run() {
	    	if(!hasPay) {
	    		goodsService.incrGoodsStock(order);
	    		System.out.println("超时后撤回库存减一成功！");
	    		if(orderService.deleteOrderInRedis(order) == 0) {
	    			System.out.println("超时后删除订单失败！");
	    		}
	    		if(orderService.deletePayInRedis(order) == 0) {
	    			System.out.println("超时后删除支付订单失败！");
	    		}
	    	}else {
	    		if(orderService.getPayState(order) == "0") {
	    			goodsService.incrGoodsStock(order);
		    		System.out.println("超时后撤回库存减一成功！");
		    		if(orderService.deleteOrderInRedis(order) == 0) {
		    			System.out.println("超时后删除订单失败！");
		    		}
		    		if(orderService.deletePayInRedis(order) == 0) {
		    			System.out.println("超时后删除支付订单失败！");
		    		}
	    		}
	    	}
	    }
	    
	}*/
	
	@RequestMapping("/createOrder.do")
	@ResponseBody
	public ServerResponse createOrder(Order order,HttpServletRequest request,HttpSession session) {
//		if (session.getAttribute("tel_num") == null || session.getAttribute("passwd") == null) {
//			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户登录已过期");
//		}
		
		//1.一个ip5分钟内只接受一次请求 
		//ps.暂未编码，编码基本思路是在Spring管理一个拦截器，重写preHandler接口
		//由于会影响到测试效果，先注释掉
		
		//2.判断商品是否存在及是否是上线状态
		Goods goods = new Goods();
		goods.setGoods_id(order.getGoods_id());
		goods = goodsService.getGoodsById(goods);
		if(goods == null) {
			return ServerResponse.createByErrorMessage("商品不存在！");
		}
		Date begin_time = DateTimeUtil.strToDate(goods.getBegin_time().substring(0, goods.getBegin_time().length() - 2));
		Date end_time = DateTimeUtil.strToDate(goods.getEnd_time().substring(0, goods.getBegin_time().length() - 2));
		Date now = new Date();
		if(!(begin_time.before(now) && end_time.after(now))) {
			return ServerResponse.createByErrorMessage("商品不处于上线状态！");
		}
		//3. 一个用户不允许购买同一商品多次(在mysql中检查)
		//ps:在redis中的检查已经封装到createOrderInRedis方法中
		//由于会影响到测试效果，先注释掉
		if(orderService.orderIsExistByTelnumAndGoodsId(order)) {
			return ServerResponse.createByErrorMessage("用户已购买过该商品！");
		}
		
		//使用MD5算法加密用户信息和当前时间的字符串，构造不重复order_id
		//(1)防止生成相同的订单号导致的创建订单失败
		//(2)避免使用自增id，自增id能产生的订单数有局限性
		String order_id = MD5Util.MD5EncodeUtf8(order.getTel_num()+order.getGoods_id()+DateTime.now().toString());
		order.setOrder_id(order_id);
		order.setTel_num((String)session.getAttribute("tel_num"));
		//4.如果该订单关联商品库存为0，返回“商品已抢光”
		//否则继续进行
		if(!goodsService.checkGoodsStockInRedis(order)) {
			return ServerResponse.createByErrorMessage("商品库存已抢光！秒杀接口已关闭！");
		}
		//5.检查是否存在该订单
		if(orderService.orderIsExistInRedis(order)) {
			log.error("订单已经存在！");
			return ServerResponse.createByErrorMessage("订单已经存在！");
		}
		
		//6.执行创建订单操作
		try {
			
			//6.1.在redis中创建订单，若失败，返回“创建订单失败”
			long flag = orderService.createOrderInRedis(order);
			if(flag == 0) {
				log.error("该订单id已经存在！");
				return ServerResponse.createByErrorMessage("该订单id已经存在！");
			}else if(flag == -1) {
				log.error("redis操作:创建订单失败！");
				return ServerResponse.createByErrorMessage("redis操作:创建订单失败！");
			}/*else if(flag == -2) {
				log.info("用户已创建订单,继续支付！");
				return pay(order,request,session);
			}*/
			
			//6.2.在redis中库存减一，若失败，撤回创建订单操作并返回“创建订单失败”
			if(goodsService.decrGoodsStock(order) < 0) {
				log.error("库存已抢光！");
				return ServerResponse.createByErrorMessage("库存已抢光！");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error("redis中创建订单失败！", e);
			return ServerResponse.createByErrorMessage("创建订单失败！");
		}
		//创建定时器对象
        //Timer t=new Timer();
        //在3秒后执行MyTask类中的run方法
        //t.schedule(new MyTask(order), 360000);
		System.out.println("创建订单成功！");
		return pay(order,request,session);
		
	}
	
	@RequestMapping("/alipay.do")
	public ModelAndView alipay(String order_id,String qrUrl,HttpServletRequest request,HttpSession session) {
		if (session.getAttribute("tel_num") == null || session.getAttribute("passwd") == null) {
			return new ModelAndView("fail");
		}
		Order order = new Order();
		order.setOrder_id(order_id);
		if(!orderService.orderIsExistInRedis(order)) {
			return null;
		}
		Map<String ,String> resultMap = Maps.newHashMap();
        resultMap.put("order_id",order_id);
        resultMap.put("qrUrl",qrUrl);
		return new ModelAndView("alipay","resultMap",resultMap);
	}
	
	@RequestMapping("/pay.do")
	@ResponseBody
	private ServerResponse pay(Order order,HttpServletRequest request,HttpSession session) {
		//ab测试
		if(true)return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户登录已过期");
		
		if(!orderService.orderIsExistInRedis(order)) {
			//撤回库存减一的操作
			goodsService.incrGoodsStock(order);
			return ServerResponse.createByErrorMessage(order.getOrder_id() + "订单已失效，请重新创建订单");
		}
		//todo
		//在redis中创建支付订单，若失败，返回“_pay订单已存在”
		if(orderService.createPayInRedis(order)==0) {
			log.warn("重复创建支付订单:"+order.getOrder_id());
		}
		String path = request.getSession().getServletContext().getRealPath("upload");
        return orderService.pay(order,path);
	
	}
	
	@RequestMapping("/alipayCallback.do")
	@ResponseBody
	public Object alipayCallback(HttpServletRequest request) {
		//todo
		//在request中取到支付宝回调的params
		Map<String,String> params = Maps.newHashMap();

        Map requestParams = request.getParameterMap();
        for(Iterator iter = requestParams.keySet().iterator();iter.hasNext();){
            String name = (String)iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for(int i = 0 ; i <values.length;i++){

                valueStr = (i == values.length -1)?valueStr + values[i]:valueStr + values[i]+",";
            }
            params.put(name,valueStr);
        }
        log.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());

        //由于使用的是RSA2，看源码可以知道默认是使用RSA的，所以这里要进行处理，使用RSA2

        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());

            if(!alipayRSACheckedV2){
                return ServerResponse.createByErrorMessage("非法请求,验证不通过,再恶意请求我就报警找网警了");
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验证回调异常",e);
        }

        ServerResponse serverResponse = orderService.aliCallback(params);
        if(serverResponse.isSuccess()){
            return "success";
        }
        return "failed";
		
	}
	
	@RequestMapping("/payIsSuccess.do")
	@ResponseBody
	public ServerResponse payIsSuccess(Order order,HttpServletRequest request,HttpSession session) {
		if (session.getAttribute("tel_num") == null || session.getAttribute("passwd") == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户登录已过期");
		}
		//查询该订单是否支付成功：在redis中查询key{订单号+pay}的value值是否为1，若为1，返回支付成功，若为0，返回未支付。
		String flag = orderService.getPayState(order);
		if(flag == null) {
			return ServerResponse.createByErrorMessage("该支付订单不存在!");
		}
		boolean data = ("0".equals(flag) ? false : true);
		return ServerResponse.createBySuccess("订单状态:data",data);

	}
	
	
}
