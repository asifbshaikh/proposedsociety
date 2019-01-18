$(document).ready(function(){
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

function assignBuero(agentId){
	var generated_id = "ibId"+agentId;
	var ibName = $('#'+generated_id).find('option:selected').text();
	var ibId =  $("#"+generated_id).val();
	event.preventDefault();
	$.ajax({
		url: '/admin/assignAgentToIb/'+agentId+"/"+ibId,	            
		type: 'GET',
		success: function(response) {
			var agent_id = "assign"+ agentId;
			$("#"+agent_id).prop('value', 'Re-Assign');
			
			alert("Assigned successfully to"+" "+ibName);
		},
		error: function() {
			alert('Something went wrong while GET! or Buero Not Selected');
		}
	});	
}

