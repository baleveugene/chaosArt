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
	var artId = document.getElementById("img").getAttribute('name');
    var params = "artId="+artId+"&ajax=true&page=art&controlParam=art";
    alert(params);
    if(document.getElementById("textarea")!=null){
    	var comment = document.getElementById("textarea").value;
    	alert(comment);
    	if(comment!=""){
        	params = "artId="+artId+"&comment="+comment+"&ajax=true&page=art&controlParam=newComment";
        }
    } 
    alert(params);
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
        		var artList = response.match(/\|[^\|]*\|/g);
        		var artDiv = document.getElementById("artList");
        		artDiv.innerHTML='';
        		for (var i=0; i<artList.length; i++) {
        			var artIdSrc = artList[i].split(/_/);        			
        			var artId = artIdSrc[0].match(/[^\|].*/);        			
        			var src = artIdSrc[1].match(/[^\|]*/);      			
        			var a = document.createElement("a");             	
    		    	a.innerHTML = "<a id=\"img\" href=\"/Chaos/ControllerServlet?artId="+ 
    		    	artId+"&controlParam=art\"><img src= \""+src+"\"height=\"120\"></a>";
    		    	artDiv.appendChild(a);	
        		}
        		var commentList = response.match(/[0-9]{1,7}_\w*_[^~]*~/g);
        		var commentDiv = document.getElementById("commentTable");
        		commentDiv.innerHTML='';
        		for (var i=0; i<commentList.length; i++) {
        			var split = commentList[i].split(/_/);
        			var login = split[1];
        			var text = split[2].match(/[^~]*/);
        			var tr = document.createElement("tr");
        			tr.innerHTML = "<td id=\"td1\">" + login + "</td>" 
    		    		+ "<td>" + text + "</td>";
        			commentDiv.appendChild(tr);	    	
        		}      	
        } else {
            alert(xmlHttp.status);
        };
    };
}