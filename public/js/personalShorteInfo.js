$(document).ready(function(){
	var locations = [] ; //Arrays of locations
	$("#requirementType").val($("#dummyRequirementSubType").val());

	if($('input[name = "registrationBy"]:radio:checked').val() != "Agent"){
		$('.hideAgentid').hide();
		$('.hideAgentid').val("");
	}

	$('#requiredLoan').change(function(){
		calculateBudget();
	});

	$('#totalBudget').change(function(){
		calculateBudget();
	});

	$('#otherTotalBudget').change(function(){
		calculateBudget();
	});

	$('#applicantIncomeShortForm').change(function(){
		calculateLoan();
	});

	$("input:radio[name='shortFormApplicantIncomePer']").click(function(){
		calculateLoan();
	});

	$("input:radio[name='shortFormCoApplicantIncomePer']").click(function(){
		calculateLoan();
	});

	$('#coApplicantIncomeShortForm').change(function(){
		calculateLoan();
	});

	function calculateBudget(){
		var totalBudget = 0 ;
		var personalContribution=0;
		var requiredLoanAmount =0;

		totalBudget =$('#totalBudget').val();
		requiredLoanAmount = $('#requiredLoan').val();

		var other = $('#totalBudget').val();
		if(other === '0'){
			$('#otherTotalBudget').show();
			totalBudget = $('#otherTotalBudget').val();
			personalContribution =  totalBudget - requiredLoanAmount;
		}else{
			personalContribution = totalBudget - requiredLoanAmount;
			$('#otherTotalBudget').hide();
			$('#otherTotalBudget').val('');
		}
		//Fixing negative personalContribution amount
		if(parseInt(personalContribution,10) > 0){
			$('#personalContribution').text(personalContribution);
		}else{
			$('#personalContribution').text('0');
		}
	}

	function calculateLoan(){
		var applicantIncome = 0 ;
		var approxLoanEligible = 0;
		var coApplicantIncome = 0;
		var approxLoan = 0;
		var coApproxLoan = 0;

		applicantIncome = $('#applicantIncomeShortForm').val();
		coApplicantIncome = $('#coApplicantIncomeShortForm').val();

		if(isNaN(applicantIncome)){
			$('#applicantError').show();
			$('#approxLoanAmount').text('0');
		}else{
			$('#applicantError').hide();
		}

		if(isNaN(coApplicantIncome)){
			$('#coapplicantError').show();
			$('#approxLoanAmount').text('0');
		}else{
			$('#coapplicantError').hide();
		}

		if(!isNaN(applicantIncome) && !isNaN(coApplicantIncome)){

			if($('input[name = "shortFormApplicantIncomePer"]:radio:checked').val() == "Monthly"){
				approxLoan = (applicantIncome * 50);
			}else{
				approxLoan = ((applicantIncome/12) * 50);
			}

			if($('input[name = "shortFormCoApplicantIncomePer"]:radio:checked').val() == "Monthly"){
				coApproxLoan = (coApplicantIncome * 50);
			}else{
				coApproxLoan = ((coApplicantIncome/12) * 50);
			}

			approxLoanEligible = Math.round(approxLoan + coApproxLoan);
			$('#approxLoanAmount').text(approxLoanEligible);
		}
	}

	$("form").submit(function(){
		$('#otherTotalBudget').show();
		var totalBudget = $('#otherTotalBudget').val();
		var other = $('#totalBudget').val();
		if(other === '0' && totalBudget === ''){
			alert("Enter Total budget");
		}
	});

	$('[name = "registrationBy"]').change(function(){
		$('.hideAgentid').hide();
		$('.hideAgentid').val("");
		if($('input[name = "registrationBy"]:radio:checked').val() == "Agent"){
			$('.hideAgentid').show();
		}
	});

	$("#dummyRequirementSubType").change(function(){

		var requirementType = $("#dummyRequirementSubType").val();
		$("#requirementType").val(requirementType);
	});


	hiddenLocationVal = $("#locationsHiddenField").val();
	if(hiddenLocationVal && hiddenLocationVal != ""){
		jsonObj = jQuery.parseJSON(hiddenLocationVal);
		jsonObj.forEach(function(item) {
		    createRowForRecentLocation(item);
		});
	}

	showAddLocationDiv();

	$("#showLocTable").click(function() {
		showAddLocationDiv();
		$("#cancelAddedLocation").show();
	});

	$("#cancelAddedLocation").click(function() {
		$("#requirementLocationTable").hide();
		$("#saveLocations").hide();
		$("#cancelAddedLocation").hide();
		$("#showLocTable").show();
	});

	$("#saveLocations").click(function() {
		if($("#Pincode").val() == "Other"){
			  var pincode  = $("#otherPincode").val();
			  var locality = $("#otherLocality").val();

			  if(pincode === ""){
				  alert("Enter Pincode");
			  }else{

				  if(!pincode.match(/^\d{6}$/)){
					  alert("Enter Valid Pincode");
				  }else{
					  addValuesToTable("#State","#District","#Taluka","#otherPincode","#otherLocality","#Street");
				  }
			  }

		}else{
			  if($("#Pincode").val()=== "whatever"){
				  $("#showLocationError").show();
			  }else{
				  var pinAndLocality =  $("#Pincode").val().split(",");
				  $("#otherPincode").val(pinAndLocality[0]);
				  $("#otherLocality").val(pinAndLocality[1]);
				  addValuesToTable("#State","#District","#Taluka","#otherPincode","#otherLocality","#Street");
				  $("#showLocationError").hide();
			  }
	    }

		emptyField("#State","#District","#Taluka","#Pincode","#Street");
	    $("#requirementLocationTable").hide();
		$("#saveLocations").hide();
		$("#cancelAddedLocation").hide();
		$("#showLocTable").show();
	});

	function showAddLocationDiv(){
		$("#requirementLocationTable").show();
		$("#saveLocations").show();
		$("#showLocTable").hide();
		emptyField("#State","#District","#Taluka","#Pincode","#otherPincode","#otherLocality","#Street");
		$("#otherPincode").css("visibility", "hidden");
		$("#otherLocality").css("visibility", "hidden");
		disable("#District");disable("#Taluka");disable("#Pincode");
	}
	function addValuesToTable(state,district,taluka,pincode,locality,street){

		var state = $(state).val();
		var district = $(district).val();
		var taluka = $(taluka).val();
		var pincode = $(pincode).val();
		var locality = $(locality).val();
		var street = $(street).val();
		var locationObj = {
	    	"pincode": pincode,"taluka": taluka,"district": district,"state": state,"locality":locality,"street":street
	    };
	    $("#Pincode").append($('<option>', { value: $("#Pincode").val(), text: $("#Pincode").val() }));
	    createRowForRecentLocation(locationObj);
	}
	function disable(elementId){
		$(elementId).attr('disabled', true);
	}
	function emptyField(state,district,taluka,pincode,otherPincode,otherLocality,street){
		$(state).val("");
	    $(taluka).val("");
	    $(district).val("");
	    $(pincode).val("");
	    $(otherPincode).val("");
	    $(otherLocality).val("");
		$(street).val("");
	}
	function hiddenLocations(){
		$("#locationsHiddenField").val().concat("|").concat(JSON.stringify(location));
	}
	function createRowForRecentLocation(locationObj) {
		$("#dynamicLocationTable").show();
		$("#dynamicLocationTable tbody").append("<tr><td>"+locationObj.state+"</td><td>"+locationObj.district+"</td><td>"+locationObj.taluka+"</td><td>"+locationObj.pincode+"</td><<td>"+locationObj.locality+"</td><td>"+locationObj.street+"</td><td>"+"</td><td><input type='button' class='btn btn-mini btn-danger' value='X' style='font-weight: bold;'></td></tr>");

	    $("#dynamicLocationTable  input[type='button']").off("click").on("click",function(){
	    	deleteLocationRow($(this));
	    });

	}

	function deleteLocationRow(currentElem){
		currentElem.closest('tr').remove();
	}

	function getAllValuesOfAllLocations(){
		jsonArray = [];
		$("#dynamicLocationTable tbody tr").each(function() {
			var state = $(this).find("td:first").text();
			var district = $(this).find("td").eq(1).text();
			var taluka = $(this).find("td").eq(2).text();
			var pincode = $(this).find("td").eq(3).text();
			var locality = $(this).find("td").eq(4).text();
			var street = $(this).find("td").eq(5).text();
			var locationJson = {
					"pincode": pincode,"taluka": taluka,"district": district,"state": state,"locality": locality,"street": street  //"country": country,"villageOrDetailLocation": villageOrDetailLocation
		    };
			jsonArray.push(locationJson);
		});

		$("#locationsHiddenField").val(JSON.stringify(jsonArray));
	}
	$("#shortForm").submit(function( event ) {
		  var locations = $("#locationsHiddenField").val(JSON.stringify(jsonArray)).val();
		  if(locations === "[]"){
			  $("#showLocationError").show();
			  $(window).scrollTop($('#showLocationError').offset().top);
			  event.preventDefault();
		  }
	});

	$("#submitShortForm").click(function(){
		getAllValuesOfAllLocations();
	});

	function showOrHideLocationValidationError(loc_value,trIdToShowOrHide){
		if(loc_value == ""){
			trIdToShowOrHide.show();
		}else{
			trIdToShowOrHide.hide();
		}
	}

});
