$(document).ready(function(){

	$('#nextbtnforincome').attr('disabled','disabled');
	
	var coapp_occupationSelection = $("#co_applicant_occupation").val();
	//var occupation = $("input[name='occupation']:checked").val();
	if (coapp_occupationSelection == 'select_') {
		$("#co_applicant_employedTable").hide(); 
	    $("#co_applicant_selfEmployedTable").hide();
	}
	if (coapp_occupationSelection == 'E') {
		$('#nextbtnforincome').removeAttr('disabled');
		var x = $("#co_applicant_employedTable");
		showAppropriateTableForCoApplcnt(x);
		$("#co_applicant_isLoanTakenTr").css({'padding-right': '42px'});

		var isloan = $("#loan").val();
		if(isloan == "true"){
			$("#loanDetailsTable").show();
		}
		else{
			$("#loanDetailsTable").hide();
		}
	}
	if (coapp_occupationSelection == 'S') {
		$('#nextbtnforincome').removeAttr('disabled');
		var x = $("#co_applicant_selfEmployedTable");
		showAppropriateTableForCoApplcnt(x);
		$("#co_applicant_isLoanTakenTr").css({'padding-right': '9px'});

		var isloan = $("#loan").val();
		if(isloan == "true"){
			$("#loanDetailsTable").show();
		}
		else{
			$("#loanDetailsTable").hide();
		}		
	}

	$("#co_applicant_occupation").change(function(){
		$('#nextbtnforincome').removeAttr('disabled');
		if ($("#co_applicant_occupation").val() == 'select_') {
			$("#co_applicant_employedTable").hide(); 
		    $("#co_applicant_selfEmployedTable").hide();
		}
	    if ($("#co_applicant_occupation").val() == 'E') {
	       	var x = $("#co_applicant_employedTable");
	       	$("#co_applicant_selfEmployedTable input[type=text]").val("");
			showAppropriateTableForCoApplcnt(x);
			$("#co_applicant_isLoanTakenTr").css({'padding-right': '54px'});
			
			var isloan = $("#loan").val();
			if(isloan == "true"){
				$("#loanDetailsTable").show();
			}
			else{
				$("#loanDetailsTable").hide();
			}

	    }
	    if ($("#co_applicant_occupation").val() == 'S') {
	       	var x = $("#co_applicant_selfEmployedTable");
	       	$("#co_applicant_employedTable input[type=text]").val("");
			showAppropriateTableForCoApplcnt(x);
			$("#co_applicant_isLoanTakenTr").css({'padding-right': '21px'});
			
			var isloan = $("#loan").val();
			if(isloan == "true"){
				$("#loanDetailsTable").show();
			}
			else{
				$("#loanDetailsTable").hide();
			}

	    }
	});

	$("#loan").change(function(){
		var isloan = $("#loan").val();
		//alert("isloan: "+isloan);
		if(isloan == "true"){
			$("#loanDetailsTable").show();
		}
		else{
			$("#loanDetailsTable").hide();
		}
	});

});

function showAppropriateTableForCoApplcnt(showVar)
{
	$("#Startdiv").show();
	$("#co_applicant_employedTable").hide(); 
    $("#co_applicant_selfEmployedTable").hide();
    $("#officeAddressTable").show();
    $("#co_applicant_isLoanTaken").show();

    showVar.show();
}