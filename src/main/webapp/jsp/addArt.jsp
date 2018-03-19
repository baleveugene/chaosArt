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
						out.println("<p>"+messageMap.get("Название арта")+"</p>");
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
						out.println("<p>"+messageMap.get("Имя художника")+"</p>");
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
						out.println("<p>"+messageMap.get("Название категории")+"</p>");
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
						out.println("<p>"+messageMap.get("Ссылка на оригинал")+"</p>");
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
