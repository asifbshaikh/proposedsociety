$(document).ready(function(){
	$("#bankFormShow").hide();
	$("#buttonShowFormBankAccnt").show();
	if ($("input:radio[name=anyDedFromBankAccount]:checked").val() == null){
		$("#deductionsFromBankDiv").hide();
    }
	if ($("input:radio[name=anyDedFromBankAccount]:checked").val() == 'true'){
        $("#deductionsFromBankDiv").show();
    }else if ($("input:radio[name=anyDedFromBankAccount]:checked").val() == 'false'){
        $("#deductionsFromBankDiv").hide();
    }
	
	/*if ($('#loan').is(':checked') == true){
    	$(".loanTakenTr").show();
    }else{
    	$(".loanTakenTr").hide();
    }*/
	
	if ($("input:radio[name=loan]:checked").val() == 'true'){
		$(".loanTakenTr").show();
    }else {
    	$(".loanTakenTr").hide();
    }
	
	$("#buttonShowFormBankAccnt").click(function(){
		$("#bankFormShow").show();
		$("#buttonShowFormBankAccnt").hide();
	});
	
	$("#buttonHideFormBankAccnt").click(function(){
		$("#bankFormShow").hide();
		$("#buttonShowFormBankAccnt").show();
	});
	
	$("input:radio[name='anyDedFromBankAccount']").click(function(){
	    $("#deductionsFromBankDiv").hide();
	    if ($("input:radio[name=anyDedFromBankAccount]:checked").val() == 'true'){
	        $("#deductionsFromBankDiv").show();
	    }else if ($("input:radio[name=anyDedFromBankAccount]:checked").val() == 'false'){
	        $("#deductionsFromBankDiv").hide();
	    }
	});
	
	$("input:radio[name='loan']").click(function(){
		if ($("input:radio[name=loan]:checked").val() == 'true'){
			$(".loanTakenTr").show();
	    }else if ($("input:radio[name=loan]:checked").val() == 'false'){
	    	$(".loanTakenTr").hide();
	    }
	});
	
	/*$('#loan').change(function() {
        if($(this).is(":checked")) {
        	$(".loanTakenTr").show();
        }else{
        	$(".loanTakenTr").hide();
        }        
    });*/
	

});

function deleteBankAccntsFromList(id){
	
	if (confirm('Are you sure you want to do this ?')) {
		
		$.ajax({
			url: '/application/fill/bankAccountDelete/'+id,
			type: 'POST',
			success: function(response) {
						$.ajax({
							url: '/application/fill/bankaccount',
							type: 'GET',
							success: function(response) {
								$('#viewBankAccntTable').empty();
								$('#viewBankAccntTable').html(response);
								$('.error').text("");
								$('.error').attr("style", "display:none");
							},
							error: function() {
								alert('Something went wrong while GET!');
							}
						});
			},
			error: function() {
				alert('Something went wrong while GET!');
			}
		});
		
	} else {
	    
		
	}
}

function addBankAccntsToList() {
    var bankName = $("#bankBalanceDetailsDiv #bankName").val();
    var accountNumber = $("#bankBalanceDetailsDiv #accountNumber").val();
    var balance = $("#bankBalanceDetailsDiv #balance").val();
    var anyDedFromBankAccount = $('#bankBalanceDetailsDiv input[name=anyDedFromBankAccount]:radio:checked').val();
    var loan = $('#bankBalanceDetailsDiv input[name=loan]:radio:checked').val();
    var loanEmi = $("#bankBalanceDetailsDiv #loanEmi").val();
    var balanceInstallments = $("#bankBalanceDetailsDiv #balanceInstallments").val();
    var balanceLoanAmount = $("#bankBalanceDetailsDiv #balanceLoanAmount").val();
    var otherMonthlyDeduction = $("#bankBalanceDetailsDiv #otherMonthlyDeduction").val();
    var totalMonthlyDeduction = $("#bankBalanceDetailsDiv #totalMonthlyDeduction").val();
    var allocateToBuy = $("#bankBalanceDetailsDiv #allocateToBuy").val();

    var deductions = {
    	"loan": loan,
    	"otherMonthlyDeduction": otherMonthlyDeduction,
	    "totalMonthlyDeduction": totalMonthlyDeduction
    }
    
    var loanTaken = {
		"loanEmi": loanEmi,
	    "balanceInstallments": balanceInstallments,
	    "balanceLoanAmount": balanceLoanAmount
    }
    
	var bankaccount = {
		"bankName": bankName,
	    "accountNumber": accountNumber,
	    "balance": balance,
	    "anyDedFromBankAccount": anyDedFromBankAccount,
	    "allocateToBuy": allocateToBuy,
	    "deductions": deductions,
	    "loanTaken": loanTaken
	};

	$.ajax({
		url: '/application/fill/bankaccount',
		type: 'POST',
		data: JSON.stringify(bankaccount),
		contentType: "application/json",
		beforeSend: function() {
			$("#bankBalanceDetailsDiv input").attr("disabled", "disabled");
		},
		success: function() {
			
			$("#bankFormShow").hide();
			$("#buttonShowFormBankAccnt").show();
			$("#bankBalanceDetailsDiv input[type=text]").val("");
			$("#bankBalanceDetailsDiv input[type=radio]").attr('checked', false);
			//$("#bankDetails input[type=checkbox]").attr('checked', false);
			//$("#buttonAddBankAccnt").val("Add More");

			$.ajax({
				url: '/application/fill/bankaccount',
				type: 'GET',
				success: function(response) {
					$('#viewBankAccntTable').empty();
					$('#viewBankAccntTable').html(response);
					$('.error').text("");
					$('.error').attr("style", "display:none");
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
			if ("BankAccount" in response){
				$.each(response.BankAccount.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#bankBalanceDetailsDiv #"+paraId).show();
				    $("#bankBalanceDetailsDiv #"+paraId).text(errorData.error);
				});
			}
			if ("BankAccountDeductions" in response){
				$.each(response.BankAccountDeductions.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#bankBalanceDetailsDiv #"+paraId).show();
				    $("#bankBalanceDetailsDiv #"+paraId).text(errorData.error);
				});
			}
			if ("BankAccountLoan" in response){
				$.each(response.BankAccountLoan.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#bankBalanceDetailsDiv #"+paraId).show();
				    $("#bankBalanceDetailsDiv #"+paraId).text(errorData.error);
				});
			}
		},
		complete: function() {
			$("#bankBalanceDetailsDiv input").removeAttr("disabled");
		}
	});

}