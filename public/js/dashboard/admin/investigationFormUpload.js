 $(document).ready(function() {
	var options = {
		inputClass: "input-medium",
		buttonClass: "btn btn-small btn-pink",
		onUpload: function(data) {
			$('#fileId').val(data);
		},
		onRemove: function() {
			$('#fileId').val('');
		}
	}

 	$("#investigationFormUpload").fileUpload(options)
 });