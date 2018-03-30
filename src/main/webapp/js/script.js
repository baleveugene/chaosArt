var xmlHttp = false;
try {
	xmlHttp = new XMLHttpRequest();
} catch (trymicrosoft) {
  try {
	  xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
  } catch (othermicrosoft) {
    try {
    	xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } catch (failed) {
    	xmlHttp = false;
    }
  }
}
    
function process() {
	var page = document.getElementById("click").getAttribute('name');
    var params = getReqParams(page);
    var url = "/Chaos/ControllerServlet";
    xmlHttp.open("post", url, true);
    xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlHttp.onreadystatechange = respProcess; 
    xmlHttp.send(params);  
}

function getReqParams(page) {
	var params = null;
	if(page=="logIn") {
		var login = document.getElementById("login").value;
	    var password = document.getElementById("password").value;
	    params = "login="+login+"&password="+password+"&controlParam=logIn&ajax=true&logIn=Войти";	    
	} else if(page=="newAccount") {
		var name = document.getElementById("name").value;
		var surname = document.getElementById("surname").value;
		var login = document.getElementById("login").value;
	    var password = document.getElementById("password").value;
	    var password2 = document.getElementById("password2").value;
	    params = "name="+name+"&surname="+surname+"&login="+login+
	    "&password="+password+"&password2="+password2+
	    "&controlParam=newAccount&ajax=true&newAccount=Создать";    
	} else if(page=="addArt") {
		var artName = document.getElementById("artName").value;
		var artistName = document.getElementById("artistName").value;
		var categoryName = document.getElementById("categoryName").value;
	    var originalUrl = document.getElementById("originalUrl").value;
	    params = "artName="+artName+"&artistName="+artistName+
	    "&categoryName="+categoryName+"&originalUrl="+originalUrl+
	    "&controlParam=addArt&ajax=true&addArt=Создать";    
	} else if(page=="addCategory") {		
		var categoryName = document.getElementById("categoryName").value;
	    params = "categoryName="+categoryName+
	    "&controlParam=addCategory&ajax=true&addCategory=Создать";    
	} else if(page=="updateArt") {
		alert(page);
		var artistName = document.getElementById("artistName").value;
		var categoryName = document.getElementById("categoryName").value;
	    var originalUrl = document.getElementById("originalUrl").value;
	    params = "artistName="+artistName+"&categoryName="+categoryName+
	    "&originalUrl="+originalUrl+"&controlParam=updateArt&ajax=true&updateArt=Изменить Арт";
	}
	return params;
}

function respProcess() {
        if(xmlHttp.readyState == 4){
        	if(xmlHttp.status == 200){       		
        		var response = xmlHttp.responseText;  
        		var param;
        		var inputName;
        		var pattern;
        		if(response.length==0){
        			document.location.href = "http://localhost:8080/Chaos/ControllerServlet";
        		} else {
        		if(response.match("Логин messagePattern2")){
        			param = "Логин";
        			inputName = "login";
        			pattern = "messagePattern2";
        			inputProcess(param, inputName, pattern);
        		}; 
        		if(response.match("Логин emptyMessagePattern")){ 
        			param = "Логин";
        			inputName = "login";
        			pattern = "emptyMessagePattern";
        			inputProcess(param, inputName, pattern);
        		}; 
        		if(response.match("Логин alreadyExistPattern")){
        			param = "Логин";
        			inputName = "login";
        			pattern = "alreadyExistPattern";
        			inputProcess(param, inputName, pattern);
        		}; 
        		if(response.match("Пароль emptyMessagePattern")){
        			param = "Пароль";
        			inputName = "password";
        			pattern = "emptyMessagePattern";
        			inputProcess(param, inputName, pattern);
        		}; 
        		if(response.match("Пароль messagePattern2")){ 
        			param = "Пароль";
        			inputName = "password";
        			pattern = "messagePattern2";
        			inputProcess(param, inputName, pattern);
        		}; 
        		if(response.match("Повторите пароль messagePattern2")){
        			param = "Повторите пароль";
        			inputName = "password2";
        			pattern = "messagePattern2";
        			inputProcess(param, inputName, pattern);
        		}; 
        		if(response.match("Повторите пароль passwordsEqualsPattern")){ 
        			param = "Повторите пароль";
        			inputName = "password2";
        			pattern = "passwordsEqualsPattern";
        			inputProcess(param, inputName, pattern);
        		}; 
        		if(response.match("Повторите пароль emptyMessagePattern")){ 
        			param = "Повторите пароль";
        			inputName = "password2";
        			pattern = "emptyMessagePattern";
        			inputProcess(param, inputName, pattern);
        		};
        		if(response.match("Имя emptyMessagePattern")){
        			param = "Имя";
        			inputName = "name";
        			pattern = "emptyMessagePattern";
        			inputProcess(param, inputName, pattern);
        		};
        		if(response.match("Имя messagePattern1")){ 
        			param = "Имя";
        			inputName = "name";
        			pattern = "messagePattern1";
        			inputProcess(param, inputName, pattern);
        		};
        		if(response.match("Фамилия messagePattern1")){ 
        			param = "Фамилия";
        			inputName = "surname";
        			pattern = "messagePattern1";
        			inputProcess(param, inputName, pattern);
        		};
        		if(response.match("Название категории messagePattern1")){ 
        			param = "Название категории";
        			inputName = "categoryName";
        			pattern = "messagePattern1";
        			inputProcess(param, inputName, pattern);
        		};
        		if(response.match("Название категории emptyMessagePattern")){ 
        			param = "Название категории";
        			inputName = "categoryName";
        			pattern = "emptyMessagePattern";
        			inputProcess(param, inputName, pattern);
        		};
        		if(response.match("Имя художника emptyMessagePattern")){ 
        			param = "Имя художника";
        			inputName = "artistName";
        			pattern = "emptyMessagePattern";
        			inputProcess(param, inputName, pattern);
        		};
        		if(response.match("Имя художника messagePattern2")){ 
        			param = "Имя художника";
        			inputName = "artistName";
        			pattern = "messagePattern2";
        			inputProcess(param, inputName, pattern);
        		};
        		if(response.match("Название арта emptyMessagePattern")){ 
        			param = "Название арта";
        			inputName = "artName";
        			pattern = "emptyMessagePattern";
        			inputProcess(param, inputName, pattern);
        		};
        		if(response.match("Название арта messagePattern3")){ 
        			param = "Название арта";
        			inputName = "artName";
        			pattern = "messagePattern3";
        			inputProcess(param, inputName, pattern);
        		}; 
        		if(response.match("Ссылка на оригинал emptyMessagePattern")){ 
        			param = "Ссылка на оригинал";
        			inputName = "originalUrl";
        			pattern = "emptyMessagePattern";
        			inputProcess(param, inputName, pattern);
        		};
        		if(response.match("Ссылка на оригинал messagePattern4")){ 
        			param = "Ссылка на оригинал";
        			inputName = "originalUrl";
        			pattern = "messagePattern4";
        			inputProcess(param, inputName, pattern);
        		}; 
        		if(!response.match("Логин")){ 
        			inputName = "login";
        			resetMessage(inputName);
        		};
        		if(!response.match("Пароль")){ 
        			inputName = "password";
        			resetMessage(inputName);
        		}; 
        		if(!response.match("Повторите пароль")){ 
        			inputName = "password2";
        			resetMessage(inputName);
        		}; 
        		if(!response.match("Имя")){ 
        			inputName = "name";
        			resetMessage(inputName);
        		}; 
        		if(!response.match("Фамилия")){ 
        			inputName = "surname";
        			resetMessage(inputName);
        		}; 
        		if(!response.match("Название категории")){ 
        			inputName = "categoryName";
        			resetMessage(inputName);
        		}; 
        		if(!response.match("Имя художника")){ 
        			inputName = "artistName";
        			resetMessage(inputName);
        		}; 
        		if(!response.match("Название арта")){ 
        			inputName = "artName";
        			resetMessage(inputName);
        		}; 
        		if(!response.match("Ссылка на оригинал")){ 
        			inputName = "originalUrl";
        			resetMessage(inputName);
        		};
        	};
        } else {
            alert(xmlHttp.status);
        }
    };
}

function formProcess() {
	var inputs = document.getElementsByTagName('input');
	for(var i=0; i<inputs.length; i++){
		if(inputs[i].getAttribute('type')=='text'||inputs[i].getAttribute('type')=='password'){
			var inputName = inputs[i].getAttribute('name');
			var inputDiv = document.getElementById(inputName+"Input");
			inputDiv.className = "message";
			// вставляем сообщение об ошибке
			var p = inputDiv.getElementsByTagName("p");
			if(p.length==0){            			
				var p = document.createElement("p");             	
		    	p.innerHTML = getPattern(param, pattern);
		    	loginInput.appendChild(p);
			}     		           		
		}
	}
}

// Функция добабляет сообщение об ошибке к полю с name = inputName и меняет стиль css блока
function inputProcess(param, inputName, pattern) {
		var inputDiv = document.getElementById(inputName+"Input");
		inputDiv.className = "message";
		var ps = inputDiv.getElementsByTagName('p');
		if(ps.length==0){ 
			var p = document.createElement('p');
			p.innerHTML = getPattern(param, pattern);
			inputDiv.appendChild(p);
		} else if(ps.length==1){
			var p = ps[0];
			p.innerHTML = getPattern(param, pattern);
		}   		           		
}

// Функция возвращает поле в исходное состояние
function resetMessage(inputName) {	
	var inputDiv = document.getElementById(inputName+"Input");
	if(inputDiv != null){
		inputDiv.className = "";
		var ps = inputDiv.getElementsByTagName("p");
		if(ps.length != 0){ 
			inputDiv.removeChild(ps[0]);
		};
	};
}

// функция, возвращающая текст сообщения по шаблону
function getPattern(param, pattern) {
	var patternMessage = null;
	if(pattern=="emptyMessagePattern"){
		patternMessage = "Поле "+param+"<br>является обязательным к заполнению!";
	}
	if (pattern=="messagePattern1"){
		patternMessage = "Проверьте данные в поле "+param+"<br>(формат: Иван либо Ivan)";
	}
	if (pattern=="messagePattern2"){
		patternMessage = "Проверьте данные в поле "+param+"<br>(допустимы лишь цифры и буквы)";
	}
	if (pattern=="messagePattern3"){
		patternMessage = "Проверьте данные в поле "+param+"<br>(формат: название файла. Пример: арт1.jpg)";
	}
	if (pattern=="messagePattern4"){
		patternMessage = "Проверьте данные в поле "+param+"<br>(формат: http://... либо https://...)";	
	}
	if (pattern=="alreadyExistPattern"){
		patternMessage = param+" уже существует";	
	}
	if (pattern=="passwordsEqualsPattern"){
		patternMessage = "Пароли должны совпадать!";	
	}
	return patternMessage;
}