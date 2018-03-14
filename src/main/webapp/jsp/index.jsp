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
<img id="imgIndex" src="img/logo.jpg" alt="logo">
<div id="tabs">
	<form ACTION="/Chaos/ControllerServlet" METHOD="POST">
		<input id="buttonIndex" type="submit" name="Chaos" value="Оооо, да! Обожаю Хаос!">	
		<input id="buttonIndex" type="submit" name="Chaos" value="Немного страшно, но я заинтригован.">
		<input id="buttonIndex" type="submit" name="Chaos" value="Хочу сбежать, но все дороги ведут к Хаосу.">
	</form>		
</div>
</body>
</html>