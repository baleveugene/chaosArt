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
<script type="text/javascript" src="js/scriptArt.js"></script>
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
	<div id="contentArt">
		<div id="art">
			<%
			Art art = (Art)session.getAttribute("art");
			out.println("<img id=\"img\" name=\""+art.getId()+"\" src=\"" +art.getImage()+"\" height=\"400\">");	
			if (session.getAttribute("roleId")!= null && session.getAttribute("roleId").equals("1")) {
				out.println("<div class=\"buttons\">");
				out.println("<div>");
				out.println("<form ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
				out.println("<input type=\"hidden\" name=\"controlParam\" value=\"updateArt\">");
				out.println("<input class=\"buttonArt\" type=\"submit\" name = \"updateArt\" value=\"Изменить\">");
				out.println("</form>");
				out.println("</div>");
				out.println("<div>");
				out.println("<form ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
				out.println("<input type=\"hidden\" name=\"controlParam\" value=\"deleteArt\">");
				out.println("<input class=\"buttonArt\" type=\"submit\" name = \"deleteArt\" value=\"Удалить\">");
				out.println("</form>");
				out.println("</div>");
				out.println("</div>");
			}	
			%>
		</div>
		<div id="comments">
			<h3>Комментарии</h3>
			<table id="commentTable"></table>		
			<%
			if (session.getAttribute("roleId")!= null && (session.getAttribute("roleId").equals("1") || 
					session.getAttribute("roleId").equals("2"))) {
				out.println("<div id=\"form\">");
				out.println("<form id=\"comment-form\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
				out.println(
						"<textarea id=\"textarea\" rows=\"3\" cols=\"20\" name = \"comment\" placeholder=\"Текст комментария\"/></textarea>");
				out.println(
						"<input class=\"buttonArt\" id=\"click\" type=\"button\" name =\"newComment\" value=\"Добавить комментарий\" onclick=\"process()\"/>");
				out.println("<input type=\"hidden\" name=\"controlParam\" value=\"newComment\"/>");
				out.println("</form>");
				out.println("</div>");
			}
			%>
		</div>
	</div>
	<div id="sidebarArt">
		<% Artist artist = (Artist)session.getAttribute("artist");%>
		<h2>Еще работы от <%=artist.getName()%></h2>
		<div id="artList"></div>
	</div>
	<div id="footer">&copy; Balev</div>
</body>
</html>
