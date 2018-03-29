<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<style>
.goodsImg{
	float:left;
}
.top{
	display:block;
	width:auto;
	margin:30px 60px;
}
.align-center{ 
margin:0 auto; /* 居中 这个是必须的，，其它的属性非必须 */ 
width:500px; /* 给个宽度 顶到浏览器的两边就看不出居中效果了 */ 
text-align:center; /* 文字等内容居中 */ 
} 
</style>
<link rel="stylesheet" href="http://localhost/layui/css/layui.css" media="all">
<script src="http://localhost/layui/layui.js" charset="utf-8"></script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- jQuery -->
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.js"></script>
<script src="https://ciphertrick.com/demo/jquerysession/js/jquerysession.js"></script>

<script language="javascript" type="text/javascript">  
$(function(){
	$("#buy").click(function(){
		var goodsid = ${thisGoods.getGoods_id()};
		var goodsname = $("#goodsname").text();
		var goodsstock = $("#goodsstock").text();
		var goodsprice = $("#goodsprice").text();
		alert(goodsname+","+goodsstock+","+goodsprice);
		window.location.href="getShippingAddress.do?goods_id="+goodsid+"&goods_name="+goodsname+"&goods_stock="+goodsstock+"&goods_price="+goodsprice;
	})
	
	if($("#username").text()=="null"){
		window.location.href="/";
	}
	layui.use('element', function(){
		var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
		//监听导航点击
		element.on('nav(demo)', function(elem){
			//console.log(elem)
			layer.msg(elem.text());
		});
	});
});
</script>

<title>商品信息页</title>
</head>
<body>

	<ul class="layui-nav">
		<li class="layui-nav-item">
			<a href="http://localhost/KillSystem/sort.do">控制台<span class="layui-badge">9</span></a>
		</li>
		<li class="layui-nav-item">
			<a href="http://localhost/KillSystem/sort.do">去抢购<span class="layui-badge-dot"></span></a>
		</li>
		<li class="layui-nav-item">
			<a id="username" href="javascript:;"><img src="http://t.cn/RCzsdCq" class="layui-nav-img"><%=session.getAttribute("username")%></a>
				<dl class="layui-nav-child">
				<dd><a id="" href="javascript:;">修改信息</a></dd>
				<dd><a id="logout" href="javascript:;">退出登陆</a></dd>
				</dl>
		</li>
	</ul> 
 		
 	<div class="align-center">
 		<a class="top">-</a>
 		商品名:<a id="goodsname" class="top">${thisGoods.getGoods_name()}</a>
		价格:<a id="goodsprice" class="top">${thisGoods.getGoods_price()}</a>
		库存:<a id="goodsstock" class="top">${thisGoods.getGoods_stock()}</a>
		活动开始时间:<a class="top">${thisGoods.getBegin_time()}</a>
		活动结束时间:<a class="top">${thisGoods.getEnd_time()}</a>
		<a id="buy" class="top">!!立即抢购!!</a>
		<a class="top">-</a>
 	</div>
 	
 	<div class="align-center">
 		${thisGoods.getGoods_info()}
 	</div>
</body>
</html>