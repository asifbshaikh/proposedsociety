$(document).ready(function(){
	
	//Redirect to error element
	var second = $('p[haserror]');
	$(second.get(0)).prev().focus();
	
	$('#btndiv').hide();
	$("#smtbtn").click(function(){
		var approveckeck = $("input[name=approve]:checked").val();
		if(!approveckeck){
			$("#redioerror").show();
		}else{
			var btn = $("#smtbtn").val();
		     var buttonValue = btn;
		     if (buttonValue === "Approve" ){
		    	 $("#txtarea").val("Your application is approved! To complete the approval process, please proceed with payment of deposit by visiting www.proposedsociety.com.");
		    	 alert("Agent approved message emailed to agent");
		     }else{
		    	 //When Reject 
		    	 var txt = $("#txtarea").val();
		    	 if (txt === ""){
					$("#sendMsg").show();
			        $("#erroeMsg").show();
				}
		    	alert("Agent rejected message emailed to agent");
		     }
		}
	});	
	
	$("input[name='approve']").click(function () { 
		$('#btndiv').show();
		var approveckeck = $("input[name=approve]:checked").val();
		if(approveckeck == "N" || approveckeck == "H"){
			$("#sendMsg").show();
			$("#approve").hide();
			$("#txtarea").show();
			$("#send").show();
			$("#smtbtn").val("Send To Agent");
		}else{
			$("#sendMsg").show();
			$("#approve").show();
			$("#txtarea").hide();
			$("#send").hide();
			$("#erroeMsg").hide();
			$("#smtbtn").val("Approve");
		}
		
	});
});