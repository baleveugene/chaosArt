<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ХаосArt</title>
<link rel="shortcut icon" href="img/logo_1.jpg" type="image/jpg">
<style>
<%@include file="/WEB-INF/css/deleteArt.css"%>
</style>
</head>
<body>
	<%@ page  
	import="by.chaosart.domain.Art"%>
	<div id="deleteArt-page">
		<div id="form">
			<div id="content">
			<%
			Art art = (Art)session.getAttribute("art");		
				out.println("<img src=\""+art.getImage()+"\" width=\"100%\">");
			%>
			</div>
			<form id="delete-form" ACTION="/Chaos/ControllerServlet" METHOD="POST">
				<p id="message">Вы действительно хотите удалить этот Арт?</p>
				<input type="hidden" name="deleteArt" value="Удалить арт"/> 
				<input id="button" type="submit" name="yes" value="Да"> 
				<input id="button" type="submit" name="no" value="Нет">
			</form>
		</div>
	</div>
</body>
</html>
