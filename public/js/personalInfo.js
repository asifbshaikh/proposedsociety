$(document).ready(function(){
	// Fixing focus Issue
	var second = $('p[haserror]');
	$(second.get(0)).prev().focus();
	
	$('input:radio[name="applicant.resi_status"]').filter('[value="I"]').attr('checked', true);
	$('input:radio[name="co_applicant.resi_status"]').filter('[value="I"]').attr('checked', true);
	
	var marStat = $("input[name='applicant.marital_status']:checked").val();
	if (marStat == null){
		$("#applicant_spouse-details").hide();
	}
	if (marStat == 'M') {
		$("#applicant_spouse-details").show();			
	}else if (marStat == 'U') {
		$("#applicant_spouse-details").hide();
	}
	
	var perAddOpt = $("input[name='applicant.permanent_address_same_as']:checked").val();
	if (perAddOpt == null){
		$("input:radio[name='applicant.permanent_address_same_as']:nth(1)").attr('checked',true);
		$("#applicant_perAddFieldSet").show();
	}
	else if (perAddOpt == 'other') {
		$("#applicant_perAddFieldSet").show();			
	}else{
		$("#applicant_perAddFieldSet").hide();
	}

	var resAddOpt = $("input[name='applicant.residential_address_same_as']:checked").val();
	if (resAddOpt == null){
		$("input:radio[name='applicant.residential_address_same_as']:nth(2)").attr('checked',true);
		$("#applicant_resAddFieldSet").show();
	}
	else if (resAddOpt == 'other') {
		$("#applicant_resAddFieldSet").show();			
	}else{
		$("#applicant_resAddFieldSet").hide();
	}

	var comAddOpt = $("input[name='applicant.communication_address_same_as']:checked").val();
	if (comAddOpt == null){
		$("input:radio[name='applicant.communication_address_same_as']:nth(3)").attr('checked',true);
		$("#applicant_comAddFieldSet").show();
	}
	else if (comAddOpt == 'other') {
		$("#applicant_comAddFieldSet").show();			
	}else{
		$("#applicant_comAddFieldSet").hide();
	}

	$("input[name='applicant.marital_status']").click(function(){
	    if ($("input[name='applicant.marital_status']:checked").val() == 'M') {
	       	$("#applicant_spouse-details").show();
	    }
	    if ($("input[name='applicant.marital_status']:checked").val() == 'U') {
	       	$("#applicant_spouse-details").hide();
	       	$("#applicant_spouse-details input[type=text]").val("");
	    }
	});

	$("input[name='applicant.permanent_address_same_as']").click(function(){
	    if ($("input[name='applicant.permanent_address_same_as']:checked").val() == 'other') {
	       	$("#applicant_perAddFieldSet").show();
	    }
	    else{
	       	$("#applicant_perAddFieldSet").hide();
	       	$("#applicant_perAddFieldSet input[type=text]").val("");
	    }
	});

	$("input[name='applicant.residential_address_same_as']").click(function(){
	    if ($("input[name='applicant.residential_address_same_as']:checked").val() == 'other') {
	       	$("#applicant_resAddFieldSet").show();
	    }
	    else{
	       	$("#applicant_resAddFieldSet").hide();
	       	$("#applicant_resAddFieldSet input[type=text]").val("");
	    }
	});

	$("input[name='applicant.communication_address_same_as']").click(function(){
	    if ($("input[name='applicant.communication_address_same_as']:checked").val() == 'other') {
	       	$("#applicant_comAddFieldSet").show();
	    }
	    else{
	       	$("#applicant_comAddFieldSet").hide();
	       	$("#applicant_comAddFieldSet input[type=text]").val("");
	    }
	});
	
	$(".capitalize").change(function(){
		$(this).val($(this).val().toUpperCase());
	});

	initializeFileUpload("[ps-file-upload]");

});

function initializeFileUpload(selector)
{
	$(selector).each(
		function ()
		{
			var control = $(this);
			var attachmentId = control.attr("value");
			var attachmentName = control.attr("attachment-name");
			var targetElement = control.attr("target-element");

			if (targetElement) 
			{
				var options = {
					inputClass: "input-medium",
					buttonClass: "btn btn-small btn-pink",
					onUpload: function(data) {
						control.val(data);
					},
					onRemove: function() {
						control.val('');
					}
				}

				if (attachmentId) 
				{
					options.initialValue = attachmentId;
					options.initialName = attachmentName;
				}

				$("#" + targetElement).fileUpload(options);
			}
		}
	);	
}
