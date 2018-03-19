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
	import="java.util.HashMap" %> 
	<div id="login-page">
			<div id="loginForm">
				<form ACTION="/Chaos/ControllerServlet" METHOD="POST">				
					<% 
					HashMap<String, String> messageMap = (HashMap<String, String>) request.getAttribute("messageMap");
					String login = (String)request.getParameter("login");
					if(login!=null&&messageMap.get("messageLogin")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"login\">Логин</label>");
						out.println("<input type=\"text\" name =\"login\" value=\""+login+"\"/>");
						out.println("<p>"+messageMap.get("messageLogin")+"</p>");
						out.println("</div>");
					} else if(login!=null){
						out.println("<label for=\"login\">Логин</label>");
						out.println("<input type=\"text\" name =\"login\" value=\""+login+"\"/>");		
					} else {
						out.println("<label for=\"login\">Логин</label>");
						out.println("<input type=\"text\" name =\"login\" placeholder=\"введите логин (обязательное поле)\"/>");
					}
					String password = (String)request.getParameter("password");
					if(password!=null&&messageMap.get("messagePassword")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"password\">Пароль</label>");
						out.println("<input type=\"password\" name =\"password\" value=\""+password+"\"/>");
						out.println("<p>"+messageMap.get("messagePassword")+"</p>");
						out.println("</div>");
					} else if(password!=null){
						out.println("<label for=\"password\">Пароль</label>");
						out.println("<input type=\"password\" name =\"password\" value=\""+password+"\"/>");		
					} else {
						out.println("<label for=\"password\">Пароль</label>");
						out.println("<input type=\"password\" name =\"password\" placeholder=\"введите пароль (обязательное поле)\"/>");
					}
					%>
					<input type="hidden" name="controlParam" value="logIn"/>
					<div class="buttons">
					<input id="button1" type="submit" name ="logIn" value="Войти">
					<input id="button1" type="submit" name ="logIn" value="Отмена">
					</div>
				</form>		
				<form ACTION="/Chaos/ControllerServlet" METHOD="POST">
					<label id="label1" for="newAccount">Еще не зарегистрированы?</label>
					<input id="link1" type="submit" name = "newAccount" value="Регистрация">
					<input type="hidden" name="controlParam" value="newAccount"/>
				</form>
			</div>
	</div>
</body>
</html>