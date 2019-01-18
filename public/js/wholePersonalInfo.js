$(document).ready(function(){
	
	var exists = $("input[name='coApplicantExists']:checked").val();
	if (exists == null){
		$("#showEntireDivForCoapplicant").hide();
	}
	if (exists == "true") {
		$("#showEntireDivForCoapplicant").show();			
	}else{
		$("#showEntireDivForCoapplicant").hide();
	}
	
	$("input[name='coApplicantExists']").click(function(){
		if ($("input[name='coApplicantExists']:checked").val() == "true") {
	       	$("#showEntireDivForCoapplicant").show();
	    }else {
	    	$("#showEntireDivForCoapplicant").hide();
	    }
	});
});