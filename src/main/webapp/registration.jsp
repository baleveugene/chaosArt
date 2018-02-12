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
				<form id="register-form" ACTION="/Chaos/ControllerServlet" METHOD="POST">
				<input type="text" name = "name" placeholder="Имя (обязательное поле)"/>
				<input type="text" name = "surname" placeholder="Фамилия"/>
				<input type="text" name = "login" placeholder="Логин (обязательное поле)"/>
				<input type="password" name = "password" placeholder="Пароль (обязательное поле)"/>
				<input type="password" name = "password2" placeholder="Повторите пароль"/>
				<input id="button" type="submit" name = "newAccount" value="Создать">
				<input id="button" type="submit" name ="logIn" value="Отмена">
				</form>
				<p id="message">Уже зарегистрированы?<form name ="newAccount" ACTION="/Chaos/ControllerServlet" METHOD="POST">
					<input id="link" type="submit" name ="logIn" value="Вход"></form></p>
		</div>
	</div>
</body>
</html>


			
			
