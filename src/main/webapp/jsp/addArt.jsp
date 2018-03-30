<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>ХаосArt</title>
<link rel="shortcut icon" href="img/logo_1.jpg" type="image/jpg"/>
<link rel="stylesheet" type="text/css" href="css/main.css"/>
<script type="text/javascript" src="js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="js/script.js"></script>
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
						out.println("<div id=\"artNameInput\">");
						out.println("<label for=\"artName\">Название арта</label>");
						out.println("<input id=\"artName\" type=\"text\" name =\"artName\" value=\""+artName+"\"/>");		
						out.println("</div>");
					} else {
						out.println("<div id=\"artNameInput\">");
						out.println("<label for=\"artName\">Название арта</label>");
						out.println("<input id=\"artName\" type=\"text\" name =\"artName\" placeholder=\"формат: арт1.jpg (обязательное поле)\"/>");
						out.println("</div>");
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
						out.println("<div id=\"artistNameInput\">");
						out.println("<label for=\"artistName\">Имя художника</label>");
						out.println("<input id=\"artistName\" type=\"text\" name =\"artistName\" value=\""+artistName+"\"/>");		
						out.println("</div>");
					 } else {
						out.println("<div id=\"artistNameInput\">");
						out.println("<label for=\"artistName\">Имя художника</label>");
						out.println("<input id=\"artistName\" type=\"text\" name =\"artistName\" placeholder=\"(обязательное поле)\"/>");
						out.println("</div>");
					}
					
					String categoryName = (String)request.getParameter("categoryName");
					if(categoryName!=null&&messageMap.get("Название категории")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"categoryName\">Название категории</label>");
						out.println("<input type=\"text\" name =\"categoryName\" value=\""+categoryName+"\"/>");
						if(messageMap.get("Название категории").equals("emptyMessagePattern")){
							out.println("<p>Поле Название категории<br>является обязательным к заполнению!</p>");
						} else {
							out.println("<p>Проверьте данные в поле Название категории.<br>(формат: Красивые либо Beautiful)</p>");
						}
						out.println("</div>");
					} else if(categoryName!=null){
						out.println("<div id=\"categoryNameInput\">");
						out.println("<label for=\"categoryName\">Название категории</label>");
						out.println("<input id=\"categoryName\" type=\"text\" name =\"categoryName\" value=\""+categoryName+"\"/>");		
						out.println("</div>");
					} else {
						out.println("<div id=\"categoryNameInput\">");
						out.println("<label for=\"categoryName\">Название категории</label>");
						out.println("<input id=\"categoryName\" type=\"text\" name =\"categoryName\" placeholder=\"формат: Красивые либо Beautiful\"/>");
						out.println("</div>");
					}
					
					String originalUrl = (String)request.getParameter("originalUrl");
					if(originalUrl!=null&&messageMap.get("Ссылка на оригинал")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"originalUrl\">Ссылка на оригинал</label>");
						out.println("<input type=\"text\" name =\"originalUrl\" value=\""+originalUrl+"\"/>");
						if(messageMap.get("Ссылка на оригинал").equals("emptyMessagePattern")){
							out.println("<p>Поле Ссылка на оригинал<br>является обязательным к заполнению!</p>");
						} else {
							out.println("<p>Проверьте данные в поле Ссылка на оригинал.<br>(формат: http://... либо https://...)</p>");
						}
						out.println("</div>");
					} else if(originalUrl!=null){
						out.println("<div id=\"originalUrlInput\">");
						out.println("<label for=\"originalUrl\">Ссылка на оригинал</label>");
						out.println("<input id=\"originalUrl\" type=\"text\" name =\"originalUrl\" value=\""+originalUrl+"\"/>");		
						out.println("</div>");
					} else {
						out.println("<div id=\"originalUrlInput\">");
						out.println("<label for=\"originalUrl\">Ссылка на оригинал</label>");
						out.println("<input id=\"originalUrl\" type=\"text\" name =\"originalUrl\" placeholder=\"формат: http://... либо https://...\"/>");
						out.println("</div>");
					}					
					%>															
					<input type="hidden" name="controlParam" value="addArt"/>
					<div class="buttons">
					<input class="button1" id="click" type="button" name ="addArt" value="Создать" onclick="process()"/>
					<input class="button1" type="submit" name ="addArt" value="Отмена"/>
					</div>
			</form>
		</div>
	</div>
</body>
</html>
