$(document).ready( function(){
	
	var userType = $('[name = "userType"]').val();
	if(userType == "Select"){
		$('[name = "user"]').prop('disabled', true);
		$('[name = "password"]').prop('disabled', true);
	}
	$('[name = "userType"]').change(function(){
		var userType = $('[name = "userType"]').val();
		if(userType != "Select"){
			$('[name = "user"]').prop('disabled', false);
			$('[name = "password"]').prop('disabled', false);
		}else{
			$('[name = "user"]').prop('disabled', true);
			$('[name = "password"]').prop('disabled', true);
		}
	});
	
});