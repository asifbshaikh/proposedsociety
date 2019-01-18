$(document).ready(function(){
	$("#FDFormShow").hide();
	$("#buttonShowFormFD").show();
	
  	if($("input:radio[name=fixedDeposit]:checked").val() == 'false'){
		$("#fdBankDetailsDiv").hide();
		$("#deductionFrmSalTr").hide();
		$("#fdLoanDiv").hide();
		$("#willtoSurrenderTable").hide();
		$("#amountAllocationYes").hide();
		$("#amountAllocationNo").hide();
	}
  		
  	if ($("#fixedDepositeDetailsDiv input:radio[name=haveTakenLoanOnFD]:checked").val() == 'true'){
		$("#deductionFrmSalTr").show();
		$("#fdLoanDiv").hide();
  		$("#willtoSurrenderTable").hide();
  		$("#amountAllocationYes").hide();
  		$("#amountAllocationNo").hide();
  	}else {
  		$("#deductionFrmSalTr").hide();
  		$("#fdLoanDiv").hide();
  		$("#willtoSurrenderTable").hide();
  		$("#amountAllocationYes").hide();
  		$("#amountAllocationNo").hide();
  	}
  	
  	if ($("#fixedDepositeDetailsDiv input:radio[name=monthlyAmntDedctedFrmSal]:checked").val() == 'true'){
  		$("#fdLoanDiv").hide();
  		$("#willtoSurrenderTable").show();
  		$("#amountAllocationYes").hide();
  		$("#amountAllocationNo").hide();
  	}else if ($("#fixedDepositeDetailsDiv input:radio[name=monthlyAmntDedctedFrmSal]:checked").val() == 'false'){
  		$("#fdLoanDiv").show();
  		$("#willtoSurrenderTable").show();
  		$("#amountAllocationYes").hide();
  		$("#amountAllocationNo").hide();
  	}
  	
  	if ($("#fixedDepositeDetailsDiv input:radio[name=willSurrender]:checked").val() == 'true'){
  		$("#amountAllocationYes").show();
  		$("#amountAllocationNo").hide();
  	}else if($("#fixedDepositeDetailsDiv input:radio[name=willSurrender]:checked").val() == 'false'){
  		$("#amountAllocationYes").hide();
  		$("#amountAllocationNo").hide();
  	}
  	/*$("input:radio[name='fixedDeposit']").click(function(){
	  	if($("input:radio[name=fixedDeposit]:checked").val() == 'false'){
			$("#fdBankDetailsDiv").hide();
			$("#deductionFrmSalTr").hide();
			$("#fdLoanDiv").hide();
			$("#willtoSurrenderTable").hide();
			$("#amountAllocationYes").hide();
			$("#amountAllocationNo").hide();
			$("#fdSubmitButtonDiv").hide();
			$("#fdRedirectButtonDiv").show();
		}else {
			$("#fdBankDetailsDiv").show();
			$("#fdSubmitButtonDiv").show();
			$("#fdRedirectButtonDiv").hide();
		}
	});*/
  	
  	$("#buttonShowFormFD").click(function(){
  		$("#FDFormShow").show();
  		$("#buttonShowFormFD").hide();
	});

  	$("#buttonHideFormFD").click(function(){
  		$("#FDFormShow").hide();
  		$("#buttonShowFormFD").show();
	});
  	
	$("#fixedDepositeDetailsDiv input:radio[name='haveTakenLoanOnFD']").click(function(){
		$('#fixedDepositeDetailsDiv input[name="monthlyAmntDedctedFrmSal"]').attr('checked', false);
		$('#fixedDepositeDetailsDiv input[name="willSurrender"]').attr('checked', false);
		$("#fdLoanDiv").hide();
  		$("#willtoSurrenderTable").hide();
  		$("#amountAllocationYes").hide();
  		$("#amountAllocationNo").hide();
  		$("#fdLoanDiv input[type=text]").val("");
  		$("#willtoSurrenderTable input[type=text]").val("");
  		$("#amountAllocationYes input[type=text]").val("");
  		$("#amountAllocationNo input[type=text]").val("");
		if ($("#fixedDepositeDetailsDiv input:radio[name=haveTakenLoanOnFD]:checked").val() == 'true'){
			$("#deductionFrmSalTr").show();
	  	}else {
	  		$("#deductionFrmSalTr").hide();	  		
	  		$("#deductionFrmSalTr input[type=text]").val("");
	  	}
	});
	
	$("#fixedDepositeDetailsDiv input:radio[name='monthlyAmntDedctedFrmSal']").click(function(){
		$('#fixedDepositeDetailsDiv input[name="willSurrender"]').attr('checked', false);
		$("#amountAllocationYes").hide();
  		$("#amountAllocationNo").hide();
  		$("#amountAllocationYes input[type=text]").val("");
  		$("#amountAllocationNo input[type=text]").val("");
		if ($("#fixedDepositeDetailsDiv input:radio[name=monthlyAmntDedctedFrmSal]:checked").val() == 'true'){
	  		$("#fdLoanDiv").hide();
	  		$("#willtoSurrenderTable").show();
	  		$("#fdLoanDiv input[type=text]").val("");
	  	}else {
	  		$("#fdLoanDiv").show();
	  		$("#willtoSurrenderTable").show();
	  	}
	});
	
	$("#fixedDepositeDetailsDiv input:radio[name='willSurrender']").click(function(){
		if ($("#fixedDepositeDetailsDiv input:radio[name=willSurrender]:checked").val() == 'true'){
	  		$("#amountAllocationYes").show();
	  		$("#amountAllocationNo").hide();
	  		$("#amountAllocationNo input[type=text]").val("");
	  	}else {
	  		$("#amountAllocationYes").hide();
	  		$("#amountAllocationYes input[type=text]").val("");
	  		$("#amountAllocationNo").hide();
	  	}
	});
});


function deleteFDDetailsFromList(id){
	if (confirm('Are you sure you want to do this ?')) {
			
			$.ajax({
				url: '/application/fixed_deposit/delete/'+id,
				type: 'GET',
				success: function(response) {
						$.ajax({
							url: '/application/fill/fd',
							type: 'GET',
							success: function(response) {
								$('#viewfdTable').empty();
								$('#viewfdTable').html(response);
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
			})
			
		} else {
		    
			
		}
			
}
function addFDsToList() {
    var bankName = $("#fdBankDetailsDiv #bankName").val();
    var nameOfHolder = $("#fdBankDetailsDiv #nameOfHolder").val();
    var startDate = $("#fdBankDetailsDiv #startDate").val();
    var maturityDate = $("#fdBankDetailsDiv #maturityDate").val();
    var haveTakenLoanOnFD = $("#fdBankDetailsDiv #haveTakenLoanOnFD").val();
    var haveTakenLoanOnFD = $('#fdBankDetailsDiv input[name=haveTakenLoanOnFD]:radio:checked').val();
    //var monthlyAmntDedctedFrmSal = $("#fdBankDetailsDiv #monthlyAmntDedctedFrmSal").val();
    var monthlyAmntDedctedFrmSal = $('#fdBankDetailsDiv input[name=monthlyAmntDedctedFrmSal]:radio:checked').val();
    var pricipal = $("#fdBankDetailsDiv #pricipal").val();
    var interestRate = $("#fdBankDetailsDiv #interestRate").val();
    var maturityAmount = $("#fdBankDetailsDiv #maturityAmount").val();

    //var willSurrender = $("#mnthlyAmntDedFrmSalNoYes #willSurrender").val();
    var willSurrender = $('#willtoSurrenderTable input[name=willSurrender]:radio:checked').val();
    var allocation = $("#amountAllocationYes #allocation").val();
    var amountCanAvail = $("#amountAllocationYes #amountCanAvail").val();

	var financer = "Not Required";
	var loanAmount = $("#fdLoanDiv #loanAmount").val(); 
	var montlyEmi = $("#fdLoanDiv #montlyEmi").val();
	var installmentsPaid = $("#fdLoanDiv #installmentsPaid").val();
	var balanceInstallments = $("#fdLoanDiv #balanceInstallments").val();
	var balanceLoanAmount = $("#fdLoanDiv #balanceLoanAmount").val();

    var FdLoan = {
    	"financer": financer,
        "loanAmount": loanAmount,
        "montlyEmi": montlyEmi,
        "installmentsPaid": installmentsPaid,
        "balanceInstallments": balanceInstallments,
        "balanceLoanAmount": balanceLoanAmount
    };
    
    var FdDedFrmSal = {
    	"monthlyAmntDedctedFrmSal": monthlyAmntDedctedFrmSal
    }
    
    var FdWillSurrender = {
    	"willSurrender": willSurrender,
    }
    
    var FdUseAmnt = {
		"allocation": allocation,
	    "amountCanAvail": amountCanAvail
    }

	var fd = {
		"bankName": bankName,
	    "nameOfHolder": nameOfHolder,
	    "startDate": startDate,
	    "maturityDate": maturityDate,
	    "haveTakenLoanOnFD": haveTakenLoanOnFD,
	    "pricipal": pricipal,
	    "interestRate": interestRate,
	    "maturityAmount": maturityAmount,
	    "fdDedFrmSal": FdDedFrmSal,
	    "fdWillSurrender": FdWillSurrender,
	    "fdUseAmnt": FdUseAmnt,
	    "loanDetails": FdLoan
	};

	$.ajax({
		url: '/application/fill/fd',
		type: 'POST',
		data: JSON.stringify(fd),
		contentType: "application/json",
		beforeSend: function() {
			$("#fixedDepositeDetailsDiv input").attr("disabled", "disabled");
		},
		success: function() {
			//alert("Json is successfully posted.");
			$("#FDFormShow").hide();
			$("#buttonShowFormFD").show();
			$("#fixedDepositeDetailsDiv input[type=text]").val("");
			$("#fixedDepositeDetailsDiv input[type=radio]").attr('checked', false);
			
			//$("#buttonAddFD").val("Add More");
			$.ajax({
				url: '/application/fill/fd',
				type: 'GET',
				success: function(response) {
					$('#viewfdTable').empty();
					$('#viewfdTable').html(response);
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
			if ("FD" in response){
				$.each(response.FD.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#fixedDepositeDetailsDiv #"+paraId).show();
				    $("#fixedDepositeDetailsDiv #"+paraId).text(errorData.error);
				});
			}
			if ("FdDedFrmSal" in response){
    			$.each(response.FdDedFrmSal.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#fixedDepositeDetailsDiv #"+paraId).show();
				    $("#fixedDepositeDetailsDiv #"+paraId).text(errorData.error);
				});
			}
			if ("FdWillSurrender" in response){
    			$.each(response.FdWillSurrender.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#fixedDepositeDetailsDiv #"+paraId).show();
				    $("#fixedDepositeDetailsDiv #"+paraId).text(errorData.error);
				});
			}
			if ("FDLoan" in response){
    			$.each(response.FDLoan.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#fixedDepositeDetailsDiv #"+paraId).show();
				    $("#fixedDepositeDetailsDiv #"+paraId).text(errorData.error);
				});
			}
			if ("FdUseAmnt" in response){
    			$.each(response.FdUseAmnt.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#fixedDepositeDetailsDiv #"+paraId).show();
				    $("#fixedDepositeDetailsDiv #"+paraId).text(errorData.error);
				});
			}
			
		},
		complete: function() {
			$("#fixedDepositeDetailsDiv input").removeAttr("disabled");
			if ($("#fixedDepositeDetailsDiv input:radio[name=haveTakenLoanOnFD]:checked").val() == 'true'){
				$("#deductionFrmSalTr").show();
				$("#fdLoanDiv").hide();
		  		$("#willtoSurrenderTable").hide();
		  		$("#amountAllocationYes").hide();
		  		$("#amountAllocationNo").hide();
		  	}else {
		  		$("#deaddFDsToListductionFrmSalTr").hide();
		  		$("#fdLoanDiv").hide();
		  		$("#willtoSurrenderTable").hide();
		  		$("#amountAllocationYes").hide();
		  		$("#amountAllocationNo").hide();
		  	}
			if ($("#fixedDepositeDetailsDiv input:radio[name=monthlyAmntDedctedFrmSal]:checked").val() == 'true'){
		  		$("#fdLoanDiv").hide();
		  		$("#willtoSurrenderTable").show();
		  		$("#amountAllocationYes").hide();
		  		$("#amountAllocationNo").hide();
		  	}else if ($("#fixedDepositeDetailsDiv input:radio[name=monthlyAmntDedctedFrmSal]:checked").val() == 'false'){
		  		$("#fdLoanDiv").show();
		  		$("#willtoSurrenderTable").show();
		  		$("#amountAllocationYes").hide();
		  		$("#amountAllocationNo").hide();
		  	}
		  	
		  	if ($("#fixedDepositeDetailsDiv input:radio[name=willSurrender]:checked").val() == 'true'){
		  		$("#amountAllocationYes").show();
		  		$("#amountAllocationNo").hide();
		  	}else if($("#fixedDepositeDetailsDiv input:radio[name=willSurrender]:checked").val() == 'false'){
		  		$("#amountAllocationYes").hide();
		  		$("#amountAllocationNo").hide();
		  	}
			//$("#deductionFrmSalTr").hide();
	  		//$("#fdLoanDiv").hide();
	  		//$("#willtoSurrenderTable").hide();
	  		//$("#amountAllocationYes").hide();
	  		//$("#amountAllocationNo").hide();
		}
	});

}