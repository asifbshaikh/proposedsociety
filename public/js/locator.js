$(document).ready(function(){
	var State, District,Taluka,Pincode;	//variable are declared to get value of selected option
	//Note: Variables are passed to function argument to show "Select var" eg. "Select State" to avoid "Select state"   
	populateStates();
	
	$('#get_location').click(function () {
			requestLocation();
	});
	
	$("#State").change(function(){
		State = $("#State option:selected").text();
		populateDistricts(State);
		enable("#District");
		disable("#Taluka");
	});

	$("#District").change(function(){
		State = $("#State option:selected").text();
		District = $("#District option:selected").text();
		populateTalukas(State,District);
		enable("#Taluka");
	});

	$("#Taluka").change(function(){
		State = $("#State option:selected").text();
		District = $("#District option:selected").text();
		Taluka = $("#Taluka option:selected").text();
	});
	
});

function requestLocation() {
	var state = $('#State').val();
	var district = $('#District').val();
	var taluka = $('#Taluka').val();
	var city = $('#City').val();

	var agent = {
		'state': state,
		'district': district,
		'taluka': taluka,
		'city': city,
	};

	$.ajax({
		url: '/agentLocator',
		type: 'POST',
		data: JSON.stringify(agent),
		contentType: 'application/json',
		success: function(response) {
			var divError = $('#show_error');
			var notify = $("#error_msg");
			var resDiv = $("#search_response");
			if ( $("#State").val().length == 0){
				divError.show();
				notify.text("Please fill State search field.");
				resDiv.hide();
			}
			else if ( $("#District").val().length == 0){
				divError.show();
				notify.text("Please fill District search field.");
				resDiv.hide();
			}
			else if( $("#Taluka").val().length == 0){
				divError.show();
				notify.text("Please fill Taluka search field.");
				resDiv.hide();
			}
			else {
				divError.hide();
				resDiv.show();
				$('#search_response').html(response);
			}
		},
	});
}

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
	$('#District').find('option').remove().end().append('<option value="">Select'+" "+"District");
	$('#Taluka').find('option').remove().end().append('<option value="">Select'+" "+"Taluka");
	$('#Pincode').find('option').remove().end().append('<option value="">Select'+" "+"Pincode");
	queryLocation("State","");
	disable("#District");disable("#Taluka");disable("#Pincode");
	hideField("#otherPincode","#otherLocality");
} 
function populateDistricts(State){
	$('#Taluka').find('option').remove().end().append('<option value="">Select'+" "+"Taluka");
	$('#Pincode').find('option').remove().end().append('<option value="">Select'+" "+"Pincode");
	queryLocation("District",State);
	hideField("#otherPincode","#otherLocality");
}
function populateTalukas(State,District){
	$('#Pincode').find('option').remove().end().append('<option value="">Select'+" "+"Pincode");
	queryLocation("Taluka",State,District);
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
    $('#'+control).find('option').remove().end().append('<option value="">Select'+" "+control);
	$.each(items, function (value_id, value) {
		$('#'+control).append($('<option>', { value: value, text: value }));
	});
	
	if(control == "Pincode"){
		$('#'+control).append($('<option>', { value: "Other", text : "Other"}));
	}
}