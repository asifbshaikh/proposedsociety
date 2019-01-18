$(document).ready(function(){
	$("#btnsbmit").hide();
	 if ($("input[name='office_address_same_as']:checked").val() == 'notHave'){
			$(":radio[value=office_address]").attr('disabled', true);
			$("#office_add_proof").hide();
	}
	$('#idUpload').hide();
	// Fixing focus Issue
	var second = $('p[haserror]');
	$(second.get(0)).prev().focus();
	
	//Setting Language Value If Has One
	if($("#marathi_canRead").val() == "true") $("#mread").attr("checked",true);
	if($("#marathi_canWrite").val() == "true") $("#mwrite").attr("checked",true);
	if($("#marathi_canSpeak").val() == "true") $("#mspeak").attr("checked",true);
	if($("#hindi_canRead").val() == "true") $("#hread").attr("checked",true);
	if($("#hindi_canWrite").val() == "true") $("#hwrite").attr("checked",true);
	if($("#hindi_canSpeak").val() == "true") $("#hspeak").attr("checked",true);
	if($("#english_canRead").val() == "true") $("#eread").attr("checked",true);
	if($("#english_canWrite").val() == "true") $("#ewrite").attr("checked",true);
	if($("#english_canSpeak").val() == "true") $("#espeak").attr("checked",true);
	
	
	//Form Day And Place
	$("#formPlace").val($("#formFilledPlace").val());
	$("#formDay").val($("#formFilledDay").val());
	$("#formPlace").change(function(){
		$("#formFilledPlace").val($("#formPlace").val());
	});
	$("#formDay").change(function(){
		$("#formFilledDay").val($("#formDay").val());
	});
	$("#formYear").change(function(){
		$("#formFilledYear").val($("#formYear").val());
	});
	
	
	var nationalityOpt = $("input[name='nationality']:checked").val();
	if(nationalityOpt == "N"){
		$("#nriAddField").show();
	}
	$('[name = "nationality"]').click(function(){
		
		var nationalityOpt = $("input[name='nationality']:checked").val();
		if(nationalityOpt == "N"){
			$("#nriAddField").show();
		}
		if(nationalityOpt == "I"){
			$("#nriAddField").hide();
		}
	});
	
	var perAddOpt = $("input[name='permanent_address_same_as']:checked").val(); 
	
	if (perAddOpt == null){
		$("#agentAddRadio input[value= 'other']").attr("checked",true);;
		$("#perAddFieldSet").show();
	}
	else if (perAddOpt == 'other') {
		$("#perAddFieldSet").show();			
	}else{
		$("#perAddFieldSet").hide();
	}

	var resAddOpt = $("input[name='residential_address_same_as']:checked").val();
	if (resAddOpt == null){
		$("#agentResiAdd input[value= 'other']").attr("checked",true);
		$("#resAddFieldSet").show();
	}
	else if (resAddOpt == 'other') {
		$("#resAddFieldSet").show();			
	}else{
		$("#resAddFieldSet").hide();
	}

	var comAddOpt = $("input[name='communication_address_same_as']:checked").val();
	if (comAddOpt == null){
		$("#commAddRadio input[value= 'other']").attr("checked",true);
		$("#comAddFieldSet").show();
	}
	else if (comAddOpt == 'other') {
		$("#comAddFieldSet").show();			
	}else{
		$("#comAddFieldSet").hide();
	}

	

	$("input[name='permanent_address_same_as']").click(function(){
	    if ($("input[name='permanent_address_same_as']:checked").val() == 'other') {
	       	$("#perAddFieldSet").show();
	      	$("#perAddFieldSet input[type=text]").val("");
	    }
	    else{
	       	$("#perAddFieldSet").hide();
	       	$("#perAddFieldSet input[type=text]").val("");
	    }
	});

	$("input[name='residential_address_same_as']").click(function(){
	    if ($("input[name='residential_address_same_as']:checked").val() == 'other') {
	       	$("#resAddFieldSet").show();
	       	$("#resAddFieldSet input[type=text]").val("");
	    }
	    else{
	       	$("#resAddFieldSet").hide();
	     	$("#resAddFieldSet input[type=text]").val("");
	    }
	});

	$("input[name='communication_address_same_as']").click(function(){
	    if ($("input[name='communication_address_same_as']:checked").val() == 'other') {
	       	$("#comAddFieldSet").show();
	       	$("#comAddFieldSet input[type=text]").val("");
	    }
	    else{
	       	$("#comAddFieldSet").hide();
	       	$("#comAddFieldSet input[type=text]").val("");
	    }
	});
	
	
	var officeAddOpt = $("input[name='office_address_same_as']:checked").val(); 
	if (officeAddOpt == null){
		$("#officeRadio input[value= 'other']").attr("checked",true);
		$("#office_details").show();
		$("#optnfor_add").show();
	}
	else if (officeAddOpt == 'other') {
		$("#office_details").show();
		$("#optnfor_add").show();		
	}else{
		$("#office_details").hide();
		$("#optnfor_add").hide();	
	}
	
	$("input[name='office_address_same_as']").click(function(){
		$("#office_add_proof").show();
		$(":radio[value=office_address]").attr('disabled', false);
	    if ($("input[name='office_address_same_as']:checked").val() == 'other') {
	    	$("#office_details").show();
			$("#optnfor_add").show();	
			$("#office_details input[type=text]").val("");
	     	$("#optnfor_add input[type=text]").val("");
	    }
	    else{
	    	$("#office_details").hide();
			$("#optnfor_add").hide();	
	      
	    }
	    if ($("input[name='office_address_same_as']:checked").val() == 'notHave'){
			$(":radio[value=office_address]").attr('disabled', true);
			$("#office_add_proof").hide();
		}
	});
	
	
	//agent
	
	$("#qualification").change(function(){
		
			$("#otherQualificationDiv").hide();
			var others = $("#qualification").val();
			
			if(others == "other"){$("#otherQualificationDiv").show();}
		}
	);
	$('[name="avc"]').click(function(){
		
		var selected =$('input[name="avc"]:radio:checked').val();
		if(selected == "no"){
			$("#intro_box").show();
		}
		if(selected == "yes"){
			$("#intro_box").hide();
		}
	});
	
	$('[name="accept"]').click(function(){
		var selected =$('input[name="accept"]:radio:checked').val();
		if(selected == "accept"){
			$("#btnsbmit").show();
			$('#idUpload').show();
		}
		if(selected == "ntaccept"){
			$("#btnsbmit").hide();
			$('#idUpload').hide();
		}
	});
	
	var name = $("#fname").val();
	$("#agentName").text(name);
	
	$("#fname").change(function(){
		var name = $("#fname").val();
		$("#agentName").text(name);
	});
	$("#agentTitle").text($("#title").val() + ".");
	$("#title").change(function(){
		var title = $("#title").val();
		$("#agentTitle").text(title + ".");
	});
	
	//Handling Laguage Issue
	
	$("#marathi_name").val("Marathi");
	$("#hindi_name").val("Hindi");
	$("#english_name").val("English");
	
	
	$("#hindi_canRead").val("False");
	$("#hindi_canSpeak").val("False");
	$("#hindi_canWrite").val("False");
	
	$("#marathi_canRead").val("False");
	$("#marathi_canSpeak").val("False");
	$("#marathi_canWrite").val("False");
	
	$("#english_canRead").val("False");
	$("#english_canSpeak").val("False");
	$("#english_canWrite").val("False");
	
	$(':checkbox').change(function(){
		
		$("#hindi_canRead").val($("#hread").is(':checked'));
		$("#hindi_canSpeak").val($("#hwrite").is(':checked'));
		$("#hindi_canWrite").val($("#hspeak").is(':checked'));
		

		$("#marathi_canRead").val($("#mread").is(':checked'));
		$("#marathi_canSpeak").val($("#mwrite").is(':checked'));
		$("#marathi_canWrite").val($("#mspeak").is(':checked'));
		
		$("#english_canRead").val($("#eread").is(':checked'));
		$("#english_canSpeak").val($("#ewrite").is(':checked'));
		$("#english_canWrite").val($("#espeak").is(':checked'));
	});

	$(".capitalize").change(function(){
		$(this).val($(this).val().toUpperCase());
	});

	
	initializeFileUpload("[ps-file-upload]");

});



function initializeFileUpload(selector)
{
	$(selector).each(
		function (index)
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

