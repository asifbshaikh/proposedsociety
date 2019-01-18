$(document).ready(function(){
	$("#MiscBorrowingFormShow").hide();
	$("#buttonShowFormMiscBorrowing").show();

	var isAmount = $("#miscellaneousBorrowingDiv input[name='isAmountFromFriends']:checked").val();
	if (isAmount == null) {
		$("#moneyFromFriends").hide();
		$("#amountNatureTable").hide();
	}
	if (isAmount == 'true') {
		$("#moneyFromFriends").show();	
		$("#amountNatureTable").show();			
	}
	if (isAmount == 'false') {
		$("#moneyFromFriends").hide();
		$("#amountNatureTable").hide();			
	}
	
	var amountNatureVar = $("#miscellaneousBorrowingDiv input[name='amountNature']:checked").val();
	
	if (amountNatureVar == "asLoan"){
		$(".asLoanClass").show();
	}else{
		$(".asLoanClass").hide();
	}

	if (amountNatureVar == "asDonation"){
		$(".asDonationClass").show();			
	}else{
		$(".asDonationClass").hide();
	}

	if (amountNatureVar == "moneyOwed"){
		$(".asOwedMoneyClass").show();			
	}else{
		$(".asOwedMoneyClass").hide();
	}

	$("#buttonShowFormMiscBorrowing").click(function(){
  		$("#MiscBorrowingFormShow").show();
  		$("#buttonShowFormMiscBorrowing").hide();
  		$("#miscNoDataParaDiv").hide();
	});
	
	$("#buttonHideFormMiscBorrowing").click(function(){
  		$("#MiscBorrowingFormShow").hide();
  		$("#buttonShowFormMiscBorrowing").show();
  		$("#miscNoDataParaDiv").show();
	});
	
	$("#miscellaneousBorrowingDiv input[name='isAmountFromFriends']").click(function(){
		if ($("#miscellaneousBorrowingDiv input[name='isAmountFromFriends']:checked").val() == 'true') {
	       	$("#moneyFromFriends").show();
	       	$("#amountNatureTable").show();	
	    }
	    else{
	       	$("#moneyFromFriends").hide();
	       	$("#amountNatureTable").hide();
	    }
	});
	
	$("#miscellaneousBorrowingDiv input[name='amountNature']").change(function(){
		var amountNatureVar = $("#miscellaneousBorrowingDiv input[name='amountNature']:checked").val();
		
		if (amountNatureVar == "asLoan"){
			$(".asDonationClass input[type=text]").val("");
			$(".asOwedMoneyClass input[type=text]").val("");
			$(".asLoanClass").show();
		}else{
			$(".asLoanClass").hide();
		}

		if (amountNatureVar == "asDonation"){
			$(".asDonationClass").show();
			$(".asLoanClass input[type=text]").val("");
			$(".asOwedMoneyClass input[type=text]").val("");			
		}else{
			$(".asDonationClass").hide();
		}

		if (amountNatureVar == "moneyOwed"){
			$(".asOwedMoneyClass").show();
			$(".asDonationClass input[type=text]").val("");
			$(".asLoanClass input[type=text]").val("");			
		}else{
			$(".asOwedMoneyClass").hide();
		}
    });

});

function deleteAmntFrmFrndsFromList(id){
	
	if (confirm('Are you sure you want to do this ?')) {
			
			$.ajax({
				url: '/application/miscellaneous_borrowings/delete/'+id,
				type: 'GET',
				success: function(response) {
						$.ajax({
							url: '/application/fill/miscBorrowing',
							type: 'GET',
							success: function(response) {
								$('#viewMiscBorrowingDetailsTable').empty();
								$('#viewMiscBorrowingDetailsTable').html(response);
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
			
		}
		
}
function addAmntFrmFrndsToList() {
	
	var isAmountFromFriends = $('#miscellaneousBorrowingDiv input[name=isAmountFromFriends]:radio:checked').val();
	//alert("isAmountFromFriends: "+isAmountFromFriends);
    var amountNature = $('#miscellaneousBorrowingDiv input[name=amountNature]:radio:checked').val();
    //alert("amountNature: "+amountNature);
    var amount = $("#miscellaneousBorrowingDiv #amount").val();
    var interestRate = $("#miscellaneousBorrowingDiv #interestRate").val();
    var emi = $("#miscellaneousBorrowingDiv #emi").val();
    var numberOfYears = $("#miscellaneousBorrowingDiv #numberOfYears").val();
    var totalDeduction = $("#miscellaneousBorrowingDiv #totalDeduction").val();
    var donation = $("#miscellaneousBorrowingDiv #donation").val();
    var repayment = $("#miscellaneousBorrowingDiv #repayment").val();

    var	AmntNtr = {
    	"amountNature": amountNature
    };
	var loanFields = {
   		"amount": amount,
	    "interestRate": interestRate,
	    "emi": emi,
	    "numberOfYears": numberOfYears,
	    "totalDeduction": totalDeduction
	};
	var donationFields = {
   		"donation": donation
   	};
	var moneyOwedFields = {
   		"repayment": repayment
   	};
	   	
	var affdetails = {
		"isAmountFromFriends": isAmountFromFriends,
		"AmntNtr": AmntNtr,
		"loanFields": loanFields,
		"donationFields": donationFields,
		"moneyOwedFields": moneyOwedFields
	};
		
	$.ajax({
		url: '/application/fill/miscBorrowing',
		type: 'POST',
		data: JSON.stringify(affdetails),
		contentType: "application/json",
		beforeSend: function() {
			$("#miscellaneousBorrowingDiv input").attr("disabled", "disabled");
			//alert(affdetails);
		},
		success: function() {
			//alert("Json is successfully posted.");
			$("#MiscBorrowingFormShow").hide();
			$("#buttonShowFormMiscBorrowing").show();
			$("#miscellaneousBorrowingDiv input[type=text]").val("");
			$("#miscellaneousBorrowingDiv input[type=radio]").attr('checked', false);
			$("#moneyFromFriends").hide();
			$("#amountNatureTable").hide();
			
			$.ajax({
				url: '/application/fill/miscBorrowing',
				type: 'GET',
				success: function(response) {
					$('#viewMiscBorrowingDetailsTable').empty();
					$('#viewMiscBorrowingDetailsTable').html(response);
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
			if ("IsAmountFromFriends" in response){
    			$.each(response.IsAmountFromFriends.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#miscellaneousBorrowingDiv #"+paraId).show();
				    $("#miscellaneousBorrowingDiv #"+paraId).text(errorData.error);
				});
			}
			if ("AmountNature" in response){
    			$.each(response.AmountNature.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#miscellaneousBorrowingDiv #"+paraId).show();
				    $("#miscellaneousBorrowingDiv #"+paraId).text(errorData.error);
				});
			}
			if ("loanFields" in response){
				//alert("Error In loanFields");
    			$.each(response.loanFields.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#miscellaneousBorrowingDiv #"+paraId).show();
				    $("#miscellaneousBorrowingDiv #"+paraId).text(errorData.error);
				});
			}
			if ("donationFields" in response){
    			$.each(response.donationFields.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#miscellaneousBorrowingDiv #"+paraId).show();
				    $("#miscellaneousBorrowingDiv #"+paraId).text(errorData.error);
				});
			}
			if ("moneyOwedFields" in response){
    			$.each(response.moneyOwedFields.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#miscellaneousBorrowingDiv #"+paraId).show();
				    $("#miscellaneousBorrowingDiv #"+paraId).text(errorData.error);
				});
			}
		},
		complete: function() {
			$("#miscellaneousBorrowingDiv input").removeAttr("disabled");
		}
	});

}