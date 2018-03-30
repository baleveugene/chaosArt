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
						if(messageMap.get("Имя").equals("emptyMessagePattern")){
							out.println("<p>Поле Имя<br>является обязательным к заполнению!</p>");
						} else {
							out.println("<p>Проверьте данные в поле Имя.<br>(формат: Иван либо Ivan)</p>");
						}
						out.println("</div>");
					} else if(name!=null){
						out.println("<div id=\"nameInput\">");
						out.println("<label for=\"name\">Имя</label>");
						out.println("<input id=\"name\" type=\"text\" name =\"name\" value=\""+name+"\"/>");
						out.println("</div>");
					} else {
						out.println("<div id=\"nameInput\">");
						out.println("<label for=\"name\">Имя</label>");
						out.println("<input id=\"name\" type=\"text\" name =\"name\" placeholder=\"формат: Иван либо Ivan (обязательное поле)\"/>");
						out.println("</div>");
					}
					
					String surname = (String)request.getParameter("surname");
					if(surname!=null&&messageMap.get("Фамилия")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"surname\">Фамилия</label>");
						out.println("<input type=\"text\" name =\"surname\" value=\""+surname+"\"/>");
						out.println("<p>Проверьте данные в поле Фамилия.<br>(формат: Иванов либо Ivanov)</p>");
						out.println("</div>");
					 } else if(surname!=null){
						out.println("<div id=\"surnameInput\">");
						out.println("<label for=\"surname\">Фамилия</label>");
						out.println("<input id=\"surname\" type=\"text\" name =\"surname\" value=\""+surname+"\"/>");		
						out.println("</div>");
					 } else {
						out.println("<div id=\"surnameInput\">");
						out.println("<label for=\"name\">Фамилия</label>");
						out.println("<input id=\"surname\" type=\"text\" name =\"surname\" placeholder=\"формат: Иванов\"/>");
						out.println("</div>");
					 }
					
					String login = (String)request.getParameter("login");
					if(login!=null&&messageMap.get("Логин")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"login\">Логин</label>");
						out.println("<input type=\"text\" name =\"login\" value=\""+login+"\"/>");
						if(messageMap.get("Логин").equals("emptyMessagePattern")){
							out.println("<p>Поле Логин<br>является обязательным к заполнению!</p>");
						} else if (messageMap.get("Логин").equals("messagePattern2")){
							out.println("<p>Проверьте данные в поле Логин.<br>(допустимы лишь цифры и буквы)</p>");
						} else {
							out.println("<p>Пользователь с таким логином уже существует.</p>");
						}
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
					if(password!=null&&messageMap.get("Пароль")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"password\">Пароль</label>");
						out.println("<input type=\"password\" name =\"password\" value=\""+password+"\"/>");
						if(messageMap.get("Пароль").equals("emptyMessagePattern")){
							out.println("<p>Поле Пароль<br>является обязательным к заполнению!</p>");
						} else if (messageMap.get("Пароль").equals("messagePattern2")){
							out.println("<p>Проверьте данные в поле Пароль.<br>(допустимы лишь цифры и буквы)</p>");
						}
						out.println("</div>");
					} else if(password!=null){
						out.println("<div id=\"passwordInput\">");
						out.println("<label for=\"password\">Пароль</label>");
						out.println("<input id=\"password\" type=\"password\" name =\"password\" value=\""+password+"\"/>");		
						out.println("</div>");
					} else {
						out.println("<div id=\"passwordInput\">");
						out.println("<label for=\"password\">Пароль</label>");
						out.println("<input id=\"password\" type=\"password\" name =\"password\" placeholder=\"Придумайте пароль (обязательное поле)\"/>");
						out.println("</div>");
					}
					
					String password2 = (String)request.getParameter("password2");
					if(password2!=null&&messageMap.get("Повторите пароль")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"password2\">Пароль (повторно)</label>");
						out.println("<input type=\"password\" name =\"password2\" value=\""+password2+"\"/>");
						if(messageMap.get("Повторите пароль").equals("emptyMessagePattern")){
							out.println("<p>Поле Повторите пароль<br>является обязательным к заполнению!</p>");
						} else if (messageMap.get("Повторите пароль").equals("messagePattern2")){
							out.println("<p>Проверьте данные в поле Логин.<br>(допустимы лишь цифры и буквы)</p>");
						} else {
							out.println("<p>Пароли должны совпадать!</p>");
						}
						out.println("</div>");
					} else if(password2!=null){
						out.println("<div id=\"password2Input\">");
						out.println("<label for=\"password2\">Пароль (повторно)</label>");
						out.println("<input id=\"password2\" type=\"password\" name =\"password2\" value=\""+password2+"\"/>");		
						out.println("</div>");
					} else {
						out.println("<div id=\"password2Input\">");
						out.println("<label for=\"password2\">Пароль (повторно)</label>");
						out.println("<input id=\"password2\" type=\"password\" name =\"password2\" placeholder=\"Повторите пароль (обязательное поле)\"/>");
						out.println("</div>");
					}
					%>					
				<input type="hidden" name="controlParam" value="newAccount"/>
				<div class="buttons">
				<input class="button1" id="click" type="button" name = "newAccount" value="Создать" onclick="process()"/>
				<input class="button1" type="submit" name ="newAccount" value="Отмена"/>
				</div>
				</form>
				<form name ="newAccount" ACTION="/Chaos/ControllerServlet" METHOD="POST">
					<label id="label1" for="logIn">Уже зарегистрированы?</label>
					<input id="link1" type="submit" name ="logIn" value="Вход"/>
					<input type="hidden" name="controlParam" value="logIn"/>
				</form>
		</div>
	</div>
</body>
</html>