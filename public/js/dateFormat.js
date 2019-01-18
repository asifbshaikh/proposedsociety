
$(document).ready(function(){
	
	/* datepicker is used here for hyphen date format (yyyy-mm-dd) 
	 * it can be used either for yyyy-mm-dd or dd-mm-yyyy date formats */
	
	$(".datepicker")
	.datepicker({
		viewMode : 'years',
		format : 'yyyy-mm-dd'
	}).on('changeDate', function(ev){
		//close when viewMode='0' (days)
		if(ev.viewMode === 'days'){
			var datepickerId = ev.target.id; //to get id on which event is occur
			$("#"+datepickerId).datepicker('hide');
		}
	});
});




