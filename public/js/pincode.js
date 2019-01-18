$(document).ready(function(){
	var State, District,Taluka,Pincode;	//variable are declared to get value of selected option
	//Note: Variables are passed to function argument to show "Select var" eg. "Select State" to avoid "Select state"   
	populateStates();
	$("#State").change(function(){
		State = $("#State option:selected").text();
		populateDistricts(State);
		enable("#District");
		disable("#Taluka");disable("#Pincode");
	});

	$("#District").change(function(){
		State = $("#State option:selected").text();
		District = $("#District option:selected").text();
		populateTalukas(State,District);
		disable("#Pincode");enable("#Taluka");
	});

	$("#Taluka").change(function(){
		State = $("#State option:selected").text();
		District = $("#District option:selected").text();
		Taluka = $("#Taluka option:selected").text();
		populatePincodes(State,District,Taluka);
		enable("#Pincode");
	});
	$("#Pincode").change(function(){
		if($("#Pincode").val() == "Other"){
			$("#otherPincode").css("visibility", "visible");
			$("#otherLocality").css("visibility", "visible");
		}else{
			hideField("#otherPincode","#otherLocality");
		}
	});
	
});

function disable(elementId){
	$(elementId).attr('disabled', true);
}

function enable(elementId){
	$(elementId).attr('disabled', false);
}

function hideField(otherPincode,otherLocality ){
	$(otherPincode).css("visibility", "hidden");
	$(otherLocality).css("visibility", "hidden");
}
function populateStates(){
	$('#District').find('option').remove().end().append('<option value="whatever">Select'+" "+"District");
	$('#Taluka').find('option').remove().end().append('<option value="whatever">Select'+" "+"Taluka");
	$('#Pincode').find('option').remove().end().append('<option value="whatever">Select'+" "+"Pincode");
	queryLocation("State","");
	disable("#District");disable("#Taluka");disable("#Pincode");
	hideField("#otherPincode","#otherLocality");
} 
function populateDistricts(State){
	$('#Taluka').find('option').remove().end().append('<option value="whatever">Select'+" "+"Taluka");
	$('#Pincode').find('option').remove().end().append('<option value="whatever">Select'+" "+"Pincode");
	queryLocation("District",State);
	hideField("#otherPincode","#otherLocality");
}
function populateTalukas(State,District){
	$('#Pincode').find('option').remove().end().append('<option value="whatever">Select'+" "+"Pincode");
	queryLocation("Taluka",State,District);
	hideField("#otherPincode","#otherLocality");
}
function populatePincodes(State,District,Taluka){
	queryLocation("Pincode",State,District,Taluka);
	hideField("#otherPincode","#otherLocality");
}
 
function queryLocation(elementId ,State , District,Taluka){
	var url = "/api/staticdata/pincodes/";
	if(State){
		url =url+State;
	}
	if(District){
		url =url+"/"+District;
	}
	if(Taluka){
		url=url +"/"+ Taluka;
	}
	$.ajax({
        type: "GET",
        url: url,
        success : function(response){
        	addToDropdown(elementId,response);
        }
	});
}

function addToDropdown(control , items){
    $('#'+control).find('option').remove().end().append('<option value="whatever">Select'+" "+control);
	$.each(items, function (value_id, value) {
		$('#'+control).append($('<option>', { value: value, text: value }));
	});
	
	if(control == "Pincode"){
		$('#'+control).append($('<option>', { value: "Other", text : "Other"}));
	}
}