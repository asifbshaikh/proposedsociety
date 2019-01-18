$(document).ready(function(){
	$("#InsuranceFormShow").hide();
	$("#buttonShowFormInsurance").show();
	
  	if ($("#insuranceDetailsDiv input:radio[name=receivingAnyMaturedAmount]:checked").val() == 'true'){
		$("#insuAmountUsageDiv").show();
	}else {
		$("#insuAmountUsageDiv").hide();
	}	
	
  	$("#buttonShowFormInsurance").click(function(){
  		$("#InsuranceFormShow").show();
  		$("#buttonShowFormInsurance").hide();
	});
  	
  	$("#buttonHideFormInsurance").click(function(){
  		$("#InsuranceFormShow").hide();
  		$("#buttonShowFormInsurance").show();
	});
  	
  	$("#insuranceDetailsDiv input:radio[name='receivingAnyMaturedAmount']").click(function(){
		if ($("#insuranceDetailsDiv input:radio[name=receivingAnyMaturedAmount]:checked").val() == 'true'){
			$("#insuAmountUsageDiv").show();
		}else {
			$("#insuAmountUsageDiv").hide();
			$("#insuAmountUsageDiv input[type=text]").val("");
		}
	});
  
});

function deleteInsuDetailsFromList(id ){
	
		if (confirm('Are you sure you want to do this ?')) {
			
			$.ajax({
				url: '/application/insurance/delete/'+id,
				type: 'GET',
				success: function(response) {
					$.ajax({
						url: '/application/fill/insurance',
						type: 'GET',
						success: function(response) {
							$('#viewInsuranceDetailsTable').empty();
							$('#viewInsuranceDetailsTable').html(response);
							$('.error').text("");
							$('.error').attr("style", "display:none");
						},
						error: function() {
							alert("Not Found");
						}
					});
				},
				error: function() {
					alert('Wrong User');
				}
			})
			
		}
	}
function addInsuDetailsToList() {
    var nameOfHolder = $("#insuranceDetailsDiv  #nameOfHolder").val();
    var insurerName = $("#insuranceDetailsDiv #insurerName").val();
    var insuStartDate = $("#insuranceDetailsDiv #insuStartDate").val();
    var insuMaturityDate = $("#insuranceDetailsDiv #insuMaturityDate").val();
    var sumAssured = $("#insuranceDetailsDiv #sumAssured").val();
    var premiumFreqency = $("#insuranceDetailsDiv #premiumFreqency").val();
    var premium = $("#insuranceDetailsDiv #premium").val();
    var premiumsPaid = $("#insuranceDetailsDiv #premiumsPaid").val();
    var premiumsBalance = $("#insuranceDetailsDiv #premiumsBalance").val();
    var insuLastPaymentDate = $("#insuranceDetailsDiv #insuLastPaymentDate").val();
    var maturityAmount = $("#insuranceDetailsDiv #maturityAmount").val();
    var monthlyAmntDedctedFrmSal = $('#insuranceDetailsDiv input[name=monthlyAmntDedctedFrmSal]:radio:checked').val();
    var receivingAnyMaturedAmount = $('#insuranceDetailsDiv input[name=receivingAnyMaturedAmount]:radio:checked').val();
    var allocation = $("#insuranceDetailsDiv #allocation").val();

    var insuAmountUsage = {
    	"allocation": allocation
    }
    
	var insdetails = {
		"nameOfHolder": nameOfHolder,
		"insurerName": insurerName,
	    "insuStartDate": insuStartDate,
	    "insuMaturityDate": insuMaturityDate,
	    "sumAssured": sumAssured,
	    "premiumFreqency": premiumFreqency,
	    "premium": premium,
	    "premiumsPaid": premiumsPaid,
	    "premiumsBalance": premiumsBalance,
	    "insuLastPaymentDate": insuLastPaymentDate,
	    "maturityAmount": maturityAmount,
	    "monthlyAmntDedctedFrmSal": monthlyAmntDedctedFrmSal,
	    "receivingAnyMaturedAmount": receivingAnyMaturedAmount,
	    "insuAmountUsage": insuAmountUsage
	};

	$.ajax({
		url: '/application/fill/insurance',
		type: 'POST',
		data: JSON.stringify(insdetails),
		contentType: "application/json",
		beforeSend: function() {
			$("#insuranceDetailsDiv input").attr("disabled", "disabled");
		},
		success: function() {
			
			$("#InsuranceFormShow").hide();
			$("#buttonShowFormInsurance").show();
			$("#insuranceDetailsDiv input[type=text]").val("");
			$("#insuranceDetailsDiv input[type=radio]").attr('checked', false);
			//$("#buttonAddInsurance").val("Add More");
			$.ajax({
				url: '/application/fill/insurance',
				type: 'GET',
				success: function(response) {
					$('#viewInsuranceDetailsTable').empty();
					$('#viewInsuranceDetailsTable').html(response);
					$('.error').text("");
					$('.error').attr("style", "display:none");
				},
				error: function() {
					alert("Something went wrong while GET!");
				}
			});
		},
		error: function(response) {
			var response = jQuery.parseJSON(response.responseText);
			$('.error').text("");
			$('.error').attr("style", "display:none");
			if ("Insurance" in response){
    			$.each(response.Insurance.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#insuranceDetailsDiv #"+paraId).show();
				    $("#insuranceDetailsDiv #"+paraId).text(errorData.error);
				});
			}
			if ("InsuranceAllocation" in response){
    			$.each(response.InsuranceAllocation.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#insuranceDetailsDiv #"+paraId).show();
				    $("#insuranceDetailsDiv #"+paraId).text(errorData.error);
				});
			}
		},
		complete: function() {
			$("#insuranceDetailsDiv input").removeAttr("disabled");
			if ($("#insuranceDetailsDiv input:radio[name=receivingAnyMaturedAmount]:checked").val() == 'true'){
				$("#insuAmountUsageDiv").show();
			}else {
				$("#insuAmountUsageDiv").hide();
			}
			//$("#insuAmountUsageDiv").hide();
		}
	});

}
