$(document).ready(function(){
	$("#RDFormShow").hide();
	$("#buttonShowFormRD").show();
	 
  	if ($("#recurringDepositDetailsDiv input:radio[name=willSurrender]:checked").val() == null){
		$("#willSurrenderYesDiv").hide();
		$("#willSurrenderNoDiv").hide();
		$("#rdNotCuttingFrmSalDiv").hide();
	}
  	if ($("#recurringDepositDetailsDiv input:radio[name=willSurrender]:checked").val() == 'true'){
		$("#willSurrenderYesDiv").show();
		$("#willSurrenderNoDiv").hide();
		$("#rdNotCuttingFrmSalDiv").hide();
	}else if ($("input:radio[name=willSurrender]:checked").val() == 'false'){
		$("#willSurrenderYesDiv").hide();
		$("#willSurrenderNoDiv").show();
		$("#rdNotCuttingFrmSalDiv").hide();
	}
  	
  	if ($("#recurringDepositDetailsDiv input:radio[name=monthlyAmntDedctedFrmSal]:checked").val() == 'false'){
		$("#rdNotCuttingFrmSalDiv").show();
	}else if ($("#recurringDepositDetailsDiv input:radio[name=monthlyAmntDedctedFrmSal]:checked").val() == 'true'){
		$("#rdNotCuttingFrmSalDiv").hide();
	}
  	
  	$("#buttonShowFormRD").click(function(){
  		$("#RDFormShow").show();
  		$("#buttonShowFormRD").hide();
	});
  	
  	$("#buttonHideFormRD").click(function(){
  		$("#RDFormShow").hide();
  		$("#buttonShowFormRD").show();
	});
  
  $("#recurringDepositDetailsDiv input:radio[name='willSurrender']").click(function(){
		$('#recurringDepositDetailsDiv input[name="monthlyAmntDedctedFrmSal"]').attr('checked', false);
		$("#rdNotCuttingFrmSalDiv").hide();
		$("#rdNotCuttingFrmSalDiv input[type=text]").val("");
		if ($("#recurringDepositDetailsDiv input:radio[name=willSurrender]:checked").val() == 'true'){
			$("#willSurrenderYesDiv").show();
			$("#willSurrenderNoDiv").hide();
			$("#willSurrenderNoDiv input[type=text]").val("");
		}else {
			$("#willSurrenderYesDiv").hide();
			$("#willSurrenderYesDiv input[type=text]").val("");
			$("#willSurrenderNoDiv").show();
		}
  });
  
  $("#recurringDepositDetailsDiv input:radio[name='monthlyAmntDedctedFrmSal']").click(function(){
		if ($("input:radio[name=monthlyAmntDedctedFrmSal]:checked").val() == 'false'){
			$("#rdNotCuttingFrmSalDiv").show();
		}else {
			$("#rdNotCuttingFrmSalDiv").hide();
			$("#rdNotCuttingFrmSalDiv input[type=text]").val("");
		}
  });
  
});

function deleteRDDetailsFromList(id){
if (confirm('Are you sure you want to do this ?')) {
		
		$.ajax({
			url: '/application/recurring_deposit/delete/'+id,
			type: 'GET',
			success: function(response) {
					$.ajax({
						url: '/application/fill/rd_details',
						type: 'GET',
						success: function(response) {
							$('#viewRdDetailsTable').empty();
							$('#viewRdDetailsTable').html(response);
							$('.error').text("");
							$('.error').attr("style", "display:none");
						},
						error: function() {
							alert('Something went wrong while GET!');
						}
					});
			},
			error: function() {
				alert('Unautherised');
			}
		})
		
	} 
}
function addRdDetailsToList() {
    var nameOfHolder = $("#recurringDepositDetailsDiv #nameOfHolder").val();
    var bankName = $("#recurringDepositDetailsDiv #bankName").val();
    var rdstartDate = $("#recurringDepositDetailsDiv #rdstartDate").val();
    var rdmaturityDate = $("#recurringDepositDetailsDiv #rdmaturityDate").val();
    var installment = $("#recurringDepositDetailsDiv #installment").val();
    var installmentsPaid = $("#recurringDepositDetailsDiv #installmentsPaid").val();
    var installmentsBalance = $("#recurringDepositDetailsDiv #installmentsBalance").val();
    var principal = $("#recurringDepositDetailsDiv #principal").val();
    var interestRate = $("#recurringDepositDetailsDiv #interestRate").val();
    var maturityAmount = $("#recurringDepositDetailsDiv #maturityAmount").val();
    var willSurrender = $('#recurringDepositDetailsDiv input[name=willSurrender]:radio:checked').val();
    var amountCanAvail = $("#recurringDepositDetailsDiv #amountCanAvail").val();
    var allocation = $("#recurringDepositDetailsDiv #allocation").val();
    var monthlyAmntDedctedFrmSal = $('#recurringDepositDetailsDiv input[name=monthlyAmntDedctedFrmSal]:radio:checked').val();
    var monthlyInstallments = $("#recurringDepositDetailsDiv #monthlyInstallments").val();
    var balanceInstallments = $("#recurringDepositDetailsDiv #balanceInstallments").val();
    var balanceDedAmount = $("#recurringDepositDetailsDiv #balanceDedAmount").val();

    var rdWillSurrenderNo = {
    	"monthlyAmntDedctedFrmSal": monthlyAmntDedctedFrmSal
    }
    
    var rdWillSurrenderYes = {
    	"amountCanAvail": amountCanAvail,
    	"allocation": allocation
    }
    
    var rdNoCuttingFrmSal = {
		"monthlyInstallments": monthlyInstallments,
	    "balanceInstallments": balanceInstallments,
	    "balanceDedAmount": balanceDedAmount
    }
    
	var rddetails = {
		"nameOfHolder": nameOfHolder,
		"bankName": bankName,
	    "rdstartDate": rdstartDate,
	    "rdmaturityDate": rdmaturityDate,
	    "installment": installment,
	    "installmentsPaid": installmentsPaid,
	    "installmentsBalance": installmentsBalance,
	    "principal": principal,
	    "interestRate": interestRate,
	    "maturityAmount": maturityAmount,
	    "willSurrender": willSurrender,
	    "rdWillSurrenderNo": rdWillSurrenderNo,
	    "rdWillSurrenderYes": rdWillSurrenderYes,
	    "rdNoCuttingFrmSal": rdNoCuttingFrmSal
	};

	$.ajax({
		url: '/application/fill/rd_details',
		type: 'POST',
		data: JSON.stringify(rddetails),
		contentType: "application/json",
		beforeSend: function() {
			$("#recurringDepositDetailsDiv input").attr("disabled", "disabled");
		},
		success: function() {
			//alert("Json is successfully posted.");
			$("#RDFormShow").hide();
			$("#buttonShowFormRD").show();
			$("#recurringDepositDetailsDiv input[type=text]").val("");
			$("#recurringDepositDetailsDiv input[type=radio]").attr('checked', false);
			//$("#buttonAddRD").val("Add More");

			$.ajax({
				url: '/application/fill/rd_details',
				type: 'GET',
				success: function(response) {
					$('#viewRdDetailsTable').empty();
					$('#viewRdDetailsTable').html(response);
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
			if ("RD" in response){
				$.each(response.RD.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#recurringDepositDetailsDiv #"+paraId).show();
				    //alert("paraId :"+paraId);
				    $("#recurringDepositDetailsDiv #"+paraId).text(errorData.error);
				});
			}
			if ("WillSurrenderNo" in response){
				$.each(response.WillSurrenderNo.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#recurringDepositDetailsDiv #"+paraId).show();
				    $("#recurringDepositDetailsDiv #"+paraId).text(errorData.error);
				});
			}
			if ("WillSurrenderYes" in response){
				$.each(response.WillSurrenderYes.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#recurringDepositDetailsDiv #"+paraId).show();
				    $("#recurringDepositDetailsDiv #"+paraId).text(errorData.error);
				});
			}
			if ("NoCuttingFrmSal" in response){
				$.each(response.NoCuttingFrmSal.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#recurringDepositDetailsDiv #"+paraId).show();
				    $("#recurringDepositDetailsDiv #"+paraId).text(errorData.error);
				});
			}
		},
		complete: function() {
			$("#recurringDepositDetailsDiv  input").removeAttr("disabled");
			if ($("#recurringDepositDetailsDiv input:radio[name=willSurrender]:checked").val() == null){
				$("#willSurrenderYesDiv").hide();
				$("#willSurrenderNoDiv").hide();
				$("#rdNotCuttingFrmSalDiv").hide();
			}
		  	if ($("#recurringDepositDetailsDiv input:radio[name=willSurrender]:checked").val() == 'true'){
				$("#willSurrenderYesDiv").show();
				$("#willSurrenderNoDiv").hide();
				$("#rdNotCuttingFrmSalDiv").hide();
			}else if ($("input:radio[name=willSurrender]:checked").val() == 'false'){
				$("#willSurrenderYesDiv").hide();
				$("#willSurrenderNoDiv").show();
				$("#rdNotCuttingFrmSalDiv").hide();
			}
		  	
		  	if ($("#recurringDepositDetailsDiv input:radio[name=monthlyAmntDedctedFrmSal]:checked").val() == 'false'){
				$("#rdNotCuttingFrmSalDiv").show();
			}else if ($("#recurringDepositDetailsDiv input:radio[name=monthlyAmntDedctedFrmSal]:checked").val() == 'true'){
				$("#rdNotCuttingFrmSalDiv").hide();
			}
			//$("#willSurrenderYesDiv").hide();
			//$("#willSurrenderNoDiv").hide();
			//$("#rdNotCuttingFrmSalDiv").hide();
		}
	});

}