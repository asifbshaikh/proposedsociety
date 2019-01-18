$(document).ready(function(){
	$("#PropDetailsFormShow").hide();
	$("#buttonShowFormPropDetails").show();
	$("#loanTaken").hide();
	$(".amountNotCuttingFromSal").hide();
	$(".propWillingToSellClass").hide();
	
	
	var haveIncomeVar =$(".IncomeFromPropertyDiv input[name='IncomeFrmExistingProperty']:checked").val();
	if (haveIncomeVar == 'false') {
		$(".propertyDetailsDiv").hide();
	}else{
		$(".propertyDetailsDiv").show();
	}
	
	var typeSelection = $('#propertyTypeDiv input[name="type"]:checked').val();
	if (typeSelection == 'AllResidential') {
		$("#resTR").show(); 
	    $("#commTR").hide();
	    $("#agriTR").hide();
	}else if (typeSelection == 'AllCommercial') {
		$("#resTR").hide(); 
	    $("#commTR").show();
	    $("#agriTR").hide();
	}else if (typeSelection == 'AllAgricultural') {
		$("#resTR").hide(); 
	    $("#commTR").hide();
	    $("#agriTR").show();
	}else{
		$("#resTR").hide(); 
	    $("#commTR").hide();
	    $("#agriTR").hide();
	}
	
	$("#buttonShowFormPropDetails").click(function(){
  		$("#PropDetailsFormShow").show();
  		$("#buttonShowFormPropDetails").hide();
  		$("#PropNoDataParaDiv").hide();
	});
	
	$("#buttonHideFormPropDetails").click(function(){
  		$("#PropDetailsFormShow").hide();
  		$("#buttonShowFormPropDetails").show();
  		$("#PropNoDataParaDiv").show();
	});
	
	$(".IncomeFromPropertyDiv input[name='IncomeFrmExistingProperty']").click(function(){
		var haveIncome = $(".IncomeFromPropertyDiv input[name='IncomeFrmExistingProperty']:checked").val();
		//alert(haveIncome);
		if (haveIncome == 'true') {
			$(".propertyDetailsDiv").show();
			//alert(haveIncome);
		}else{
			$(".propertyDetailsDiv").hide();
			//alert(haveIncome);
		}
	});
	
	$("#propertyTypeDiv input[name='type']").click(function(){
		var typeSel = $('#propertyTypeDiv input[name="type"]:checked').val();
		if (typeSel == 'AllResidential') {
			//alert(typeSel);
			$("#resTR").show(); 
		    $("#commTR").hide();
		    $("#agriTR").hide();
		}else if (typeSel == 'AllCommercial') {
			//alert(typeSel);
			$("#resTR").hide(); 
		    $("#commTR").show();
		    $("#agriTR").hide();
		}else if (typeSel == 'AllAgricultural') {
			//alert(typeSel);
			$("#resTR").hide(); 
		    $("#commTR").hide();
		    $("#agriTR").show();
		}
		//alert(typeSel);
	});
	
	$("#IncomeFromPropertyDiv input[name='loan']").click(function(){
		var haveLoan = $('#IncomeFromPropertyDiv input[name="loan"]:checked').val();
		if (haveLoan == 'true') {
			//alert(haveLoan);
			$("#loanTaken").show(); 
		}else if (haveLoan == 'false') {
			//alert(haveLoan);
			$("#loanTaken").hide();
		}
		//alert(haveLoan);
	});
	
	$("#IncomeFromPropertyDiv input[name='monthlyAmntDedctedFrmSal']").click(function(){
		var amntDedctedVar = $('#IncomeFromPropertyDiv input[name="monthlyAmntDedctedFrmSal"]:checked').val();
		if (amntDedctedVar == 'false') {
			//alert(amntDedctedVar);
			$(".amountNotCuttingFromSal").show(); 
		}else if (amntDedctedVar == 'true') {
			//alert(amntDedctedVar);
			$(".amountNotCuttingFromSal").hide();
		}
		//alert(amntDedctedVar);
	});
	
	$("#IncomeFromPropertyDiv input[name='willingToSell']").click(function(){
		var propWillingToSellVar = $('#IncomeFromPropertyDiv input[name="willingToSell"]:checked').val();
		if (propWillingToSellVar == 'true') {
			//alert(propWillingToSellVar);
			$(".propWillingToSellClass").show(); 
		}else if (propWillingToSellVar == 'false') {
			//alert(propWillingToSellVar);
			$(".propWillingToSellClass").hide();
		}
		//alert(propWillingToSellVar);
	});
	
});
function deletePropertyFromList(id){
	
		if (confirm('Are you sure you want to do this ?')) {
				
				$.ajax({
					url: '/application/property/delete/'+id,
					type: 'GET',
					success: function(response) {
								$
								$.ajax({
									url: '/application/fill/property_Details',
									type: 'GET',
									success: function(response) {
										$('#viewPropertyDetailsTable').empty();
										$('#viewPropertyDetailsTable').html(response);
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
function addProperyDetailsToList() {

	var IncomeFrmExistingProperty = $('#IncomeFromPropertyDiv input[name=IncomeFrmExistingProperty]:radio:checked').val();
	var type = $('#IncomeFromPropertyDiv input[name=type]:radio:checked').val();
	var typeDetail = $("#IncomeFromPropertyDiv #typeDetail").val();  
	var area = $("#IncomeFromPropertyDiv #area").val();
	
    var city = $("#IncomeFromPropertyDiv #location\\.city").val();
    //alert("city :"+city);
    var line1 = $("#IncomeFromPropertyDiv input[name='location.line1']").val();
    
	var taluka = $("#IncomeFromPropertyDiv input[name='location.taluka']").val();
    var district = $("#IncomeFromPropertyDiv input[name='location.district']").val();
    var state = $("#IncomeFromPropertyDiv input[name='location.state']").val();
    var country = $("#IncomeFromPropertyDiv input[name='location.country']").val();
    var pin = $("#IncomeFromPropertyDiv input[name='location.pin']").val();
	
	var loan = $('#IncomeFromPropertyDiv input[name=loan]:radio:checked').val();
    var monthlyAmntDedctedFrmSal = $('#IncomeFromPropertyDiv input[name=monthlyAmntDedctedFrmSal]:radio:checked').val();
    
	var financer = $("#IncomeFromPropertyDiv #financer").val();
    var loanAmount = $("#IncomeFromPropertyDiv #loanAmount").val();
    var montlyEmi = $("#IncomeFromPropertyDiv #montlyEmi").val();
    var installmentsPaid = $("#IncomeFromPropertyDiv #installmentsPaid").val();
    var balanceInstallments = $("#IncomeFromPropertyDiv #balanceInstallments").val();
    var balanceLoanAmount = $("#IncomeFromPropertyDiv #balanceLoanAmount").val();
	
	var willingToSell = $('#IncomeFromPropertyDiv input[name=willingToSell]:radio:checked').val();
    var expectedPrice = $("#IncomeFromPropertyDiv #expectedPrice").val();
	var allocation = $("#IncomeFromPropertyDiv #allocation").val();
	var propertyDesc =$("#propertyDesc").val();
	var location = {
		    "city": city,
		    "line1": line1,
		    "taluka": taluka,
		    "district": district,
		    "state": state,
		    "country": country,
		    "pin": pin
	}
	
    /*var haveIncome = {
	    "IncomeFrmExistingProperty": IncomeFrmExistingProperty
    }
    
    var propType = {
		"type": type
    }
    
	var propAdd = {
	    "city": location.city,
	    "taluka": location.taluka,
	    "district": location.district,
	    "state": location.state,
	    "country": location.country,
	    "pin": location.pin
	}
	
	var propDedFromSal = {
		"monthlyAmntDedctedFrmSal": monthlyAmntDedctedFrmSal
	}
	
	var propLoan = {
		"financer": financer,
		"loanAmount": loanAmount,
		"montlyEmi": montlyEmi,
		"installmentsPaid": installmentsPaid,
		"balanceInstallments": balanceInstallments,
		"balanceLoanAmount": balanceLoanAmount
	}
	
	var propSell = {
		"expectedPrice": expectedPrice,
		"allocation": allocation
	}
	
	var propertyObj = {
		"typeDetail": typeDetail,
		"area": area,
		"loan": loan,
		"willingToSell": willingToSell,
		"haveIncome": haveIncome,
		"propType": propType,
		"propAdd": propAdd,
		"propDedFromSal": propDedFromSal,
		"propLoan": propLoan,
		"propSell": propSell
	}*/

	var propertyObj = {
			"type": type,
			"monthlyAmntDedctedFrmSal": monthlyAmntDedctedFrmSal,
			"financer": financer,
			"loanAmount": loanAmount,
			"montlyEmi": montlyEmi,
			"installmentsPaid": installmentsPaid,
			"balanceInstallments": balanceInstallments,
			"balanceLoanAmount": balanceLoanAmount,
			"expectedPrice": expectedPrice,
			"allocation": allocation,
			"typeDetail": typeDetail,
			"area": area,
			"loan": loan,
			"willingToSell": willingToSell,
			"location": location,
			"propertyDesc": propertyDesc
		}
	
	$.ajax({
		url: '/application/fill/property_Details',
		type: 'POST',
		data: JSON.stringify(propertyObj),
		contentType: "application/json",
		beforeSend: function() {
			$("#IncomeFromPropertyDiv input").attr("disabled", "disabled");
		},
		success: function() {
			//alert("Json is successfully posted.");
			$("#PropDetailsFormShow").hide();
			$("#buttonShowFormPropDetails").show();
			$("#IncomeFromPropertyDiv input[type=text]").val("");
			$("#IncomeFromPropertyDiv input[type=radio]").attr('checked', false);
			
			$.ajax({
				url: '/application/fill/property_Details',
				type: 'GET',
				success: function(response) {
					$('#viewPropertyDetailsTable').empty();
					$('#viewPropertyDetailsTable').html(response);
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
			if ("propDetails" in response){
				$.each(response.propDetails.errors, function(errorsID,errorData) {
					var fieldName = errorData.fieldname;

					/*if(fieldName.search("location")){
						var length = 'location.';
						fieldName = fieldName.subStr(length);
						errorData.fieldname = fieldName;
					}*/
					
					fieldName = fieldName.replace(".", "\\.");
				    var paraId = fieldName+"Error";
				    //alert("paraId :"+paraId+" errorData.error :"+errorData.error);
				    $("#IncomeFromPropertyDiv #"+paraId).show();
				    $("#IncomeFromPropertyDiv #"+paraId).text(errorData.error);
				});
			}
			/*if ("haveIncome" in response){
				$.each(response.haveIncome.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#IncomeFromPropertyDiv #"+paraId).show();
				    $("#IncomeFromPropertyDiv #"+paraId).text(errorData.error);
				});
			}
			if ("propType" in response){
				$.each(response.propType.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#IncomeFromPropertyDiv #"+paraId).show();
				    $("#IncomeFromPropertyDiv #"+paraId).text(errorData.error);
				});
			}
			if ("propAdd" in response){
				$.each(response.propAdd.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#IncomeFromPropertyDiv #"+paraId).show();
				    $("#IncomeFromPropertyDiv #"+paraId).text(errorData.error);
				});
			}
			if ("propDedFromSal" in response){
				$.each(response.propAdd.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#IncomeFromPropertyDiv #"+paraId).show();
				    $("#IncomeFromPropertyDiv #"+paraId).text(errorData.error);
				});
			}
			if ("propLoan" in response){
				$.each(response.propAdd.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#IncomeFromPropertyDiv #"+paraId).show();
				    $("#IncomeFromPropertyDiv #"+paraId).text(errorData.error);
				});
			}
			if ("propSell" in response){
				$.each(response.propAdd.errors, function(errorsID,errorData) {
				    var paraId = errorData.fieldname+"Error";
				    $("#IncomeFromPropertyDiv #"+paraId).show();
				    $("#IncomeFromPropertyDiv #"+paraId).text(errorData.error);
				});
			}*/
		},
		complete: function() {
			$("#IncomeFromPropertyDiv input").removeAttr("disabled");
		}
	});

}