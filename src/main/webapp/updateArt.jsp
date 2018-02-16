<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ХаосArt</title>
<link rel="shortcut icon" href="img/logo_1.jpg" type="image/jpg">
<style>
<%@include file="/WEB-INF/css/updateArt.css"%>
</style>
</head>
<body>
	<%@ page 
	import="java.util.List" 
	import="by.chaosart.domain.Art"
	import="by.chaosart.domain.Category"
	import="by.chaosart.domain.Artist"%>
	<%
	String message = (String)request.getAttribute("message");  
	if(message!=null){
	out.println("<h3>"+message+"</h3>");
	}
	%>
	<div id="updateArt-page">
		<div id="form">
			<%
			Art art = (Art)session.getAttribute("art");
			Artist artist = (Artist)session.getAttribute("artist");
			Category category = (Category)session.getAttribute("category");
			%>
			<div id="content">
				<img src="<%=art.getImage()%>" width="100%">
			</div>
			<form id="register-form" ACTION="/Chaos/ControllerServlet" METHOD="POST">
				<p id="message">Имя автора:</p>
				<input type="text" name="artistName" placeholder="<%=artist.getName()%>" />
				<p id="message">Категория:</p> 
				<input type="text" name="category" placeholder="<%=category.getName()%>" />
				<p id="message">Ссылка на оригинал:</p>
				<input type="text" name="originalURL" placeholder="<%=art.getOriginalUrl()%>"/> 
				<input id="button" type="submit" name="updateArt" value="Изменить Арт"> 
				<input id="button" type="submit" name="updateArt" value="Отмена">
			</form>
		</div>
	</div>
</body>
</html>
