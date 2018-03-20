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
	<div id="registration-page">
		<div id="registrationForm">
				<form id="register-form" ACTION="/Chaos/ControllerServlet" METHOD="POST">
				<% 
					HashMap<String, String> messageMap = (HashMap<String, String>) request.getAttribute("messageMap");
					String name = (String)request.getParameter("name");
					if(name!=null&&messageMap.get("Имя")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"name\">Имя</label>");
						out.println("<input type=\"text\" name =\"name\" value=\""+name+"\"/>");
						out.println("<p>"+messageMap.get("Имя")+"</p>");
						out.println("</div>");
					} else if(name!=null){
						out.println("<label for=\"name\">Имя</label>");
						out.println("<input type=\"text\" name =\"name\" value=\""+name+"\"/>");		
					} else {
						out.println("<label for=\"name\">Имя</label>");
						out.println("<input type=\"text\" name =\"name\" placeholder=\"формат: Иван (обязательное поле)\"/>");
					}
					String surname = (String)request.getParameter("surname");
					if(surname!=null&&messageMap.get("Фамилия")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"surname\">Фамилия</label>");
						out.println("<input type=\"text\" name =\"surname\" value=\""+surname+"\"/>");
						out.println("<p>"+messageMap.get("Фамилия")+"</p>");
						out.println("</div>");
					 } else if(surname!=null){
						out.println("<label for=\"surname\">Фамилия</label>");
						out.println("<input type=\"text\" name =\"surname\" value=\""+surname+"\"/>");		
					} else {
						out.println("<label for=\"name\">Фамилия</label>");
						out.println("<input type=\"text\" name =\"surname\" placeholder=\"формат: Иванов\"/>");
					}
					String login = (String)request.getParameter("login");
					if(login!=null&&messageMap.get("Логин")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"login\">Логин</label>");
						out.println("<input type=\"text\" name =\"login\" value=\""+login+"\"/>");
						out.println("<p>"+messageMap.get("Логин")+"</p>");
						out.println("</div>");
					} else if(login!=null){
						out.println("<label for=\"login\">Логин</label>");
						out.println("<input type=\"text\" name =\"login\" value=\""+login+"\"/>");		
					} else {
						out.println("<label for=\"login\">Логин</label>");
						out.println("<input type=\"text\" name =\"login\" placeholder=\"введите логин (обязательное поле)\"/>");
					}
					String password = (String)request.getParameter("password");
					if(password!=null&&messageMap.get("Пароль")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"password\">Пароль</label>");
						out.println("<input type=\"password\" name =\"password\" value=\""+password+"\"/>");
						out.println("<p>"+messageMap.get("Пароль")+"</p>");
						out.println("</div>");
					} else if(password!=null){
						out.println("<label for=\"password\">Пароль</label>");
						out.println("<input type=\"password\" name =\"password\" value=\""+password+"\"/>");		
					} else {
						out.println("<label for=\"password\">Пароль</label>");
						out.println("<input type=\"password\" name =\"password\" placeholder=\"Придумайте пароль (обязательное поле)\"/>");
					}
					String password2 = (String)request.getParameter("password2");
					if(password2!=null&&messageMap.get("Повторите пароль")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"password2\">Пароль (повторно)</label>");
						out.println("<input type=\"password\" name =\"password2\" value=\""+password2+"\"/>");
						out.println("<p>"+messageMap.get("Повторите пароль")+"</p>");
						out.println("</div>");
					} else if(password2!=null){
						out.println("<label for=\"password2\">Пароль (повторно)</label>");
						out.println("<input type=\"password\" name =\"password2\" value=\""+password2+"\"/>");		
					} else {
						out.println("<label for=\"password2\">Пароль (повторно)</label>");
						out.println("<input type=\"password\" name =\"password2\" placeholder=\"Повторите пароль (обязательное поле)\"/>");
					}
					%>					
				<input type="hidden" name="controlParam" value="newAccount"/>
				<div class="buttons">
				<input id="button1" type="submit" name = "newAccount" value="Создать">
				<input id="button1" type="submit" name ="newAccount" value="Отмена">
				</div>
				</form>
				<form name ="newAccount" ACTION="/Chaos/ControllerServlet" METHOD="POST">
					<label id="label1" for="logIn">Уже зарегистрированы?</label>
					<input id="link1" type="submit" name ="logIn" value="Вход">
					<input type="hidden" name="controlParam" value="logIn"/>
				</form>
		</div>
	</div>
</body>
</html>