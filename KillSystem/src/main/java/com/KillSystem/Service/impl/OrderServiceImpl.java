package com.KillSystem.Service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.KillSystem.DAO.GoodsDao;
import com.KillSystem.DAO.OrderDao;
import com.KillSystem.Service.OrderService;
import com.KillSystem.common.ServerResponse;
import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;
import com.KillSystem.util.FTPUtil;
import com.KillSystem.util.InitFIFOListener;
import com.KillSystem.util.PropertiesUtil;
import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 订单服务实现类
 * 
 * 提供了管理员的增删改查接口
 * 提供了用户的创建订单、创建支付订单、支付订单、支付宝回调接口
 * 
 * 2018年4月5日
 *
 */
@Service("OrderService")
public class OrderServiceImpl implements OrderService{

	
	
	private static  AlipayTradeService tradeService;
    static {

        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private GoodsDao goodsDao;
	
	@Override
	public int insert(Order t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Order order) {
		// TODO Auto-generated method stub
		return orderDao.delete(order);
	}

	@Override
	public int update(Order order) {
		// TODO Auto-generated method stub
		return orderDao.update(order);
	}

	@Override
	public List<Map<String, Order>> select(Order order) {
		// TODO Auto-generated method stub
		return orderDao.select(order);
	}

	@Override
	public int updateOrderState(Order order) {
		// TODO Auto-generated method stub
		return orderDao.updateOrderState(order);
	}

	@Override
	public PageInfo<Map<String, Order>> select(Order order, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Order>> orderMap = select(order);
	    PageInfo<Map<String, Order>> p=new PageInfo<Map<String, Order>>(orderMap);
	    return p;
	}

	@Override
	public long createOrderInRedis(Order order) {
		// TODO Auto-generated method stub
		return orderDao.createOrderInRedis(order);
	}
	
	
	@Override
	public long createPayInRedis(Order order) {
		// TODO Auto-generated method stub
		return orderDao.createPayInRedis(order);
	}
	
	
	@Override
	public String updateOrderPayInRedis(Order order) {
		// TODO Auto-generated method stub
		return orderDao.updateOrderPayInRedis(order);
	}
	
	@Override
	public boolean orderIsExist(Order order) {
		// TODO Auto-generated method stub
		return orderDao.orderIsExist(order);
	}
	
	@Override
	public boolean orderIsExistInRedis(Order order) {
		// TODO Auto-generated method stub
		return orderDao.orderIsExistInRedis(order);
	}
	
	public String getPayState(Order order) {
		String flag = orderDao.getPayState(order);
		if(flag == "nil") {
			return null;
		}else {
			return flag;
		}
	}
	
	public ServerResponse pay(Order order,String path) {
		Map<String ,String> resultMap = Maps.newHashMap();
        resultMap.put("orderNo",order.getOrder_id());
		
		// (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrder_id();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店消费”
        String subject = new StringBuilder().append("KillSystemx系统当面付消费,订单号:").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        Goods goods = new Goods();
        goods.setGoods_id(order.getGoods_id());
        goods = goodsDao.getGoodsById(goods);
        String totalAmount = String.valueOf(goods.getGoods_price());

        // (必填) 付款条码，用户支付宝钱包手机app点击“付款”产生的付款条码
        String authCode = "用户自己的支付宝付款码"; // 条码示例，286648048691290423
        // (可选，根据需要决定是否使用) 订单可打折金额，可以配合商家平台配置折扣活动，如果订单部分商品参与打折，可以将部分商品总价填写至此字段，默认全部商品可打折
        // 如果该值未传入,但传入了【订单总金额】,【不可打折金额】 则该值默认为【订单总金额】- 【不可打折金额】
        //        String discountableAmount = "1.00"; //

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0.0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品3件共20.00元"
        String body = new StringBuilder().append("购买商品1件共").append(totalAmount).append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，线下扫码交易定义为5分钟
        String timeoutExpress = "5m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance(String.valueOf(goods.getGoods_id()), goods.getGoods_name(), Integer.valueOf(totalAmount), 1);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);

        // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.00元，购买了两件
        //GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx牙刷", 500, 2);
        //goodsDetailList.add(goods2);

        //String appAuthToken = "应用授权令牌";//根据真实值填写

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
            //            .setAppAuthToken(appAuthToken)
            .setOutTradeNo(outTradeNo).setSubject(subject)
            .setTotalAmount(totalAmount).setStoreId(storeId)
            .setUndiscountableAmount(undiscountableAmount).setBody(body).setOperatorId(operatorId)
            .setExtendParams(extendParams).setSellerId(sellerId)
            .setGoodsDetailList(goodsDetailList).setTimeoutExpress(timeoutExpress)
        	.setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"));

        // 调用tradePay方法获取当面付应答
        //AlipayF2FPayResult result = service.tradePay(builder);
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预支付成功: )");
                
                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);
                File folder = new File(path);
                if(!folder.exists()){
                    folder.setWritable(true);
                    folder.mkdirs();
                }

                // 需要修改为运行机器上的路径
                //细节细节细节
                String qrPath = String.format(path+"/qr-%s.png",response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png",response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                File targetFile = new File(path,qrFileName);
                try {
                    FTPUtil.uploadFile(Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    log.error("上传二维码异常",e);
                }
                log.info("qrPath:" + qrPath);
                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFile.getName();
                resultMap.put("qrUrl",qrUrl);
                
                //直接测试回调中的代码
                //String flag = orderDao.updateOrderPayInRedis(order);
            	//InitFIFOListener.queue.offer(order);
                
                return ServerResponse.createBySuccess("支付宝预下单成功！！！",resultMap);

            case FAILED:
                log.error("支付宝预下单失败!!!");
                
                //库存加一
                goodsDao.setBackGoodsStock(order);
                //删除订单
                
                //删除支付订单
                
                return ServerResponse.createByErrorMessage("支付宝预下单失败!!!");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
              
                //库存加一
                goodsDao.setBackGoodsStock(order);
                //删除订单

                //删除支付订单
                
                return ServerResponse.createByErrorMessage("系统异常，预下单状态未知!!!");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                
                //库存加一
                goodsDao.setBackGoodsStock(order);
                //删除订单

                //删除支付订单
                
                return ServerResponse.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }
    }
    
    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }
    
    public ServerResponse aliCallback(Map<String,String> params){
    	System.out.println("支付宝回调啦！");
        String orderNo = params.get("out_trade_no");
        String tradeStatus = params.get("trade_status");
        System.out.println(tradeStatus);
        Order order = new Order();
        order.setOrder_id(orderNo);
        order = orderDao.selectByorderIdInRedis(order);
        if(orderDao.getPayState(order) == "nil"){
        	goodsDao.setBackGoodsStock(order);
            return ServerResponse.createByErrorMessage("该支付订单已失效，请重新下单！");
        }
        if(orderDao.getPayState(order) == "1"){
        	return ServerResponse.createBySuccessMessage("支付宝重复调用");
        }
        if("TRADE_SUCCESS".equals(tradeStatus)){
        	String flag = orderDao.updateOrderPayInRedis(order);
        	InitFIFOListener.queue.offer(order);
            return ServerResponse.createBySuccessMessage("订单支付成功");
        }
        return ServerResponse.createBySuccess();
    }

	
	
}
