$(document).ready(function(){

	var marStat = $("input[name='co_applicant.marital_status']:checked").val();
	if (marStat == null){
		$("#co_applicant_spouse-details").hide();
	}
	if (marStat == 'M') {
		$("#co_applicant_spouse-details").show();			
	}else if (marStat == 'U') {
		$("#co_applicant_spouse-details").hide();
	}
	
	var perAddOpt = $("input[name='co_applicant.permanent_address_same_as']:checked").val();
	if (perAddOpt == null){
		$("input:radio[name='co_applicant.permanent_address_same_as']:nth(1)").attr('checked',true);
		$("#co_applicant_perAddFieldSet").show();
	}
	else if (perAddOpt == 'other') {
		$("#co_applicant_perAddFieldSet").show();			
	}else{
		$("#co_applicant_perAddFieldSet").hide();
	}

	var resAddOpt = $("input[name='co_applicant.residential_address_same_as']:checked").val();
	if (resAddOpt == null){
		$("input:radio[name='co_applicant.residential_address_same_as']:nth(2)").attr('checked',true);
		$("#co_applicant_resAddFieldSet").show();
	}
	else if (resAddOpt == 'other') {
		$("#co_applicant_resAddFieldSet").show();			
	}else{
		$("#co_applicant_resAddFieldSet").hide();
	}

	var comAddOpt = $("input[name='co_applicant.communication_address_same_as']:checked").val();
	if (comAddOpt == null){
		$("input:radio[name='co_applicant.communication_address_same_as']:nth(3)").attr('checked',true);
		$("#co_applicant_comAddFieldSet").show();
	}
	else if (comAddOpt == 'other') {
		$("#co_applicant_comAddFieldSet").show();			
	}else{
		$("#co_applicant_comAddFieldSet").hide();
	}

	
	/*$("#buttonAtPageLoad").show();
	$("#buttonAtPageLoad").click(function(){
		$("#buttonAtPageLoad").hide();
        addDependentToList();
        $("#buttonAfterFirstClick").show();
    });*/

	$("input[name='co_applicant.marital_status']").click(function(){
	    if ($("input[name='co_applicant.marital_status']:checked").val() == 'M') {
	       	$("#co_applicant_spouse-details").show();
	    }
	    if ($("input[name='co_applicant.marital_status']:checked").val() == 'U') {
	       	$("#co_applicant_spouse-details").hide();
	       	$("#co_applicant_spouse-details input[type=text]").val("");
	    }
	});

	$("input[name='co_applicant.permanent_address_same_as']").click(function(){
	    if ($("input[name='co_applicant.permanent_address_same_as']:checked").val() == 'other') {
	       	$("#co_applicant_perAddFieldSet").show();
	    }
	    else{
	       	$("#co_applicant_perAddFieldSet").hide();
	       	$("#co_applicant_perAddFieldSet input[type=text]").val("");
	    }
	});

	$("input[name='co_applicant.residential_address_same_as']").click(function(){
	    if ($("input[name='co_applicant.residential_address_same_as']:checked").val() == 'other') {
	       	$("#co_applicant_resAddFieldSet").show();
	    }
	    else{
	       	$("#co_applicant_resAddFieldSet").hide();
	       	$("#co_applicant_resAddFieldSet input[type=text]").val("");
	    }
	});

	$("input[name='co_applicant.communication_address_same_as']").click(function(){
	    if ($("input[name='co_applicant.communication_address_same_as']:checked").val() == 'other') {
	       	$("#co_applicant_comAddFieldSet").show();
	    }
	    else{
	       	$("#co_applicant_comAddFieldSet").hide();
	       	$("#co_applicant_comAddFieldSet input[type=text]").val("");
	    }
	});
});

/*function showTableForDependent(){
	$("#addDependent").show();
}*/
