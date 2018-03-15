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
	<%
	String message = (String)request.getAttribute("message");  
	if(message!=null){
	out.println("<h3>"+message+"</h3>");
	}
	%> 
	<div id="login-page">
			<div id="loginForm">
				<form ACTION="/Chaos/ControllerServlet" METHOD="POST">
					<input type="text" name ="login" placeholder="Логин"/>			
					<input type="password" name = "password" placeholder="Пароль"/>
					<input type="hidden" name="controlParam" value="logIn"/>
					<input id="button1" type="submit" name ="logIn" value="Войти">
					<input id="button1" type="submit" name ="logIn" value="Отмена">
				</form>
				<p id="message">Еще не зарегистрированы?
				<form ACTION="/Chaos/ControllerServlet" METHOD="POST">
					<input id="link1" type="submit" name = "newAccount" value="Регистрация">
					<input type="hidden" name="controlParam" value="newAccount"/>
				</form>
				</p>
			</div>
	</div>
</body>
</html>