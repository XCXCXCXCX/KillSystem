<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="no-js">
    
    <head>
        <title>Admin Home Page</title>
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
              			var goods_id = $("#goods" + counter + "id").text();
                  		if (goods_id == ""){
                  			alert("goodsid为空");
                  			return;
                  		}
                  		$.post("/KillSystem/admin/adminDelete.do", { goods_id: goods_id },  
                  	            function (response) {  
                  					alert(response.msg);
                  	                if (response.status == "0") {  
                  						window.location.href="/KillSystem/admin/toAdminHome.do";
                  	                }
                  	            }, 'json');  
              		}
        		  };
        		};
        	var updHandler = function(counter) {
        		return function(){
            		var goods_id = $("#goods" + counter + "id").text();
            		if (goods_id == ""){
            			alert("goodsid为空");
            			return;
            		}
            		$("#inputGoodsId").val(goods_id);
            		$("#inputGoodsName").val($("#goods" + counter + "name").text());
            		$("#inputGoodsPrice").val($("#goods" + counter + "price").text());
            		$("#inputGoodsStock").val($("#goods" + counter + "stock").text());
            		$("#inputBeginTime").val($("#begin" + counter + "time").text());
            		$("#inputEndTime").val($("#end" + counter + "time").text());
        		};
        	}; 
        	var infoHandler = function(counter) {
        		return function(){
        			var goods_id = $("#goods" + counter + "id").text();
            		$("#inputGoodsId").val(goods_id);
            		$("#whichGoods").val(goods_id);
            		document.getElementById("editText").style.display="";
      		  };
        	}
        	for(var i=0; i<10; i++) {
        		$("#del" + i).click(delHandler(i));
        		$("#upd" + i).click(updHandler(i));
        		$("#info" + i).click(infoHandler(i));
        	}  
        	var editor = new Simditor({ 
        		textarea: $('#txt-content'),
        		placeholder: '这里输入文字...',
        	    toolbar: toolbar,
        	    pasteImage: true,
        	    defaultImage: '/simditor/images/image.png',
        		upload: {
                url: '/KillSystem/admin/upload.do',
                params: null,
                fileKey: "upload_file",
                connectionCount: 1,
                leaveConfirm: "正在上传,确定要取消上传文件吗?"
              	}
        	});
        	$("#infoSubmit").click(function(){
        		var goods_id = $("#inputGoodsId").val();
        		$("#inputGoodsId").val(goods_id);
        		$("#whichGoods").val(goods_id);
        		$.post("/KillSystem/admin/addGoodsInfo.do", { goods_id: goods_id, goods_info: editor.getValue() },  
        	            function (response) {  
        					alert(response.msg);
        					editor.setValue('');
        					window.location.href="#";
        	            }, 'json');  
        	});
        	$("#inputBeginTime").click(function(){
        		if($("#inputBeginTime").val()==null||$("#inputBeginTime").val()==""){
        			$("#inputBeginTime").val("2018-01-01 00:00:00.00");
        		}
        		
        	});
            $("#inputEndTime").click(function(){
        		if($("#inputEndTime").val()==null||$("#inputEndTime").val()==""){
        			$("#inputEndTime").val("2018-01-01 00:00:00.00");
        		}
        		
        	});
        };
        
        
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
        
        </script>
    </head>
    
    <body>
    
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span3" id="sidebar">
                    <ul class="nav nav-list bs-docs-sidenav nav-collapse collapse">
                        <li class="active">
                            <a href="toAdminHome.do"><i class="icon-chevron-right"></i> 商品管理</a>
                        </li>
                        <li>
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
                                <div class="muted pull-left">商品表</div>
                            </div>
                            <div class="block-content collapse in">
                                <div class="span12">
  									<table class="table">
						              <thead>
						                <tr>
						                  <th>goods_id</th>
						                  <th>goods_name</th>
						                  <th>goods_price</th>
						                  <th>goods_stock</th>
						                  <th>begin_time</th>
						                  <th>end_time</th>
						                  <th>上线状态</th>
						                  <th>修改</th>
						                  <th>删除</th>
						                  <th>增加描述</th>
						                </tr>
						              </thead>
						              <tbody>
						              	<c:if test="${page.getList().size() > 0}">
						                <tr>
						                  <td id="goods0id" >${page.getList().get(0).get("goods_id")}</td>
						                  <td id="goods0name">${page.getList().get(0).get("goods_name")}</td>
						                  <td id="goods0price">${page.getList().get(0).get("goods_price")}</td>
						                  <td id="goods0stock">${page.getList().get(0).get("goods_stock")}</td>
						                  <td id="begin0time">${page.getList().get(0).get("begin_time")}</td>
						                  <td id="end0time">${page.getList().get(0).get("end_time")}</td>
						                  <td>${msg0}</td>
						                  <td><a id="upd0" href="#">update</a></td>
						                  <td><a id="del0" href="#">delete</a></td>
						                  <td><a id="info0" href="#">detailed</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 1}">
						                <tr>
						                  <td id="goods1id">${page.getList().get(1).get("goods_id")}</td>
						                  <td id="goods1name">${page.getList().get(1).get("goods_name")}</td>
						                  <td id="goods1price">${page.getList().get(1).get("goods_price")}</td>
						                  <td id="goods1stock">${page.getList().get(1).get("goods_stock")}</td>
						                  <td id="begin1time">${page.getList().get(1).get("begin_time")}</td>
						                  <td id="end1time">${page.getList().get(1).get("end_time")}</td>
						                  <td>${msg1}</td>
						                  <td><a id="upd1" href="#">update</a></td>
						                  <td><a id="del1" href="#">delete</a></td>
						                  <td><a id="info1" href="#">detailed</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 2}">
						                <tr>
						                  <td id="goods2id">${page.getList().get(2).get("goods_id")}</td>
						                  <td id="goods2name">${page.getList().get(2).get("goods_name")}</td>
						                  <td id="goods2price">${page.getList().get(2).get("goods_price")}</td>
						                  <td id="goods2stock">${page.getList().get(2).get("goods_stock")}</td>
						                  <td id="begin2time">${page.getList().get(2).get("begin_time")}</td>
						                  <td id="end2time">${page.getList().get(2).get("end_time")}</td>
						                  <td>${msg2}</td>
						                  <td><a id="upd2" href="#">update</a></td>
						                  <td><a id="del2" href="#">delete</a></td>
						                  <td><a id="info2" href="#">detailed</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 3}">
						                <tr>
						                  <td id="goods3id">${page.getList().get(3).get("goods_id")}</td>
						                  <td id="goods3name">${page.getList().get(3).get("goods_name")}</td>
						                  <td id="goods3price">${page.getList().get(3).get("goods_price")}</td>
						                  <td id="goods3stock">${page.getList().get(3).get("goods_stock")}</td>
						                  <td id="begin3time">${page.getList().get(3).get("begin_time")}</td>
						                  <td id="end3time">${page.getList().get(3).get("end_time")}</td>
						                  <td>${msg3}</td>
						                  <td><a id="upd3" href="#">update</a></td>
						                  <td><a id="del3" href="#">delete</a></td>
						                  <td><a id="info3" href="#">detailed</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 4}">
						                <tr>
						                  <td id="goods4id">${page.getList().get(4).get("goods_id")}</td>
						                  <td id="goods4name">${page.getList().get(4).get("goods_name")}</td>
						                  <td id="goods4price">${page.getList().get(4).get("goods_price")}</td>
						                  <td id="goods4stock">${page.getList().get(4).get("goods_stock")}</td>
						                  <td id="begin4time">${page.getList().get(4).get("begin_time")}</td>
						                  <td id="end4time">${page.getList().get(4).get("end_time")}</td>
						                  <td>${msg4}</td>
						                  <td><a id="upd4" href="#">update</a></td>
						                  <td><a id="del4" href="#">delete</a></td>
						                  <td><a id="info4" href="#">detailed</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 5}">
						                <tr>
						                  <td id="goods5id">${page.getList().get(5).get("goods_id")}</td>
						                  <td id="goods5name">${page.getList().get(5).get("goods_name")}</td>
						                  <td id="goods5price">${page.getList().get(5).get("goods_price")}</td>
						                  <td id="goods5stock">${page.getList().get(5).get("goods_stock")}</td>
						                  <td id="begin5time">${page.getList().get(5).get("begin_time")}</td>
						                  <td id="end5time">${page.getList().get(5).get("end_time")}</td>
						                  <td>${msg5}</td>
						                  <td><a id="upd5" href="#">update</a></td>
						                  <td><a id="del5" href="#">delete</a></td>
						                  <td><a id="info5" href="#">detailed</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 6}">
						                <tr>
						                  <td id="goods6id">${page.getList().get(6).get("goods_id")}</td>
						                  <td id="goods6name">${page.getList().get(6).get("goods_name")}</td>
						                  <td id="goods6price">${page.getList().get(6).get("goods_price")}</td>
						                  <td id="goods6stock">${page.getList().get(6).get("goods_stock")}</td>
						                  <td id="begin6time">${page.getList().get(6).get("begin_time")}</td>
						                  <td id="end6time">${page.getList().get(6).get("end_time")}</td>
						                  <td>${msg6}</td>
						                  <td><a id="upd6" href="#">update</a></td>
						                  <td><a id="del6" href="#">delete</a></td>
						                  <td><a id="info6" href="#">detailed</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 7}">
						                <tr>
						                  <td id="goods7id">${page.getList().get(7).get("goods_id")}</td>
						                  <td id="goods7name">${page.getList().get(7).get("goods_name")}</td>
						                  <td id="goods7price">${page.getList().get(7).get("goods_price")}</td>
						                  <td id="goods7stock">${page.getList().get(7).get("goods_stock")}</td>
						                  <td id="begin7time">${page.getList().get(7).get("begin_time")}</td>
						                  <td id="end7time">${page.getList().get(7).get("end_time")}</td>
						                  <td>${msg7}</th>
						                  <td><a id="upd7" href="#">update</a></td>
						                  <td><a id="del7" href="#">delete</a></td>
						                  <td><a id="info7" href="#">detailed</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 8}">
						                <tr>
						                  <td id="goods8id">${page.getList().get(8).get("goods_id")}</td>
						                  <td id="goods8name">${page.getList().get(8).get("goods_name")}</td>
						                  <td id="goods8price">${page.getList().get(8).get("goods_price")}</td>
						                  <td id="goods8stock">${page.getList().get(8).get("goods_stock")}</td>
						                  <td id="begin8time">${page.getList().get(8).get("begin_time")}</td>
						                  <td id="end8time">${page.getList().get(8).get("end_time")}</td>
						                  <td>${msg8}</td>
						                  <td><a id="upd8" href="#">update</a></td>
						                  <td><a id="del8" href="#">delete</a></td>
						                  <td><a id="info8" href="#">detailed</a></td>
						                </tr>
						                </c:if>
						                <c:if test="${page.getList().size() > 9}">
						                <tr>
						                  <td id="goods9id">${page.getList().get(9).get("goods_id")}</td>
						                  <td id="goods9name">${page.getList().get(9).get("goods_name")}</td>
						                  <td id="goods9price">${page.getList().get(9).get("goods_price")}</td>
						                  <td id="goods9stock">${page.getList().get(9).get("goods_stock")}</td>
						                  <td id="begin9time">${page.getList().get(9).get("begin_time")}</td>
						                  <td id="end9time">${page.getList().get(9).get("end_time")}</td>
						                  <td>${msg9}</td>
						                  <td><a id="upd9" href="#">update</a></td>
						                  <td><a id="del9" href="#">delete</a></td>
						                  <td><a id="info9" href="#">detailed</a></td>
						                </tr>
						                </c:if>
						                <form action="http://localhost/KillSystem/admin/adminUpdateOrInsert.do" method="post">
						                <tr>
						                  <td><input id="inputGoodsId" style="width:40px;" type="text" name="goods_id" /></td>
						                  <td><input id="inputGoodsName" style="width:60px;" type="text" name="goods_name" /></td>
						                  <td><input id="inputGoodsPrice" style="width:60px;" type="text" name="goods_price" /></td>
						                  <td><input id="inputGoodsStock" style="width:60px;" type="text" name="goods_stock" /></td>
						                  <td><input id="inputBeginTime" style="width:90px;" type="text" name="begin_time" /></td>
						                  <td><input id="inputEndTime" style="width:90px;" type="text" name="end_time" /></td>
						                  <td><input id="submit" type="submit" value="submit" /></td>
						                  <td></td>
						                  <td></td>
						                </tr>
						                </form>
						              </tbody>
						            </table>
						            
<div id="editText" style="display:none;">
<section id="page-demo">
<textarea id="txt-content" data-autosave="editor-content" autofocus required>
hahaha
</textarea>
</section>
<input id="whichGoods" type="hidden" value="" />
<input id="infoSubmit" type="submit" value="提交详细资料" onclick="" />
</div>

						            <div class="box" align="center">  
        <font size="2">共 ${totalPageCount} 页</font> <font size="2">第  
            ${pageNo} 页</font> <a href="toAdminHome.do?pageNo=1">首页</a>  
        <c:choose>  
            <c:when test="${pageNo - 1 > 0}">  
                <a href="toAdminHome.do?pageNo=${pageNo - 1}">上一页</a>  
            </c:when>  
            <c:when test="${pageNo - 1 <= 0}">  
                <a href="toAdminHome.do?pageNo=1">上一页</a>  
            </c:when>  
        </c:choose>  
        <c:choose>  
            <c:when test="${totalPageCount==0}">  
                <a href="toAdminHome.do?pageNo=${pageNo}">下一页</a>  
            </c:when>  
            <c:when test="${pageNo + 1 < totalPageCount}">  
                <a href="toAdminHome.do?pageNo=${pageNo + 1}">下一页</a>  
            </c:when>  
            <c:when test="${pageNo + 1 >= totalPageCount}">  
                <a href="toAdminHome.do?pageNo=${totalPageCount}">下一页</a>  
            </c:when>  
        </c:choose>  
        <c:choose>  
            <c:when test="${totalPageCount==0}">  
                <a href="toAdminHome.do?pageNo=${pageNo}">尾页</a>  
            </c:when>  
            <c:otherwise>  
                <a href="toAdminHome.do?pageNo=${totalPageCount}">尾页</a>  
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