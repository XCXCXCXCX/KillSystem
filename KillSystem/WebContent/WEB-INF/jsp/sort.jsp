<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<style>
.div-left{
    float:left;
}
.div-left .top{
	display:block;
	width:auto;
	margin:30px 60px;
}
.box{width:400px;margin:450px auto;}
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
	
	var now = new Date();
	$.post("/KillSystem/getSystemTime.do", {},  
              	            function (response) {  
              	                if (response.status == "0") {
									now = new Date(response.data);
              	                }
              	            }, 'json'); 
	
	var year = now.getFullYear();
	var month = now.getMonth() + 1;
	var day = now.getDay() + 1;
	var hour = now.getHours();
	var minute = now.getMinutes();
	var second = now.getSeconds();
	setInterval(function(){
		$("#year").text(year+"年");
		$("#month").text(month+"月");
		$("#day").text(day+"日");
		$("#hour").text(hour+"时");
		$("#minute").text(minute+"分");
		$("#second").text(second+"秒");
		second = second + 1;
		if(second == 60){
			second = 0;
			minute = minute + 1;
		}
		if(minute == 60){
			minute = 0;
			hour = hour + 1;
		}
		if(hour == 25){
			hour = 1;
			window.location.reload();
		}
	},1000);
	
	if($("#username").text()=="null"){
		window.location.href="/";
	}
	$("#btn1").click(function(){
		if($("#accessibility0").text()=="可抢购"){
			var goods_id = $("#goods_id0").text();
			window.location.href="/KillSystem/getGoodsInfo.do?goods_id=" + goods_id;
		}else{
			alert("商品已下线或还未上线");
		}
	});
	$("#btn2").click(function(){
		if($("#accessibility1").text()=="可抢购"){
			var goods_id = $("#goods_id1").text();
			window.location.href="/KillSystem/getGoodsInfo.do?goods_id=" + goods_id;
		}else{
			alert("商品已下线或还未上线");
		}
		
	});
	$("#btn3").click(function(){
		if($("#accessibility2").text()=="可抢购"){
			var goods_id = $("#goods_id2").text();
			window.location.href="/KillSystem/getGoodsInfo.do?goods_id=" + goods_id;
		}else{
			alert("商品已下线或还未上线");
		}
	});
	$("#logout").click(function(){
		alert("logout");
		window.location.href="/KillSystem/logout.do";
	})
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

<title>搜索页</title>
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
		<span id="year">yyyy</span><span id="month">MM</span><span id="day">dd</span><span id="hour">hh</span ><span id="minute">mm</span><span id="second">ss</span>
	</ul> 

 	<form action="http://localhost/KillSystem/sort.do" method="post">
        商品名:<input type="text" name="goods_name" value="${goods_name}" />
        价格低于(请输入数字):<input type="text" name="goods_price" value="${goods_price}" />
        活动时间早于(格式为yy-MM-dd HH:mm:ss 可省略时分秒):<input type="text" name="begin_time" value="${begin_time}" />
        <input type="submit" value="搜索" />
    </form>
    <div>
    	<c:if test="${page.getList().size() > 0}">
    	<p id="goods_id0" hidden>${page.getList().get(0).get("goods_id")}</p>
    	<p class="div-left">
			<a class="top">${page.getList().get(0).get("goods_name")}</a>
			<a class="top">价格:${page.getList().get(0).get("goods_price")}</a>
			<a class="top">库存:${page.getList().get(0).get("goods_stock")}</a>
			<a class="top">活动开始时间:${page.getList().get(0).get("begin_time")}</a>
			<a class="top">活动结束时间:${page.getList().get(0).get("end_time")}</a>
			<a id="accessibility0" class="top">${msg0}</a>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;<button id="btn1" type="button">立即抢购</button>
		</p>
		</c:if>
		<c:if test="${page.getList().size() > 1}">
		<p id="goods_id1" hidden>${page.getList().get(1).get("goods_id")}</p>
    	<p class="div-left">
    		<a class="top">${page.getList().get(1).get("goods_name")}</a>
			<a class="top">价格:${page.getList().get(1).get("goods_price")}</a>
			<a class="top">库存:${page.getList().get(1).get("goods_stock")}</a>
			<a class="top">活动开始时间:${page.getList().get(1).get("begin_time")}</a>
			<a class="top">活动结束时间:${page.getList().get(1).get("end_time")}</a>
			<a id="accessibility1" class="top">${msg1}</a>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;<button id="btn2" type="button">立即抢购</button>
		</p>
		</c:if>
		<c:if test="${page.getList().size() > 2}">
		<p id="goods_id2" hidden>${page.getList().get(2).get("goods_id")}</p>
		<p class="div-left">
    		<a class="top">${page.getList().get(2).get("goods_name")}</a>
			<a class="top">价格:${page.getList().get(2).get("goods_price")}</a>
			<a class="top">库存:${page.getList().get(2).get("goods_stock")}</a>
			<a class="top">活动开始时间:${page.getList().get(2).get("begin_time")}</a>
			<a class="top">活动结束时间:${page.getList().get(2).get("end_time")}</a>
			<a id="accessibility2" class="top">${msg2}</a>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;<button id="btn3" type="button">立即抢购</button>
		</p>
    	</c:if>
    </div>
    <div class="box" align="center">  
        <font size="2">共 ${totalPageCount} 页</font> <font size="2">第  
            ${pageNo} 页</font> <a href="sort.do?pageNo=1&goods_name=${goods_name}&goods_price=${goods_price}&begin_name=${begin_name}">首页</a>  
        <c:choose>  
            <c:when test="${pageNo - 1 > 0}">  
                <a href="sort.do?pageNo=${pageNo - 1}&goods_name=${goods_name}&goods_price=${goods_price}&begin_name=${begin_name}">上一页</a>  
            </c:when>  
            <c:when test="${pageNo - 1 <= 0}">  
                <a href="sort.do?pageNo=1&goods_name=${goods_name}&goods_price=${goods_price}&begin_name=${begin_name}">上一页</a>  
            </c:when>  
        </c:choose>  
        <c:choose>  
            <c:when test="${totalPageCount==0}">  
                <a href="sort.do?pageNo=${pageNo}&goods_name=${goods_name}&goods_price=${goods_price}&begin_name=${begin_name}">下一页</a>  
            </c:when>  
            <c:when test="${pageNo + 1 < totalPageCount}">  
                <a href="sort.do?pageNo=${pageNo + 1}&goods_name=${goods_name}&goods_price=${goods_price}&begin_name=${begin_name}">下一页</a>  
            </c:when>  
            <c:when test="${pageNo + 1 >= totalPageCount}">  
                <a href="sort.do?pageNo=${totalPageCount}&goods_name=${goods_name}&goods_price=${goods_price}&begin_name=${begin_name}">下一页</a>  
            </c:when>  
        </c:choose>  
        <c:choose>  
            <c:when test="${totalPageCount==0}">  
                <a href="sort.do?pageNo=${pageNo}&goods_name=${goods_name}&goods_price=${goods_price}&begin_name=${begin_name}">尾页</a>  
            </c:when>  
            <c:otherwise>  
                <a href="sort.do?pageNo=${totalPageCount}&goods_name=${goods_name}&goods_price=${goods_price}&begin_name=${begin_name}">尾页</a>  
            </c:otherwise>  
        </c:choose>  
    </div>  
</body>
</html>