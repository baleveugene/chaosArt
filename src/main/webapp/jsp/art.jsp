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
	import="java.util.List" 
	import="by.chaosart.domain.Art"
	import="by.chaosart.domain.Artist"
	import="by.chaosart.domain.Comment"
	import="by.chaosart.domain.User"%>

	<div id="header">
			<a href="/Chaos"><img src='img/logo_2.png' height="70" alt="logo"></a>
		<div id="rightsideofheader">
			<div id="rightLinks">
				<a href="/Chaos">К Порядку</a> 
				<a href="/Chaos/ControllerServlet">Главная</a>
			</div>
			<div id="rightTabs">
				<form ACTION="/Chaos/ControllerServlet" METHOD="POST">
					<% if (session.getAttribute("roleId") != null) { 
					out.println("<input id=\"buttonArt\" type=\"submit\" name=\"logIn\" value=\"Выйти\">");
					} else {
					out.println("<input id=\"buttonArt\" type=\"submit\" name=\"newAccount\" value=\"Регистрация\">");
					out.println("<input id=\"buttonArt\" type=\"submit\" name = \"logIn\" value=\"Вход\">");
					}
					%>
				</form>
			</div>
		</div>
	</div>
	<div id="contentArt">
		<div id="art">
			<%
			Art art = (Art)session.getAttribute("art");
			out.println("<img src= \"" +art.getImage() + "\" height=\"400\">");	
			if (session.getAttribute("roleId")!= null && session.getAttribute("roleId").equals("1")) {
				out.println("<div id=\"buttons\">");
				out.println("<form ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
				out.println("<input id=\"buttonArt\" type=\"submit\" name = \"updateArt\" value=\"Изменить\">");
				out.println("<input id=\"buttonArt\" type=\"submit\" name = \"deleteArt\" value=\"Удалить\">");
				out.println("</form>");
				out.println("</div>");
			}	
			%>
		</div>
		<div id="comments">
			<h3>Комментарии</h3>
			<table>
			<%
			List<Comment> commentList = (List<Comment>)session.getAttribute("commentList");
			if(commentList!=null){
				List<User> userList = (List<User>)session.getAttribute("userList");
				int i = 0;
				for (Comment c : commentList) {		
					User u = userList.get(i++);
					out.println("<tr>");
					out.println("<td id=\"td1\">" + u.getName() + "</td>");
					out.println("<td>" + c.getText() + "</td>");
					out.println("</tr>");
				}
			}
			%>
			</table>
			<%
			if (session.getAttribute("roleId")!= null && (session.getAttribute("roleId").equals("1") || session.getAttribute("roleId").equals("2"))) {
				out.println("<div id=\"form\">");
				out.println("<form id=\"comment-form\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
				out.println(
						"<textarea rows=\"3\" cols=\"20\" name = \"comment\" placeholder=\"Текст комментария\"/></textarea>");
				out.println(
						"<input id=\"buttonArt\" type=\"submit\" name = \"newComment\" value=\"Добавить комментарий\">");
				out.println("</form>");
				out.println("</div>");
			}
			%>
		</div>
	</div>
	<div id="sidebarArt">
		<% Artist artist = (Artist)session.getAttribute("artist");%>
		<h2>Еще работы от <%=artist.getName()%></h2>
		<%
		List<Art> artList = (List<Art>)session.getAttribute("artList");
		if(artList!=null) {
		for (Art a : artList) {
			out.println("<a id=\"img\" href=\"/Chaos/ControllerServlet?artId=" + a.getId() + "\"><img src=\""
					+ a.getImage() + "\" height=\"120\"></a>");
			}
		}
		%>
	</div>
	<div id="footer">&copy; Balev</div>
</body>
</html>
