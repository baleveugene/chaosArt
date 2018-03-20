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
	<div id="addArt-page">
		<div id="addForm">
			<form ACTION="/Chaos/ControllerServlet" METHOD="POST">
					<h3>Введите параметры арта</h3>
					<% 
					HashMap<String, String> messageMap = (HashMap<String, String>) request.getAttribute("messageMap");
					String artName = (String)request.getParameter("artName");
					if(artName!=null&&messageMap.get("Название арта")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"artName\">Название арта</label>");
						out.println("<input type=\"text\" name =\"artName\" value=\""+artName+"\"/>");
						if(messageMap.get("Название арта").equals("emptyMessagePattern")){
							out.println("<p>Поле Название арта<br>является обязательным к заполнению!</p>");
						} else if (messageMap.get("Название арта").equals("messagePattern3")){
							out.println("<p>Проверьте данные в поле Название арта.<br>(формат: название файла. Пример: арт1.jpg)</p>");
						} else {
							out.println("<p>Арт с таким названием уже существует.</p>");
						}
						out.println("</div>");
					} else if(artName!=null){
						out.println("<label for=\"artName\">Название арта</label>");
						out.println("<input type=\"text\" name =\"artName\" value=\""+artName+"\"/>");		
					} else {
						out.println("<label for=\"artName\">Название арта</label>");
						out.println("<input type=\"text\" name =\"artName\" placeholder=\"формат: арт1.jpg (обязательное поле)\"/>");
					}
					
					String artistName = (String)request.getParameter("artistName");
					if(artistName!=null&&messageMap.get("Имя художника")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"artistName\">Имя художника</label>");
						out.println("<input type=\"text\" name =\"artistName\" value=\""+artistName+"\"/>");
						if(messageMap.get("Имя художника").equals("emptyMessagePattern")){
							out.println("<p>Поле Имя художника<br>является обязательным к заполнению!</p>");
						} else if (messageMap.get("Имя художника").equals("messagePattern2")){
							out.println("<p>Проверьте данные в поле Имя художника.<br>(допустимы лишь цифры и буквы)</p>");
						}
						out.println("</div>");
					 } else if(artistName!=null){
						out.println("<label for=\"artistName\">Имя художника</label>");
						out.println("<input type=\"text\" name =\"artistName\" value=\""+artistName+"\"/>");		
					} else {
						out.println("<label for=\"artistName\">Имя художника</label>");
						out.println("<input type=\"text\" name =\"artistName\" placeholder=\"(обязательное поле)\"/>");
					}
					
					String category = (String)request.getParameter("category");
					if(category!=null&&messageMap.get("Название категории")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"category\">Название категории</label>");
						out.println("<input type=\"text\" name =\"category\" value=\""+category+"\"/>");
						if(messageMap.get("Название категории").equals("emptyMessagePattern")){
							out.println("<p>Поле Название категории<br>является обязательным к заполнению!</p>");
						} else {
							out.println("<p>Проверьте данные в поле Название категории.<br>(формат: Красивые либо Beautiful)</p>");
						}
						out.println("</div>");
					} else if(category!=null){
						out.println("<label for=\"category\">Название категории</label>");
						out.println("<input type=\"text\" name =\"category\" value=\""+category+"\"/>");		
					} else {
						out.println("<label for=\"category\">Название категории</label>");
						out.println("<input type=\"text\" name =\"category\" placeholder=\"формат: Красивые либо Beautiful\"/>");
					}
					
					String originalURL = (String)request.getParameter("originalURL");
					if(originalURL!=null&&messageMap.get("Ссылка на оригинал")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"originalURL\">Ссылка на оригинал</label>");
						out.println("<input type=\"text\" name =\"originalURL\" value=\""+originalURL+"\"/>");
						if(messageMap.get("Ссылка на оригинал").equals("emptyMessagePattern")){
							out.println("<p>Поле Ссылка на оригинал<br>является обязательным к заполнению!</p>");
						} else {
							out.println("<p>Проверьте данные в поле Ссылка на оригинал.<br>(формат: http://... либо https://...)</p>");
						}
						out.println("</div>");
					} else if(originalURL!=null){
						out.println("<label for=\"originalURL\">Ссылка на оригинал</label>");
						out.println("<input type=\"text\" name =\"originalURL\" value=\""+originalURL+"\"/>");		
					} else {
						out.println("<label for=\"originalURL\">Ссылка на оригинал</label>");
						out.println("<input type=\"text\" name =\"originalURL\" placeholder=\"формат: http://... либо https://...\"/>");
					}					
					%>															
					<input type="hidden" name="controlParam" value="addArt"/>
					<div class="buttons">
					<input id="button1" type="submit" name ="addArt" value="Создать">
					<input id="button1" type="submit" name ="addArt" value="Отмена">
					</div>
			</form>
		</div>
	</div>
</body>
</html>
