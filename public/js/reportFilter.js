function generateReport() {
	console.log('Generating report.');
	hideMessage();
	var state = $("#State option:selected").val()
	if (state === "whatever") {
		console.error("State not selected");
		showMessage("alert-error", "Please select required input.");
		return;
	}

	var dist = $("#District option:selected").val()
	if (dist === "whatever") {
		showMessage("alert-error", "Please select required input.");
		console.error("District not selected");
		return;
	}
	var taluka = $("#Taluka option:selected").val()
	if (taluka === "whatever") {
		console.error("Taluka not selected");
		showMessage("alert-error", "Please select required input.");
		return;
	}
	var pincode = $("#Pincode option:selected").val();
	if (pincode === "whatever") {
		console.error("Pincode not selected");
		showMessage("alert-error", "Please select required input.");
		return;
	}
	if (pincode === "Other") {
		otherLocality = $('#otherLocality').val();
		otherPincode = $('#otherPincode').val();
		if (otherLocality === "" || otherPincode.length != 6) {
			console.error("Pincode not selected");
			showMessage("alert-error", "Please select required input.");
			return 

		}
		pincode = otherPincode + ", " + otherLocality;

	}
	var postObj = {
		"state" : state,
		"district" : dist,
		"taluka" : taluka,
		"pincodeAndLocality" : pincode,
		"budget": $("#budget option:selected").val(),
		"type":  $("#type option:selected").val()
	};
	console.log('Posting data: ' + JSON.stringify(postObj));

	$.ajax({
		url : '/admin/report/generate',
		type : 'POST',
		data : JSON.stringify(postObj),
		contentType : 'application/json',
		beforeSend : function() {
			showMessage("alert-info", "Generating report...");
		},
		success : function(response) {
			showMessage("alert-success", "Report genrated successfully.");
			console.log(response);
			window.location = "/d/"+ response;
		},
		error : function() {
			showMessage("alert-error", "Not able generate report.");
		},
		complete : function() {
			
		}
	});
}

function hideMessage() {
	$(".alert").hide();
	$("#message").text("");
}

function showMessage(alertClass, message) {
	$(".alert").show();
	$(".alert").addClass(alertClass);
	$("#message").text(message);
}