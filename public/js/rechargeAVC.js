$(document).ready(function(){
	$("#formCount").click(function(){
		var totalAmount = $("#formCount").val();
		totalAmount = totalAmount * 500 ;
		$("#amount").text(totalAmount);
	});
});