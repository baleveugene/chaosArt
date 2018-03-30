<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ХаосArt</title>
<link rel="shortcut icon" href="img/logo_1.jpg" type="image/jpg">
<link rel="stylesheet" type="text/css" href="css/main.css"/>
<script type="text/javascript" src="js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="js/script.js"></script>
</head>
<body>
<%@ page 
	import="java.util.HashMap" %> 
	<div id="login-page">
			<div id="loginForm">
				<form method="post" action="">				
					<% 
					String messagePattern1 = "Проверьте правильность написания логина.";
					String messagePattern2 = "Проверьте правильность написания пароля.";
					HashMap<String, String> messageMap = (HashMap<String, String>) request.getAttribute("messageMap"); 
					String login = (String)request.getParameter("login");
					if(login!=null&&messageMap.get("messageLogin")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"login\">Логин</label>");
						out.println("<input id=\"login\" type=\"text\" name =\"login\" value=\""+login+"\"/>");
						out.println("<p>"+messagePattern1+"</p>");
						out.println("</div>");
					} else if(login!=null){
						out.println("<div id=\"loginInput\">");
						out.println("<label for=\"login\">Логин</label>");
						out.println("<input id=\"login\" type=\"text\" name =\"login\" value=\""+login+"\"/>");
						out.println("</div>");
					} else {
						out.println("<div id=\"loginInput\">");
						out.println("<label for=\"login\">Логин</label>");
						out.println("<input id=\"login\" type=\"text\" name =\"login\" placeholder=\"введите логин (обязательное поле)\"/>");
						out.println("</div>");
					}
					String password = (String)request.getParameter("password");
					if(password!=null&&messageMap.get("messagePassword")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"password\">Пароль</label>");
						out.println("<input id=\"password\" type=\"password\" name =\"password\" value=\""+password+"\"/>");
						out.println("<p>"+messagePattern2+"</p>");
						out.println("</div>");
					} else if(password!=null){
						out.println("<div id=\"passwordInput\">");
						out.println("<label for=\"password\">Пароль</label>");
						out.println("<input id=\"password\" type=\"password\" name =\"password\" value=\""+password+"\"/>");
						out.println("</div>");	
					} else {
						out.println("<div id=\"passwordInput\">");
						out.println("<label for=\"password\">Пароль</label>");
						out.println("<input id=\"password\" type=\"password\" name =\"password\" placeholder=\"введите пароль (обязательное поле)\"/>");
						out.println("</div>");
					}
					%>
					<input type="hidden" name="controlParam" value="logIn"/>
					<div class="buttons">
					<input class="button1" id="click" type="button" name ="logIn" value="Войти" onclick="process()"/>
					<input class="button1" type="submit" name ="logIn" value="Отмена"/>
					</div>
				</form>		
				<form ACTION="/Chaos/ControllerServlet" METHOD="POST">
					<label id="label1" for="newAccount">Еще не зарегистрированы?</label>
					<input id="link1" type="submit" name = "newAccount" value="Регистрация"/>
					<input type="hidden" name="controlParam" value="newAccount"/>
				</form>
			</div>
	</div>
</body>
</html>