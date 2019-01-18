$(document).ready(function(){
	$("#formButton").hide();
	$("#showDependentDivForApplicant").hide();
	$("#showDependentDivForCoApplicant").hide();

	if ($.trim($("#viewDependentsTableDivOnLoad #viewDependents").html()).length > 0 ){
		$('input:radio[name="hasDependentOnApplicant"]').filter('[value="true"]').attr('checked', true);
			$("#showDependentDivForApplicant").show();
			$('#applicant_addDependent').hide();
			$('#viewDependentsTableDivOnLoad').show();
			afterLoad(""); //Div Id Sufix "1"
    }else {
    	$('input:radio[name="hasDependentOnApplicant"]').filter('[value="false"]').attr('checked', true);
    }
	
	if ($.trim($("#viewCoappDependentsTableDivOnLoad #viewDependents").html()).length > 0 ){
		$('input:radio[name="hasDependentOnCoApplicant"]').filter('[value="true"]').attr('checked', true);
		$("#showDependentDivForCoApplicant").show();
		$('#co_applicant_addDependent').hide();
		$('#viewCoappDependentsTableDivOnLoad').show();
		afterLoad("1"); //Div Id Sufix "1"
	}else{
		$('input:radio[name="hasDependentOnCoApplicant"]').filter('[value="false"]').attr('checked', true);
	}

	$("input[name='hasDependentOnApplicant']").click(function(){
		if ($("input[name='hasDependentOnApplicant']:checked").val() == 'true') {		
			
			if ($.trim($("#viewDependentsTableDivOnLoad #viewDependents").html()).length > 0 ){
				$('input:radio[name="hasDependentOnApplicant"]').filter('[value="true"]').attr('checked', true);
					$("#showDependentDivForApplicant").show();
					$('#applicant_addDependent').hide();
					$('#viewDependentsTableDivOnLoad').show();
					afterLoad(""); //Div Id Sufix "1"
		    }else {
		    	$('input:radio[name="hasDependentOnApplicant"]').filter('[value="false"]').attr('checked', true);
		    	$("#showDependentDivForApplicant").show();
		    	afterAddMore("");
				$("#applicant_addDependent").show();
		    }
			
			
		}else if($("input[name='hasDependentOnApplicant']:checked").val() == 'false') {
			$("#showDependentDivForApplicant").hide();
		}
	});
	
	$("input[name='hasDependentOnCoApplicant']").click(function(){
		if ($("input[name='hasDependentOnCoApplicant']:checked").val() == 'true') {
			
			if ($.trim($("#viewCoappDependentsTableDivOnLoad #viewDependents").html()).length > 0 ){
				$('input:radio[name="hasDependentOnCoApplicant"]').filter('[value="true"]').attr('checked', true);
				$("#showDependentDivForCoApplicant").show();
				$('#co_applicant_addDependent').hide();
				$('#viewCoappDependentsTableDivOnLoad').show();
				afterLoad("1"); //Div Id Sufix "1"
			}else{
				$('input:radio[name="hasDependentOnCoApplicant"]').filter('[value="false"]').attr('checked', true);
				$("#showDependentDivForCoApplicant").show();
		    	afterAddMore("1");
				$("#co_applicant_addDependent").show();
			}
			
			}else if($("input[name='hasDependentOnCoApplicant']:checked").val() == 'false') {
			$("#showDependentDivForCoApplicant").hide();
		}
	});
});
function afterLoad(sufix){ //Div Id Sufix
	$("#formButton").show();
	$("#buttonAfterAddMore" + sufix).show();
	$("#buttonAfterSave" + sufix).hide();
	$("#buttonAfterCancel" + sufix).hide();
}
function afterAddMore(sufix){ //Div Id Sufix
	$("#formButton").show();
	$("#buttonAfterAddMore" + sufix).hide();
	$("#buttonAfterSave" + sufix).show();
	$("#buttonAfterCancel" + sufix).show();
}
function dependentData(prefix){
	$('#'+ prefix +'applicant_dep_fname').val('');
	$('#'+ prefix +'applicant_dep_mname').val('');
	$('#'+ prefix +'applicant_dep_lname').val('');
	$('#'+ prefix +'applicant_dep_relation').val('');
}
function addMoreBottonClick(isAppcnt){
	$("#viewDependents").show();
	if(isAppcnt == true){
		$("#applicant_addDependent").show();
		afterAddMore("");
	}else{
		$("#co_applicant_addDependent").show();
		afterAddMore("1"); // Div Id Sufix "1"/null
	}
}
function afterCancelButton(isAppcnt){
	if(isAppcnt == true){
		$("#showDependentDivForApplicant").show();
		$('#applicant_addDependent').hide();
		afterLoad(""); //Div Id Sufix "1"
	}else{
	$("#showDependentDivForCoApplicant").show();
	$('#co_applicant_addDependent').hide();
	afterLoad("1");
	}
}
function deleteDependentFromList(id,isApplcnt){
	if(isApplcnt == true){
		var URLVAL = '/application/fill/dependent';
	}else{
		var URLVAL = '/application/fill/CoApplicantsDep';
	}
	if (confirm('Are you sure you want to do this ?')) {
			$.ajax({
				url: '/application/dependents/delete/'+id,
				type: 'POST',
				success: function(response) {
					$('#dependentDiv').hide();
					$.ajax({
						url: URLVAL,
						type: 'GET',
						success: function(response) {
							if(isApplcnt == true){
								$('#viewDependentsTableDiv').html(response);
								$("#viewDependentsTableDivOnLoad").hide();
								dependentData("");
							}else {
								$('#viewCoappDependentsTableDiv').html(response);
								$("#viewCoappDependentsTableDivOnLoad").hide();
								dependentData("co_");//prefix for coapplicant 
							}
							$('.error').text("");
						},
						error: function() {
							alert('Something went wrong while GET!');
						}
					});
				},
				error: function() {
					alert('Unautherised');
				}
			});
		} 
	}

function addDependentToList(isApplcnt) {
	var applicant_dep_title = "";
	var applicant_dep_fname = "";
	var applicant_dep_mname = "";
	var applicant_dep_lname = "";
	var applicant_dep_relation = "";
	
	var co_applicant_dep_title = "";
	var co_applicant_dep_fname = "";
	var co_applicant_dep_mname = "";
	var co_applicant_dep_lname = "";
	var co_applicant_dep_relation = "";
	
	var dependent = {};
	
	if(isApplcnt == true){
		
		$("#showDependentDivForApplicant").show();
		$('#applicant_addDependent').hide();
		afterLoad(""); //Div Id Sufix "1"
		
		applicant_dep_title = $("#applicant_addDependent #applicant_dep_title").val();
		applicant_dep_fname = $("#applicant_addDependent #applicant_dep_fname").val();
		applicant_dep_mname = $("#applicant_addDependent #applicant_dep_mname").val();
		applicant_dep_lname = $("#applicant_addDependent #applicant_dep_lname").val();
		applicant_dep_relation = $("#applicant_addDependent #applicant_dep_relation").val();
		
		dependent = {
				"applicant_dep_title": applicant_dep_title,
				"applicant_dep_fname": applicant_dep_fname,
				"applicant_dep_mname": applicant_dep_mname,
				"applicant_dep_lname": applicant_dep_lname,
				"applicant_dep_relation": applicant_dep_relation
		};
		
	}else{
		
		$("#showDependentDivForCoApplicant").show();
		$('#co_applicant_addDependent').hide();
		afterLoad("1"); //Div Id Sufix "1"
		
		co_applicant_dep_title = $("#co_applicant_addDependent #co_applicant_dep_title").val();
		co_applicant_dep_fname = $("#co_applicant_addDependent #co_applicant_dep_fname").val();
		co_applicant_dep_mname = $("#co_applicant_addDependent #co_applicant_dep_mname").val();
		co_applicant_dep_lname = $("#co_applicant_addDependent #co_applicant_dep_lname").val();
		co_applicant_dep_relation = $("#co_applicant_addDependent #co_applicant_dep_relation").val();
		
		dependent = {
				"co_applicant_dep_title": co_applicant_dep_title,
				"co_applicant_dep_fname": co_applicant_dep_fname,
				"co_applicant_dep_mname": co_applicant_dep_mname,
				"co_applicant_dep_lname": co_applicant_dep_lname,
				"co_applicant_dep_relation": co_applicant_dep_relation
		};
	}

	if(isApplcnt == true){
		var URLVAL = '/application/fill/dependent';
	}else{
		var URLVAL = '/application/fill/CoApplicantsDep';
	}
	
	$.ajax({
		url: URLVAL,
		type: 'POST',
		data: JSON.stringify(dependent),
		contentType: "application/json",
		beforeSend: function() {
			$("#addDependent input").attr("disabled", "disabled");
		},
		success: function() {
			var notify = $('#dependent_notify');
			notify.text("Json is successfully posted.");
			$('#dependentDiv').hide();
			$.ajax({
				url: URLVAL,
				type: 'GET',
				success: function(response) {
					$('#dependentDiv').show();
					if(isApplcnt == true){
						$('#viewDependentsTableDiv').html(response);
						$("#viewDependentsTableDivOnLoad").hide();
						dependentData("");
					}else {
						$('#viewCoappDependentsTableDiv').html(response);
						$("#viewCoappDependentsTableDivOnLoad").hide();
						dependentData("co_"); //prefix for coapplicant
					}
					$('.error').text("");
				},
				error: function() {
					alert('Something went wrong while GET!');
				}
			});
		},
		error: function(response) {
			var response = jQuery.parseJSON(response.responseText);
			$('.error').text("");
			$('.error').attr("style", "display:none");
			$.each(response.errors, function(errorsID,errorData) {
			    var paraId = errorData.fieldname+"Error";
			    $("#"+paraId).show();
			    $("#"+paraId).text(errorData.error);
			});
			if(isApplcnt == true){
			    	$('#applicant_addDependent').show();
			    	afterAddMore(""); //Div Id Sufix "null"
			}else{
				$('#co_applicant_addDependent').show();
				afterAddMore("1"); //Div Id Sufix "1"
			}
		},
		complete: function() {
			$("#addDependent input").removeAttr("disabled");
		}
	});
}