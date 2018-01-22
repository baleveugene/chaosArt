<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ХаосArt</title>
<link rel="shortcut icon" href="img/logo_1.jpg" type="image/jpg">
</head>
<style>
	body {		
		font: 11pt Arial, Helvetica, sans-serif;
		margin: 0;
		background: #4A0A05;
		}
	img {
		position: relative;
    	z-index: 2;	
		float: left;		
		margin: 0 0 0 15%;
		width: 33%;
		}
	#tabs {
		position: relative;
    	z-index: 1;		
		padding: 200px 0 0 30px;
		width: 42%;
		min-height: 450px; 
		margin: 0 0 0 45%;										
		text-align: left ;
		background-image: url(img/logo_1.jpg);
		box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);	
		}
	#button {    		    		
    	font-family: sans-serif;
    	text-transform: uppercase;
    	outline: 0;
    	background: #567348;
    	border: 0;
    	margin: 15px 15px 0 0;
    	padding: 10px;
    	color: #FFFFFF;
    	font-size: 14px;
    	cursor: pointer;    		
    	}					
</style>			
<body>

<img src="img/logo.jpg" alt="logo">
<div id="tabs">
	<form ACTION="/Chaos/ControllerServlet" METHOD="POST">
		<input id="button" type="submit" name="Chaos" value="Оооо, да! Обожаю Хаос!">	
		<input id="button" type="submit" name="Chaos" value="Немного страшно, но я заинтригован.">
		<input id="button" type="submit" name="Chaos" value="Хочу сбежать, но все дороги ведут к Хаосу.">
	</form>		
</div>
</body>
</html>