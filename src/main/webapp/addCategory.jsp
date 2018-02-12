<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ХаосArt</title>
<link rel="shortcut icon" href="img/logo_1.jpg" type="image/jpg">
<style>
<%@include file ="/WEB-INF/css/registration.css"%>
</style>
</head>
<body>
	<%
	String message = (String)request.getAttribute("message");  
	if(message!=null){
	out.println("<h3>"+message+"</h3>");
	}
	%> 
	<div id="registration-page">
			<div id="form">
			<form ACTION="/Chaos/ControllerServlet" METHOD="POST">
			<p id="message">Введите название категории:</p>
			<input type="text" name ="category" placeholder="Название категории"/>						
			<input id="button" type="submit" name ="addCategory" value="Создать">
			<input id="button" type="submit" name ="addCategory" value="Отмена">
			</form>
			</div>
			</div>
</body>
</html>
			
