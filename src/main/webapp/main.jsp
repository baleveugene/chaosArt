<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8"/>
<title>ХаосArt</title>
<link rel="shortcut icon" href="img/logo_1.jpg" type="image/jpg">
<style>
<%@ include file ="/WEB-INF/css/main.css" %>
</style>
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
				<a href="/Chaos">К Порядку</a> <a href="/Chaos/ControllerServlet">Главная</a>
			</div>
			<div id="rightTabs">
				<form name="newAccount" ACTION="/Chaos/ControllerServlet" METHOD="POST">				
					<% if (session.getAttribute("roleId") != null) { 
					out.println("<input id=\"button\" type=\"submit\" name=\"logIn\" value=\"Выйти\">");
					} else {
					out.println("<input id=\"button\" type=\"submit\" name=\"newAccount\" value=\"Регистрация\">");
					out.println("<input id=\"button\" type=\"submit\" name = \"logIn\" value=\"Вход\">");
					}
					%>
				</form>
			</div>
		</div>
	</div>
	<div id="sidebar">
		<h3>Категории</h3>
		<% 
			List<Category> categoryList = (List<Category>)session.getAttribute("categoryList"); 
			for (Category c : categoryList) {
				out.println("<p><a id=\"link\" href=\"/Chaos/ControllerServlet?categoryId=" + c.getId() + "\">"
						+ c.getName() + "</a></p>");
			}			
			if (session.getAttribute("roleId")!= null && session.getAttribute("roleId").equals("1")) {
				out.println("<form ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
				out.println("<input id=\"button\" type=\"submit\" name=\"addCategory\" value=\"Добавить категорию\">");
				out.println("</form>");
			}
			%>
	</div>
	<div id="content">
			<%
			if (session.getAttribute("roleId")!= null && session.getAttribute("roleId").equals("1")) {
				out.println("<form ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
				out.println("<input id=\"button\" type=\"submit\" name=\"addArt\" value=\"Добавить Арт\">");
				out.println("</form>");
			}			
			List<Art> artList = (List<Art>)session.getAttribute("artList");
			if (request.getParameter("categoryId") != null) {			
				out.println("<h2>" + session.getAttribute("categoryName") + "</h2>");
			}
			for (Art art : artList) {
				out.println("<a id=\"img\" href=\"/Chaos/ControllerServlet?artId=" + art.getId() + "\"><img src= \""
						+ art.getImage() + "\"height=\"300\"></a>");
			}
			%>
	</div>
	<div id="footer">&copy; Balev</div>
</body>
</html>


