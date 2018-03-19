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
	<div id="addCat-page">
		<div id="addForm">
			<form ACTION="/Chaos/ControllerServlet" METHOD="POST">
				<h3>Введите название категории</h3>
				<% 
					String message = (String)request.getAttribute("message");
					String category = (String)request.getParameter("category");
					if(category!=null&&message!=null){
						out.println("<div class=\"message\">");
						out.println("<input type=\"text\" name =\"category\" value=\""+category+"\"/>");
						out.println("<p>"+message+"</p>");
						out.println("</div>");
					} else {					
						out.println("<input type=\"text\" name =\"category\" placeholder=\"формат: Красивые либо Beautiful\"/>");
					}
					%>
				<input type="hidden" name="controlParam" value="addCategory"/>						
				<div class="buttons">
				<input id="button1" type="submit" name ="addCategory" value="Создать">
				<input id="button1" type="submit" name ="addCategory" value="Отмена">
				</div>
			</form>
		</div>
	</div>
</body>
</html>