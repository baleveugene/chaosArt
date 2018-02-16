<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ХаосArt</title>
<link rel="shortcut icon" href="img/logo_1.jpg" type="image/jpg">
<style>
<%@include file ="/WEB-INF/css/login.css"%>
</style>
</head>
<body>
	<%
	String message = (String)request.getAttribute("message");  
	if(message!=null){
	out.println("<h3>"+message+"</h3>");
	}
	%> 
	<div id="login-page">
			<div id="form">
				<form ACTION="/Chaos/ControllerServlet" METHOD="POST">
					<input type="text" name ="login" placeholder="Логин"/>			
					<input type="password" name = "password" placeholder="Пароль"/>
					<input id="button" type="submit" name ="logIn" value="Войти">
					<input id="button" type="submit" name ="logIn" value="Отмена">
				</form>
				<p id="message">Еще не зарегистрированы?<form ACTION="/Chaos/ControllerServlet" METHOD="POST">
					<input id="link" type="submit" name = "newAccount" value="Регистрация">
				</form></p>
			</div>
	</div>
</body>
</html>


			
			
