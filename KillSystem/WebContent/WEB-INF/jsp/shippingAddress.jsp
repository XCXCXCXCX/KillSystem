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
<link rel="stylesheet" type="text/css" href="http://localhost/css2/style.css">
<link rel="stylesheet" type="text/css" href="http://localhost/css2/css.css">
<script type="text/javascript" src="http://localhost/js/jquery-1.7.2.min.js"></script>
<script src="http://localhost/layui/layui.js" charset="utf-8"></script>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
		<title>我的收货地址</title>
		<script type="text/javascript">
			$(function() {
				function getParam(paramName) { 
				    paramValue = "", isFound = !1; 
				    if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) { 
				        arrSource = unescape(this.location.search).substring(1, this.location.search.length).split("&"), i = 0; 
				        while (i < arrSource.length && !isFound) arrSource[i].indexOf("=") > 0 && arrSource[i].split("=")[0].toLowerCase() == paramName.toLowerCase() && (paramValue = arrSource[i].split("=")[1], isFound = !0), i++ 
				    } 
				    return paramValue == "" && (paramValue = null), paramValue 
				} 
				var selectedAddressId = 0;
				var delHandler = function(counter) {
	        		  return function(){
	        			  if(confirm("真的要删除吗?")){
	              			var address_id = $("#addressid" + counter).text();
	                  		if (address_id == ""){
	                  			alert("address_id为空");
	                  			return;
	                  		}
	                  		$.post("/KillSystem/deleteShippingAddress.do", { address_id: address_id },  
	                  	            function (response) {  
	                  					alert(response.msg);
	                  	                if (response.status == "0") {  
	                  						window.location.href="/KillSystem/getShippingAddress.do";
	                  	                }
	                  	            }, 'json');  
	              			}  
	        		  };
	        		};
	        	var selectedAddressIdHandler = function(counter){
	        		return function(){
	        				selectedAddressId = $("#addressid" + counter).text();
	        		  };
	        	};
	        	for(var i=0; i<5; i++) {
	            	$("#selectedAddressId" + i).click(selectedAddressIdHandler(i));
	            }  
	            for(var i=0; i<5; i++) {
	            	$("#del" + i).click(delHandler(i));
	            }  
				$("#ins").click(function(){
					var user_id = $("#userid").text();
					var address = $("#address").val();
					var tel_num = $("#tel_num").val();
					var name = $("#name_").val();
					alert(user_id+","+address+","+tel_num+","+name);
					$.post("/KillSystem/insertShippingAddress.do", { user_id: user_id, address: address, tel_num: tel_num, name: name },  
              	            function (response) {  
              					alert(response.msg);
              	                if (response.status == "0") {  
              						window.location.href="/KillSystem/getShippingAddress.do";
              	                }
              	            }, 'json');  
				})
				$("#toAlipay").click(function(){
					//tel_num | address | goods_id | create_time
					var shippingAddressId = selectedAddressId;
					var goods_id = getParam("goods_id");
					if(shippingAddressId==0||shippingAddressId==null){
						alert("请选择收货地址!");
					}else{
					if(goods_id==0||goods_id==null){
						alert("未选择商品!");
					}else{
						$.post("/KillSystem/pay.do", { address_id: shippingAddressId, goods_id: goods_id},  
	              	            function (response) {  
	              					alert(response.msg);
	              	                if (response.status == "0") {  
	              						alert(response.data);
	              	                }
	              	            }, 'json'); 
					}
					}
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
				var region = $("#region");
				var address = $("#address");
				var number_this = $("#number_this");
				var name = $("#name_");
				var phone = $("#phone");
				$("#sub_setID").click(function() {
					var input_out = $(".input_style");
					for (var i = 0; i <= input_out.length; i++) {
						if ($(input_out[i]).val() == "") {
							$(input_out[i]).css("border", "1px solid red");
							return false;
						} else {
							$(input_out[i]).css("border", "1px solid #cccccc");
						}
					}
				});
				var span_momey = $(".span_momey");
				var b = 0;
				for (var i = 0; i < span_momey.length; i++) {
					b += parseFloat($(span_momey[i]).html());
				}
				var out_momey = $(".out_momey");
				out_momey.html(b);
				$(".shade_content").hide();
				$(".shade").hide();
				$('.nav_mini ul li').hover(function() {
					$(this).find('.two_nav').show(100);
				}, function() {
					$(this).find('.two_nav').hide(100);
				})
				$('.left_nav').hover(function() {
					$(this).find('.nav_mini').show(100);
				}, function() {
					$(this).find('.nav_mini').hide(100);
				})
				$('#jia').click(function() {
					$('input[name=num]').val(parseInt($('input[name=num]').val()) + 1);
				})
				$('#jian').click(function() {
					$('input[name=num]').val(parseInt($('input[name=num]').val()) - 1);
				})
				$('.Caddress .add_mi').click(function() {
					alert(selectedAddressId);
					$(this).css('background', 'url("http://localhost/images/mail_1.jpg") no-repeat').siblings('.add_mi').css('background', 'url("http://localhost/images/mail.jpg") no-repeat');
				})
			})
			var x = Array();

			function func(a, b) {
				x[b] = a.html();
				alert(x)
				a.css('border', '2px solid #f00').siblings('.min_mx').css('border', '2px solid #ccc');
			}

			function onclick_close() {
				var shade_content = $(".shade_content");
				var shade = $(".shade");
				if (confirm("确认关闭么！此操作不可恢复")) {
					shade_content.hide();
					shade.hide();
				}
			}

			function onclick_open() {
				$(".shade_content").show();
				$(".shade").show();
				var input_out = $(".input_style");
				for (var i = 0; i <= input_out.length; i++) {
					if ($(input_out[i]).val() != "") {
						$(input_out[i]).val("");
					}
				}
			}

			function onclick_remove(r) {
				if (confirm("确认删除么！此操作不可恢复")) {
					var out_momey = $(".out_momey");
					var input_val = $(r).parent().prev().children().eq(1).val();
					var span_html = $(r).parent().prev().prev().children().html();
					var out_add = parseFloat(input_val).toFixed(2) * parseFloat(span_html).toFixed(2);
					var reduce = parseFloat(out_momey.html()).toFixed(2)- parseFloat(out_add).toFixed(2);
					console.log(parseFloat(reduce).toFixed(2));
					out_momey.text(parseFloat(reduce).toFixed(2))
					$(r).parent().parent().remove();
				}
			}

			function onclick_btnAdd(a) {
				alert("只能抢购一个！");
			}

			function onclick_reduce(b) {
				var out_momey = $(".out_momey");
				var input_ = $(b).next();
				var input_val = $(b).next().val();
				if (input_val <= 1) {
					alert("商品个数不能小于1！")
				} else {
					var input_add = parseInt(input_val) - 1;
					input_.val(input_add);
					var btn_momey = parseFloat($(b).parent().prev().children().html());
					var out_momey_float = parseFloat(out_momey.html()) - btn_momey;
					out_momey.text(out_momey_float.toFixed(2));
				}
			}
		</script>
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
				<p id="userid" hidden><%=session.getAttribute("tel_num")%></p>
				<dl class="layui-nav-child">
				<dd><a id="" href="javascript:;">修改信息</a></dd>
				<dd><a id="logout" href="javascript:;">退出登陆</a></dd>
				</dl>
		</li>
	</ul> 
		
		<div class="Caddress">
			<input id="selectedAddressId" type="hidden" value=""/>
			<div class="open_new">
				<button class="open_btn" onclick="javascript:onclick_open();">使用新地址</button>
			</div>
			<c:if test="${page.getList().size() > 0}">
	    	<div id="selectedAddressId0" class="add_mi">
				<button id="del0">移除</button>
				<p id="addressid0" hidden>${page.getList().get(0).get("address_id")}</p>
				<p style="border-bottom:1px dashed #ccc;line-height:28px;">${page.getList().get(0).get("name")}</p>
				<p>${page.getList().get(0).get("address")},${page.getList().get(0).get("tel_num")}</p>
			</div>
			</c:if>
			<c:if test="${page.getList().size() > 1}">
			<div id="selectedAddressId1" class="add_mi">
				<button id="del1">移除</button>
				<p id="addressid1" hidden>${page.getList().get(1).get("address_id")}</p>
				<p style="border-bottom:1px dashed #ccc;line-height:28px;">${page.getList().get(1).get("name")}</p>
				<p>${page.getList().get(1).get("address")},${page.getList().get(1).get("tel_num")}</p>
			</div>
			</c:if>
			<c:if test="${page.getList().size() > 2}">
			<div id="selectedAddressId2" class="add_mi">
				<button id="del2">移除</button>
				<p id="addressid2" hidden>${page.getList().get(1).get("address_id")}</p>
				<p style="border-bottom:1px dashed #ccc;line-height:28px;">${page.getList().get(2).get("name")}</p>
				<p>${page.getList().get(2).get("address")},${page.getList().get(2).get("tel_num")}</p>
			</div>
			</c:if>
			<c:if test="${page.getList().size() > 3}">
			<div id="selectedAddressId3" class="add_mi">
				<button id="del3">移除</button>
				<p id="addressid3" hidden>${page.getList().get(1).get("address_id")}</p>
				<p style="border-bottom:1px dashed #ccc;line-height:28px;">${page.getList().get(3).get("name")}</p>
				<p>${page.getList().get(3).get("address")},${page.getList().get(3).get("tel_num")}</p>
			</div>
			</c:if>
			<c:if test="${page.getList().size() > 4}">
			<div id="selectedAddressId4" class="add_mi">
				<button id="del4">移除</button>
				<p id="addressid4" hidden>${page.getList().get(1).get("address_id")}</p>
				<p style="border-bottom:1px dashed #ccc;line-height:28px;">${page.getList().get(4).get("name")}</p>
				<p>${page.getList().get(4).get("address")},${page.getList().get(4).get("tel_num")}</p>
			</div>
			</c:if>
		</div>

		<div class="shopping_content">
			<div class="shopping_table">
				<table border="1" bordercolor="#cccccc" cellspacing="0" cellpadding="0" style="width: 100%; text-align: center;">
					<tr>
						<th>商品名称</th>
						<th>商品库存</th>
						<th>商品属性</th>
						<th>商品价格</th>
						<th>商品数量</th>
						<th>商品操作</th>
					</tr>
					<tr>
						<td>
							${goods_name}
						</td>
						<td><span>${goods_stock}</span></td>
						<td>
							无
						</td>
						<td>
							<span class="span_momey">${goods_price}</span>
						</td>
						<td>
							<button class="btn_reduce" onclick="javascript:onclick_reduce(this);">-</button>
							<input class="momey_input" type="" name="" id="" value="1" disabled="disabled" />
							<button class="btn_add" onclick="javascript:onclick_btnAdd(this);">+</button>
						</td>
						<td>
							<button class="btn_r" onclick="javascript:onclick_remove(this);">删除</button>
						</td>
					</tr>
				
				<div class="" style="width: 100%; text-align: right; margin-top: 10px;">
					<div class="div_outMumey" style="float: left;">
						总价：<span class="out_momey">${goods_price}</span>
					</div>
					<button id="toAlipay" class="btn_closing">结算</button>
				</div>
			</div>
		</div>

		<div class="shade">
		</div>

		<div class="shade_content">
			<div class="col-xs-12 shade_colse">
				<button onclick="javascript:onclick_close();">x</button>
			</div>
			<div class="nav shade_content_div">
				<div class="col-xs-12 shade_title">
					新增收货地址
				</div>
				<div class="col-xs-12 shade_from">
					<div class="col-xs-12">
						<span class="span_style" id="">详细地址</span>
						<input class="input_style" type="" name="address" id="address" value="" placeholder="&nbsp;&nbsp;请输入您的详细地址" />
					</div>
					<div class="col-xs-12">
						<span class="span_style" class="span_sty" id="">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名</span>
						<input class="input_style" type="" name="name" id="name_" value="" placeholder="&nbsp;&nbsp;请输入您的姓名" />
					</div>
					<div class="col-xs-12">
						<span class="span_style" id="">手机号码</span>
						<input class="input_style" type="" name="tel_num" id="tel_num" value="" placeholder="&nbsp;&nbsp;请输入您的手机号码" />
					</div>
					<div class="col-xs-12">
						<input class="btn_remove" type="button" id="" onclick="javascript:onclick_close();" value="取消" />
						<input type="submit" class="sub_set" id="ins" value="提交" />
					</div>
				</div>
			</div>
		</div>

	</body>

</html>