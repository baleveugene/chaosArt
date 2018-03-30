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
	<div id="addCat-page">
		<div id="addForm">
			<form ACTION="/Chaos/ControllerServlet" METHOD="POST">
				<h3>Введите название категории</h3>
				<% 
				HashMap<String, String> messageMap = (HashMap<String, String>) request.getAttribute("messageMap");
				String categoryName = (String)request.getParameter("categoryName");
				if(categoryName!=null&&messageMap.get("Название категории")!=null){
					out.println("<div class=\"message\">");
					out.println("<input type=\"text\" name =\"categoryName\" value=\""+categoryName+"\"/>");
					if(messageMap.get("Название категории").equals("emptyMessagePattern")){
						out.println("<p>Поле Название категории<br>является обязательным к заполнению!</p>");
					} else {
						out.println("<p>Проверьте данные в поле Название категории.<br>(формат: Красивые либо Beautiful)</p>");
					}
					out.println("</div>");
				} else if(categoryName!=null){
					out.println("<div id=\"categoryNameInput\">");
					out.println("<input id=\"categoryName\" type=\"text\" name =\"categoryName\" value=\""+categoryName+"\"/>");		
					out.println("</div>");
				} else {
					out.println("<div id=\"categoryNameInput\">");
					out.println("<input id=\"categoryName\" type=\"text\" name =\"categoryName\" placeholder=\"формат: Красивые либо Beautiful\"/>");
					out.println("</div>");
				}
					%>
				<input type="hidden" name="controlParam" value="addCategory"/>						
				<div class="buttons">
				<input class="button1" id="click" type="button" name ="addCategory" value="Создать" onclick="process()"/>
				<input class="button1" type="submit" name ="addCategory" value="Отмена">
				</div>
			</form>
		</div>
	</div>
</body>
</html>