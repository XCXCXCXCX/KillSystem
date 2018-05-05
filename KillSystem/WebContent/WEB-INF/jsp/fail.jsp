<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登陆失败！</title>
</head>
<body>
登陆失败！我是windows服务器返回的session:<%=session.getAttribute("username") %>
<meta http-equiv="refresh" content="2;url=http://localhost"> 
</body>
</html>