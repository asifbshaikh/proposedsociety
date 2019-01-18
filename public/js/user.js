$(document).ready(function(){
	$('#custom-headers').multiSelect({
	  selectableHeader: "<div class='custom-header'>All Roles </div>",
	  selectionHeader: "<div class='custom-header'>Assigned Roles</div>",
	});
	$("div .custom-header").css("background-color","#D5D6DF");
	$("div .custom-header").css("padding-left","10px");
});
