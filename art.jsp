<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ХаосArt</title>
<link rel="shortcut icon" href="img/logo_1.jpg" type="image/jpg">
<style>
<%@include file ="/WEB-INF/css/art.css"%>
</style>
</head>
<body>
	<%@ page 
	import="java.util.List" 
	import="by.java.dokwork.domain.Art"
	import="by.java.dokwork.domain.Artist"
	import="by.java.dokwork.domain.Comment"
	import="by.java.dokwork.domain.User"%>

	<div id="header">
		<div id="logo">
			<a href="/Chaos"><img src='img/logo_2.png' height="70" alt="logo"></a>
		</div>
		<div id="rightsideofheader">
			<div id="rightLinks">
				<a href="/Chaos">К Порядку</a> 
				<a href="/Chaos/ControllerServlet">Home</a>
			</div>
			<div id="rightTabs">
				<form name="newAccount" ACTION="/Chaos/ControllerServlet"
					METHOD="POST">
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
	<div id="content">
		<div id="art">
			<%
			Art art = (Art)request.getAttribute("art");
			out.println("<img src= \"" +art.getImage() + "\" height=55% >");	
			if (session.getAttribute("roleId")!= null && session.getAttribute("roleId").equals(1)) {
				out.println("<div id=\"buttons\">");
				out.println("<form ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
				out.println("<input id=\"button\" type=\"submit\" name = \"updateArt\" value=\"Изменить\">");
				out.println("<input id=\"button\" type=\"submit\" name = \"deleteArt\" value=\"Удалить\">");
				out.println("</form>");
				out.println("</div>");
			}	
			%>
		</div>
		<div id="comments">
			<h3>Комментарии</h3>
			<table>
			<%
			List<Comment> commentList = (List<Comment>)request.getAttribute("commentList");
			List<User> userList = (List<User>)request.getAttribute("userList");
			for (Comment c : commentList) {
				User u = userList.get(c.getUserId());
				out.println("<tr>");
				out.println("<td id=\"td1\">" + u.getName() + "</td>");
				out.println("<td>" + c.getText() + "</td>");
				out.println("</tr>");
			}
			%>
			</table>
			<%
			if (session.getAttribute("roleId")!= null && (session.getAttribute("roleId").equals(1) || session.getAttribute("roleId").equals(1))) {
				out.println("<div id=\"form\">");
				out.println("<form id=\"comment-form\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
				out.println(
						"<textarea rows=\"3\" cols=\"20\" name = \"comment\" placeholder=\"Текст комментария\"/></textarea>");
				out.println(
						"<input id=\"button\" type=\"submit\" name = \"newComment\" value=\"Добавить комментарий\">");
				out.println("</form>");
				out.println("</div>");
			}
			%>
		</div>
	</div>
	<div id="sidebar">
		<% Artist artist = (Artist)request.getAttribute("artist");%>
		<h2>Еще работы от<%=artist.getName()%></h2>
		<%
		List<Art> artList = (List<Art>)request.getAttribute("artList");
		for (Art a : artList) {
			out.println("<a id=\"img\" href=\"/Chaos/ControllerServlet?artId=" + a.getId() + "\"><img src= \""
					+ a.getImage() + "\"height=\"120\"></a>");
			}
		%>
	</div>
	<div id="footer">&copy; Balev</div>
</body>
</html>
