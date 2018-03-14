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
	import="by.chaosart.domain.Art"%>
	<div id="deleteArt-page">
		<div id="deleteForm">
			<div id="deletedContent">
			<%
			Art art = (Art)session.getAttribute("art");		
				out.println("<img src=\""+art.getImage()+"\" width=\"100%\">");
			%>
			</div>
			<form id="submitForm" ACTION="/Chaos/ControllerServlet" METHOD="POST">
				<p id="message">Вы действительно хотите удалить этот Арт?</p>
				<input type="hidden" name="deleteArt" value="Удалить арт"/> 
				<input id="button1" type="submit" name="yes" value="Да"> 
				<input id="button1" type="submit" name="no" value="Нет">
			</form>
		</div>
	</div>
</body>
</html>