
$(document).ready(process);
    
function process() {
    var params = "ajax=true&page=main";
    $.ajax({
  	  type: "POST",
  	  url: "/Chaos/ControllerServlet",
  	  data: params,
  	  success: respProcess
  	});
  }

function respProcess(data) {      		  		
	var response = data;
        		var catList = response.match(/[0-9]{1,7}_[A-Z]{1}[a-z]{0,20}|[0-9]{1,7}_[А-Я]{1}[а-я]{0,20}/g);      		       		
        		var div = $("#categoryList");
        		for (var i=0; i<catList.length; i++) {
        			var catIdName = catList[i].split(/_/);
        			var catId = catIdName[0];      			
        			var catName = catIdName[1];
        			div.append("<a id=\"link\" href=\"/Chaos/ControllerServlet?categoryId=" 
        		    		+catId+ "\">"+catName +"</a>");
        		}      		
        		var artList = response.match(/\|[^\|]*\|/g);
        		var artDiv = $("#artList");
        		for (var i=0; i<artList.length; i++) {
        			var artIdSrc = artList[i].split(/_/);        			
        			var artId = artIdSrc[0].match(/[^\|].*/);        			
        			var src = artIdSrc[1].match(/[^\|]*/);      			
    		    	artDiv.append("<a id=\"img\" href=\"/Chaos/ControllerServlet?artId="+ 
    	    		    	artId+"&controlParam=art\"><img src=\""+src+"\"height=\"300\"></a>");	
        		}
		};