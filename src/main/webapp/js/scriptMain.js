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

document.addEventListener("DOMContentLoaded", process);
    
function process() {
    var params = "ajax=true&page=main";
    var url = "/Chaos/ControllerServlet";
    xmlHttp.open("post", url, true);
    xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlHttp.onreadystatechange = respProcess; 
    xmlHttp.send(params);  
}

function respProcess() {
        if(xmlHttp.readyState == 4){
        	if(xmlHttp.status == 200){        		
        		var response = xmlHttp.responseText;
        		var catList = response.match(/[0-9]{1,7}_[A-Z]{1}[a-z]{0,20}|[0-9]{1,7}_[А-Я]{1}[а-я]{0,20}/g);      		
        		var div = document.getElementById("categoryList");
        		for (var i=0; i<catList.length; i++) {
        			var catIdName = catList[i].split(/_/);
        			var catId = catIdName[0];
        			var catName = catIdName[1];
        			var p = document.createElement("p");             	
    		    	p.innerHTML = "<a id=\"link\" href=\"/Chaos/ControllerServlet?categoryId=" 
    		    		+catId+ "\">"+catName +"</a>";
    		    	div.appendChild(p);	    	
        		}      		
        		var artList = response.match(/\|[^\|]*\|/g);
        		var artDiv = document.getElementById("artList");
        		for (var i=0; i<artList.length; i++) {
        			var artIdSrc = artList[i].split(/_/);        			
        			var artId = artIdSrc[0].match(/[^\|].*/);        			
        			var src = artIdSrc[1].match(/[^\|]*/);      			
        			var a = document.createElement("a");             	
    		    	a.innerHTML = "<a id=\"img\" href=\"/Chaos/ControllerServlet?artId="+ 
    		    	artId+"&controlParam=art\"><img src=\""+src+"\"height=\"300\"></a>";
    		    	artDiv.appendChild(a);	
        		}
        } else {
            alert(xmlHttp.status);
        };
    };
}