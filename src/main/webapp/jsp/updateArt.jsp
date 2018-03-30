<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>ХаосArt</title>
<link rel="shortcut icon" href="img/logo_1.jpg" type="image/jpg"/>
<link rel="stylesheet" type="text/css" href="css/main.css"/>
<script type="text/javascript" src="js/script.js"></script>
</head>
<body>
	<%@ page 
	import="java.util.HashMap" 
	import="by.chaosart.domain.Art"
	import="by.chaosart.domain.Category"
	import="by.chaosart.domain.Artist"%>
	<div id="updateArt-page">
		<div id="updateForm">
			<%
			Art art = (Art)session.getAttribute("art");
			Artist artist = (Artist)session.getAttribute("artist");
			Category category = (Category)session.getAttribute("category");
			%>
			<div id="updatedImg">
				<img src="<%=art.getImage()%>" width="100%">
			</div>
			<form id="rightSideForm" ACTION="/Chaos/ControllerServlet" METHOD="POST">
					<%
					HashMap<String, String> messageMap = (HashMap<String, String>) request.getAttribute("messageMap");
					String artistName = (String)request.getParameter("artistName");
					if(artistName!=null&&messageMap.get("Имя художника")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"artistName\">Имя художника</label>");
						out.println("<input id=\"artistName\" type=\"text\" name =\"artistName\" value=\""+artistName+"\"/>");
						out.println("<p>Проверьте данные в поле Имя художника.<br>(допустимы лишь цифры и буквы)</p>");
						out.println("</div>");
					 } else if(artistName!=null){
						out.println("<div id=\"artistNameInput\">");
						out.println("<label for=\"artistName\">Имя художника</label>");
						out.println("<input id=\"artistName\" type=\"text\" name =\"artistName\" value=\""+artistName+"\"/>");		
						out.println("</div>");
					 } else {
						out.println("<div id=\"artistNameInput\">");
						out.println("<label for=\"artistName\">Имя художника</label>");
						out.println("<input id=\"artistName\" class=\"input1\" type=\"text\" name=\"artistName\" placeholder=\""+artist.getName()+"\"/>");
						out.println("</div>");
					}
					
					String categoryName = (String)request.getParameter("categoryName");
					if(categoryName!=null&&messageMap.get("Название категории")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"categoryName\">Название категории</label>");
						out.println("<input type=\"text\" name =\"categoryName\" value=\""+categoryName+"\"/>");
						out.println("<p>Проверьте данные в поле Название категории.<br>(формат: Красивые либо Beautiful)</p>");
						out.println("</div>");
					} else if(categoryName!=null){
						out.println("<div id=\"categoryNameInput\">");
						out.println("<label for=\"categoryName\">Название категории</label>");
						out.println("<input id=\"categoryName\" type=\"text\" name =\"categoryName\" value=\""+categoryName+"\"/>");		
						out.println("</div>");
					} else {
						out.println("<div id=\"categoryNameInput\">");
						out.println("<label for=\"categoryName\">Название категории</label>");
						out.println("<input id=\"categoryName\" class=\"input1\" type=\"text\" name=\"categoryName\" placeholder=\""+category.getName()+"\"/>");
						out.println("</div>");
					}
					
					String originalUrl = (String)request.getParameter("originalUrl");
					if(originalUrl!=null&&messageMap.get("Ссылка на оригинал")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"originalUrl\">Ссылка на оригинал</label>");
						out.println("<input type=\"text\" name =\"originalUrl\" value=\""+originalUrl+"\"/>");
						out.println("<p>Проверьте данные в поле Ссылка на оригинал.<br>(формат: http://... либо https://...)</p>");
						out.println("</div>");
					} else if(originalUrl!=null){
						out.println("<div id=\"originalUrlInput\">");
						out.println("<label for=\"originalUrl\">Ссылка на оригинал</label>");
						out.println("<input id=\"originalUrl\" type=\"text\" name =\"originalUrl\" value=\""+originalUrl+"\"/>");		
						out.println("</div>");
					} else {
						out.println("<div id=\"originalUrlInput\">");
						out.println("<label for=\"originalUrl\">Ссылка на оригинал</label>");
						out.println("<input id=\"originalUrl\" class=\"input1\" type=\"text\" name=\"originalUrl\" placeholder=\""+art.getOriginalUrl()+"\"/>");
						out.println("</div>");
					}					
					%>								
				<input type="hidden" name="controlParam" value="updateArt"/>
				<div class="buttons">
				<input class="button1" id="click" type="button" name="updateArt" value="Изменить Арт" onclick="process()"/> 
				<input class="button1" type="submit" name="updateArt" value="Отмена"/>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
