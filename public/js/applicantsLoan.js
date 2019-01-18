$(document).ready(function(){
	$("#showLoanDivForApplicant").hide();
	$("#showLoanDivForCoApplicant").hide();

	if ($.trim($("#viewLoansTableDivOnLoad  #viewloans").html()).length > 0 ){
		$('input:radio[name="hasLoanOnApplicant"]').filter('[value="true"]').attr('checked', true);
		$("#showLoanDivForApplicant").show();
		cancelLoanButtonClick(true);
    }else {
    	$('input:radio[name="hasLoanOnApplicant"]').filter('[value="false"]').attr('checked', true);
    }
	
	if ($.trim($("#viewCoAppLoansTableDivOnLoad #viewloans").html()).length > 0 ){
		$('input:radio[name="hasLoanOnCoApplicant"]').filter('[value="true"]').attr('checked', true);
		$("#showLoanDivForCoApplicant").show();
		cancelLoanButtonClick();
	}else{
		$('input:radio[name="hasLoanOnCoApplicant"]').filter('[value="false"]').attr('checked', true);
	}
	
	$("input[name='hasLoanOnApplicant']").click(function(){
		if ($("input[name='hasLoanOnApplicant']:checked").val() == 'true') {
			if ($.trim($("#viewLoansTableDivOnLoad  #viewloans").html()).length > 0 ){
				$('input:radio[name="hasLoanOnApplicant"]').filter('[value="true"]').attr('checked', true);
				$("#showLoanDivForApplicant").show();
				cancelLoanButtonClick(true);
		    }else {
		    	$('input:radio[name="hasLoanOnApplicant"]').filter('[value="false"]').attr('checked', true);
		    	$("#showLoanDivForApplicant").show();
		    	addMoreLoanButtonClick(true);
		    }
		}else if($("input[name='hasLoanOnApplicant']:checked").val() == 'false') {
			$("#showLoanDivForApplicant").hide();
		}
	});
	
	$("input[name='hasLoanOnCoApplicant']").click(function(){
		if ($("input[name='hasLoanOnCoApplicant']:checked").val() == 'true') {
			if ($.trim($("#viewCoAppLoansTableDivOnLoad #viewloans").html()).length > 0 ){
				$('input:radio[name="hasLoanOnCoApplicant"]').filter('[value="true"]').attr('checked', true);
				$("#showLoanDivForCoApplicant").show();
				cancelLoanButtonClick();
			}else{
				$('input:radio[name="hasLoanOnCoApplicant"]').filter('[value="false"]').attr('checked', true);
				$("#showLoanDivForCoApplicant").show();
				addMoreLoanButtonClick(false);
			}
		}else if($("input[name='hasLoanOnCoApplicant']:checked").val() == 'false') {
			$("#showLoanDivForCoApplicant").hide();
		}
	});
});

function cancelLoanButtonClick(isAppcnt){
	if(isAppcnt == true){
		$("#applicant_loanDetailsTable").hide();
		hideSaveCancelButton("");
	}else{
		$("#co_applicant_loanDetailsTable").hide();
		hideSaveCancelButton("1");
	}
}
function addMoreLoanButtonClick(isAppcnt){
	if(isAppcnt == true){
		$("#applicant_loanDetailsTable").show();
		showSaveCancelButton("");
	}else{
		$("#co_applicant_loanDetailsTable").show();
		showSaveCancelButton("1");
	}
}

function hideSaveCancelButton(sufix) //sufix "1" for co_applicant 
{
	$("#addMoreLoanButton"+ sufix).show();
	$("#saveLoanButton"+ sufix).hide();
	$("#cancelLoanButton"+ sufix).hide();
}
function showSaveCancelButton(sufix) //sufix "1" for co_applicant 
{
	$("#addMoreLoanButton"+ sufix).hide();
	$("#saveLoanButton"+ sufix).show();
	$("#cancelLoanButton"+ sufix).show();
}
function loanDetail(prefix){
	$('#'+ prefix +'applicant_financer').val('');
	$('#'+ prefix +'applicant_loanAmount').val('');
	$('#'+ prefix +'applicant_monthlyEmi').val('');
	$('#'+ prefix +'applicant_installmentsPaid').val('');
	$('#'+ prefix +'applicant_installmentsBalance').val('');
}

function deleteLoanDetailsFromList(id,isApplcnt){
	if(isApplcnt == true){
		var URLLOANVAL = '/application/fill/loan';
	}else{
		var URLLOANVAL = '/application/fill/CoApplicantsLoan';
	}
	if (confirm('Are you sure you want to do this ?')) {
			$.ajax({
				url: '/application/loanDetails/delete/'+id,
				type: 'POST',
				success: function(response) {
					$('#loanDiv').hide();
					$.ajax({
						url: URLLOANVAL,
						type: 'GET',
						success: function(response) {
							$('#loanDiv').show();
						if(isApplcnt == true){
							$('#viewLoansTableDiv').html(response);
							$("#viewLoansTableDivOnLoad").hide();
							loanDetail("");
						}else {
							$('#viewCoAppLoansTableDiv').html(response);
							$("#viewCoAppLoansTableDivOnLoad").hide();
							loanDetail("co_")
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

function addLoanToList(isApplcnt) {
	if(isApplcnt == true){
		$("#addMoreLoanButton").show();
		$("#saveLoanButton").hide();
		$("#cancelLoanButton").hide();
	}else{
		$("#addMoreLoanButton1").show();
		$("#saveLoanButton1").hide();
		$("#cancelLoanButton1").hide();
	}
	
	var applicant_amountPerMonthFromSal = "";
	var applicant_financer = "";
	var applicant_loanAmount = "";
	var applicant_monthlyEmi = "";
	var applicant_installmentsPaid = "";
	var applicant_installmentsBalance = "";
	
	var co_applicant_amountPerMonthFromSal = "";
	var co_applicant_financer = "";
	var co_applicant_loanAmount = "";
	var co_applicant_monthlyEmi = "";
	var co_applicant_installmentsPaid = "";
	var co_applicant_installmentsBalance = "";
	
	var loan = {};
	
	if(isApplcnt == true){
		applicant_amountPerMonthFromSal = $("#applicant_loanDetailsdiv #applicant_amountPerMonthFromSal").val();
		applicant_financer = $("#applicant_loanDetailsdiv #applicant_financer").val();
		applicant_loanAmount = $("#applicant_loanDetailsdiv #applicant_loanAmount").val();
		applicant_monthlyEmi = $("#applicant_loanDetailsdiv #applicant_monthlyEmi").val();
		applicant_installmentsPaid = $("#applicant_loanDetailsdiv #applicant_installmentsPaid").val();
		applicant_installmentsBalance = $("#applicant_loanDetailsdiv #applicant_installmentsBalance").val();
		
		loan = {
			"applicant_amountPerMonthFromSal": applicant_amountPerMonthFromSal,
			"applicant_financer": applicant_financer,
			"applicant_loanAmount": applicant_loanAmount,
			"applicant_monthlyEmi": applicant_monthlyEmi,
			"applicant_installmentsPaid": applicant_installmentsPaid,
			"applicant_installmentsBalance": applicant_installmentsBalance
		};
		
	}else {
		co_applicant_amountPerMonthFromSal = $("#co_applicant_loanDetailsdiv #co_applicant_amountPerMonthFromSal").val();
		co_applicant_financer = $("#co_applicant_loanDetailsdiv #co_applicant_financer").val();
		co_applicant_loanAmount = $("#co_applicant_loanDetailsdiv #co_applicant_loanAmount").val();
		co_applicant_monthlyEmi = $("#co_applicant_loanDetailsdiv #co_applicant_monthlyEmi").val();
		co_applicant_installmentsPaid = $("#co_applicant_loanDetailsdiv #co_applicant_installmentsPaid").val();
		co_applicant_installmentsBalance = $("#co_applicant_loanDetailsdiv #co_applicant_installmentsBalance").val();
		
		loan = {
			"co_applicant_amountPerMonthFromSal": co_applicant_amountPerMonthFromSal,
			"co_applicant_financer": co_applicant_financer,
			"co_applicant_loanAmount": co_applicant_loanAmount,
			"co_applicant_monthlyEmi": co_applicant_monthlyEmi,
			"co_applicant_installmentsPaid": co_applicant_installmentsPaid,
			"co_applicant_installmentsBalance": co_applicant_installmentsBalance
		};
	}
	//alert("isApplcnt"+isApplcnt);
	if(isApplcnt == true){
		var URLLOANVAL = '/application/fill/loan';
	}else{
		var URLLOANVAL = '/application/fill/CoApplicantsLoan';
	}

	$.ajax({
		url: URLLOANVAL,
		type: 'POST',
		data: JSON.stringify(loan),
		contentType: "application/json",
		beforeSend: function() {
			$("#loanDetailsTable input").attr("disabled", "disabled");
		},
		
		success: function() {
			if(isApplcnt == true){
				$("#applicant_loanDetailsTable").hide();
			}else{
				$("#co_applicant_loanDetailsTable").hide();
			}
			var notify = $('#loan_notify');
			notify.text("Json is successfully posted.");
			$('#loanDiv').hide();
			$.ajax({
				url: URLLOANVAL,
				type: 'GET',
				success: function(response) {
					$('#loanDiv').show();
				if(isApplcnt == true){
					$('#viewLoansTableDiv').html(response);
					$("#viewLoansTableDivOnLoad").hide();
					loanDetail("");
				}else {
					$('#viewCoAppLoansTableDiv').html(response);
					$("#viewCoAppLoansTableDivOnLoad").hide();
					loanDetail("co_");
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
				$("#applicant_loanDetailsTable").show();
				showSaveCancelButton("");
			}else{
				$("#co_applicant_loanDetailsTable").show();
				showSaveCancelButton("1");
			}
		},
		complete: function() {
			$("#loanDetailsTable input").removeAttr("disabled");
		}
	});
}