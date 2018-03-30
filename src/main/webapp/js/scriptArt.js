
$(document).ready(process);
    
function process() {
	var artId = $("#img").attr('name');
    var params = "artId="+artId+"&ajax=true&page=art&controlParam=art";
    if($("#textarea")!=null){
    	var comment = $("#textarea").val();
    	if(comment!=""){
        	params = "artId="+artId+"&comment="+comment+"&ajax=true&page=art&controlParam=newComment";
        }
    } 
    $.ajax({
  	  type: "POST",
  	  url: "/Chaos/ControllerServlet",
  	  data: params,
  	  success: respProcess
  	});
  }

		function respProcess(data) {      		  		
				var response = data;      			
        		var artList = response.match(/\|[^\|]*\|/g);
        		var artDiv = $("#artList");
        		artDiv.innerHTML='';
        		for (var i=0; i<artList.length; i++) {
        			var artIdSrc = artList[i].split(/_/);        			
        			var artId = artIdSrc[0].match(/[^\|].*/);        			
        			var src = artIdSrc[1].match(/[^\|]*/);      			
    		    	artDiv.append("<a id=\"img\" href=\"/Chaos/ControllerServlet?artId="+ 
    	    		    	artId+"&controlParam=art\"><img src= \""+src+"\"height=\"120\"></a>");
        		}
        		var commentList = response.match(/[0-9]{1,7}_\w*_[^~]*~/g);
        		var commentDiv = $("#commentTable");
        		commentDiv.innerHTML='';
        		for (var i=0; i<commentList.length; i++) {
        			var split = commentList[i].split(/_/);
        			var login = split[1];
        			var text = split[2].match(/[^~]*/);
        			commentDiv.append("<tr><td id=\"td1\">" + login + "</td>" 
    		    		+ "<td>" + text + "</td></tr>");
        		}      	
    };