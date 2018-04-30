<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.js"></script>
<script type="text/javascript">

$(function(){
		$("#qrCodeDiv").hide();
		window.setTimeout(toPay,2000);
	});

function toPay(){
	$("#qrCodeDiv").show();
	window.setInterval(getPayState,5000);
}

function getPayState(){
	var order_id = $("#order_id").val();
	$.post("/KillSystem/payIsSuccess.do", { order_id: order_id },  
            function (response) {  
                if (response.status == "0") { 
					if (response.data == true){
						window.location.href="/paySuccess.html";
					}
                }
            }, 'json');
}
</script>
<title>alipay</title>
</head>
<body>
支付宝正在努力的生成二维码...
<input id="order_id" type="hidden" name="order_id" value="${resultMap.get("order_id")}" />

<div id="qrCodeDiv" class="align-center">
	<img id="qrCode" class="top" src="${resultMap.get("qrUrl")}" height="400" width="400" /> 
	二维码5分钟后失效，支付成功后自动跳转...
</div>


</body>
</html>