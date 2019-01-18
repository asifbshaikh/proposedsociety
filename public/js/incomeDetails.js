$(document).ready(function(){

	$('#nextbtnforincome').attr('disabled','disabled');
	
	var occupationSelection = $("#applicant_occupation").val();
	//var occupation = $("input[name='occupation']:checked").val();
	if (occupationSelection == 'select_') {
		$("#applicant_employedTable").hide(); 
	    $("#applicant_selfEmployedTable").hide();
	}
	if (occupationSelection == 'E') {
		$('#nextbtnforincome').removeAttr('disabled');
		var x = $("#applicant_employedTable");
		showAppropriateTable(x);
		$("#applicant_isLoanTakenTr").css({'padding-right': '54px'});

		var isloan = $("#loan").val();
		if(isloan == "true"){
			$("#loanDetailsTable").show();
		}
		else{
			$("#loanDetailsTable").hide();
		}
	}
	if (occupationSelection == 'S') {
		$('#nextbtnforincome').removeAttr('disabled');
		var x = $("#applicant_selfEmployedTable");
		showAppropriateTable(x);
		$("#applicant_isLoanTakenTr").css({'padding-right': '21px'});

		var isloan = $("#loan").val();
		if(isloan == "true"){
			$("#loanDetailsTable").show();
		}
		else{
			$("#loanDetailsTable").hide();
		}		
	}

	$("#applicant_occupation").change(function(){
		$('#nextbtnforincome').removeAttr('disabled');
		if ($("#applicant_occupation").val() == 'select_') {
			$("#applicant_employedTable").hide(); 
		    $("#applicant_selfEmployedTable").hide();
		}
	    if ($("#applicant_occupation").val() == 'E') {
	       	var x = $("#applicant_employedTable");
	       	$("#applicant_selfEmployedTable input[type=text]").val("");
			showAppropriateTable(x);
			$("#applicant_isLoanTakenTr").css({'padding-right': '54px'});
			
			var isloan = $("#loan").val();
			if(isloan == "true"){
				$("#loanDetailsTable").show();
			}
			else{
				$("#loanDetailsTable").hide();
			}

	    }
	    if ($("#applicant_occupation").val() == 'S') {
	       	var x = $("#applicant_selfEmployedTable");
	       	$("#applicant_employedTable input[type=text]").val("");
			showAppropriateTable(x);
			$("#applicant_isLoanTakenTr").css({'padding-right': '21px'});
			
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

function showAppropriateTable(showVar)
{
	$("#Startdiv").show();
	$("#applicant_employedTable").hide(); 
    $("#applicant_selfEmployedTable").hide();
    $("#officeAddressTable").show();
    $("#applicant_isLoanTaken").show();

    showVar.show();
}


