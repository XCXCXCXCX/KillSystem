<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="no-js">
    
    <head>
        <title>Admin Order Page</title>
        <!-- Bootstrap -->
        <link href="http://localhost/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
        <link href="http://localhost/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" media="screen">
        <link href="http://localhost/vendors/easypiechart/jquery.easy-pie-chart.css" rel="stylesheet" media="screen">
        <link href="http://localhost/assets/styles.css" rel="stylesheet" media="screen">
        

    	<link media="all" rel="stylesheet" type="text/css" href="http://localhost/assets/styles/app.css" />
    	<link media="all" rel="stylesheet" type="text/css" href="http://localhost/assets/styles/simditor.css" />

    	<script type="text/javascript" src="http://localhost/assets/scripts/jquery.min.js"></script>
    	<script type="text/javascript" src="http://localhost/assets/scripts/mobilecheck.js"></script>
        
        <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
            <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
        <script src="http://localhost/vendors/modernizr-2.6.2-respond-1.1.0.min.js"></script>
        <script>
        window.onload=function(){
        	var delHandler = function(counter) {
        		  return function(){
        			  if(confirm("真的要删除吗?")){
              			var order_id = $("#order" + counter + "id").text();
              			alert(order_id);
                  		if (order_id == ""){
                  			alert("order_id为空");
                  			return;
                  		}
                  		$.post("/KillSystem/admin/orderDelete.do", { order_id: order_id },  
                  	            function (response) {  
                  					alert(response.msg);
                  	                if (response.status == "0") {  
                  						window.location.href="/KillSystem/admin/toAdminOrder.do";
                  	                }
                  	            }, 'json');  
              		}
              		else{
              		}    
        		  };
        		};
        	var updHandler = function(counter) {
        		return function(){
            		var order_id = $("#order" + counter + "id").text();
            		if (order_id == ""){
            			alert("order_id为空");
            			return;
            		}
            		$("#inputOrderId").val(order_id);
            		$("#inputTelNum").val($("#tel" + counter + "num").text());
            		$("#inputAddress").val($("#address" + counter).text());
            		$("#inputGoodsId").val($("#goods" + counter + "id").text());
            		$("#inputCreateTime").val($("#create" + counter + "time").text());
            		$("#inputIsSuccess").val($("#is" + counter + "success").text());
      		  };
        	};
        	var updsHandler = function(counter) {
        		return function(){
        			var order_id = $("#order" + counter + "id").text();
        			var is_success = $("#is" + counter + "success").text();
            		$("#inputOrderId").val(order_id);
            		$.post("/KillSystem/admin/orderUpdateOrderState.do", { order_id: order_id, is_success: is_success },  
              	            function (response) {  
              					alert(response.msg);
              	                if (response.status == "0") {  
              						window.location.href="/KillSystem/admin/toAdminOrder.do";
              	                }
              	            }, 'json'); 
      		  };
        	}
        	for(var i=0; i<10; i++) {
        		$("#del" + i).click(delHandler(i));
        		$("#upd" + i).click(updHandler(i));
        		$("#upds" + i).click(updsHandler(i));
        	} 
        	$("#submitUpdate").click(function(){
        		var order_id = $("#inputOrderId").val();
        		var tel_num = $("#inputTelNum").val();
        		var address = $("#inputAddress").val();
        		$.post("/KillSystem/admin/orderUpdate.do", {order_id: order_id, tel_num: tel_num, address: address },  
          	            function (response) {  
          					alert(response.msg);
          	                if (response.status == "0") {  
          						window.location.href="/KillSystem/admin/toAdminOrder.do";
          	                }
          	            }, 'json'); 
        	});
        	$("#submitSort").click(function(){
        		var order_id = $("#inputOrderId").val();
        		var tel_num = $("#inputTelNum").val();
        		var goods_id = $("#inputGoodsId").val();
        		var create_time = $("#inputCreateTime").val();
        		var is_success = $("#inputIsSuccess").val();
        		var url = "/KillSystem/admin/toAdminOrder.do?";
        		if (order_id != null && order_id != ""){
        			url = url + "order_id=" + order_id + "&";
        		}
        		if (tel_num != null && tel_num != ""){
        			url = url + "tel_num=" + tel_num +"&";
        		}
        		if (goods_id != null && goods_id != ""){
        			url = url + "goods_id=" + goods_id +"&";
        		}
        		if (create_time != null && create_time != ""){
        			url = url + "create_time=" + create_time +"&";
        		}
        		if (is_success != null && is_success != ""){
        			url = url + "is_success=" + is_success +"&";
        		}
        		window.location.href = url;
        	});
        	$("#inputCreateTime").click(function(){
        		if($("#inputCreateTime").val()==null||$("#inputCreateTime").val()==""){
        			$("#inputCreateTime").val("2018-01-01 00:00:00.00");
        		}
        		
        	});
        	
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
        		$("#year").text(year+"-");
        		$("#month").text(month+"-");
        		$("#day").text(day+" ");
        		$("#hour").text(hour+":");
        		$("#minute").text(minute+":");
        		$("#second").text(second+".0");
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
        	
        };
        </script>
    </head>
    
    <body>
    
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span3" id="sidebar">
                    <ul class="nav nav-list bs-docs-sidenav nav-collapse collapse">
                        <li>
                            <a href="toAdminHome.do"><i class="icon-chevron-right"></i> 商品管理</a>
                        </li>
                        <li class="active">
                            <a href="toAdminOrder.do"><i class="icon-chevron-right"></i> 订单管理</a>
                        </li>
                        <li>
                            <a href="#"><span class="badge badge-important pull-right">83</span> Errors</a>
                        </li>
                    </ul>
                </div>
                
                <!--/span-->
                <div class="span9" id="content">
                	<c:if test="${success < 1}">
                    <div class="row-fluid">
                        <div class="alert alert-success">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
                            <h4>Success</h4>
                        	The operation completed successfully</div>
                    	</div>
                    </c:if>
                    <div class="row-fluid">
                    <span id="year">yyyy</span><span id="month">MM</span><span id="day">dd</span><span id="hour">hh</span ><span id="minute">mm</span><span id="second">ss</span>
                        <!-- block -->
                        <div class="block">
                            <div class="navbar navbar-inner block-header">
                                <div class="muted pull-left">订单表</div>
                            </div>
                            <div class="block-content collapse in">
                                <div class="span12">
  									<table class="table">
						              <thead>
						                <tr>
						                  <th>order_id</th>
						                  <th>tel_num</th>
						                  <th>address</th>
						                  <th>goods_id</th>
						                  <th>create_time</th>
						                  <th>is_success</th>
						                  <th>信息修改</th>
						                  <th>删除</th>
						                  <th>状态修改</th>
						                </tr>
						              </thead>
						              <tbody>
						              	<c:if test="${page.getList().size() > 0}">
						                <tr>
						                  <td id="order0id">${page.getList().get(0).get("order_id")}</td>
						                  <td id="tel0num">${page.getList().get(0).get("tel_num")}</td>
						                  <td id="address0">${page.getList().get(0).get("address")}</td>
						                  <td id="goods0id">${page.getList().get(0).get("goods_id")}</td>
						                  <td id="create0time">${page.getList().get(0).get("create_time")}</td>
						                  <td id="is0success">${page.getList().get(0).get("is_success")}</td>
						                  <td><a id="upd0" href="#">update</a></td>
						                  <td><a id="del0" href="#">delete</a></td>
						                  <td><a id="upds0" href="#">updateOrderState</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 1}">
						                <tr>
						                  <td id="order1id">${page.getList().get(1).get("order_id")}</td>
						                  <td id="tel1num">${page.getList().get(1).get("tel_num")}</td>
						                  <td id="address1">${page.getList().get(1).get("address")}</td>
						                  <td id="goods1id">${page.getList().get(1).get("goods_id")}</td>
						                  <td id="create1time">${page.getList().get(1).get("create_time")}</td>
						                  <td id="is1success">${page.getList().get(1).get("is_success")}</td>
						                  <td><a id="upd1" href="#">update</a></td>
						                  <td><a id="del1" href="#">delete</a></td>
						                  <td><a id="upds1" href="#">updateOrderState</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 2}">
						                <tr>
						                  <td id="order2id">${page.getList().get(2).get("order_id")}</td>
						                  <td id="tel2num">${page.getList().get(2).get("tel_num")}</td>
						                  <td id="address2">${page.getList().get(2).get("address")}</td>
						                  <td id="goods2id">${page.getList().get(2).get("goods_id")}</td>
						                  <td id="create2time">${page.getList().get(2).get("create_time")}</td>
						                  <td id="is2success">${page.getList().get(2).get("is_success")}</td>
						                  <td><a id="upd2" href="#">update</a></td>
						                  <td><a id="del2" href="#">delete</a></td>
						                  <td><a id="upds2" href="#">updateOrderState</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 3}">
						                <tr>
						                  <td id="order3id">${page.getList().get(3).get("order_id")}</td>
						                  <td id="tel3num">${page.getList().get(3).get("tel_num")}</td>
						                  <td id="address3">${page.getList().get(3).get("address")}</td>
						                  <td id="goods3id">${page.getList().get(3).get("goods_id")}</td>
						                  <td id="create3time">${page.getList().get(3).get("create_time")}</td>
						                  <td id="is3success">${page.getList().get(3).get("is_success")}</td>
						                  <td><a id="upd3" href="#">update</a></td>
						                  <td><a id="del3" href="#">delete</a></td>
						                  <td><a id="upds3" href="#">updateOrderState</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 4}">
						                <tr>
						                  <td id="order4id">${page.getList().get(4).get("order_id")}</td>
						                  <td id="tel4num">${page.getList().get(4).get("tel_num")}</td>
						                  <td id="address4">${page.getList().get(4).get("address")}</td>
						                  <td id="goods4id">${page.getList().get(4).get("goods_id")}</td>
						                  <td id="create4time">${page.getList().get(4).get("create_time")}</td>
						                  <td id="is4success">${page.getList().get(4).get("is_success")}</td>
						                  <td><a id="upd4" href="#">update</a></td>
						                  <td><a id="del4" href="#">delete</a></td>
						                  <td><a id="upds4" href="#">updateOrderState</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 5}">
						                <tr>
						                  <td id="order5id">${page.getList().get(5).get("order_id")}</td>
						                  <td id="tel5num">${page.getList().get(5).get("tel_num")}</td>
						                  <td id="address5">${page.getList().get(5).get("address")}</td>
						                  <td id="goods5id">${page.getList().get(5).get("goods_id")}</td>
						                  <td id="create5time">${page.getList().get(5).get("create_time")}</td>
						                  <td id="is5success">${page.getList().get(5).get("is_success")}</td>
						                  <td><a id="upd5" href="#">update</a></td>
						                  <td><a id="del5" href="#">delete</a></td>
						                  <td><a id="upds5" href="#">updateOrderState</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 6}">
						                <tr>
						                  <td id="order6id">${page.getList().get(6).get("order_id")}</td>
						                  <td id="tel6num">${page.getList().get(6).get("tel_num")}</td>
						                  <td id="address6">${page.getList().get(6).get("address")}</td>
						                  <td id="goods6id">${page.getList().get(6).get("goods_id")}</td>
						                  <td id="create6time">${page.getList().get(6).get("create_time")}</td>
						                  <td id="is6success">${page.getList().get(6).get("is_success")}</td>
						                  <td><a id="upd6" href="#">update</a></td>
						                  <td><a id="del6" href="#">delete</a></td>
						                  <td><a id="upds6" href="#">updateOrderState</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 7}">
						                <tr>
						                  <td id="order7id">${page.getList().get(7).get("order_id")}</td>
						                  <td id="tel7num">${page.getList().get(7).get("tel_num")}</td>
						                  <td id="address7">${page.getList().get(7).get("address")}</td>
						                  <td id="goods7id">${page.getList().get(7).get("goods_id")}</td>
						                  <td id="create7time">${page.getList().get(7).get("create_time")}</td>
						                  <td id="is7success">${page.getList().get(7).get("is_success")}</td>
						                  <td><a id="upd7" href="#">update</a></td>
						                  <td><a id="del7" href="#">delete</a></td>
						                  <td><a id="upds7" href="#">updateOrderState</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 8}">
						                <tr>
						                  <td id="order8id">${page.getList().get(8).get("order_id")}</td>
						                  <td id="tel8num">${page.getList().get(8).get("tel_num")}</td>
						                  <td id="address8">${page.getList().get(8).get("address")}</td>
						                  <td id="goods8id">${page.getList().get(8).get("goods_id")}</td>
						                  <td id="create8time">${page.getList().get(8).get("create_time")}</td>
						                  <td id="is8success">${page.getList().get(8).get("is_success")}</td>
						                  <td><a id="upd8" href="#">update</a></td>
						                  <td><a id="del8" href="#">delete</a></td>
						                  <td><a id="upds8" href="#">updateOrderState</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 9}">
						                <tr>
						                  <td id="order9id">${page.getList().get(9).get("order_id")}</td>
						                  <td id="tel9num">${page.getList().get(9).get("tel_num")}</td>
						                  <td id="address9">${page.getList().get(9).get("address")}</td>
						                  <td id="goods9id">${page.getList().get(9).get("goods_id")}</td>
						                  <td id="create9time">${page.getList().get(9).get("create_time")}</td>
						                  <td id="is9success">${page.getList().get(9).get("is_success")}</td>
						                  <td><a id="upd9" href="#">update</a></td>
						                  <td><a id="del9" href="#">delete</a></td>
						                  <td><a id="upds9" href="#">updateOrderState</a></td>
						                </tr>
						                </c:if>
						     
						                <tr>
						                  <td><input id="inputOrderId" style="width:250px;" type="text" name="order_id" /></td>
						                  <td><input id="inputTelNum" style="width:60px;" type="text" name="tel_num" /></td>
						                  <td><input id="inputAddress" style="width:60px;" type="text" name="address" /></td>
						                  <td><input id="inputGoodsId" style="width:60px;" type="text" name="goods_id" /></td>
						                  <td><input id="inputCreateTime" style="width:90px;" type="text" name="create_time" /></td>
						                  <td><input id="inputIsSuccess" style="width:90px;" type="text" name="is_success" /></td>
						                  <td><a id="submitUpdate"/>提交修改</a></td>
						                  <td><a id="submitSort"/>搜索</a></td>
						                </tr>
						                
						              </tbody>
						            </table>
						            

						            <div class="box" align="center">  
        <font size="2">共 ${totalPageCount} 页</font> <font size="2">第  
            ${pageNo} 页</font> <a href="toAdminOrder.do?pageNo=1">首页</a>  
        <c:choose>  
            <c:when test="${pageNo - 1 > 0}">  
                <a href="toAdminOrder.do?pageNo=${pageNo - 1}">上一页</a>  
            </c:when>  
            <c:when test="${pageNo - 1 <= 0}">  
                <a href="toAdminOrder.do?pageNo=1">上一页</a>  
            </c:when>  
        </c:choose>  
        <c:choose>  
            <c:when test="${totalPageCount==0}">  
                <a href="toAdminOrder.do?pageNo=${pageNo}">下一页</a>  
            </c:when>  
            <c:when test="${pageNo + 1 < totalPageCount}">  
                <a href="toAdminOrder.do?pageNo=${pageNo + 1}">下一页</a>  
            </c:when>  
            <c:when test="${pageNo + 1 >= totalPageCount}">  
                <a href="toAdminOrder.do?pageNo=${totalPageCount}">下一页</a>  
            </c:when>  
        </c:choose>  
        <c:choose>  
            <c:when test="${totalPageCount==0}">  
                <a href="toAdminOrder.do?pageNo=${pageNo}">尾页</a>  
            </c:when>  
            <c:otherwise>  
                <a href="toAdminOrder.do?pageNo=${totalPageCount}">尾页</a>  
            </c:otherwise>  
        </c:choose>  
    </div>  
                                </div>
                            </div>
                        </div>
                        <!-- /block -->
                </div>
            </div>
            <hr>



<script type="text/javascript" src="http://localhost/assets/scripts/module.js"></script>
<script type="text/javascript" src="http://localhost/assets/scripts/uploader.js"></script>
<script type="text/javascript" src="http://localhost/assets/scripts/hotkeys.js"></script>
<script type="text/javascript" src="http://localhost/assets/scripts/simditor.js"></script>
<script type="text/javascript" src="http://localhost/assets/scripts/page-demo.js"></script>
            
            
            <footer>
                <p>&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&copy;Vincent Gabriel 2013 - More Templates <a href="#" target="_blank" title="#">#</a>
            </footer>
        </div>
        <!--/.fluid-container-->
        <script src="http://localhost/vendors/jquery-1.9.1.min.js"></script>
        <script src="http://localhost/bootstrap/js/bootstrap.min.js"></script>
        <script src="http://localhost/vendors/easypiechart/jquery.easy-pie-chart.js"></script>
        <script src="http://localhost/assets/scripts.js"></script>
    </body>

</html>