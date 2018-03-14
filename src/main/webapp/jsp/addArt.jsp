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
	<%
	String message = (String)request.getAttribute("message");  
	if(message!=null){
	out.println("<h3>"+message+"</h3>");
	}
	%> 
	<div id="addArt-page">
		<div id="addForm">
			<form ACTION="/Chaos/ControllerServlet" METHOD="POST">
					<p id="message">Введите параметры арта:</p>
					<input type="text" name ="artName" placeholder="Название арта (формат: арт1.jpg)"/>
					<input type="text" name ="artistName" placeholder="Имя художника"/>
					<input type="text" name ="category" placeholder="Название категории"/>
					<input type="text" name ="originalURL" placeholder="Ссылка на оригинал"/>								
					<input id="button1" type="submit" name ="addArt" value="Создать">
					<input id="button1" type="submit" name ="addArt" value="Отмена">
			</form>
		</div>
	</div>
</body>
</html>
