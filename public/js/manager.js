$(document).ready(function(){
	
	$("#searchBtn").click(function(){
		  if($("#Pincode").val() == "Other"){
			  var pincode  = $("#otherPincode").val();
			  var locality = $("#otherLocality").val();
			  $("#Pincode").text( pincode );
			  $("#Pincode").append($('<option>', { value: pincode+locality, text: pincode }));
		  }else{
			  if($("#Pincode").val()=== "whatever"){
				  alert("Select State District Taluka Pincode");
			  }
			  $("#Pincode").append($('<option>', { value: $("#Pincode").val(), text: $("#Pincode").val() }));
		  }
	});
	
});