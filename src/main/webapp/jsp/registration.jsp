<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ХаосArt</title>
<link rel="shortcut icon" href="img/logo_1.jpg" type="image/jpg">
<link rel="stylesheet" type="text/css" href="css/main.css"/>
</head>
<body>
	<%@ page 
	import="java.util.ArrayList" %>
	<%
	ArrayList<String> messageList = (ArrayList<String>)request.getAttribute("messageList");  
	if(messageList!=null){
		for(String message: messageList) {
			out.println("<h3>"+message+"</h3>");
		}
	}
	%> 
	<div id="registration-page">
		<div id="registrationForm">
				<form id="register-form" ACTION="/Chaos/ControllerServlet" METHOD="POST">
				<input type="text" name = "name" placeholder="Имя (обязательное поле)"/>
				<input type="text" name = "surname" placeholder="Фамилия"/>
				<input type="text" name = "login" placeholder="Логин (обязательное поле)"/>
				<input type="password" name = "password" placeholder="Пароль (обязательное поле)"/>
				<input type="password" name = "password2" placeholder="Повторите пароль (обязательное поле)"/>
				<input type="hidden" name="controlParam" value="newAccount"/>
				<input id="button1" type="submit" name = "newAccount" value="Создать">
				<input id="button1" type="submit" name ="logIn" value="Отмена">
				</form>
				<p id="message">Уже зарегистрированы?
				<form name ="newAccount" ACTION="/Chaos/ControllerServlet" METHOD="POST">
					<input id="link1" type="submit" name ="logIn" value="Вход">
					<input type="hidden" name="controlParam" value="logIn"/>
				</form></p>
		</div>
	</div>
</body>
</html>