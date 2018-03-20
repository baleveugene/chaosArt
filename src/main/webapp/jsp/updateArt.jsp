<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ХаосArt</title>
<link rel="shortcut icon" href="img/logo_1.jpg" type="image/jpg"/>
<link rel="stylesheet" type="text/css" href="css/main.css"/>
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
						out.println("<input type=\"text\" name =\"artistName\" value=\""+artistName+"\"/>");
						out.println("<p>"+messageMap.get("Имя художника")+"</p>");
						out.println("</div>");
					 } else if(artistName!=null){
						out.println("<label for=\"artistName\">Имя художника</label>");
						out.println("<input type=\"text\" name =\"artistName\" value=\""+artistName+"\"/>");		
					} else {
						out.println("<label for=\"artistName\">Имя художника</label>");
						out.println("<input id=\"input1\" type=\"text\" name=\"artistName\" placeholder=\""+artist.getName()+"\"/>");
					}
					String categoryName = (String)request.getParameter("categoryName");
					if(categoryName!=null&&messageMap.get("Название категории")!=null){
						out.println("<div class=\"message\">");
						out.println("<label for=\"categoryName\">Название категории</label>");
						out.println("<input type=\"text\" name =\"categoryName\" value=\""+categoryName+"\"/>");
						out.println("<p>"+messageMap.get("Название категории")+"</p>");
						out.println("</div>");
					} else if(categoryName!=null){
						out.println("<label for=\"categoryName\">Название категории</label>");
						out.println("<input type=\"text\" name =\"categoryName\" value=\""+categoryName+"\"/>");		
					} else {
						out.println("<label for=\"categoryName\">Название категории</label>");
						out.println("<input id=\"input1\" type=\"text\" name=\"categoryName\" placeholder=\""+category.getName()+"\"/>");
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
						out.println("<input id=\"input1\" type=\"text\" name=\"originalURL\" placeholder=\""+art.getOriginalUrl()+"\"/>");
					}					
					%>								
				<input type="hidden" name="controlParam" value="updateArt"/>
				<div class="buttons">
				<input id="button1" type="submit" name="updateArt" value="Изменить Арт"> 
				<input id="button1" type="submit" name="updateArt" value="Отмена">
				</div>
			</form>
		</div>
	</div>
</body>
</html>
