<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8"/>
<title>ХаосArt</title>
<link rel="shortcut icon" href="img/logo_1.jpg" type="image/jpg">
<link rel="stylesheet" type="text/css" href="css/main.css"/>
</head>
<body>
	<%@ page
	import="java.util.List" 
	import="by.chaosart.domain.Art"
	import="by.chaosart.domain.Category"
	import="by.chaosart.domain.Artist"%>
	<div id="header">	
			<a href="/Chaos"><img src='img/logo_2.png' height="70" alt="logo"></a>	
		<div id="rightsideofheader">
			<div id="rightLinks">
				<a href="/Chaos">К Порядку</a> 
				<a href="/Chaos/ControllerServlet">Главная</a>
			</div>
			<div class="rightTabs">			
					<% 	if (session.getAttribute("roleId") != null) { 					
						out.println("<form ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
						out.println("<input type=\"hidden\" name=\"controlParam\" value=\"logIn\">");	
						out.println("<input id=\"buttonMain\" type=\"submit\" name=\"logIn\" value=\"Выйти\">");
						out.println("</form>");
						} else {
							out.println("<div>");
							out.println("<form ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
							out.println("<input type=\"hidden\" name=\"controlParam\" value=\"newAccount\">");
							out.println("<input id=\"buttonMain\" type=\"submit\" name=\"newAccount\" value=\"Регистрация\">");
							out.println("</form>");
							out.println("</div>");
							out.println("<div>");
							out.println("<form ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
							out.println("<input type=\"hidden\" name=\"controlParam\" value=\"logIn\">");
							out.println("<input id=\"buttonMain\" type=\"submit\" name = \"logIn\" value=\"Вход\">");
							out.println("</form>");
							out.println("</div>");
						}
					%>
			</div>
		</div>
	</div>
	<div id="sidebarMain">
		<h3>Категории</h3>
		<% 
			List<Category> categoryList = (List<Category>)session.getAttribute("categoryList"); 
			for (Category c : categoryList) {
				out.println("<p><a id=\"link\" href=\"/Chaos/ControllerServlet?categoryId=" + c.getId() + "\">"
						+ c.getName() + "</a></p>");
			}			
			if (session.getAttribute("roleId")!= null && session.getAttribute("roleId").equals("1")) {
				out.println("<form ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
				out.println("<input type=\"hidden\" name=\"controlParam\" value=\"addCategory\">");
				out.println("<input id=\"buttonMain\" type=\"submit\" name=\"addCategory\" value=\"Добавить категорию\">");
				out.println("</form>");
			}
			%>
	</div>
	<div id="contentMain">
			<%
			if (session.getAttribute("roleId")!= null && session.getAttribute("roleId").equals("1")) {
				out.println("<form ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
				out.println("<input type=\"hidden\" name=\"controlParam\" value=\"addArt\">");
				out.println("<input id=\"buttonMain\" type=\"submit\" name=\"addArt\" value=\"Добавить Арт\">");
				out.println("</form>");
			}			
			List<Art> artList = (List<Art>)session.getAttribute("artList");
			if (request.getParameter("categoryId") != null) {			
				out.println("<h2>" + session.getAttribute("categoryName") + "</h2>");
			}
			for (Art art : artList) {
				out.println("<a id=\"img\" href=\"/Chaos/ControllerServlet?artId=" + art.getId() + "&controlParam=art\"><img src= \""
						+ art.getImage() + "\"height=\"300\"></a>");
			}
			%>
	</div>
	<div id="footer">&copy; Balev</div>
</body>
</html>