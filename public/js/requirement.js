$(document).ready(function(){

	var selected = $("#dummyRequirementSubType").find('option[selected]');
	$("#requirementSubType").val($("#dummyRequirementSubType").val());
	
	if($("#dummyRequirementSubType").val() == "ChawlRoom"){
		showHideTextboxForChawlTypeOther($('#subType').val());
	}

	$("#dummyRequirementSubType").change(function(){
		var selected = $("#dummyRequirementSubType option:selected");
		var entirePageLoad = false;
		actionOnCheckedRequirementType(selected,entirePageLoad);
		
	});
	
	$("#subType").change(function(){
		showHideTextboxForChawlTypeOther($('#subType').val());
	});
	
	$("#shop_powerConnectionType").change(function(){
		if ($(this).val() == "Other"){
			$("#shop_ifConnectionTypeOtherTR").show();
		}else{
			$("#shop_ifConnectionTypeOtherTR").hide();
		}
	});
	
	$("#cancelAddedLocation").click(function() {
		$("#requirementLocationTable").hide();
		$("#saveLocations").hide();
		$("#cancelAddedLocation").hide();
		$("#showLocTable").show();
	});
});

function actionOnCheckedRequirementType(selected,entirePageLoad){
	if(selected.parent()[0].id == "residentialTypeDetail"){
        $("#requirementSubType").val(selected)
        if(!entirePageLoad){
        	actionOnTypeDetailSelection("res",selected.val(),entirePageLoad);
        }else if (selected.val() == "ChawlRoom"){
        	showHideTextboxForChawlTypeOther($('#subType').val());
        }
    } else if(selected.parent()[0].id == "commercialTypeDetail"){
        if(!entirePageLoad){
        	actionOnTypeDetailSelection("comm",selected.val(),entirePageLoad);
        }
    } else if(selected.parent()[0].id == "agriculturalTypeDetail"){
        if(!entirePageLoad){
        	actionOnTypeDetailSelection("agri",selected.val(),entirePageLoad);
        }
    }
}

function showHideTextboxForChawlTypeOther(chawl_subTypeVar){
	if (chawl_subTypeVar == "other"){
		$("#chawl_ifChawlTypeOtherTR").show();
	}else{
		$("#chawl_ifChawlTypeOtherTR").hide();
	}
}

function actionOnTypeDetailSelection(prefix,typeDetailVar,entirePageLoad)
{
	renderSelectedRequirement(prefix,typeDetailVar,entirePageLoad);
	$("#requirementSubType").val(typeDetailVar);
	if (typeDetailVar == "ChawlRoom"){
		$(document).on("change", "#subType", function(){
			 showHideTextboxForChawlTypeOther($(this).val());
		});
	}
}


function renderSelectedRequirement(prefix,typeDetailVar,entirePageLoad) {
	
	$.ajax({
		url: '/api/applicant/form/requirement/'+prefix+'_'+typeDetailVar,
		type: 'GET',
		success: function(response) {
			//alert('Ajax Success: typeDetailVar: '+typeDetailVar);
			$('#renderSelectedRequirementDiv').empty();
			$('#renderSelectedRequirementDiv').html(response);
			$("#requirementDiv input[type=text]").val("");
			if(typeDetailVar == "ChawlRoom"){
				showHideTextboxForChawlTypeOther($('#subType').val());
			}
		},
		error: function() {
			alert('Ajax Error: typeDetailVar: '+typeDetailVar);
		}
	});
	
}
function hideShowBathRequirement(residencialBath){
	$("input[name='toiletWcBathRequirements']").click(function () { 
		if($("input[name='toiletWcBathRequirements']:checked").val() == "Required"){
			$("#"+residencialBath).show();
		}else{
			$("#"+residencialBath).hide();
		}
	});
}

function hideShowBathRequirementClass(hideItClass){
	$("input[name='toiletWcBathRequirements']").click(function () { 
		if($("input[name='toiletWcBathRequirements']:checked").val() == "Required"){
			$("."+hideItClass).show();
		}else{
			$("."+hideItClass).hide();
		}
	});
}